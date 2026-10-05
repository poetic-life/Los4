package com.laclippers.los.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laclippers.los.entity.News;
import com.laclippers.los.entity.Season;
import com.laclippers.los.repository.NewsRepository;
import com.laclippers.los.repository.SeasonRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 从虎扑 NBA 资讯页定时抓取快船相关新闻，去重后写入新闻表。
 * 列表/详情均为 Next.js SSR，数据嵌入 &lt;script id="__NEXT_DATA__"&gt; JSON 中。
 */
@Service
public class NewsSyncService {

    private static final Logger log = LoggerFactory.getLogger(NewsSyncService.class);

    private static final String LIST_URL = "https://voice.hupu.com/nba/1";
    private static final String UA =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Safari/537.36";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");

    // 快船相关关键词（队名 + 核心球员），用于过滤出与快船相关的资讯
    private static final String[] CLIPPERS_KEYWORDS = {
            "快船", "莱昂纳德", "伦纳德", "祖巴茨", "加兰", "马瑟林", "柯林斯",
            "鲍威尔", "科菲", "巴图姆", "邓恩", "博格达诺维奇", "洛佩斯", "哈登"
    };

    private final NewsRepository newsRepository;
    private final SeasonRepository seasonRepository;

    public NewsSyncService(NewsRepository newsRepository, SeasonRepository seasonRepository) {
        this.newsRepository = newsRepository;
        this.seasonRepository = seasonRepository;
    }

    /** 启动 30 秒后首次同步，之后每 6 小时一次。 */
    @Scheduled(initialDelay = 30_000, fixedDelay = 6 * 60 * 60 * 1000)
    public void syncScheduled() {
        int count = syncNews();
        log.info("新闻定时同步完成，新增 {} 条快船相关资讯", count);
    }

    /** 手动触发同步，返回本次新增条数。 */
    public int syncNews() {
        int inserted = 0;
        try {
            Document listDoc = Jsoup.connect(LIST_URL).userAgent(UA).ignoreContentType(true).timeout(15000).get();
            Element nextData = listDoc.selectFirst("script#__NEXT_DATA__");
            if (nextData == null) {
                return 0;
            }
            JsonNode dataArr = MAPPER.readTree(nextData.data()).at("/props/pageProps/data");
            if (dataArr == null || !dataArr.isArray()) {
                return 0;
            }
            String season = latestSeason();
            for (JsonNode item : dataArr) {
                try {
                    String title = item.path("title").asText("");
                    String sourceUrl = item.path("url").asText("");
                    if (title.isEmpty() || sourceUrl.isEmpty() || !isClippersRelated(title)) {
                        continue;
                    }
                    if (newsRepository.existsBySourceUrl(sourceUrl)) {
                        continue;
                    }
                    String img = downloadImage(item.path("img").asText(""));
                    if (img == null || img.trim().isEmpty()) {
                        img = "/uploads/news1.jpg";
                    }
                    News news = fetchDetail(sourceUrl, stripPrefix(title), img);
                    if (news == null) {
                        continue;
                    }
                    news.setSeason(season);
                    news.setSourceUrl(sourceUrl);
                    news.setCategory(categorize(title + " " + news.getContent()));
                    newsRepository.save(news);
                    inserted++;
                } catch (Exception e) {
                    // 跳过单条失败，不影响整体
                    log.debug("单条新闻抓取失败: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.warn("新闻同步失败: {}", e.getMessage());
        }
        return inserted;
    }

    private News fetchDetail(String url, String title, String img) {
        try {
            Document doc = Jsoup.connect(url).userAgent(UA).ignoreContentType(true).timeout(15000).get();
            Element nextData = doc.selectFirst("script#__NEXT_DATA__");
            if (nextData == null) {
                return null;
            }
            JsonNode thread = MAPPER.readTree(nextData.data()).at("/props/pageProps/detail/thread");
            String content = "";
            String date = today();
            if (thread != null && !thread.isMissingNode()) {
                content = Jsoup.parse(thread.path("content").asText("")).text();
                long createdAt = thread.path("createdAt").asLong(0);
                if (createdAt > 0) {
                    date = Instant.ofEpochMilli(createdAt).atZone(ZONE).toLocalDate().format(DATE_FMT);
                }
            }
            News news = new News();
            news.setTitle(title);
            news.setContent(content);
            news.setImage(img);
            news.setDate(date);
            return news;
        } catch (Exception e) {
            log.debug("抓取详情失败 {}: {}", url, e.getMessage());
            return null;
        }
    }

    private String latestSeason() {
        List<Season> seasons = seasonRepository.findAll(Sort.by(Sort.Direction.DESC, "season"));
        if (seasons != null && !seasons.isEmpty()) {
            return seasons.get(0).getSeason();
        }
        return "2025-26";
    }

    private boolean isClippersRelated(String text) {
        for (String kw : CLIPPERS_KEYWORDS) {
            if (text.contains(kw)) {
                return true;
            }
        }
        return false;
    }

    private String categorize(String text) {
        if (containsAny(text, "伤", "手术", "缺席", "跟腱", "报销", "复出", "骨折", "扭伤")) {
            return "伤情";
        }
        if (containsAny(text, "交易", "签约", "加盟", "离队", "续约", "合同", "裁", "买断", "新援", "解雇")) {
            return "交易签约";
        }
        return "球队动态";
    }

    private boolean containsAny(String text, String... keys) {
        for (String key : keys) {
            if (text.contains(key)) {
                return true;
            }
        }
        return false;
    }

    private String stripPrefix(String title) {
        if (title.startsWith("[流言板]")) {
            return title.substring("[流言板]".length());
        }
        return title;
    }

    /** 下载封面图到本地 uploads 目录，避免外链防盗链/时效导致图片不显示。 */
    private String downloadImage(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "";
        }
        try {
            Connection.Response resp = Jsoup.connect(url).userAgent(UA).ignoreContentType(true).timeout(15000).execute();
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
            String name = "news_" + System.currentTimeMillis() + "_" + Math.abs(url.hashCode()) + "." + ext;
            Path dir = Paths.get(System.getProperty("user.dir"), "uploads");
            Files.createDirectories(dir);
            Files.write(dir.resolve(name), bytes);
            return "/uploads/" + name;
        } catch (Exception e) {
            log.debug("下载新闻图片失败 {}: {}", url, e.getMessage());
            return "";
        }
    }

    private String today() {
        return java.time.LocalDateTime.now().atZone(ZONE).toLocalDate().format(DATE_FMT);
    }
}