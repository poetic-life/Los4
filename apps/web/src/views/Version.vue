<template>
  <div class="version-page">
    <div class="auth-bg">
      <div class="bg-glow glow-1"></div>
      <div class="bg-glow glow-2"></div>
    </div>

    <div class="wrapper">
      <header class="topbar">
        <router-link to="/home" class="home-link">
          <el-icon><ArrowLeft /></el-icon> 返回首页
        </router-link>
        <span class="topbar-brand">手机版</span>
      </header>

      <section class="hero">
        <div class="hero-text">
          <span class="section-tag">MOBILE APP</span>
          <h1 class="title">快船之家，<span class="text-gradient">随身携带</span></h1>
          <p class="desc">下载官方 App，随时随地查看赛程、浏览周边商城，与全球快船球迷互动。</p>
          <div class="actions">
            <button class="btn btn-primary store-btn" @click="comingSoon('App Store')">
              <el-icon><Iphone /></el-icon> App Store
            </button>
            <button class="btn btn-outline store-btn" @click="comingSoon('Android')">
              <el-icon><Android /></el-icon> Android
            </button>
          </div>
        </div>

        <div class="phone-mock">
          <div class="phone">
            <div class="screen">
              <div class="app-head">
                <span class="app-logo">LA</span>
                <div>
                  <div class="app-team">LA CLIPPERS</div>
                  <div class="app-sub">官方球迷 App</div>
                </div>
              </div>
              <div class="app-hero"></div>
              <div class="app-row">
                <div class="app-chip"></div>
                <div class="app-chip"></div>
                <div class="app-chip"></div>
              </div>
              <div class="app-list">
                <div class="app-item"></div>
                <div class="app-item"></div>
                <div class="app-item"></div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section class="features">
        <div v-for="f in features" :key="f.title" class="feature-card card">
          <el-icon class="feature-icon"><component :is="f.icon" /></el-icon>
          <h3>{{ f.title }}</h3>
          <p>{{ f.desc }}</p>
        </div>
      </section>

      <section class="qr-card glass-card">
        <div class="qr-box">
          <div class="qr-grid">
            <span v-for="n in 25" :key="n" :class="['qr-cell', qrActive.includes(n) ? 'on' : '']"></span>
          </div>
        </div>
        <div class="qr-text">
          <h2>扫码立即下载</h2>
          <p>使用手机扫描二维码，即可快速跳转下载页面，开启你的快船之旅。</p>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'

const features = [
  { icon: 'Calendar', title: '实时赛程', desc: '完整赛季赛程与实时比分，赛前提醒不错过每一场。' },
  { icon: 'Message', title: '球迷私信', desc: '关注好友动态，一对一私信畅聊。' },
  { icon: 'ShoppingBag', title: '官方商城', desc: '正版球衣周边一键下单，掌上逛遍快船商城。' },
  { icon: 'ChatDotRound', title: '球迷社区', desc: '加入球迷社区，与全球快船球迷交流互动。' }
]

const qrActive = [1, 2, 3, 4, 6, 7, 9, 10, 11, 13, 14, 16, 18, 19, 21, 22, 23, 25]

const comingSoon = (platform) => {
  ElMessage.info(`${platform} 版本即将上线，敬请期待`)
}
</script>

<style scoped>
.version-page {
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

.topbar {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 40px;
}

.home-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: var(--text-muted);
  text-decoration: none;
  transition: color var(--transition);
}

.home-link:hover {
  color: var(--text-primary);
}

.topbar-brand {
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 1px;
  color: var(--text-muted);
}

.hero {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 48px;
  align-items: center;
  margin-bottom: 48px;
}

.hero-text .title {
  font-family: var(--font-display);
  font-size: 44px;
  font-weight: 700;
  line-height: 1.15;
  color: var(--text-primary);
  margin: 14px 0 16px;
}

.hero-text .desc {
  font-size: 16px;
  line-height: 1.7;
  color: var(--text-muted);
  margin: 0 0 28px;
}

.actions {
  display: flex;
  gap: 16px;
}

.store-btn {
  padding: 13px 24px;
  font-size: 15px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.phone-mock {
  display: flex;
  justify-content: center;
}

.phone {
  width: 260px;
  height: 520px;
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: 40px;
  padding: 16px;
  box-shadow: var(--shadow-lg);
}

.screen {
  width: 100%;
  height: 100%;
  background: var(--bg-card);
  border-radius: 28px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  overflow: hidden;
}

.app-head {
  display: flex;
  align-items: center;
  gap: 12px;
}

.app-logo {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background: var(--gradient-brand);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: var(--font-display);
  font-weight: 700;
  color: #fff;
  font-size: 15px;
}

.app-team {
  font-family: var(--font-display);
  font-weight: 700;
  font-size: 15px;
  color: var(--text-primary);
}

.app-sub {
  font-size: 12px;
  color: var(--text-muted);
}

.app-hero {
  height: 120px;
  border-radius: 16px;
  background: linear-gradient(135deg, var(--accent), var(--accent-2));
  opacity: 0.85;
}

.app-row {
  display: flex;
  gap: 8px;
}

.app-chip {
  flex: 1;
  height: 24px;
  border-radius: 8px;
  background: var(--bg-hover);
}

.app-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.app-item {
  height: 48px;
  border-radius: 12px;
  background: var(--bg-hover);
}

.features {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}

.feature-card {
  padding: 28px 24px;
  text-align: center;
}

.feature-icon {
  font-size: 30px;
  color: var(--accent);
  margin-bottom: 14px;
}

.feature-card h3 {
  font-family: var(--font-display);
  font-size: 17px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px;
}

.feature-card p {
  font-size: 13px;
  line-height: 1.6;
  color: var(--text-muted);
  margin: 0;
}

.qr-card {
  display: flex;
  align-items: center;
  gap: 32px;
  padding: 40px;
}

.qr-box {
  width: 150px;
  height: 150px;
  background: #fff;
  border-radius: var(--radius-md);
  padding: 12px;
  flex-shrink: 0;
}

.qr-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 5px;
  width: 100%;
  height: 100%;
}

.qr-cell {
  background: transparent;
  border-radius: 2px;
}

.qr-cell.on {
  background: #0b0d12;
}

.qr-text h2 {
  font-family: var(--font-display);
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 10px;
}

.qr-text p {
  font-size: 15px;
  line-height: 1.7;
  color: var(--text-muted);
  margin: 0;
}

@media (max-width: 900px) {
  .hero {
    grid-template-columns: 1fr;
    gap: 32px;
  }
  .hero-text .title {
    font-size: 32px;
  }
  .features {
    grid-template-columns: repeat(2, 1fr);
  }
  .qr-card {
    flex-direction: column;
    text-align: center;
  }
}
</style>