<template>
  <div class="homepage">
    <!-- Hero 区域 -->
    <section class="hero" id="hero">
      <div class="hero-bg">
        <video class="hero-video" :src="heroVideo" autoplay muted loop playsinline></video>
        <div class="hero-video-shade"></div>
      </div>

      <div class="wrapper hero-inner">
        <div class="hero-copy">
          <span class="hero-badge">NBA · WESTERN CONFERENCE</span>
          <h1 class="hero-title" aria-label="四大超巨齐聚洛杉矶">
            <span
              v-for="(ch, i) in heroTitleChars"
              :key="i"
              class="title-char"
              :class="{ 'text-gradient': i >= 4 }"
              :style="{ '--i': i }"
            >{{ ch }}</span>
          </h1>
          <p class="hero-subtitle">
            <span class="typing-text">{{ currentText }}</span><span class="cursor">|</span>
          </p>

          <div class="hero-stats">
            <div class="hero-stat">
              <span class="stat-value">50</span>
              <span class="stat-label">胜场 WINS</span>
            </div>
            <div class="hero-stat">
              <span class="stat-value">32</span>
              <span class="stat-label">负场 LOSSES</span>
            </div>
            <div class="hero-stat">
              <span class="stat-value">61%</span>
              <span class="stat-label">胜率 WIN RATE</span>
            </div>
          </div>

          <div class="cta-buttons">
            <router-link to="/team" class="btn btn-primary">探索球队 <i class="iconfont icon-arrow-right"></i></router-link>
            <a class="btn btn-outline" href="#skills" @click.prevent="scrollToSection('skills')"><i class="iconfont icon-play"></i> 球星技术</a>
          </div>
        </div>

        <!-- 动态球星视觉 -->
        <div class="hero-player">
          <div class="player-visual" :key="'v-' + currentStar">
            <img :src="stars[currentStar].img" :alt="stars[currentStar].name" class="player-img" />
          </div>

          <div class="player-caption" :key="'c-' + currentStar">
            <span class="player-no">{{ stars[currentStar].no }}</span>
            <div class="player-caption-text">
              <strong>{{ stars[currentStar].name }}</strong>
              <span>{{ stars[currentStar].en }}</span>
            </div>
          </div>

          <div class="player-switch">
            <button
              v-for="(s, i) in stars"
              :key="i"
              class="player-dot"
              :class="{ active: i === currentStar }"
              @click="goStar(i)"
            >
              {{ String(i + 1).padStart(2, '0') }}
            </button>
          </div>
        </div>
      </div>

      <div class="scroll-indicator" @click="scrollToNext">
        <span>SCROLL</span>
        <div class="scroll-line"></div>
      </div>
    </section>

    <!-- 四大球星：置于最显眼位置 -->
    <section id="stars">
      <LosFour />
    </section>

    <!-- 精彩瞬间轮播图 -->
    <BanNer />

    <!-- 球星技术特点 -->
    <section id="skills">
      <PlayerSkills />
    </section>

    <!-- 球队数据：虎扑式历年战绩 -->
    <section class="section stats-section" id="stats">
      <div class="wrapper">
        <div class="section-header">
          <span class="section-tag">TEAM STATS</span>
          <h2 class="section-title">历年战绩</h2>
          <p class="section-desc">按赛季一览快船战绩</p>
        </div>
        <SeasonSwitcher />
        <div class="season-table card">
          <div class="season-head">
            <span class="col col-season">赛季</span>
            <span class="col col-record">常规赛战绩</span>
            <span class="col">胜率</span>
            <span class="col">分区排名</span>
            <span class="col">季后赛</span>
          </div>
          <div class="season-row" v-for="(s, index) in seasonData" :key="index">
            <div class="cell col-season">
              <span class="season-year">{{ s.season }}</span>
              <span v-if="s.latest" class="season-badge">当前赛季</span>
            </div>
            <div class="cell col-record"><b>{{ s.wins }}</b> 胜 - <b>{{ s.losses }}</b> 负</div>
            <div class="cell">{{ s.pct }}</div>
            <div class="cell">{{ s.rank }}</div>
            <div class="cell">{{ s.playoff }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 最新新闻 -->
    <section class="section news-section" id="news">
      <div class="wrapper">
        <div class="section-header">
          <span class="section-tag">LATEST NEWS</span>
          <h2 class="section-title">最新新闻</h2>
          <p class="section-desc">第一时间获取球队资讯</p>
        </div>
        <div class="news-grid">
          <router-link to="/news" class="news-card featured card" v-if="latestNews.length">
            <div class="news-image">
              <img :src="latestNews[0].image" :alt="latestNews[0].title" />
              <div class="news-overlay"></div>
              <span class="news-badge">HOT</span>
            </div>
            <div class="news-content">
              <span class="news-date">{{ latestNews[0].date }}</span>
              <h3>{{ latestNews[0].title }}</h3>
              <p>{{ latestNews[0].content }}</p>
              <span class="read-more">阅读更多 <i class="iconfont icon-arrow-right"></i></span>
            </div>
          </router-link>
          <div class="news-list">
            <router-link to="/news" class="news-item card" v-for="(news, index) in latestNews.slice(1)" :key="index">
              <div class="news-thumb"><img :src="news.image" :alt="news.title" /></div>
              <div class="news-info">
                <span class="news-date">{{ news.date }}</span>
                <h4>{{ news.title }}</h4>
                <p>{{ news.content }}</p>
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- 快捷导航 -->
    <section class="section quick-nav-section">
      <div class="wrapper">
        <div class="section-header">
          <span class="section-tag">QUICK ACCESS</span>
          <h2 class="section-title">快速导航</h2>
        </div>
        <div class="quick-nav-grid">
          <router-link v-for="(item, index) in quickNavItems" :key="index" :to="item.path" class="nav-item card">
            <div class="nav-icon"><el-icon><component :is="item.icon" /></el-icon></div>
            <span class="nav-label">{{ item.label }}</span>
          </router-link>
        </div>
      </div>
    </section>

    <!-- 合作伙伴 -->
    <section class="section partners-section">
      <div class="wrapper">
        <div class="section-header">
          <span class="section-tag">PARTNERS</span>
          <h2 class="section-title">合作伙伴</h2>
        </div>
        <div class="partners-grid">
          <div class="partner-logo card" v-for="(partner, index) in partners" :key="index">
            <img :src="partner.logo" :alt="partner.name" />
          </div>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <FooTer />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import BanNer from '@/components/BanNer.vue'
import LosFour from '@/components/LosFour.vue'
import PlayerSkills from '@/components/PlayerSkills.vue'
import FooTer from '@/components/FooTer.vue'
import SeasonSwitcher from '@/components/SeasonSwitcher.vue'
import { useSeasons } from '@/composables/useSeasons'
import { getNews } from '@/api'

import brand1 from '@/assets/uploads/brand1.jpg'
import brand2 from '@/assets/uploads/brand2.jpg'
import brand3 from '@/assets/uploads/brand3.jpg'

import harden from '@/assets/uploads/harden.png'
import westbrook from '@/assets/uploads/westbrook.png'
import george from '@/assets/uploads/george.png'
import kawhi from '@/assets/uploads/kawhi.png'
import heroVideo from '@/assets/videos/hero-bg.mp4'

const isMounted = ref(false)

const stars = [
  { no: '01', name: '詹姆斯·哈登', en: 'James Harden', img: harden },
  { no: '00', name: '拉塞尔·威斯布鲁克', en: 'Russell Westbrook', img: westbrook },
  { no: '13', name: '保罗·乔治', en: 'Paul George', img: george },
  { no: '02', name: '科怀·伦纳德', en: 'Kawhi Leonard', img: kawhi }
]

const heroTitleChars = ['四', '大', '超', '巨', '齐', '聚', '洛', '杉', '矶']

const currentStar = ref(0)
let starTimer = null

const nextStar = () => {
  currentStar.value = (currentStar.value + 1) % stars.length
}

const goStar = (i) => {
  currentStar.value = i
  restartStar()
}

const startStar = () => {
  stopStar()
  starTimer = setInterval(nextStar, 4600)
}

const stopStar = () => {
  if (starTimer) clearInterval(starTimer)
  starTimer = null
}

const restartStar = () => startStar()

const typingTexts = ['冠军争夺者 CHAMPIONSHIP CONTENDERS', '四巨头阵容 FOUR SUPERSTARS', '未来篮球体验 THE FUTURE']
const currentText = ref('')
const textIndex = ref(0)
const charIndex = ref(0)
const isDeleting = ref(false)

// 虎扑式历年战绩：由后端 /api/seasons 汇总，按赛季倒序展示
const { seasons, currentSeason, loadSeasons } = useSeasons()

const seasonData = computed(() =>
  seasons.value.map((s) => {
    const total = s.wins + s.losses
    const pct = total > 0 ? ((s.wins / total) * 100).toFixed(1) + '%' : '—'
    return {
      season: s.label || s.value,
      wins: s.wins,
      losses: s.losses,
      pct,
      rank: s.standing || '—',
      playoff: s.note || '—',
      latest: s.value === currentSeason.value
    }
  })
)

const latestNews = ref([])

const partners = ref([
  { name: 'Nike', logo: brand1 },
  { name: 'Adidas', logo: brand2 },
  { name: 'Under Armour', logo: brand3 }
])

const quickNavItems = ref([
  { label: '球队', path: '/team', icon: 'User' },
  { label: '赛程', path: '/schedule', icon: 'Calendar' },
  { label: '个人中心', path: '/member-center', icon: 'Avatar' },
  { label: '商城', path: '/shopping', icon: 'ShoppingCart' },
  { label: '社区', path: '/community', icon: 'ChatDotRound' }
])

const scrollToSection = (id) => {
  const section = document.getElementById(id)
  if (section) section.scrollIntoView({ behavior: 'smooth' })
}

const scrollToNext = () => {
  scrollToSection('stars')
}

const typeText = () => {
  if (!isMounted.value) return

  const current = typingTexts[textIndex.value]
  if (!isDeleting.value) {
    currentText.value = current.substring(0, charIndex.value + 1)
    charIndex.value++
    if (charIndex.value === current.length) {
      setTimeout(() => { isDeleting.value = true }, 2000)
      return
    }
  } else {
    currentText.value = current.substring(0, charIndex.value - 1)
    charIndex.value--
    if (charIndex.value === 0) {
      isDeleting.value = false
      textIndex.value = (textIndex.value + 1) % typingTexts.length
      return
    }
  }
  setTimeout(typeText, isDeleting.value ? 50 : 100)
}

const fetchLatestNews = async () => {
  try {
    const data = (await getNews({ page: 1, size: 3 })) || {}
    latestNews.value = data.list || []
  } catch (e) {
    latestNews.value = []
  }
}

onMounted(() => {
  isMounted.value = true
  typeText()
  fetchLatestNews()
  startStar()
  loadSeasons()
})

onUnmounted(() => {
  isMounted.value = false
  stopStar()
})
</script>

<style scoped>
.homepage {
  min-height: 100vh;
  background-color: var(--bg-base);
}

/* ============ Hero ============ */
.hero {
  position: relative;
  min-height: 92vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 100px 0 80px;
}

.hero-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.hero-video {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hero-video-shade {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(8, 10, 14, 0.74) 0%, rgba(8, 10, 14, 0.55) 45%, rgba(8, 10, 14, 0.86) 100%);
}

.hero-inner {
  position: relative;
  z-index: 2;
  display: grid;
  grid-template-columns: 1.05fr 0.95fr;
  gap: 48px;
  align-items: center;
  text-align: left;
  width: 100%;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  padding: 9px 22px;
  background: rgba(200, 16, 46, 0.12);
  border: 1px solid rgba(200, 16, 46, 0.32);
  border-radius: var(--radius-full);
  font-family: var(--font-display);
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 3px;
  color: var(--accent);
  margin-bottom: 28px;
}

.hero-title {
  font-family: var(--font-display);
  display: flex;
  flex-wrap: wrap;
  font-size: clamp(2.4rem, 6vw, 5rem);
  font-weight: 800;
  line-height: 1.15;
  letter-spacing: 2px;
  color: var(--text-primary);
  margin: 0 0 20px;
}

.title-char {
  display: inline-block;
  opacity: 0;
  white-space: pre;
  animation: charReveal 0.6s cubic-bezier(0.22, 1, 0.36, 1) forwards;
  animation-delay: calc(var(--i) * 0.09s + 0.35s);
}

@keyframes charReveal {
  from {
    opacity: 0;
    transform: translateY(36px) rotateX(-90deg);
  }
  to {
    opacity: 1;
    transform: translateY(0) rotateX(0);
  }
}

.hero-subtitle {
  font-family: var(--font-display);
  font-size: 1.15rem;
  letter-spacing: 2px;
  color: var(--text-secondary);
  margin-bottom: 40px;
  height: 1.8em;
}

.typing-text {
  color: var(--text-primary);
}

.cursor {
  color: var(--accent);
  animation: cursorBlink 1s step-end infinite;
}

@keyframes cursorBlink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

.hero-stats {
  display: flex;
  justify-content: flex-start;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 40px;
}

.hero-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 150px;
  padding: 22px 30px;
  background: var(--glass-bg);
  backdrop-filter: blur(16px);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-lg);
}

.hero-stat .stat-value {
  font-family: var(--font-display);
  font-size: 2.6rem;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1;
}

.hero-stat .stat-label {
  font-size: 12px;
  letter-spacing: 2px;
  color: var(--text-muted);
  margin-top: 6px;
}

.cta-buttons {
  display: flex;
  justify-content: flex-start;
  gap: 16px;
  flex-wrap: wrap;
}

.scroll-indicator {
  position: absolute;
  bottom: 28px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  z-index: 2;
}

.scroll-indicator span {
  font-family: var(--font-display);
  font-size: 10px;
  letter-spacing: 3px;
  color: var(--text-faint);
}

.scroll-line {
  width: 2px;
  height: 44px;
  border-radius: 2px;
  background: linear-gradient(180deg, var(--accent), transparent);
  position: relative;
}

.scroll-line::after {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 7px;
  height: 7px;
  background: var(--accent);
  border-radius: 50%;
  animation: scrollBounce 2s ease-in-out infinite;
}

@keyframes scrollBounce {
  0%, 100% { transform: translateX(-50%) translateY(0); opacity: 1; }
  50% { transform: translateX(-50%) translateY(36px); opacity: 0.4; }
}

/* ============ Hero 动态球星 ============ */
.hero-copy {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.hero-player {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.player-visual {
  position: relative;
  width: 100%;
  max-width: 460px;
}

.player-img {
  display: block;
  width: 100%;
  height: auto;
  opacity: 0.78;
  -webkit-mask-image: linear-gradient(to bottom, #000 68%, transparent 100%);
  mask-image: linear-gradient(to bottom, #000 68%, transparent 100%);
  filter: brightness(0.88) saturate(0.78) contrast(1.06) drop-shadow(0 16px 30px rgba(0, 0, 0, 0.5));
  animation: playerKen 4.6s ease-out forwards, playerFade 0.9s ease;
}

.player-caption {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 22px;
  z-index: 2;
}

.player-no {
  font-family: var(--font-display);
  font-size: 56px;
  font-weight: 800;
  line-height: 1;
  color: transparent;
  -webkit-text-stroke: 2px rgba(255, 255, 255, 0.6);
}

.player-caption-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.player-caption-text strong {
  font-family: var(--font-display);
  font-size: 26px;
  font-weight: 700;
  color: #fff;
}

.player-caption-text span {
  font-family: var(--font-display);
  font-size: 13px;
  letter-spacing: 2px;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.6);
}

.player-switch {
  display: flex;
  gap: 10px;
  margin-top: 26px;
}

.player-dot {
  width: 46px;
  height: 46px;
  border-radius: 50%;
  border: 1px solid var(--glass-border);
  background: var(--glass-bg);
  backdrop-filter: blur(10px);
  font-family: var(--font-display);
  font-size: 13px;
  font-weight: 700;
  color: var(--text-muted);
  cursor: pointer;
  transition: all var(--transition);
}

.player-dot:hover {
  color: #fff;
  border-color: rgba(255, 255, 255, 0.4);
}

.player-dot.active {
  background: var(--accent);
  border-color: var(--accent);
  color: #fff;
}

@keyframes playerKen {
  from {
    transform: scale(0.96);
  }
  to {
    transform: scale(1.08);
  }
}

@keyframes playerFade {
  from {
    opacity: 0;
  }
  to {
    opacity: 0.78;
  }
}

@media (max-width: 992px) {
  .hero-inner {
    grid-template-columns: 1fr;
    gap: 44px;
    text-align: center;
  }

  .hero-copy {
    align-items: center;
  }

  .hero-stats,
  .cta-buttons {
    justify-content: center;
  }

  .hero-player {
    order: 2;
  }
}

/* ============ 通用区块 ============ */
.section {
  padding: 90px 0;
}

.section-header {
  text-align: center;
  margin-bottom: 56px;
}

.stats-section {
  background: var(--bg-elevated);
}

/* ============ 历届战绩 ============ */
.season-table {
  padding: 0;
  overflow-x: auto;
  border-radius: var(--radius-lg);
}

.season-head,
.season-row {
  display: grid;
  grid-template-columns: 1.2fr 1.6fr 1fr 1fr 1fr;
  align-items: center;
  min-width: 620px;
}

.season-head {
  padding: 16px 24px;
  background: var(--bg-hover);
  border-bottom: 1px solid var(--border);
}

.season-head .col {
  font-family: var(--font-display);
  font-size: 12px;
  letter-spacing: 1px;
  color: var(--text-muted);
  text-transform: uppercase;
}

.season-row {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
  transition: background var(--transition);
}

.season-row:last-child {
  border-bottom: none;
}

.season-row:hover {
  background: var(--bg-hover);
}

.season-row .cell {
  font-size: 15px;
  color: var(--text-secondary);
}

.season-row .cell b {
  font-family: var(--font-display);
  font-size: 18px;
  color: var(--text-primary);
}

.season-year {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
  margin-right: 10px;
}

.season-badge {
  display: inline-block;
  padding: 3px 10px;
  background: var(--gradient-brand);
  border-radius: var(--radius-full);
  font-size: 11px;
  font-weight: 600;
  color: #fff;
  vertical-align: middle;
}

/* ============ 新闻 ============ */
.news-grid {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 24px;
}

.news-card {
  position: relative;
  display: block;
  text-decoration: none;
  color: inherit;
  overflow: hidden;
}

.news-card.featured {
  grid-row: span 2;
}

.news-image {
  position: relative;
  height: 320px;
  overflow: hidden;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.news-card:hover .news-image img {
  transform: scale(1.06);
}

.news-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(11, 13, 18, 0.15) 0%, rgba(11, 13, 18, 0.9) 100%);
}

.news-badge {
  position: absolute;
  top: 16px;
  right: 16px;
  padding: 5px 14px;
  background: var(--gradient-brand);
  border-radius: var(--radius-full);
  font-family: var(--font-display);
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 2px;
  color: #fff;
}

.news-content {
  padding: 24px;
}

.featured .news-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 28px;
}

.news-date {
  font-size: 13px;
  color: var(--accent);
  margin-bottom: 8px;
}

.news-content h3 {
  font-family: var(--font-display);
  font-size: 1.4rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 10px;
  line-height: 1.35;
}

.news-content p {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0 0 16px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.read-more {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-family: var(--font-display);
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 1px;
  color: var(--accent);
  transition: gap var(--transition);
}

.news-card:hover .read-more {
  gap: 14px;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.news-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  text-decoration: none;
  color: inherit;
}

.news-thumb {
  width: 120px;
  height: 84px;
  flex-shrink: 0;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.news-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition);
}

.news-item:hover .news-thumb img {
  transform: scale(1.08);
}

.news-info {
  flex: 1;
  min-width: 0;
}

.news-info h4 {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 6px;
  line-height: 1.4;
}

.news-info p {
  font-size: 13px;
  color: var(--text-muted);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin: 0;
}

/* ============ 快捷导航 ============ */
.quick-nav-section {
  background: var(--bg-elevated);
}

.quick-nav-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14px;
  padding: 32px 16px;
  text-decoration: none;
  color: inherit;
}

.nav-icon {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-hover);
  border-radius: var(--radius-md);
  font-size: 1.7rem;
  color: var(--text-secondary);
  transition: all var(--transition);
}

.nav-item:hover .nav-icon {
  background: var(--accent);
  color: #fff;
  transform: scale(1.08);
}

.nav-label {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-secondary);
}

/* ============ 合作伙伴 ============ */
.partners-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  max-width: 960px;
  margin: 0 auto;
}

.partner-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 44px;
}

.partner-logo img {
  max-width: 150px;
  max-height: 70px;
  filter: grayscale(100%);
  opacity: 0.6;
  transition: all var(--transition);
}

.partner-logo:hover img {
  filter: grayscale(0%);
  opacity: 1;
}

/* ============ 响应式 ============ */
@media (max-width: 992px) {
  .news-grid {
    grid-template-columns: 1fr;
  }

  .quick-nav-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 640px) {
  .quick-nav-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .partners-grid {
    grid-template-columns: 1fr;
  }

  .news-item {
    flex-direction: column;
  }

  .news-thumb {
    width: 100%;
    height: 160px;
  }
}
</style>