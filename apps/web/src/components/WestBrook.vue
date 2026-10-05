<template>
  <div class="player-page">
    <div class="auth-bg">
      <div class="bg-glow glow-1"></div>
      <div class="bg-glow glow-2"></div>
    </div>

    <div class="wrapper">
      <router-link to="/home" class="home-link">
        <el-icon><ArrowLeft /></el-icon> 返回首页
      </router-link>

      <section class="hero glass-card">
        <div class="hero-photo">
          <img :src="mainPhoto" :alt="nameCn" />
        </div>
        <div class="hero-info">
          <span class="section-tag">PLAYER PROFILE</span>
          <h1 class="name-cn">{{ nameCn }}</h1>
          <h2 class="name-en">{{ nameEn }}</h2>
          <div class="meta-tags">
            <span class="meta-chip">球衣号 {{ number }}</span>
            <span class="meta-chip">{{ position }}</span>
            <span class="meta-chip">洛杉矶快船</span>
          </div>
          <div class="stats">
            <div v-for="s in stats" :key="s.label" class="stat">
              <span class="stat-value text-gradient">{{ s.value }}</span>
              <span class="stat-label">{{ s.label }}</span>
            </div>
          </div>
        </div>
      </section>

      <section class="block card">
        <div class="block-head">
          <span class="block-index">01</span>
          <h2 class="block-title">球员介绍</h2>
        </div>
        <p v-for="(p, i) in bioParagraphs" :key="i" class="bio-p">{{ p }}</p>
      </section>

      <section class="block card">
        <div class="block-head">
          <span class="block-index">02</span>
          <h2 class="block-title">精彩图集</h2>
        </div>
        <div class="gallery-grid">
          <img v-for="(g, i) in gallery" :key="i" :src="g" :alt="nameCn + ' - ' + (i + 1)" loading="lazy" />
        </div>
      </section>

      <section class="block card">
        <div class="block-head">
          <span class="block-index">03</span>
          <h2 class="block-title">高光集锦</h2>
        </div>
        <video class="highlight-video" :src="videoSrc" :poster="mainPhoto" controls playsinline preload="metadata"></video>
      </section>
    </div>
  </div>
</template>

<script setup>
import mainPhoto from '@/assets/uploads/rw.jpg'
import g1 from '@/assets/uploads/威少介绍1.jpg'
import g2 from '@/assets/uploads/威少介绍2.jpg'
import g3 from '@/assets/uploads/威少介绍3.jpg'
import g4 from '@/assets/uploads/威少介绍4.jpg'
import g5 from '@/assets/uploads/威少介绍5.jpg'
import g6 from '@/assets/uploads/威少介绍6.jpg'
import g7 from '@/assets/uploads/威少介绍7.jpg'
import g8 from '@/assets/uploads/威少介绍8.jpg'
import videoSrc from '@/assets/uploads/威少集锦.mp4'

const nameCn = '拉塞尔·威斯布鲁克'
const nameEn = 'Russell Westbrook'
const number = '#0'
const position = '控球后卫'

const stats = [
  { value: '22.5', label: '场均得分' },
  { value: '8.3', label: '场均助攻' },
  { value: '7.4', label: '场均篮板' },
  { value: '1.7', label: '场均抢断' }
]

const bioParagraphs = [
  '拉塞尔·威斯布鲁克（Russell Westbrook），美国职业篮球运动员，司职控球后卫，现效力于 NBA 的洛杉矶快船队。他以其爆发力、全能的表现和激烈的比赛风格而闻名，是现代篮球中的顶尖球员之一。威斯布鲁克在职业生涯中获得了多次 NBA 全明星荣誉，并曾获得 NBA 最有价值球员（MVP）奖。',
  '威斯布鲁克在 2017 年获得了 NBA 最有价值球员（MVP）奖，并在 2016-2017 赛季创下了场均三双的辉煌成就。他在俄克拉荷马城雷霆队的表现使他成为了联盟中最具威胁的球员之一。威斯布鲁克以他的快速突破、强力扣篮和精准的三分投篮闻名，其标志性动作包括高速的突破上篮和激烈的防守抢断。'
]

const gallery = [g1, g2, g3, g4, g5, g6, g7, g8]
</script>

<style scoped>
.player-page {
  position: relative;
  min-height: 100vh;
  padding: 40px 0 80px;
  background-color: var(--bg-base);
  overflow: hidden;
}

.auth-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.bg-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(90px);
  opacity: 0.35;
}

.glow-1 {
  width: 420px;
  height: 420px;
  background: var(--accent);
  top: -10%;
  left: -10%;
}

.glow-2 {
  width: 360px;
  height: 360px;
  background: var(--accent-2);
  bottom: -10%;
  right: -8%;
}

.wrapper {
  position: relative;
  z-index: 2;
}

.home-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 28px;
  font-size: 14px;
  color: var(--text-muted);
  text-decoration: none;
  transition: color var(--transition);
}

.home-link:hover {
  color: var(--text-primary);
}

.hero {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 40px;
  align-items: center;
  padding: 40px;
  margin-bottom: 28px;
}

.hero-photo img {
  width: 320px;
  height: 320px;
  object-fit: cover;
  border-radius: var(--radius-lg);
}

.name-cn {
  font-family: var(--font-display);
  font-size: 42px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 14px 0 4px;
}

.name-en {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 500;
  letter-spacing: 2px;
  color: var(--text-muted);
  margin: 0 0 20px;
  text-transform: uppercase;
}

.meta-tags {
  display: flex;
  gap: 12px;
  margin-bottom: 28px;
  flex-wrap: wrap;
}

.meta-chip {
  padding: 8px 16px;
  border-radius: var(--radius-full);
  background: var(--bg-hover);
  border: 1px solid var(--border);
  font-size: 13px;
  color: var(--text-primary);
}

.stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  padding: 24px;
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
}

.stat {
  display: flex;
  flex-direction: column;
  gap: 6px;
  text-align: center;
}

.stat-value {
  font-family: var(--font-display);
  font-size: 30px;
  font-weight: 700;
}

.stat-label {
  font-size: 13px;
  color: var(--text-muted);
}

.block {
  padding: 32px;
  margin-bottom: 24px;
}

.block-head {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 22px;
}

.block-index {
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 600;
  color: var(--accent);
}

.block-title {
  font-family: var(--font-display);
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.bio-p {
  font-size: 15px;
  line-height: 1.9;
  color: var(--text-muted);
  margin: 0;
}

.bio-p + .bio-p {
  margin-top: 14px;
}

.gallery-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}

.gallery-grid img {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  border-radius: var(--radius-md);
  transition: transform var(--transition);
}

.gallery-grid img:hover {
  transform: scale(1.03);
}

.highlight-video {
  width: 100%;
  aspect-ratio: 16 / 9;
  object-fit: cover;
  border-radius: var(--radius-md);
  background: #000;
}

@media (max-width: 900px) {
  .hero {
    grid-template-columns: 1fr;
    padding: 28px;
  }
  .hero-photo img {
    width: 100%;
    height: auto;
    aspect-ratio: 1;
  }
  .name-cn {
    font-size: 32px;
  }
  .gallery-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>