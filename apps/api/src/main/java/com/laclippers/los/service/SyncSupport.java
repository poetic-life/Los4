package com.laclippers.los.service;

import com.laclippers.los.entity.Season;
import com.laclippers.los.repository.SeasonRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 联网同步公共能力：抓取页面、解析赛季年份、确保赛季行存在、下载图片到本地。
 * 球员名单与赛程同步共用，避免重复代码。
 */
@Component
public class SyncSupport {

    private static final Logger log = LoggerFactory.getLogger(SyncSupport.class);

    public static final String UA =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Safari/537.36";

    // 2026-2027赛季 / 2026至2027赛季（必须紧邻"赛季"，避免匹配到生日等年份）
    private static final Pattern SEASON_PATTERN = Pattern.compile("(\\d{4})[-至/](\\d{2,4})\\s*赛季");

    private final SeasonRepository seasonRepository;

    public SyncSupport(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    /** 抓取页面，失败返回 null。 */
    public Document fetchDoc(String url) {
        try {
            return Jsoup.connect(url).userAgent(UA).ignoreContentType(true).timeout(15000).get();
        } catch (Exception e) {
            log.warn("页面抓取失败 {}: {}", url, e.getMessage());
            return null;
        }
    }

    /**
     * 从页面文本中解析赛季标识，如 "2026至2027赛季" → "2026-27"；
     * 页面未标注时（如名单页）按当前日期推算：7 月后属于新赛季。
     */
    public String parseSeason(Document doc, String fallback) {
        if (doc != null) {
            Matcher m = SEASON_PATTERN.matcher(doc.text());
            if (m.find()) {
                String start = m.group(1);
                String endRaw = m.group(2);
                String end = endRaw.length() == 2 ? endRaw : endRaw.substring(2);
                return start + "-" + end;
            }
        }
        java.time.LocalDate now = java.time.LocalDate.now();
        int startYear = now.getMonthValue() >= 7 ? now.getYear() : now.getYear() - 1;
        String end = String.valueOf(startYear + 1).substring(2);
        return startYear + "-" + end;
    }

    /**
     * 确保赛季行存在（新赛季首次同步时自动创建，前端赛季按钮即可看到）。
     */
    public Season ensureSeason(String season) {
        return seasonRepository.findBySeason(season).orElseGet(() -> {
            Season s = new Season();
            s.setSeason(season);
            String start = season.substring(2, 4);
            String end = season.substring(season.length() - 2);
            s.setLabel(start + "-" + end + " 赛季");
            s.setWins(0);
            s.setLosses(0);
            s.setNote("常规赛");
            return seasonRepository.save(s);
        });
    }

    /**
     * 下载图片到本地 uploads 目录，返回可访问的相对路径；失败或为占位图返回空串。
     */
    public String downloadImage(String url, String prefix) {
        if (url == null || url.trim().isEmpty() || url.contains("brand.jpg")) {
            return "";
        }
        try {
            Connection.Response resp = Jsoup.connect(url).userAgent(UA).ignoreContentType(true)
                    .timeout(15000).referrer("https://nba.hupu.com/").execute();
            byte[] bytes = resp.bodyAsBytes();
            if (bytes == null || bytes.length < 200) {
                return "";
            }
            String ext = "jpg";
            String contentType = resp.contentType();
            if (contentType != null) {
                if (contentType.contains("png")) {
                    ext = "png";
                } else if (contentType.contains("webp")) {
                    ext = "webp";
                }
            }
            String name = prefix + "_" + System.currentTimeMillis() + "_" + Math.abs(url.hashCode()) + "." + ext;
            Path dir = Paths.get(System.getProperty("user.dir"), "uploads");
            Files.createDirectories(dir);
            Files.write(dir.resolve(name), bytes);
            return "/uploads/" + name;
        } catch (Exception e) {
            log.debug("图片下载失败 {}: {}", url, e.getMessage());
            return "";
        }
    }
}
