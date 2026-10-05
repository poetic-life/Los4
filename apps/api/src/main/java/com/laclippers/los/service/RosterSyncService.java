package com.laclippers.los.service;

import com.laclippers.los.entity.Player;
import com.laclippers.los.repository.PlayerRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从虎扑快船球员页定时抓取最新名单，按虎扑球员 ID upsert 到球员表；
 * 新球员自动抓取详情页补全学校/选秀/头像/场均数据，老球员每天刷新一次数据。
 */
@Service
public class RosterSyncService {

    private static final Logger log = LoggerFactory.getLogger(RosterSyncService.class);

    private static final String ROSTER_URL = "https://nba.hupu.com/players/clippers";
    private static final Pattern ID_PATTERN = Pattern.compile("-(\\d+)\\.html");
    private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{4})年");

    private final PlayerRepository playerRepository;
    private final SyncSupport support;

    public RosterSyncService(PlayerRepository playerRepository, SyncSupport support) {
        this.playerRepository = playerRepository;
        this.support = support;
    }

    /** 启动 45 秒后首次同步（晚于新闻），之后每 6 小时一次。 */
    @Scheduled(initialDelay = 45_000, fixedDelay = 6 * 60 * 60 * 1000)
    public void syncScheduled() {
        int count = syncRoster();
        log.info("球员名单定时同步完成，同步 {} 名球员", count);
    }

    /** 手动触发同步，返回本次处理的球员数。 */
    public int syncRoster() {
        Document doc = support.fetchDoc(ROSTER_URL);
        if (doc == null) return 0;
        String season = support.parseSeason(doc, "2026-27");
        support.ensureSeason(season);

        int processed = 0;
        for (Element row : doc.select("table.players_table tbody tr")) {
            Element link = row.selectFirst("td.td_padding a[href]");
            if (link == null) continue; // 表头行
            try {
                Matcher idm = ID_PATTERN.matcher(link.attr("href"));
                if (!idm.find()) continue;
                String externalId = idm.group(1);

                Element nameA = row.select("td").get(1).selectFirst("b > a");
                Element enB = row.select("td").get(1).selectFirst("p b");
                String zh = nameA != null ? nameA.text().trim() : "";
                String en = enB != null ? enB.text().trim() : "";
                // 个别球员虎扑暂无中文名，回退用英文名
                String display = !zh.isEmpty() ? zh : en;
                if (display.isEmpty()) continue;

                Integer number = parseIntOrNull(cell(row, 2));
                String[] pos = mapPosition(cell(row, 3));
                String height = mapHeight(cell(row, 4));
                String weight = mapWeight(cell(row, 5));
                String rosterImg = row.selectFirst("td.td_padding img") != null
                        ? row.selectFirst("td.td_padding img").attr("src") : "";

                Player p = playerRepository.findBySeasonAndExternalId(season, externalId).orElseGet(() -> {
                    Player np = new Player();
                    np.setSeason(season);
                    np.setExternalId(externalId);
                    np.setStatus("现役");
                    return np;
                });

                boolean isNew = p.getId() == null;
                p.setName(display);
                if (!en.isEmpty()) p.setEnName(en);
                p.setNumber(number);
                p.setPosition(pos[0]);
                p.setPositionEn(pos[1]);
                if (!height.isEmpty()) p.setHeight(height);
                if (!weight.isEmpty()) p.setWeight(weight);

                boolean detailDue = isNew || p.getStatsUpdated() == null
                        || p.getStatsUpdated().isBefore(LocalDateTime.now().minusHours(20));
                if (detailDue) {
                    enrichDetail(p, link.attr("href"));
                    p.setStatsUpdated(LocalDateTime.now());
                    Thread.sleep(350); // 礼貌限速，避免高频抓取
                } else if ((p.getImage() == null || p.getImage().isEmpty()) && !rosterImg.isEmpty()) {
                    String img = support.downloadImage(rosterImg, "player");
                    if (!img.isEmpty()) p.setImage(img);
                }
                if (p.getBio() == null || p.getBio().isEmpty()) {
                    p.setBio(buildBio(p));
                }

                playerRepository.save(p);
                processed++;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.debug("单个球员同步失败: {}", e.getMessage());
            }
        }
        return processed;
    }

    /** 抓取球员详情页：头像、学校、选秀、场均数据。 */
    private void enrichDetail(Player p, String url) {
        Document doc = support.fetchDoc(url);
        if (doc == null) return;

        Element headImg = doc.selectFirst(".content_a .img img");
        if (headImg != null && (p.getImage() == null || p.getImage().isEmpty())) {
            String img = support.downloadImage(headImg.attr("src"), "player");
            if (!img.isEmpty()) p.setImage(img);
        }

        for (Element line : doc.select(".content_a .font p")) {
            String text = line.text();
            int idx = text.indexOf('：');
            if (idx < 0) continue;
            String key = text.substring(0, idx);
            String value = text.substring(idx + 1).trim();
            if (key.equals("学校") && !value.equals("-") && p.getCollege() == null) {
                p.setCollege(value);
            } else if (key.equals("选秀") && !value.equals("-") && p.getDraft() == null) {
                p.setDraft(value);
                p.setExperience(experienceFromDraft(value));
            }
        }

        for (Element stat : doc.select(".table_team_box .border")) {
            Element label = stat.selectFirst("span.a");
            Element value = stat.selectFirst("span.b b");
            if (label == null || value == null) continue;
            Double v = parseDoubleOrNull(value.text());
            if (v == null) continue;
            switch (label.text()) {
                case "场均得分": p.setPointsAvg(v); break;
                case "场均助攻": p.setAssistsAvg(v); break;
                case "场均篮板": p.setReboundsAvg(v); break;
                case "场均盖帽": p.setBlocksAvg(v); break;
                case "场均抢断": p.setStealsAvg(v); break;
                case "投篮命中率": p.setFieldGoalPct(v); break;
                case "三分命中率": p.setThreePct(v); break;
                case "罚球命中率": p.setFreeThrowPct(v); break;
                default: break;
            }
        }
    }

    private String cell(Element row, int i) {
        org.jsoup.select.Elements tds = row.select("td");
        return tds.size() > i ? tds.get(i).text().trim() : "";
    }

    /** G/F/C（含 G-F 之类组合）→ 中文位置 + 英文缩写。 */
    private String[] mapPosition(String raw) {
        if (raw == null || raw.isEmpty()) return new String[]{"未知", "—"};
        if (raw.contains("-")) {
            String[] parts = raw.split("-");
            String en = mapPosEn(parts[0]) + "/" + mapPosEn(parts[1]);
            return new String[]{"锋线摇摆人", en};
        }
        switch (raw) {
            case "G": return new String[]{"后卫", "G"};
            case "F": return new String[]{"前锋", "F"};
            case "C": return new String[]{"中锋", "C"};
            default: return new String[]{"未知", raw};
        }
    }

    private String mapPosEn(String s) {
        switch (s.trim()) {
            case "G": return "G";
            case "F": return "F";
            case "C": return "C";
            default: return s.trim();
        }
    }

    /** "1.96米/6尺5" → "6'5\""。 */
    private String mapHeight(String raw) {
        if (raw == null || !raw.contains("/")) return "";
        String imp = raw.substring(raw.indexOf('/') + 1).replace("尺", "'").trim();
        if (imp.endsWith("'")) imp += "0";
        return imp + "\"";
    }

    /** "84公斤/185磅" → "185 lbs"。 */
    private String mapWeight(String raw) {
        if (raw == null || !raw.contains("/")) return "";
        String imp = raw.substring(raw.indexOf('/') + 1).replace("磅", "").trim();
        return imp.isEmpty() ? "" : imp + " lbs";
    }

    private String buildBio(Player p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getEnName() != null ? p.getEnName() : p.getName());
        sb.append("，司职").append(p.getPosition());
        if (p.getDraft() != null) sb.append("，").append(p.getDraft());
        if (p.getCollege() != null) sb.append("，大学就读于").append(p.getCollege());
        sb.append("。");
        return sb.toString();
    }

    private String experienceFromDraft(String draft) {
        Matcher m = YEAR_PATTERN.matcher(draft);
        if (m.find()) {
            int years = LocalDateTime.now().getYear() - Integer.parseInt(m.group(1));
            return years >= 1 ? years + "年" : "新秀";
        }
        return null;
    }

    private Integer parseIntOrNull(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return Integer.parseInt(s.replaceAll("\\D", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDoubleOrNull(String s) {
        if (s == null || s.isEmpty() || s.equals("-")) return null;
        try {
            return Double.parseDouble(s.replace("%", "").trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
