<template>
  <div class="cyber-home">
    <!-- 扫描线效果 -->
    <div class="scanline"></div>
    
    <!-- 粒子背景 -->
    <canvas id="particles"></canvas>
    
    <!-- 导航栏 -->
    <nav class="cyber-nav" :class="{ scrolled: isScrolled }">
      <div class="nav-content">
        <div class="nav-logo">
          <span class="logo-icon">🏀</span>
          <span class="logo-text">LA CLIPPERS</span>
        </div>
        <div class="nav-links">
          <a href="#hero" class="nav-link active">首页</a>
          <a href="#intro" class="nav-link">球队</a>
          <a href="#stars" class="nav-link">球星</a>
          <a href="#dunks" class="nav-link">扣篮</a>
          <a href="#stats" class="nav-link">数据</a>
          <a href="#news" class="nav-link">新闻</a>
        </div>
        <div class="nav-indicator"></div>
      </div>
    </nav>

    <!-- Hero区域 -->
    <section class="hero-section" id="hero" @mousemove="handleMouseMove">
      <div class="hero-grid-bg"></div>
      <div class="hero-glow-shapes">
        <div class="glow-circle glow-1" :style="glow1Style"></div>
        <div class="glow-circle glow-2" :style="glow2Style"></div>
        <div class="glow-circle glow-3" :style="glow3Style"></div>
      </div>
      
      <div class="hero-content">
        <div class="hero-badge">NBA // WESTERN CONFERENCE</div>
        <h1 class="hero-title">
          <span class="title-word">LOS</span>
          <span class="title-word accent">ANGELES</span>
          <span class="title-word">CLIPPERS</span>
        </h1>
        <p class="hero-subtitle">
          <span class="typing-text">{{ currentText }}</span>
          <span class="cursor">|</span>
        </p>
        <div class="hero-stats">
          <div class="hero-stat">
            <span class="stat-value">50</span>
            <span class="stat-label">WINS</span>
          </div>
          <div class="stat-divider"></div>
          <div class="hero-stat">
            <span class="stat-value">32</span>
            <span class="stat-label">LOSSES</span>
          </div>
          <div class="stat-divider"></div>
          <div class="hero-stat">
            <span class="stat-value">61%</span>
            <span class="stat-label">WIN RATE</span>
          </div>
        </div>
        <div class="cta-buttons">
          <a href="#stars" class="cyber-btn cyber-btn-primary">
            <span>EXPLORE TEAM</span>
            <i class="iconfont icon-arrow-right"></i>
          </a>
          <a href="#dunks" class="cyber-btn">
            <span>WATCH HIGHLIGHTS</span>
            <i class="iconfont icon-play"></i>
          </a>
        </div>
      </div>
      
      <div class="scroll-indicator" @click="scrollToNext">
        <div class="scroll-line"></div>
        <span>SCROLL</span>
      </div>
    </section>

    <!-- 球队简介 -->
    <section class="intro-section" id="intro">
      <div class="section-grid-bg"></div>
      <div class="wrapper">
        <div class="section-header">
          <span class="section-tag">ABOUT CLIPPERS</span>
          <h2 class="section-title">关于快船队</h2>
          <p class="section-desc">承载城市梦想，冲击总冠军</p>
        </div>
        <div class="intro-content">
          <div class="intro-text">
            <p class="highlight-text">EXPERIENCE THE FUTURE OF BASKETBALL</p>
            <p>洛杉矶快船队成立于1970年，现隶属于NBA西部联盟太平洋赛区。球队拥有詹姆斯·哈登、拉塞尔·威斯布鲁克、保罗·乔治和科怀·伦纳德等超级球星，是联盟中最具竞争力的球队之一。</p>
            <p>球队以其快速的进攻风格和坚韧的防守而闻名，主场是位于洛杉矶的Crypto.com球馆。</p>
            <div class="intro-features">
              <div class="feature-card">
                <i class="iconfont icon-trophy"></i>
                <span>CHAMPIONSHIP</span>
              </div>
              <div class="feature-card">
                <i class="iconfont icon-star"></i>
                <span>ALL-STARS</span>
              </div>
              <div class="feature-card">
                <i class="iconfont icon-global"></i>
                <span>GLOBAL FANS</span>
              </div>
            </div>
          </div>
          <div class="intro-image">
            <div class="image-container">
              <img :src="teamImage" alt="LA Clippers" />
              <div class="image-grid-overlay"></div>
              <div class="image-border-glow"></div>
              <div class="image-corner tl"></div>
              <div class="image-corner tr"></div>
              <div class="image-corner bl"></div>
              <div class="image-corner br"></div>
            </div>
            <div class="floating-badge">EST. 1970</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 四大球星 -->
    <BanNer />
    <LosFour />

    <!-- 扣篮展示 -->
    <DuNk />

    <!-- 球队数据 -->
    <section class="stats-section" id="stats">
      <div class="stats-grid-bg"></div>
      <div class="wrapper">
        <div class="section-header light">
          <span class="section-tag">TEAM STATS</span>
          <h2 class="section-title">球队数据</h2>
          <p class="section-desc">用数据见证实力</p>
        </div>
        <div class="stats-grid">
          <div class="stat-card" v-for="(stat, index) in statsData" :key="index">
            <div class="stat-icon">
              <i :class="stat.icon"></i>
            </div>
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
            <div class="stat-bar">
              <div class="stat-fill" :style="{ width: stat.percentage + '%' }"></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 最新新闻 -->
    <section class="news-section" id="news">
      <div class="news-grid-bg"></div>
      <div class="wrapper">
        <div class="section-header">
          <span class="section-tag">LATEST NEWS</span>
          <h2 class="section-title">最新新闻</h2>
          <p class="section-desc">第一时间获取球队资讯</p>
        </div>
        <div class="news-grid">
          <div class="news-card featured" v-if="latestNews.length">
            <div class="news-image">
              <img :src="latestNews[0].image" :alt="latestNews[0].title" />
              <div class="news-overlay"></div>
              <div class="news-badge">HOT</div>
            </div>
            <div class="news-content">
              <span class="news-date">{{ latestNews[0].date }}</span>
              <h3>{{ latestNews[0].title }}</h3>
              <p>{{ latestNews[0].content }}</p>
              <a href="#" class="read-more">
                READ MORE
                <i class="iconfont icon-arrow-right"></i>
              </a>
            </div>
          </div>
          <div class="news-list">
            <div class="news-item" v-for="(news, index) in latestNews.slice(1)" :key="index">
              <div class="news-thumb">
                <img :src="news.image" :alt="news.title" />
                <div class="thumb-overlay"></div>
              </div>
              <div class="news-info">
                <span class="news-date">{{ news.date }}</span>
                <h4>{{ news.title }}</h4>
                <p>{{ news.content }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 合作伙伴 -->
    <section class="partners-section">
      <div class="partners-grid-bg"></div>
      <div class="wrapper">
        <div class="section-header">
          <span class="section-tag">PARTNERS</span>
          <h2 class="section-title">合作伙伴</h2>
        </div>
        <div class="partners-track">
          <div class="partners-grid">
            <div class="partner-logo" v-for="(partner, index) in partners" :key="index">
              <img :src="partner.logo" :alt="partner.name" />
            </div>
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
import DuNk from '@/components/DuNk.vue'
import FooTer from '@/components/FooTer.vue'

import teamImage from '@/assets/uploads/fourtogether.jpg'
import news1 from '@/assets/uploads/news1.jpg'
import news2 from '@/assets/uploads/news2.jpg'
import news3 from '@/assets/uploads/news3.jpg'
import brand1 from '@/assets/uploads/brand1.jpg'
import brand2 from '@/assets/uploads/brand2.jpg'
import brand3 from '@/assets/uploads/brand3.jpg'

const scrollProgress = ref(0)
const isScrolled = ref(false)
const mouseX = ref(0)
const mouseY = ref(0)

const glow1Style = computed(() => ({
  transform: `translate(${mouseX.value * 0.05}px, ${mouseY.value * 0.05}px)`
}))
const glow2Style = computed(() => ({
  transform: `translate(${mouseX.value * -0.03}px, ${mouseY.value * -0.03}px)`
}))
const glow3Style = computed(() => ({
  transform: `translate(${mouseX.value * 0.04}px, ${mouseY.value * -0.04}px)`
}))

const typingTexts = ['EXPERIENCE THE FUTURE', 'CHAMPIONSHIP CONTENDERS', 'FOUR SUPERSTARS']
const currentText = ref('')
const textIndex = ref(0)
const charIndex = ref(0)
const isDeleting = ref(false)

const statsData = ref([
  { label: 'POINTS', value: '118.5', icon: 'iconfont icon-basketball', percentage: 85 },
  { label: 'ASSISTS', value: '26.8', icon: 'iconfont icon-assist', percentage: 78 },
  { label: 'REBOUNDS', value: '45.2', icon: 'iconfont icon-rebound', percentage: 72 },
  { label: 'FG%', value: '48.5%', icon: 'iconfont icon-shooting', percentage: 82 },
  { label: '3P%', value: '38.2%', icon: 'iconfont icon-three-point', percentage: 75 },
  { label: 'DEF RATING', value: '106.5', icon: 'iconfont icon-defense', percentage: 68 }
])

const latestNews = ref([
  {
    title: '快船队签下詹姆斯·哈登，组建豪华阵容',
    content: '洛杉矶快船队官方宣布，球队已经签下了超级巨星詹姆斯·哈登，这将为球队带来更强大的进攻火力，新赛季剑指总冠军。',
    date: '2024-01-15',
    image: news1
  },
  {
    title: '保罗·乔治伤愈归来，状态火热',
    content: '保罗·乔治已经完全康复，在训练中展现出极佳的状态，有望在新赛季带领球队取得突破。',
    date: '2024-01-12',
    image: news2
  },
  {
    title: '伦纳德领衔防守效率榜',
    content: '科怀·伦纳德在最新的防守效率榜上高居榜首，展现出顶级的防守能力。',
    date: '2024-01-10',
    image: news3
  }
])

const partners = ref([
  { name: 'Nike', logo: brand1 },
  { name: 'Adidas', logo: brand2 },
  { name: 'Under Armour', logo: brand3 }
])

const handleScroll = () => {
  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight - window.innerHeight
  scrollProgress.value = (scrollTop / docHeight) * 100
  isScrolled.value = scrollTop > 50
}

const handleMouseMove = (e) => {
  mouseX.value = e.clientX - window.innerWidth / 2
  mouseY.value = e.clientY - window.innerHeight / 2
}

const scrollToNext = () => {
  const nextSection = document.getElementById('intro')
  if (nextSection) {
    nextSection.scrollIntoView({ behavior: 'smooth' })
  }
}

const typeText = () => {
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

let particlesCanvas, particlesCtx
let particles = []

const initParticles = () => {
  particlesCanvas = document.getElementById('particles')
  if (!particlesCanvas) return

  particlesCtx = particlesCanvas.getContext('2d')
  particlesCanvas.width = window.innerWidth
  particlesCanvas.height = window.innerHeight

  for (let i = 0; i < 80; i++) {
    particles.push({
      x: Math.random() * particlesCanvas.width,
      y: Math.random() * particlesCanvas.height,
      vx: (Math.random() - 0.5) * 0.5,
      vy: (Math.random() - 0.5) * 0.5,
      radius: Math.random() * 2 + 1,
      color: `rgba(0, 212, 255, ${Math.random() * 0.5 + 0.2})`
    })
  }
}

const animateParticles = () => {
  if (!particlesCtx) return

  particlesCtx.clearRect(0, 0, particlesCanvas.width, particlesCanvas.height)

  particles.forEach(p => {
    p.x += p.vx
    p.y += p.vy

    if (p.x < 0 || p.x > particlesCanvas.width) p.vx *= -1
    if (p.y < 0 || p.y > particlesCanvas.height) p.vy *= -1

    particlesCtx.beginPath()
    particlesCtx.arc(p.x, p.y, p.radius, 0, Math.PI * 2)
    particlesCtx.fillStyle = p.color
    particlesCtx.fill()
  })

  particles.forEach((p1, i) => {
    particles.forEach((p2, j) => {
      if (j <= i) return
      const dx = p1.x - p2.x
      const dy = p1.y - p2.y
      const dist = Math.sqrt(dx * dx + dy * dy)
      if (dist < 150) {
        particlesCtx.beginPath()
        particlesCtx.moveTo(p1.x, p1.y)
        particlesCtx.lineTo(p2.x, p2.y)
        particlesCtx.strokeStyle = `rgba(0, 212, 255, ${0.1 * (1 - dist / 150)})`
        particlesCtx.stroke()
      }
    })
  })

  requestAnimationFrame(animateParticles)
}

const handleResize = () => {
  if (particlesCanvas) {
    particlesCanvas.width = window.innerWidth
    particlesCanvas.height = window.innerHeight
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  window.addEventListener('mousemove', handleMouseMove)
  window.addEventListener('resize', handleResize)

  initParticles()
  animateParticles()
  typeText()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('mousemove', handleMouseMove)
  window.removeEventListener('resize', handleResize)
})
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;600;700;800;900&family=Rajdhani:wght@300;400;500;600;700&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html {
  scroll-behavior: smooth;
}

body {
  background-color: #0a0a0f;
  color: white;
  font-family: 'Rajdhani', sans-serif;
  overflow-x: hidden;
}

::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #0a0a0f;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(180deg, #00d4ff, #6c5ce7);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(180deg, #6c5ce7, #ff00ff);
}

.wrapper {
  width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
</style>

<style scoped>
.cyber-home {
  position: relative;
  min-height: 100vh;
  background-color: #0a0a0f;
}

/* 扫描线 */
.scanline {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, #00d4ff, transparent);
  animation: scanLine 4s linear infinite;
  opacity: 0.3;
  z-index: 1000;
  pointer-events: none;
}

@keyframes scanLine {
  0% { transform: translateY(-100%); }
  100% { transform: translateY(100vh); }
}

/* 粒子背景 */
#particles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}

/* 导航栏 */
.cyber-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  padding: 20px 50px;
  transition: all 0.3s;
}

.cyber-nav.scrolled {
  padding: 15px 50px;
  background: rgba(10, 10, 15, 0.9);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 212, 255, 0.2);
}

.nav-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 32px;
}

.logo-text {
  font-family: 'Orbitron', sans-serif;
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #00d4ff 0%, #6c5ce7 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 2px;
}

.nav-links {
  display: flex;
  gap: 40px;
}

.nav-link {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  font-family: 'Orbitron', sans-serif;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 2px;
  transition: all 0.3s;
  position: relative;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #00d4ff, #6c5ce7);
  transition: width 0.3s;
}

.nav-link:hover::after,
.nav-link.active::after {
  width: 100%;
}

.nav-link:hover,
.nav-link.active {
  color: #00d4ff;
}

/* Hero区域 */
.hero-section {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.hero-grid-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(0, 212, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 212, 255, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
  z-index: 1;
}

.hero-glow-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 2;
  pointer-events: none;
}

.glow-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.3;
  transition: transform 0.3s ease-out;
}

.glow-1 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #00d4ff 0%, #6c5ce7 100%);
  top: 10%;
  left: 10%;
}

.glow-2 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #6c5ce7 0%, #ff00ff 100%);
  top: 60%;
  right: 15%;
}

.glow-3 {
  width: 250px;
  height: 250px;
  background: linear-gradient(135deg, #00ff88 0%, #00d4ff 100%);
  bottom: 20%;
  left: 30%;
}

.hero-content {
  position: relative;
  z-index: 10;
  text-align: center;
  max-width: 1000px;
  padding: 0 20px;
}

.hero-badge {
  display: inline-block;
  padding: 10px 25px;
  background: rgba(0, 212, 255, 0.1);
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 50px;
  font-family: 'Orbitron', sans-serif;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 3px;
  color: #00d4ff;
  margin-bottom: 30px;
}

.hero-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 72px;
  font-weight: 900;
  letter-spacing: 8px;
  margin-bottom: 25px;
  line-height: 1.1;
}

.title-word {
  display: block;
  color: #fff;
}

.title-word.accent {
  background: linear-gradient(135deg, #00d4ff 0%, #6c5ce7 50%, #ff00ff 100%);
  background-size: 200% auto;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: gradientShift 3s ease-in-out infinite;
}

@keyframes gradientShift {
  0%, 100% { background-position: 0% center; }
  50% { background-position: 100% center; }
}

.hero-subtitle {
  font-family: 'Rajdhani', sans-serif;
  font-size: 24px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 45px;
  min-height: 35px;
}

.typing-text {
  font-weight: 400;
  letter-spacing: 2px;
}

.cursor {
  animation: blink 1s infinite;
  color: #00d4ff;
}

@keyframes blink {
  0%, 50% { opacity: 1; }
  51%, 100% { opacity: 0; }
}

.hero-stats {
  display: flex;
  justify-content: center;
  gap: 50px;
  margin-bottom: 50px;
}

.hero-stat {
  text-align: center;
  padding: 20px 40px;
  background: rgba(0, 212, 255, 0.05);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 15px;
}

.stat-value {
  display: block;
  font-family: 'Orbitron', sans-serif;
  font-size: 48px;
  font-weight: 700;
  color: #00d4ff;
  text-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-family: 'Orbitron', sans-serif;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  letter-spacing: 2px;
}

.stat-divider {
  width: 1px;
  height: 60px;
  background: rgba(0, 212, 255, 0.2);
  align-self: center;
}

.cta-buttons {
  display: flex;
  gap: 25px;
  justify-content: center;
  flex-wrap: wrap;
}

.cyber-btn {
  position: relative;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 15px 35px;
  font-family: 'Orbitron', sans-serif;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 2px;
  text-decoration: none;
  border: 2px solid #00d4ff;
  background: transparent;
  color: #00d4ff;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s ease;
  border-radius: 50px;
}

.cyber-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 212, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.cyber-btn:hover {
  color: #fff;
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.5), inset 0 0 20px rgba(0, 212, 255, 0.1);
}

.cyber-btn:hover::before {
  left: 100%;
}

.cyber-btn-primary {
  background: linear-gradient(135deg, #00d4ff, #6c5ce7);
  border: none;
  color: #fff;
}

.cyber-btn-primary:hover {
  box-shadow: 0 0 30px rgba(0, 212, 255, 0.6), 0 0 60px rgba(108, 92, 231, 0.4);
  transform: translateY(-2px);
}

.scroll-indicator {
  position: absolute;
  bottom: 40px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  cursor: pointer;
  z-index: 10;
  animation: bounce 2s infinite;
}

.scroll-line {
  width: 2px;
  height: 40px;
  background: linear-gradient(to bottom, #00d4ff, transparent);
  position: relative;
}

.scroll-line::after {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 6px;
  height: 6px;
  background: #00d4ff;
  border-radius: 50%;
  animation: scrollDown 1.5s infinite;
}

@keyframes scrollDown {
  0% { top: 0; opacity: 1; }
  100% { top: 34px; opacity: 0; }
}

.scroll-indicator span {
  font-family: 'Orbitron', sans-serif;
  font-size: 11px;
  color: rgba(255, 255, 255, 0.5);
  letter-spacing: 2px;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateX(-50%) translateY(0); }
  40% { transform: translateX(-50%) translateY(-10px); }
  60% { transform: translateX(-50%) translateY(-5px); }
}

/* 球队简介 */
.intro-section {
  position: relative;
  padding: 120px 0;
  background: linear-gradient(180deg, #0a0a0f 0%, #0d1117 50%, #0a0a0f 100%);
  z-index: 5;
}

.section-grid-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(0, 212, 255, 0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 212, 255, 0.02) 1px, transparent 1px);
  background-size: 50px 50px;
  pointer-events: none;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
  position: relative;
  z-index: 10;
}

.section-tag {
  display: inline-block;
  padding: 8px 20px;
  background: rgba(0, 212, 255, 0.1);
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 50px;
  font-family: 'Orbitron', sans-serif;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 3px;
  color: #00d4ff;
  margin-bottom: 20px;
}

.section-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 42px;
  font-weight: 800;
  color: #fff;
  margin-bottom: 15px;
  position: relative;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 3px;
  background: linear-gradient(90deg, transparent, #00d4ff, transparent);
}

.section-desc {
  font-family: 'Rajdhani', sans-serif;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.6);
}

.intro-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
  position: relative;
  z-index: 10;
}

.intro-text {
  padding-right: 40px;
}

.highlight-text {
  font-family: 'Orbitron', sans-serif;
  font-size: 24px;
  font-weight: 700;
  color: #00d4ff;
  margin-bottom: 30px;
  letter-spacing: 2px;
  text-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
}

.intro-text p {
  font-size: 16px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 25px;
}

.intro-features {
  display: flex;
  gap: 25px;
  margin-top: 40px;
}

.feature-card {
  flex: 1;
  padding: 20px;
  background: rgba(0, 212, 255, 0.05);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 12px;
  text-align: center;
  transition: all 0.3s;
}

.feature-card:hover {
  background: rgba(0, 212, 255, 0.1);
  border-color: rgba(0, 212, 255, 0.4);
  transform: translateY(-5px);
}

.feature-card i {
  font-size: 28px;
  color: #00d4ff;
  margin-bottom: 10px;
}

.feature-card span {
  font-family: 'Orbitron', sans-serif;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 1px;
}

.intro-image {
  position: relative;
}

.image-container {
  position: relative;
  border-radius: 20px;
  overflow: hidden;
  border: 1px solid rgba(0, 212, 255, 0.3);
  box-shadow: 0 0 40px rgba(0, 212, 255, 0.1);
}

.image-container img {
  width: 100%;
  height: 500px;
  object-fit: cover;
  transition: transform 0.5s;
}

.image-container:hover img {
  transform: scale(1.08);
}

.image-grid-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(0, 212, 255, 0.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 212, 255, 0.05) 1px, transparent 1px);
  background-size: 40px 40px;
  pointer-events: none;
}

.image-border-glow {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  border-radius: 22px;
  border: 1px solid transparent;
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.3), rgba(108, 92, 231, 0.3)) border-box;
  -webkit-mask: linear-gradient(#fff 0 0) padding-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-container:hover .image-border-glow {
  opacity: 1;
}

.image-corner {
  position: absolute;
  width: 30px;
  height: 30px;
  border: 2px solid #00d4ff;
  opacity: 0.4;
  z-index: 2;
}

.image-corner.tl {
  top: 10px;
  left: 10px;
  border-right: none;
  border-bottom: none;
}

.image-corner.tr {
  top: 10px;
  right: 10px;
  border-left: none;
  border-bottom: none;
}

.image-corner.bl {
  bottom: 10px;
  left: 10px;
  border-right: none;
  border-top: none;
}

.image-corner.br {
  bottom: 10px;
  right: 10px;
  border-left: none;
  border-top: none;
}

.image-container:hover .image-corner {
  opacity: 0.8;
  box-shadow: 0 0 10px rgba(0, 212, 255, 0.5);
}

.floating-badge {
  position: absolute;
  top: -20px;
  right: -20px;
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, #00d4ff 0%, #6c5ce7 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Orbitron', sans-serif;
  font-size: 14px;
  font-weight: 700;
  color: #fff;
  box-shadow: 0 10px 30px rgba(0, 212, 255, 0.4);
  animation: rotate 15s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 球队数据 */
.stats-section {
  position: relative;
  padding: 120px 0;
  background: linear-gradient(180deg, #0d1117 0%, #0a0a0f 100%);
  overflow: hidden;
  z-index: 5;
}

.stats-grid-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    radial-gradient(circle at 20% 50%, rgba(0, 212, 255, 0.05) 0%, transparent 50%),
    radial-gradient(circle at 80% 50%, rgba(108, 92, 231, 0.05) 0%, transparent 50%);
  pointer-events: none;
}

.section-header.light .section-title {
  color: #fff;
}

.section-header.light .section-title::after {
  background: linear-gradient(90deg, transparent, #6c5ce7, transparent);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  position: relative;
  z-index: 10;
}

.stat-card {
  background: rgba(10, 10, 15, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 15px;
  padding: 40px 30px;
  text-align: center;
  transition: all 0.3s;
}

.stat-card:hover {
  background: rgba(0, 212, 255, 0.05);
  border-color: rgba(0, 212, 255, 0.4);
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0, 212, 255, 0.1);
}

.stat-icon {
  font-size: 36px;
  margin-bottom: 20px;
}

.stat-icon i {
  color: #00d4ff;
}

.stat-value {
  font-family: 'Orbitron', sans-serif;
  font-size: 48px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 10px;
}

.stat-label {
  font-family: 'Orbitron', sans-serif;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  margin-bottom: 20px;
  letter-spacing: 2px;
}

.stat-bar {
  height: 4px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 2px;
  overflow: hidden;
}

.stat-fill {
  height: 100%;
  background: linear-gradient(90deg, #00d4ff, #6c5ce7);
  border-radius: 2px;
  transition: width 1s ease-out;
}

/* 最新新闻 */
.news-section {
  position: relative;
  padding: 120px 0;
  background: #0a0a0f;
  z-index: 5;
}

.news-grid-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(108, 92, 231, 0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(108, 92, 231, 0.02) 1px, transparent 1px);
  background-size: 50px 50px;
  pointer-events: none;
}

.news-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  position: relative;
  z-index: 10;
}

.news-card {
  background: rgba(10, 10, 15, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(108, 92, 231, 0.2);
  border-radius: 20px;
  overflow: hidden;
  transition: all 0.3s;
}

.news-card:hover {
  border-color: rgba(108, 92, 231, 0.5);
  transform: translateY(-5px);
}

.news-card.featured {
  grid-row: span 2;
}

.news-image {
  position: relative;
  height: 300px;
  overflow: hidden;
}

.news-card.featured .news-image {
  height: 100%;
  min-height: 400px;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s;
}

.news-card:hover .news-image img {
  transform: scale(1.08);
}

.news-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to top, rgba(10, 10, 15, 0.9) 0%, transparent 50%);
}

.news-badge {
  position: absolute;
  top: 20px;
  left: 20px;
  padding: 8px 20px;
  background: linear-gradient(135deg, #ff00ff, #6c5ce7);
  color: #fff;
  font-family: 'Orbitron', sans-serif;
  font-size: 11px;
  font-weight: 600;
  border-radius: 50px;
  letter-spacing: 2px;
}

.news-content {
  padding: 30px;
}

.news-date {
  font-family: 'Orbitron', sans-serif;
  font-size: 11px;
  color: rgba(108, 92, 231, 0.8);
  letter-spacing: 2px;
}

.news-content h3 {
  font-family: 'Orbitron', sans-serif;
  font-size: 22px;
  font-weight: 700;
  margin: 15px 0;
  color: #fff;
  line-height: 1.4;
}

.news-content p {
  font-size: 14px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 20px;
}

.read-more {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-family: 'Orbitron', sans-serif;
  font-size: 12px;
  font-weight: 600;
  color: #6c5ce7;
  text-decoration: none;
  letter-spacing: 2px;
  transition: all 0.3s;
}

.read-more:hover {
  gap: 15px;
  color: #00d4ff;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.news-item {
  display: flex;
  gap: 20px;
  padding: 20px;
  background: rgba(10, 10, 15, 0.5);
  border: 1px solid rgba(108, 92, 231, 0.1);
  border-radius: 15px;
  transition: all 0.3s;
}

.news-item:hover {
  background: rgba(108, 92, 231, 0.05);
  border-color: rgba(108, 92, 231, 0.3);
}

.news-thumb {
  width: 120px;
  height: 100px;
  border-radius: 10px;
  overflow: hidden;
  flex-shrink: 0;
  position: relative;
}

.news-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumb-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(108, 92, 231, 0.2);
}

.news-info {
  flex: 1;
}

.news-info h4 {
  font-family: 'Orbitron', sans-serif;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #fff;
  line-height: 1.4;
}

.news-info p {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 合作伙伴 */
.partners-section {
  position: relative;
  padding: 80px 0;
  background: linear-gradient(180deg, #0a0a0f 0%, #0d1117 100%);
  z-index: 5;
}

.partners-grid-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(0, 212, 255, 0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 212, 255, 0.02) 1px, transparent 1px);
  background-size: 60px 60px;
  pointer-events: none;
}

.partners-track {
  overflow: hidden;
}

.partners-grid {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 60px;
  flex-wrap: wrap;
  position: relative;
  z-index: 10;
}

.partner-logo {
  padding: 30px 50px;
  background: rgba(10, 10, 15, 0.8);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 15px;
  transition: all 0.3s;
}

.partner-logo:hover {
  background: rgba(0, 212, 255, 0.05);
  border-color: rgba(0, 212, 255, 0.4);
  transform: translateY(-5px);
}

.partner-logo img {
  max-width: 120px;
  max-height: 60px;
  object-fit: contain;
  filter: brightness(0) invert(1);
  opacity: 0.7;
  transition: all 0.3s;
}

.partner-logo:hover img {
  opacity: 1;
  filter: none;
}
</style>