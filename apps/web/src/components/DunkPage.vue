<template>
  <div class="dunk-page">
    <div class="auth-bg">
      <div class="bg-glow glow-1"></div>
      <div class="bg-glow glow-2"></div>
    </div>

    <div class="wrapper">
      <router-link to="/home" class="home-link">
        <el-icon><ArrowLeft /></el-icon> 返回首页
      </router-link>

      <section class="hero glass-card">
        <span class="section-tag">DUNK HIGHLIGHTS</span>
        <h1 class="title">扣篮高燃瞬间</h1>
        <p class="desc">重温快船众星的暴力美学，每一记重扣都点燃全场。</p>
      </section>

      <section class="block">
        <div class="block-head">
          <span class="block-index">01</span>
          <h2 class="block-title">精彩扣篮瞬间</h2>
        </div>
        <div class="image-grid">
          <div v-for="(image, index) in dunkImages" :key="index" class="dunk-card">
            <img :src="image.url" :alt="image.title" class="dunk-img" loading="lazy" />
            <div class="dunk-overlay">
              <span class="player-name">{{ image.player }}</span>
              <span class="dunk-title">{{ image.title }}</span>
            </div>
          </div>
        </div>
      </section>

      <section class="block">
        <div class="block-head">
          <span class="block-index">02</span>
          <h2 class="block-title">扣篮集锦</h2>
        </div>
        <div class="video-grid">
          <div v-for="(video, index) in dunkVideos" :key="index" class="video-card card">
            <video :src="video.url" controls playsinline preload="metadata" class="highlight-video"></video>
            <div class="video-info">
              <h3 class="video-title">{{ video.title }}</h3>
            </div>
          </div>
        </div>
      </section>

      <div class="action-section">
        <router-link to="/home" class="btn btn-primary back-btn">
          <el-icon><ArrowLeft /></el-icon> 返回主页
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import hddunk from '@/assets/uploads/hddunk.jpg'
import wsdunk from '@/assets/uploads/wsdunk.jpg'
import lnddunk from '@/assets/uploads/lnddunk.jpg'
import qzdunk from '@/assets/uploads/qzdunk.jpg'
import hdVideo from '@/assets/uploads/哈登集锦.mp4'
import rwVideo from '@/assets/uploads/威少集锦.mp4'
import pgVideo from '@/assets/uploads/乔治集锦.mp4'
import lndVideo from '@/assets/uploads/伦纳德集锦.mp4'

const dunkImages = [
  { url: hddunk, title: '哈登暴扣', player: 'James Harden' },
  { url: wsdunk, title: '威少战斧', player: 'Russell Westbrook' },
  { url: lnddunk, title: '伦纳德隔扣', player: 'Kawhi Leonard' },
  { url: qzdunk, title: '乔治飞扣', player: 'Paul George' }
]

const dunkVideos = [
  { url: hdVideo, title: '哈登扣篮集锦' },
  { url: rwVideo, title: '威少扣篮集锦' },
  { url: pgVideo, title: '乔治扣篮集锦' },
  { url: lndVideo, title: '伦纳德扣篮集锦' }
]
</script>

<style scoped>
.dunk-page {
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
  padding: 48px 36px;
  text-align: center;
  margin-bottom: 32px;
}

.title {
  font-family: var(--font-display);
  font-size: 42px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 12px 0 10px;
}

.desc {
  font-size: 15px;
  color: var(--text-muted);
  margin: 0;
}

.block {
  margin-bottom: 32px;
}

.block-head {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 20px;
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

.image-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.dunk-card {
  position: relative;
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.dunk-img {
  width: 100%;
  aspect-ratio: 3 / 2;
  object-fit: cover;
  display: block;
  transition: transform var(--transition);
}

.dunk-card:hover .dunk-img {
  transform: scale(1.05);
}

.dunk-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  gap: 6px;
  padding: 20px;
  background: linear-gradient(180deg, transparent 40%, rgba(0, 0, 0, 0.75) 100%);
}

.player-name {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
  color: #fff;
}

.dunk-title {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.video-card {
  overflow: hidden;
  padding: 0;
}

.highlight-video {
  width: 100%;
  aspect-ratio: 16 / 9;
  object-fit: cover;
  display: block;
  background: #000;
}

.video-info {
  padding: 16px 20px;
}

.video-title {
  font-family: var(--font-display);
  font-size: 17px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.action-section {
  text-align: center;
  margin-top: 40px;
}

.back-btn {
  padding: 13px 32px;
  font-size: 15px;
}

@media (max-width: 900px) {
  .title {
    font-size: 30px;
  }
  .image-grid,
  .video-grid {
    grid-template-columns: 1fr;
  }
}
</style>