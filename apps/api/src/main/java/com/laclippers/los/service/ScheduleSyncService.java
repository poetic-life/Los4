package com.laclippers.los.service;

import com.laclippers.los.entity.Game;
import com.laclippers.los.entity.Season;
import com.laclippers.los.repository.GameRepository;
import com.laclippers.los.repository.SeasonRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从虎扑快船赛程页定时抓取最新赛程与比分，按虎扑比赛 ID upsert；
 * 同步后自动聚合快船胜场负场，回写赛季表（赛季按钮上的战绩随之更新）。
 */
@Service
public class ScheduleSyncService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleSyncService.class);

    private static final String SCHEDULE_URL = "https://nba.hupu.com/schedule/clippers";
    private static final Pattern TEAM_PATTERN = Pattern.compile("/teams/([a-z0-9-]+)");
    private static final Pattern GAME_ID_PATTERN = Pattern.compile("boxscore/(\\d+)");
    private static final Pattern SCORE_PATTERN = Pattern.compile("\\d+");

    // 客场球馆（按虎扑球队 slug）
    private static final java.util.Map<String, String> VENUES = new java.util.HashMap<>();
    static {
        VENUES.put("clippers", "Intuit Dome");
        VENUES.put("lakers", "Crypto.com Arena");
        VENUES.put("warriors", "Chase Center");
        VENUES.put("kings", "Golden 1 Center");
        VENUES.put("suns", "Footprint Center");
        VENUES.put("nuggets", "Ball Arena");
        VENUES.put("jazz", "Delta Center");
        VENUES.put("timberwolves", "Target Center");
        VENUES.put("thunder", "Paycom Center");
        VENUES.put("blazers", "Moda Center");
        VENUES.put("raptors", "Scotiabank Arena");
        VENUES.put("knicks", "Madison Square Garden");
        VENUES.put("nets", "Barclays Center");
        VENUES.put("celtics", "TD Garden");
        VENUES.put("76ers", "Wells Fargo Center");
        VENUES.put("hornets", "Spectrum Center");
        VENUES.put("magic", "Kaseya Center");
        VENUES.put("hawks", "State Farm Arena");
        VENUES.put("wizards", "Capital One Arena");
        VENUES.put("bucks", "Fiserv Forum");
        VENUES.put("pacers", "Gainbridge Fieldhouse");
        VENUES.put("pistons", "Little Caesars Arena");
        VENUES.put("cavaliers", "Rocket Mortgage FieldHouse");
        VENUES.put("bulls", "United Center");
        VENUES.put("rockets", "Toyota Center");
        VENUES.put("grizzlies", "FedExForum");
        VENUES.put("pelicans", "Smoothie King Center");
        VENUES.put("mavericks", "American Airlines Center");
        VENUES.put("spurs", "Frost Bank Center");
        VENUES.put("heat", "Kaseya Center");
    }

    private final GameRepository gameRepository;
    private final SeasonRepository seasonRepository;
    private final SyncSupport support;

    public ScheduleSyncService(GameRepository gameRepository, SeasonRepository seasonRepository, SyncSupport support) {
        this.gameRepository = gameRepository;
        this.seasonRepository = seasonRepository;
        this.support = support;
    }

    /** 启动 60 秒后首次同步（晚于名单），之后每 6 小时一次。 */
    @Scheduled(initialDelay = 60_000, fixedDelay = 6 * 60 * 60 * 1000)
    public void syncScheduled() {
        int count = syncSchedule();
        log.info("赛程定时同步完成，同步 {} 场比赛", count);
    }

    /** 手动触发同步，返回本次处理的比赛数。 */
    public int syncSchedule() {
        Document doc = support.fetchDoc(SCHEDULE_URL);
        if (doc == null) return 0;
        String season = support.parseSeason(doc, "2026-27");
        support.ensureSeason(season);

        int processed = 0;
        for (Element row : doc.select("table.players_table tbody tr")) {
            if (row.hasClass("linglei") || row.hasClass("color_font1") || row.hasClass("title")) continue;
            List<Element> tds = row.select("td");
            if (tds.size() < 4) continue;
            try {
                List<Element> teamLinks = tds.get(0).select("a[href]");
                if (teamLinks.size() < 2) continue;
                String awaySlug = slugOf(teamLinks.get(0).attr("href"));
                String homeSlug = slugOf(teamLinks.get(1).attr("href"));
                if (awaySlug == null || homeSlug == null) continue;

                Element boxLink = tds.get(tds.size() - 1).selectFirst("a[href]");
                String externalId = null;
                if (boxLink != null) {
                    Matcher gm = GAME_ID_PATTERN.matcher(boxLink.attr("href"));
                    if (gm.find()) externalId = gm.group(1);
                }
                if (externalId == null) continue;
                final String gameId = externalId;

                String dateTime = tds.get(3).text().trim();
                Matcher numbers = SCORE_PATTERN.matcher(tds.get(1).text());
                Integer awayScore = numbers.find() ? Integer.parseInt(numbers.group()) : null;
                Integer homeScore = numbers.find() ? Integer.parseInt(numbers.group()) : null;
                boolean finished = awayScore != null && homeScore != null;

                boolean clippersHome = homeSlug.equals("clippers");
                String clippersCode = "LA CLIPPERS";

                Game g = gameRepository.findBySeasonAndExternalId(season, gameId).orElseGet(() -> {
                    Game ng = new Game();
                    ng.setSeason(season);
                    ng.setExternalId(gameId);
                    return ng;
                });

                g.setHomeTeam(clippersHome ? clippersCode : teamCode(homeSlug));
                g.setAwayTeam(clippersHome ? teamCode(awaySlug) : clippersCode);
                g.setIsHome(clippersHome);
                g.setHomeScore(homeScore);
                g.setAwayScore(awayScore);
                g.setStatus(finished ? "已结束" : "未开始");
                if (dateTime.length() >= 16) {
                    g.setDate(dateTime.substring(0, 10));
                    g.setTime(dateTime.substring(11, 16));
                }
                String homeForVenue = clippersHome ? "clippers" : homeSlug;
                g.setVenue(VENUES.getOrDefault(homeForVenue, "客场球馆"));

                gameRepository.save(g);
                processed++;
            } catch (Exception e) {
                log.debug("单场比赛同步失败: {}", e.getMessage());
            }
        }

        aggregateRecord(season);
        return processed;
    }

    /** 根据已结束的比赛聚合快船战绩，回写赛季表。 */
    private void aggregateRecord(String season) {
        List<Game> games = gameRepository.findBySeason(season,
                org.springframework.data.domain.Sort.by("date"));
        int wins = 0;
        int losses = 0;
        for (Game g : games) {
            if (!"已结束".equals(g.getStatus()) || g.getHomeScore() == null || g.getAwayScore() == null) continue;
            boolean clippersWin = Boolean.TRUE.equals(g.getIsHome())
                    ? g.getHomeScore() > g.getAwayScore()
                    : g.getAwayScore() > g.getHomeScore();
            if (clippersWin) wins++; else losses++;
        }
        Season s = support.ensureSeason(season);
        s.setWins(wins);
        s.setLosses(losses);
        if (s.getNote() == null || s.getNote().isEmpty()) {
            s.setNote("常规赛");
        }
        seasonRepository.save(s);
    }

    private String slugOf(String href) {
        Matcher m = TEAM_PATTERN.matcher(href);
        return m.find() ? m.group(1) : null;
    }

    /** 虎扑 slug → 球队英文标识（与前端 teamNames 的键对齐）。 */
    private String teamCode(String slug) {
        if ("blazers".equals(slug)) return "TRAIL BLAZERS";
        return slug.toUpperCase().replace("-", " ");
    }
}
