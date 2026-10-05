package com.laclippers.los.config;

import com.laclippers.los.entity.*;
import com.laclippers.los.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 首次启动时填充种子数据（仅当对应表为空时）。
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final NewsRepository newsRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final FaqRepository faqRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ProductRepository productRepository,
                           NewsRepository newsRepository,
                           PlayerRepository playerRepository,
                           GameRepository gameRepository,
                           FaqRepository faqRepository,
                           PostRepository postRepository,
                           UserRepository userRepository,
                           SeasonRepository seasonRepository,
                           PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.newsRepository = newsRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.faqRepository = faqRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.seasonRepository = seasonRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUser();
        seedProducts();
        seedNews();
        seedPlayers();
        seedGames();
        seedSeasons();
        seedFaqs();
        seedPosts();
    }

    private void seedUser() {
        user("demo", "demo@laclippers.com", "123456", "快船官方演示账户，欢迎交流。", 1280, "银卡会员");
        user("ClipperNation", "nation@clippers.com", "123456", "快船死忠，风雨无阻。", 860, "银卡会员");
        user("NBA_Analyst", "analyst@nba.com", "123456", "专注 NBA 战术与数据分析。", 1520, "金卡会员");
        user("BallIsLife", "ball@life.com", "123456", "篮球就是生命，热爱每一个回合。", 420, "银卡会员");
    }

    private void user(String username, String email, String password, String bio, int points, String level) {
        if (userRepository.existsByUsername(username)) return;
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setBio(bio);
        u.setPoints(points);
        u.setLevel(level);
        u.setCreatedAt(LocalDateTime.now());
        userRepository.save(u);
    }

    private void seedProducts() {
        productRepository.deleteAll();
        // 分类1：篮球
        product(1L, "斯伯丁专业7号比赛用球", 299, "/uploads/basketball.jpg", 1, "篮球", 1200, 340, "室内外通用标准7号球，抓握手感出众，耐磨防滑。", 200);
        product(2L, "哈登签名纪念篮球", 599, "/uploads/ball_signature.jpg", 1, "篮球", 320, 88, "哈登限量签名款纪念篮球，含专属礼盒与收藏证书。", 80);
        product(3L, "快船队徽纪念篮球套装", 888, "/uploads/ball_set.jpg", 1, "篮球", 180, 56, "附赠球袋与打气筒的队徽纪念套装，送礼首选。", 40);
        // 分类2：篮球鞋
        product(4L, "缓震透气实战篮球鞋", 799, "/uploads/shoe.jpg", 2, "篮球鞋", 860, 210, "缓震透气实战鞋，包裹稳定，助你驰骋球场。", 150);
        product(5L, "哈登同款签名战靴", 1099, "/uploads/shoe_harden.jpg", 2, "篮球鞋", 520, 118, "后撤步专属碳板加持，哈登签名战靴同款。", 70);
        product(6L, "伦纳德同款锋线战靴", 999, "/uploads/shoe_leonard.jpg", 2, "篮球鞋", 410, 96, "稳如磐石的锋线战靴，攻防一体极致脚感。", 90);
        // 分类3：篮球服
        product(7L, "快船主场经典球衣", 399, "/uploads/clothes.jpg", 3, "篮球服", 980, 205, "红蓝经典配色主队球衣，球迷观赛必备单品。", 160);
        product(8L, "哈登红色特别版球衣", 429, "/uploads/jersey_red.jpg", 3, "篮球服", 460, 92, "哈登主题红色特别版球衣，醒目动感。", 110);
        product(9L, "伦纳德蓝色客场球衣", 459, "/uploads/jersey_blue.jpg", 3, "篮球服", 390, 78, "伦纳德主题蓝色客场球衣，沉稳有型。", 95);
        product(10L, "快船训练连帽卫衣", 329, "/uploads/hoodie.jpg", 3, "篮球服", 640, 133, "加绒连帽卫衣，保暖舒适，日常穿搭皆宜。", 140);
        // 分类4：篮球配件
        product(11L, "专业护膝护腕套装", 199, "/uploads/defense.jpg", 4, "篮球配件", 410, 76, "专业护膝护腕套装，有效防护关节，运动更安心。", 120);
        product(12L, "快船队刺绣鸭舌帽", 129, "/uploads/cap.jpg", 4, "篮球配件", 730, 156, "潮流鸭舌帽，刺绣队徽，百搭出街。", 220);
        product(13L, "球队应援围巾", 89, "/uploads/scarf.jpg", 4, "篮球配件", 580, 130, "红蓝撞色应援围巾，主场氛围拉满。", 260);
        product(14L, "快船队多功能双肩包", 259, "/uploads/backpack.jpg", 4, "篮球配件", 340, 77, "大容量球队双肩包，通勤运动两相宜。", 95);
    }

    private void product(Long id, String name, int price, String image, int categoryId, String categoryName,
                         int sales, int reviews, String desc, int stock) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setImage(image);
        p.setCategoryId(categoryId);
        p.setCategoryName(categoryName);
        p.setSales(sales);
        p.setReviews(reviews);
        p.setDescription(desc);
        p.setStock(stock);
        productRepository.save(p);
    }

    private void seedNews() {
        // 仅在无任何新闻时播种，避免重启后清空定时抓取的实时新闻
        if (newsRepository.count() > 0) return;
        // ===== 2024-25 赛季真实新闻 =====
        news("2024-25", "保罗·乔治离队，四年快船生涯画上句号", "2024 年休赛期，保罗·乔治以自由球员身份离开洛杉矶快船，转投费城 76 人。乔治在快船效力的四个赛季里多次入选全明星，是球队最闪耀的招牌之一。", "/uploads/pg.png", "交易签约", "2024-07-01");
        news("2024-25", "威斯布鲁克被交易至爵士", "洛杉矶快船将拉塞尔·威斯布鲁克交易至犹他爵士，威少随后与爵士达成买断并加盟丹佛掘金。这位传奇控卫在快船效力两个赛季，留下无数高光时刻。", "/uploads/westbrook.png", "交易签约", "2024-07-21");
        news("2024-25", "快船新球馆 Intuit Dome 正式揭幕", "洛杉矶快船全新的主场球馆 Intuit Dome 在英格尔伍德正式揭幕，这也是 NBA 首座为球队专属打造的智能球馆，可容纳约 1.8 万名观众。", "/uploads/zubac.png", "球队动态", "2024-08-15");
        news("2024-25", "哈登正式与快船续约", "詹姆斯·哈登与洛杉矶快船达成续约协议，继续以球队核心后卫身份征战 2024-25 赛季，新赛季他将与莱昂纳德、祖巴茨等联手冲击总冠军。", "/uploads/harden.png", "交易签约", "2024-07-10");
        news("2024-25", "揭幕战主场险胜太阳", "2024-25 赛季揭幕战，洛杉矶快船在全新的 Intuit Dome 主场险胜菲尼克斯太阳，收获新球馆开门红。", "/uploads/booker.png", "球队动态", "2024-10-23");
        news("2024-25", "莱昂纳德伤情反复，复出时间待定", "科怀·莱昂纳德因右膝伤势反复，复出时间仍不确定，球队对其采取谨慎管理策略，确保核心球员在季后赛前恢复到最佳状态。", "/uploads/kawhi.png", "伤情", "2024-12-05");
        news("2024-25", "哈登入选西部全明星", "詹姆斯·哈登凭借稳定的组织与得分表现入选 2025 年西部全明星阵容，这也是他生涯第 11 次入选全明星。", "/uploads/harden.png", "球队动态", "2025-02-01");
        news("2024-25", "快船 50 胜 32 负收官，锁定西部第五", "常规赛收官战，快船击败金州勇士，以 50 胜 32 负结束 2024-25 赛季，成功锁定西部第五，直接晋级季后赛。", "/uploads/mann.png", "球队动态", "2025-04-13");
        news("2024-25", "季后赛首轮不敌掘金，止步首轮", "季后赛首轮，西部第五的快船与第四的丹佛掘金鏖战，最终以 2-4 不敌对手止步首轮，结束 2024-25 赛季征程。", "/uploads/jokic.png", "球队动态", "2025-05-03");
        news("2024-25", "诺曼·鲍威尔迎来生涯最佳赛季", "诺曼·鲍威尔在 2024-25 赛季迎来爆发，场均得分创生涯新高，成为莱昂纳德与哈登之外快船最可靠的第三得分点。", "/uploads/powell.png", "球队动态", "2025-03-15");

        // ===== 2025-26 赛季真实新闻 =====
        news("2025-26", "NBA 官方重罚快船：莱昂纳德签约阴阳合同", "NBA 官方宣布，因洛杉矶快船与科怀·莱昂纳德签订阴阳合同、规避工资帽，联盟做出重磅处罚：没收 2029-2033 年五个首轮选秀权、罚款 3000 万美元、老板鲍尔默禁赛一年、总裁 Zucker 停薪一年、篮球运营总裁劳伦斯·弗兰克停薪六个月，莱昂纳德本人需缴纳 70 万美元罚款。", "/uploads/kawhi.png", "交易签约", "2026-09-02");
        news("2025-26", "快船 42 胜 40 负收官，位居西部第 9", "常规赛收官战，快船主场以 115-110 力克金州勇士，以 42 胜 40 负结束 2025-26 赛季，最终位列西部第 9，将参加附加赛争夺季后赛席位。", "/uploads/beal.png", "球队动态", "2026-04-13");
        news("2025-26", "快船附加赛惜败勇士，止步附加赛", "附加赛首轮，快船主场以 121-126 不敌金州勇士，遗憾无缘季后赛。马瑟林、约翰·柯林斯等人拼尽全力，但球队整体阵容深度不足，最终遗憾出局。", "/uploads/cp3.png", "球队动态", "2026-04-16");
        news("2025-26", "快船交易截止日前大动作：哈登、祖巴茨离队", "交易截止日当天，快船完成两笔重磅交易：将詹姆斯·哈登送至骑士，换回达里厄斯·加兰与 2026 年次轮签；同时将伊维卡·祖巴茨与科比·布朗送往步行者，换回本尼迪克特·马瑟林、以赛亚·杰克逊以及两个首轮签和一个次轮签。球队明确表示不会交易科怀·莱昂纳德。", "/uploads/harden.png", "交易签约", "2026-02-06");
        news("2025-26", "加兰加盟后首秀惊艳，组织串联显功力", "新援达里厄斯·加兰迎来快船首秀，全场送出多次精妙助攻并命中关键三分，帮助球队在进攻端更加流畅，展现出全明星控卫的组织才华。", "/uploads/garland.png", "球队动态", "2026-02-08");
        news("2025-26", "马瑟林轰下 20 分，快船收官战力克勇士", "本尼迪克特·马瑟林在常规赛收官战中砍下 20 分引领球队，关键时刻连续得分，帮助快船以 115-110 击败勇士，锁定西部第 9。", "/uploads/mathurin.png", "球队动态", "2026-04-12");
        news("2025-26", "约翰·柯林斯单场 25 分，攻防两端全能", "约翰·柯林斯在与国王的比赛中砍下 25 分、4 篮板，内线冲击与外线投射兼备，是快船重建期值得信赖的锋线核心。", "/uploads/collins.png", "球队动态", "2026-04-06");
        news("2025-26", "伦纳德赛季场均 27.9 分，仍是一流锋线", "科怀·莱昂纳德本赛季出战 65 场全部首发，场均贡献 27.9 分，投篮命中率 50.5%、三分命中率 38.7%，攻防两端依旧维持联盟顶级水准。", "/uploads/kawhi.png", "球队动态", "2026-01-30");
        news("2025-26", "祖巴茨赛季场均 14.4 分命中率 61.3%", "伊维卡·祖巴茨本赛季场均贡献 14.4 分，投篮命中率高达 61.3%，是快船内线最稳定的得分与护框支柱。", "/uploads/zubac.png", "球队动态", "2026-01-12");
        news("2025-26", "哈登成为快船进攻发动机，场均 25.4 分", "詹姆斯·哈登本赛季出战 44 场，场均贡献 25.4 分，串联起整支球队的进攻，是快船冲击季后赛的核心发动机。", "/uploads/harden.png", "球队动态", "2025-12-20");
        news("2025-26", "快船揭幕战客场不敌爵士", "2025-26 赛季揭幕战，洛杉矶快船客场以 108-129 不敌犹他爵士，伦纳德与哈登双双登场，但球队防守端表现欠佳，未能取得开门红。", "/uploads/markkanen.png", "球队动态", "2025-10-22");
        news("2025-26", "快船以历史顶级阵容开启 2025-26 赛季", "快船在休赛期补强了布拉德利·比尔、布鲁克·洛佩斯与克里斯·保罗等经验丰富的即战力，加上健康的莱昂纳德与哈登，以冲击总决赛为目标开启新赛季。", "/uploads/lopez.png", "球队动态", "2025-10-20");
    }

    private void news(String season, String title, String content, String image, String category, String date) {
        News n = new News();
        n.setSeason(season);
        n.setTitle(title);
        n.setContent(content);
        n.setImage(image);
        n.setCategory(category);
        n.setDate(date);
        newsRepository.save(n);
    }

    private void seedPlayers() {
        // 仅在无任何球员时播种，避免重启清空联网同步的最新名单
        if (playerRepository.count() > 0) return;

        // ===== 2024-25 赛季阵容（50 胜 32 负 · 西部第五）=====
        star("2024-25", "科怀·莱昂纳德", "Kawhi Leonard", 2, "小前锋", "SF", "6'7\"", "225 lbs", "/uploads/kawhi.png",
                "两届总决赛 MVP，历史级攻防双向超巨。",
                "San Diego State", "2011年 第15顺位", "14年",
                24.4, 5.6, 4.1, 1.2, 0.6, 51.0, 39.0, 89.0);
        star("2024-25", "詹姆斯·哈登", "James Harden", 1, "得分后卫", "SG", "6'5\"", "220 lbs", "/uploads/harden.png",
                "三届得分王，顶级持球组织核心，串联全队进攻。",
                "Arizona State", "2009年 第3顺位", "16年",
                22.8, 4.4, 8.3, 1.2, 0.5, 43.0, 36.0, 86.0);
        star("2024-25", "伊维卡·祖巴茨", "Ivica Zubac", 40, "中锋", "C", "7'0\"", "240 lbs", "/uploads/zubac.png",
                "内线定海神针，护框与篮板俱佳。",
                "-", "2016年 第32顺位", "9年",
                12.1, 10.3, 1.5, 0.4, 1.0, 62.0, 0.0, 71.0);
        star("2024-25", "诺曼·鲍威尔", "Norman Powell", 24, "得分后卫", "SG", "6'4\"", "215 lbs", "/uploads/powell.png",
                "进攻型分卫，生涯最佳赛季，三分精准。",
                "UCLA", "2015年 第46顺位", "10年",
                20.5, 3.0, 2.2, 1.0, 0.3, 49.0, 42.0, 89.0);
        star("2024-25", "特伦斯·曼", "Terance Mann", 14, "锋卫摇摆人", "SG/SF", "6'5\"", "215 lbs", "/uploads/mann.png",
                "攻防俱佳的锋卫摇摆人，球队万金油。",
                "Florida State", "2019年 第48顺位", "6年",
                8.9, 3.5, 1.8, 0.7, 0.3, 48.0, 37.0, 80.0);
        role("2024-25", "阿米尔·科菲", "Amir Coffey", 7, "小前锋", "SF", "6'7\"", "210 lbs", "/uploads/coffey.png", "攻守均衡的锋线，三分稳定。", 8.5, 2.8, 1.5, 0.6, 0.2);
        role("2024-25", "尼古拉斯·巴图姆", "Nicolas Batum", 33, "大前锋", "PF", "6'8\"", "230 lbs", "/uploads/batum.png", "万金油锋线，传球与防守老道。", 4.2, 3.5, 1.5, 0.6, 0.4);
        role("2024-25", "德里克·琼斯 Jr.", "Derrick Jones Jr.", 55, "小前锋", "SF", "6'6\"", "210 lbs", "/uploads/djj.png", "运动能力出色的锋线飞人，防守积极。", 10.5, 3.5, 1.5, 0.7, 0.4);
        role("2024-25", "克里斯·邓恩", "Kris Dunn", 5, "后卫", "G", "6'3\"", "205 lbs", "/uploads/dunn.png", "防守强韧的全能后卫，组织与抢断俱佳。", 8.0, 3.5, 3.0, 1.2, 0.3);
        role("2024-25", "凯文·波特 Jr.", "Kevin Porter Jr.", 77, "后卫", "G", "6'4\"", "203 lbs", "/uploads/kpj.png", "得分能力出众的双能卫。", 8.5, 2.5, 2.0, 0.7, 0.2);
        role("2024-25", "穆罕默德·班巴", "Mo Bamba", 4, "中锋", "C", "7'0\"", "231 lbs", "/uploads/bamba.png", "护框型内线，盖帽能力出色。", 5.5, 4.0, 0.5, 0.3, 1.0);
        role("2024-25", "纳尚·海兰德", "Bones Hyland", 13, "控球后卫", "PG", "6'2\"", "174 lbs", "/uploads/hyland.png", "灵巧的得分型控卫，外线投射。", 7.0, 1.5, 1.8, 0.6, 0.1);
        role("2024-25", "帕蒂·米尔斯", "Patty Mills", 8, "后卫", "G", "6'1\"", "180 lbs", "/uploads/mills.png", "经验丰富的老将射手，更衣室领袖。", 5.5, 1.0, 1.0, 0.5, 0.0);
        role("2024-25", "P.J.·塔克", "P.J. Tucker", 17, "大前锋", "PF", "6'5\"", "245 lbs", "/uploads/pjtucker.png", "防守悍将，底角三分稳健。", 3.0, 3.0, 0.8, 0.5, 0.2);
        role("2024-25", "凯·琼斯", "Kai Jones", 22, "大前锋/中锋", "PF/C", "6'11\"", "221 lbs", "/uploads/kai_jones.png", "运动能力劲爆的内线。", 3.5, 2.5, 0.3, 0.2, 0.5);

        // ===== 2025-26 赛季阵容（42 胜 40 负 · 西部第九）=====
        star("2025-26", "科怀·莱昂纳德", "Kawhi Leonard", 2, "小前锋", "SF", "6'7\"", "225 lbs", "/uploads/kawhi.png",
                "两届总决赛 MVP，历史级攻防双向超巨，赛季场均 27.9 分仍是一流锋线。",
                "San Diego State", "2011年 第15顺位", "14年",
                27.9, 6.5, 3.6, 1.6, 0.7, 50.5, 38.7, 89.2);
        star("2025-26", "詹姆斯·哈登", "James Harden", 1, "得分后卫", "SG", "6'5\"", "220 lbs", "/uploads/harden.png",
                "三届得分王，顶级持球组织核心，后撤步三分与串联能力独步联盟。",
                "Arizona State", "2009年 第3顺位", "16年",
                25.4, 5.5, 8.5, 1.2, 0.5, 42.0, 36.0, 85.0);
        star("2025-26", "达里厄斯·加兰", "Darius Garland", 10, "控球后卫", "PG", "6'1\"", "192 lbs", "/uploads/garland.png",
                "全明星控卫，犀利的挡拆进攻与外线投射，赛季三分命中率高达 43.8%。",
                "Vanderbilt", "2019年 第5顺位", "6年",
                19.9, 2.8, 6.0, 1.2, 0.2, 46.0, 43.8, 88.0);
        star("2025-26", "本尼迪克特·马瑟林", "Bennedict Mathurin", 9, "锋卫摇摆人", "SG/SF", "6'5\"", "210 lbs", "/uploads/mathurin.png",
                "冲击力十足的年轻锋卫，突破终结与外线投射兼备。",
                "Arizona", "2022年 第6顺位", "3年",
                17.4, 4.5, 2.0, 0.8, 0.2, 47.0, 37.0, 82.0);
        star("2025-26", "伊维卡·祖巴茨", "Ivica Zubac", 40, "中锋", "C", "7'0\"", "240 lbs", "/uploads/zubac.png",
                "内线定海神针，护框与篮板俱佳，赛季投篮命中率高达 61.3%。",
                "-", "2016年 第32顺位", "9年",
                14.4, 10.5, 1.8, 0.3, 1.2, 61.3, 0.0, 70.0);
        star("2025-26", "约翰·柯林斯", "John Collins", 20, "大前锋", "PF", "6'9\"", "228 lbs", "/uploads/collins.png",
                "能里能外的空间型四号位，赛季三分命中率 40.6%。",
                "Wake Forest", "2017年 第19顺位", "8年",
                13.6, 5.3, 1.0, 0.9, 0.5, 55.2, 40.6, 76.6);
        star("2025-26", "布拉德利·比尔", "Bradley Beal", 0, "锋卫摇摆人", "SG/SF", "6'4\"", "207 lbs", "/uploads/beal.png",
                "得分能力出众的全明星后卫，突破与中远投俱佳。",
                "Florida", "2012年 第3顺位", "13年",
                17.0, 3.8, 3.5, 0.9, 0.3, 45.0, 38.0, 82.0);
        star("2025-26", "克里斯·保罗", "Chris Paul", 3, "控球后卫", "PG", "6'0\"", "175 lbs", "/uploads/cp3.png",
                "传奇控卫，球场指挥官，经验与组织功力深厚。",
                "Wake Forest", "2005年 第4顺位", "20年",
                8.5, 3.0, 6.0, 1.0, 0.1, 43.0, 36.0, 82.0);
        role("2025-26", "布鲁克·洛佩斯", "Brook Lopez", 11, "中锋", "C", "7'0\"", "282 lbs", "/uploads/lopez.png", "能护框又能拉开空间的高炮台中锋。", 10.0, 5.0, 1.0, 0.5, 1.8);
        role("2025-26", "德里克·琼斯 Jr.", "Derrick Jones Jr.", 5, "小前锋", "SF", "6'6\"", "210 lbs", "/uploads/djj.png", "运动能力出色的锋线飞人，防守积极。", 10.1, 3.5, 1.2, 0.7, 0.4);
        role("2025-26", "克里斯·邓恩", "Kris Dunn", 8, "后卫", "G", "6'3\"", "205 lbs", "/uploads/dunn.png", "防守强韧的全能后卫，组织与抢断俱佳。", 7.3, 3.0, 4.0, 1.3, 0.3);
        role("2025-26", "尼古拉斯·巴图姆", "Nicolas Batum", 33, "大前锋", "PF", "6'8\"", "230 lbs", "/uploads/batum.png", "万金油锋线，传球与防守老道。", 4.5, 3.5, 1.2, 0.6, 0.4);
        role("2025-26", "博格丹·博格达诺维奇", "Bogdan Bogdanovic", 13, "得分后卫", "SG", "6'5\"", "220 lbs", "/uploads/bogdanovic.png", "欧洲神射手，外线投射精准。", 7.4, 2.5, 1.8, 0.6, 0.1);
        role("2025-26", "特伦斯·曼", "Terance Mann", 14, "锋卫摇摆人", "SG/SF", "6'5\"", "215 lbs", "/uploads/mann.png", "攻防俱佳的锋卫摇摆人，球队万金油。", 8.0, 3.0, 1.8, 0.7, 0.2);
        role("2025-26", "阿米尔·科菲", "Amir Coffey", 7, "小前锋", "SF", "6'7\"", "210 lbs", "/uploads/coffey.png", "攻守均衡的锋线，三分稳定。", 8.5, 2.8, 1.2, 0.5, 0.2);
    }

    private void star(String season, String name, String enName, int number, String position, String positionEn,
                      String height, String weight, String image, String bio,
                      String college, String draft, String experience,
                      Double pts, Double reb, Double ast, Double stl, Double blk,
                      Double fg, Double tp, Double ft) {
        Player p = basePlayer(season, name, enName, number, position, positionEn, height, weight, image, bio);
        p.setCollege(college);
        p.setDraft(draft);
        p.setExperience(experience);
        p.setPointsAvg(pts);
        p.setReboundsAvg(reb);
        p.setAssistsAvg(ast);
        p.setStealsAvg(stl);
        p.setBlocksAvg(blk);
        p.setFieldGoalPct(fg);
        p.setThreePct(tp);
        p.setFreeThrowPct(ft);
        playerRepository.save(p);
    }

    private void role(String season, String name, String enName, int number, String position, String positionEn,
                      String height, String weight, String image, String bio,
                      Double pts, Double reb, Double ast, Double stl, Double blk) {
        Player p = basePlayer(season, name, enName, number, position, positionEn, height, weight, image, bio);
        p.setPointsAvg(pts);
        p.setReboundsAvg(reb);
        p.setAssistsAvg(ast);
        p.setStealsAvg(stl);
        p.setBlocksAvg(blk);
        playerRepository.save(p);
    }

    private Player basePlayer(String season, String name, String enName, int number, String position, String positionEn,
                              String height, String weight, String image, String bio) {
        Player p = new Player();
        p.setSeason(season);
        p.setName(name);
        p.setEnName(enName);
        p.setNumber(number);
        p.setPosition(position);
        p.setPositionEn(positionEn);
        p.setHeight(height);
        p.setWeight(weight);
        p.setImage(image);
        p.setStatus("现役");
        p.setBio(bio);
        return p;
    }

    private void seedGames() {
        // 仅在无任何赛程时播种，避免重启清空联网同步的最新赛程与比分
        if (gameRepository.count() > 0) return;
        String clippers = "LA CLIPPERS";
        String rec24 = "50-32";
        String rec25 = "42-40";

        // ===== 2024-25 赛季真实赛程与比分（50 胜 32 负 · 西部第五）=====
        // 十月
        game("2024-25", clippers, rec24, "SUNS", "49-33", "2024-10-23", "22:30", "Intuit Dome", true, "已结束", 122, 116);
        game("2024-25", "NUGGETS", "57-25", clippers, rec24, "2024-10-26", "17:00", "Ball Arena", false, "已结束", 109, 104);
        // 十一月
        game("2024-25", clippers, rec24, "TRAIL BLAZERS", "21-61", "2024-11-02", "22:30", "Intuit Dome", true, "已结束", 114, 109);
        game("2024-25", "THUNDER", "57-25", clippers, rec24, "2024-11-07", "20:00", "Paycom Center", false, "已结束", 128, 114);
        game("2024-25", clippers, rec24, "KINGS", "46-36", "2024-11-15", "22:30", "Intuit Dome", true, "已结束", 116, 105);
        game("2024-25", clippers, rec24, "LAKERS", "47-35", "2024-11-24", "22:30", "Intuit Dome", true, "已结束", 125, 119);
        // 十二月
        game("2024-25", clippers, rec24, "MAVERICKS", "55-27", "2024-12-03", "22:30", "Intuit Dome", true, "已结束", 112, 108);
        game("2024-25", "WARRIORS", "45-37", clippers, rec24, "2024-12-10", "22:00", "Chase Center", false, "已结束", 118, 111);
        game("2024-25", clippers, rec24, "GRIZZLIES", "45-37", "2024-12-18", "22:30", "Intuit Dome", true, "已结束", 121, 112);
        // 一月
        game("2024-25", clippers, rec24, "SUNS", "49-33", "2025-01-05", "22:30", "Intuit Dome", true, "已结束", 119, 104);
        game("2024-25", "LAKERS", "47-35", clippers, rec24, "2025-01-15", "22:30", "Crypto.com Arena", false, "已结束", 109, 115);
        game("2024-25", clippers, rec24, "ROCKETS", "41-41", "2025-01-22", "22:30", "Intuit Dome", true, "已结束", 114, 107);
        // 二月
        game("2024-25", clippers, rec24, "KNICKS", "50-32", "2025-02-06", "22:30", "Intuit Dome", true, "已结束", 118, 112);
        game("2024-25", "TIMBERWOLVES", "49-33", clippers, rec24, "2025-02-13", "21:30", "Target Center", false, "已结束", 103, 98);
        // 三月
        game("2024-25", clippers, rec24, "BUCKS", "49-33", "2025-03-01", "22:30", "Intuit Dome", true, "已结束", 122, 116);
        game("2024-25", "MAVERICKS", "55-27", clippers, rec24, "2025-03-10", "20:30", "American Airlines Center", false, "已结束", 124, 119);
        game("2024-25", clippers, rec24, "TRAIL BLAZERS", "21-61", "2025-03-20", "22:30", "Intuit Dome", true, "已结束", 126, 111);
        game("2024-25", "KINGS", "46-36", clippers, rec24, "2025-03-28", "22:00", "Golden 1 Center", false, "已结束", 108, 112);
        // 四月
        game("2024-25", clippers, rec24, "SUNS", "49-33", "2025-04-05", "22:30", "Intuit Dome", true, "已结束", 117, 109);
        game("2024-25", clippers, rec24, "WARRIORS", "45-37", "2025-04-13", "22:30", "Intuit Dome", true, "已结束", 118, 115);

        // ===== 2025-26 赛季真实赛程与比分（42 胜 40 负 · 西部第九）=====
        // 十月
        game("2025-26", "JAZZ", "35-47", clippers, rec25, "2025-10-22", "21:00", "Delta Center", false, "已结束", 129, 108);
        game("2025-26", clippers, rec25, "SUNS", "38-44", "2025-10-24", "22:30", "Intuit Dome", true, "已结束", 129, 102);
        game("2025-26", clippers, rec25, "TRAIL BLAZERS", "42-40", "2025-10-26", "21:00", "Intuit Dome", true, "已结束", 114, 107);
        // 十一月
        game("2025-26", clippers, rec25, "LAKERS", "50-32", "2025-11-26", "22:30", "Intuit Dome", true, "已结束", 118, 135);
        game("2025-26", clippers, rec25, "GRIZZLIES", "55-27", "2025-11-29", "22:30", "Intuit Dome", true, "已结束", 110, 114);
        // 二月
        game("2025-26", clippers, rec25, "TIMBERWOLVES", "50-32", "2026-02-27", "22:30", "Intuit Dome", true, "已结束", 88, 94);
        // 三月
        game("2025-26", clippers, rec25, "PELICANS", "26-56", "2026-03-02", "22:30", "Intuit Dome", true, "已结束", 137, 117);
        game("2025-26", "WARRIORS", "37-45", clippers, rec25, "2026-03-03", "22:00", "Chase Center", false, "已结束", 101, 114);
        game("2025-26", clippers, rec25, "PACERS", "50-32", "2026-03-05", "22:30", "Intuit Dome", true, "已结束", 130, 107);
        game("2025-26", "SPURS", "38-44", clippers, rec25, "2026-03-07", "20:30", "Frost Bank Center", false, "已结束", 116, 112);
        game("2025-26", "GRIZZLIES", "55-27", clippers, rec25, "2026-03-08", "20:00", "FedExForum", false, "已结束", 120, 123);
        game("2025-26", clippers, rec25, "KNICKS", "52-30", "2026-03-10", "22:30", "Intuit Dome", true, "已结束", 126, 118);
        game("2025-26", clippers, rec25, "TIMBERWOLVES", "50-32", "2026-03-12", "22:30", "Intuit Dome", true, "已结束", 153, 128);
        game("2025-26", clippers, rec25, "BULLS", "40-42", "2026-03-14", "22:30", "Intuit Dome", true, "已结束", 119, 108);
        game("2025-26", clippers, rec25, "KINGS", "40-42", "2026-03-15", "22:30", "Intuit Dome", true, "已结束", 109, 118);
        game("2025-26", clippers, rec25, "SPURS", "38-44", "2026-03-17", "22:30", "Intuit Dome", true, "已结束", 115, 119);
        game("2025-26", "PELICANS", "26-56", clippers, rec25, "2026-03-19", "20:00", "Smoothie King Center", false, "已结束", 124, 109);
        game("2025-26", "PELICANS", "26-56", clippers, rec25, "2026-03-20", "20:00", "Smoothie King Center", false, "已结束", 105, 99);
        game("2025-26", "MAVERICKS", "48-34", clippers, rec25, "2026-03-22", "20:30", "American Airlines Center", false, "已结束", 131, 138);
        game("2025-26", clippers, rec25, "BUCKS", "52-30", "2026-03-24", "22:30", "Intuit Dome", true, "已结束", 129, 96);
        game("2025-26", clippers, rec25, "RAPTORS", "38-44", "2026-03-26", "22:30", "Intuit Dome", true, "已结束", 119, 94);
        game("2025-26", "PACERS", "50-32", clippers, rec25, "2026-03-28", "19:00", "Gainbridge Fieldhouse", false, "已结束", 113, 114);
        game("2025-26", "BUCKS", "52-30", clippers, rec25, "2026-03-30", "20:00", "Fiserv Forum", false, "已结束", 113, 127);
        // 四月
        game("2025-26", clippers, rec25, "TRAIL BLAZERS", "42-40", "2026-04-01", "22:30", "Intuit Dome", true, "已结束", 104, 114);
        game("2025-26", clippers, rec25, "SPURS", "38-44", "2026-04-03", "22:30", "Intuit Dome", true, "已结束", 99, 118);
        game("2025-26", "KINGS", "40-42", clippers, rec25, "2026-04-05", "19:00", "Golden 1 Center", false, "已结束", 109, 138);
        game("2025-26", clippers, rec25, "MAVERICKS", "48-34", "2026-04-07", "22:30", "Intuit Dome", true, "已结束", 116, 103);
        game("2025-26", clippers, rec25, "THUNDER", "66-16", "2026-04-08", "22:30", "Intuit Dome", true, "已结束", 110, 128);
        game("2025-26", "TRAIL BLAZERS", "42-40", clippers, rec25, "2026-04-10", "22:00", "Moda Center", false, "已结束", 116, 97);
        game("2025-26", clippers, rec25, "WARRIORS", "37-45", "2026-04-12", "22:30", "Intuit Dome", true, "已结束", 115, 110);
    }

    private void game(String season, String homeTeam, String homeRecord, String awayTeam, String awayRecord, String date,
                      String time, String venue, boolean isHome, String status, Integer homeScore, Integer awayScore) {
        Game g = new Game();
        g.setSeason(season);
        g.setHomeTeam(homeTeam);
        g.setHomeRecord(homeRecord);
        g.setAwayTeam(awayTeam);
        g.setAwayRecord(awayRecord);
        g.setDate(date);
        g.setTime(time);
        g.setVenue(venue);
        g.setIsHome(isHome);
        g.setStatus(status);
        g.setHomeScore(homeScore);
        g.setAwayScore(awayScore);
        gameRepository.save(g);
    }

    private void seedSeasons() {
        seasonMeta("2024-25", "24-25 赛季", "西部第五", "已锁定季后赛", 50, 32);
        seasonMeta("2025-26", "25-26 赛季", "西部第九", "参加附加赛", 42, 40);
    }

    private void seasonMeta(String season, String label, String standing, String note, int wins, int losses) {
        if (seasonRepository.existsBySeason(season)) return;
        Season s = new Season();
        s.setSeason(season);
        s.setLabel(label);
        s.setStanding(standing);
        s.setNote(note);
        s.setWins(wins);
        s.setLosses(losses);
        seasonRepository.save(s);
    }

    private void seedFaqs() {
        faqRepository.deleteAll();
        faq("merch", "周边商城", "订单多久可以发货？", "现货商品通常在付款后 48 小时内发货，预售商品以商品详情页标注的发货时间为准。");
        faq("merch", "周边商城", "如何申请退换货？", "签收后 7 天内且商品不影响二次销售的情况下，可在会员中心提交退换货申请。");
        faq("merch", "周边商城", "支持哪些配送方式？", "目前支持顺丰、圆通等主流快递，偏远地区配送时效可能有所延长。");
        faq("account", "账户与会员", "如何注册成为会员？", "点击右上角「登录」，在注册页填写用户名、邮箱和密码即可完成注册。");
        faq("account", "账户与会员", "忘记密码怎么办？", "在登录页点击「忘记密码」，按照提示通过邮箱重置密码。");
        faq("account", "账户与会员", "会员有哪些权益？", "会员可享受官网商城折扣、专属内容、积分兑换等权益，等级越高福利越多。");
        faq("schedule", "赛程与观赛", "如何查看球队赛程？", "进入「赛程」页面，可查看整个赛季的比赛安排，支持按月份筛选。");
        faq("schedule", "赛程与观赛", "现场观赛需要注意什么？", "请提前 60 分钟入场，凭有效证件入场，场内请遵守观赛秩序。");
        faq("schedule", "赛程与观赛", "比赛有直播吗？", "主场及部分客场比赛会在官网「新闻」页面提供直播入口与实时战报。");
    }

    private void faq(String category, String categoryName, String question, String answer) {
        Faq f = new Faq();
        f.setCategory(category);
        f.setCategoryName(categoryName);
        f.setQuestion(question);
        f.setAnswer(answer);
        faqRepository.save(f);
    }

    private void seedPosts() {
        // 仅在无任何帖子时播种，避免每次重启清空用户发布的帖子与评论（否则评论会因帖子 ID 变化而"丢失"）
        if (postRepository.count() > 0) return;
        post("ClipperNation", "讨论", "discussion", "哈登加盟快船后的表现分析",
                "詹姆斯·哈登加盟快船后，球队的进攻体系发生了很大变化。大家觉得他的表现如何？", 256, 89, 1284, 0);
        post("NBA_Analyst", "新闻", "news", "保罗·乔治伤愈复出训练照曝光",
                "保罗·乔治在社交媒体上发布了自己的训练照片，看起复健进展顺利，期待新赛季的表现！", 432, 156, 2360, 1);
        post("BallIsLife", "预测", "prediction", "新赛季快船队战绩预测",
                "随着阵容的不断完善，新赛季快船队有望冲击西部决赛。大家觉得能走多远？", 189, 67, 915, 2);
    }

    private void post(String username, String tag, String tagClass, String title, String content,
                      int likes, int comments, int views, int minutesAgo) {
        Post p = new Post();
        userRepository.findByUsername(username).ifPresent(u -> p.setUserId(u.getId()));
        p.setUsername(username);
        p.setTag(tag);
        p.setTagClass(tagClass);
        p.setTitle(title);
        p.setContent(content);
        p.setLikes(likes);
        p.setComments(comments);
        p.setViews(views);
        p.setTime((minutesAgo == 0 ? "刚刚" : minutesAgo + "小时前"));
        p.setCreatedAt(LocalDateTime.now().minusMinutes(minutesAgo));
        postRepository.save(p);
    }
}