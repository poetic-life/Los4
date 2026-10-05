<template>
  <div class="member-center">
    <!-- 两侧氛围光，填充宽屏两侧空白 -->
    <div class="ambient ambient-left"></div>
    <div class="ambient ambient-right"></div>

    <header class="center-header">
      <div class="center-wrap header-inner">
        <router-link to="/home" class="logo">
          <span class="logo-mark">🏀</span>
          <span class="logo-text">LA CLIPPERS</span>
        </router-link>
        <a class="back-link" @click="goBack">
          <el-icon><ArrowLeft /></el-icon> 返回
        </a>
      </div>
    </header>

    <!-- 通栏用户信息 Hero -->
    <section class="profile-hero">
      <div class="hero-bg-img" :style="{ backgroundImage: `url(${heroBg})` }"></div>
      <div class="hero-mask"></div>

      <div class="center-wrap hero-inner">
        <div class="hero-left">
          <div class="avatar">{{ userName.charAt(0) }}</div>
          <div class="profile-meta">
            <div class="name-row">
              <span class="name">{{ userName }}</span>
              <span class="level-badge"><el-icon><Medal /></el-icon> VIP · {{ levelText }}</span>
            </div>
            <p class="profile-sub">洛杉矶快船官方球迷账户，欢迎回来 · TOGETHER WE CLIP</p>
            <div class="hero-actions">
              <router-link class="hero-btn primary" to="/member-center/orders">
                <el-icon><Tickets /></el-icon> 我的订单
              </router-link>
              <router-link class="hero-btn" :to="myId ? `/user/${myId}` : '/login'">
                <el-icon><HomeFilled /></el-icon> 我的主页
              </router-link>
              <router-link class="hero-btn" to="/messages">
                <el-icon><ChatDotRound /></el-icon> 私信
              </router-link>
              <router-link class="hero-btn" to="/member-center/count">
                <el-icon><Setting /></el-icon> 账户设置
              </router-link>
            </div>
          </div>
        </div>

        <div class="hero-stats">
          <div class="h-stat">
            <span class="h-value">{{ points }}</span>
            <span class="h-label">积分 POINTS</span>
          </div>
          <div class="h-stat">
            <span class="h-value">5</span>
            <span class="h-label">优惠券 COUPONS</span>
          </div>
          <div class="h-stat">
            <span class="h-value h-highlight">VIP</span>
            <span class="h-label">会员等级 MEMBER</span>
          </div>
          <div class="h-stat">
            <span class="h-value">{{ levelText }}</span>
            <span class="h-label">会员卡 CARD</span>
          </div>
        </div>
      </div>
    </section>

    <div class="center-wrap main">
      <aside class="sidebar">
        <nav class="menu">
          <router-link
            v-for="(item, index) in menuItems"
            :key="index"
            :to="item.path"
            class="menu-item"
            :class="{ active: isActive(item.path) }"
          >
            <el-icon class="menu-icon"><component :is="item.icon" /></el-icon>
            <span class="menu-text">{{ item.name }}</span>
            <el-icon class="menu-arrow"><ArrowRight /></el-icon>
          </router-link>
        </nav>

        <div class="sidebar-help">
          <p class="help-title">需要帮助？</p>
          <router-link to="/help-center" class="help-link">帮助中心</router-link>
          <router-link to="/customer-service" class="help-link">联系在线客服</router-link>
        </div>
      </aside>

      <main class="content">
        <router-view></router-view>
      </main>
    </div>

    <!-- 页脚，填充页面底部 -->
    <FooTer />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import FooTer from '@/components/FooTer.vue'
import heroBg from '@/assets/uploads/defense.jpg'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const goBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/home')
  }
}

const userName = computed(() => userStore.userInfo?.username || '球迷')

const points = computed(() => userStore.userInfo?.points ?? 1280)

const levelText = computed(() => userStore.userInfo?.level || '银卡会员')

const myId = computed(() => {
  const info = userStore.userInfo
  return info?.id || info?.userId || localStorage.getItem('userId') || ''
})

const menuItems = computed(() => [
  { name: '个人信息', path: '/member-center/profile', icon: 'User' },
  { name: '我的主页', path: myId.value ? `/user/${myId.value}` : '/login', icon: 'HomeFilled' },
  { name: '我的订单', path: '/member-center/orders', icon: 'Tickets' },
  { name: '私信', path: '/messages', icon: 'ChatDotRound' },
  { name: '配送地址', path: '/member-center/address', icon: 'Location' },
  { name: '收藏夹', path: '/member-center/collect', icon: 'Star' },
  { name: '账户设置', path: '/member-center/count', icon: 'Setting' }
])

const isActive = (path) => route.path === path
</script>

<style scoped>
.member-center {
  position: relative;
  min-height: 100vh;
  background-color: var(--bg-base);
  overflow: hidden;
}

/* 宽屏两侧氛围光 */
.ambient {
  position: fixed;
  top: 20%;
  width: 420px;
  height: 60vh;
  z-index: 0;
  pointer-events: none;
  filter: blur(120px);
  opacity: 0.14;
}

.ambient-left {
  left: -180px;
  background: var(--brand-red);
}

.ambient-right {
  right: -180px;
  background: var(--brand-blue);
}

/* 比通用版心更宽的容器 */
.center-wrap {
  position: relative;
  z-index: 1;
  width: 1440px;
  max-width: calc(100% - 48px);
  margin: 0 auto;
  box-sizing: border-box;
}

.center-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(11, 13, 18, 0.86);
  backdrop-filter: blur(16px);
  border-bottom: 1px solid var(--border);
}

.header-inner {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
}

.logo-mark {
  font-size: 22px;
}

.logo-text {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 1.5px;
  background: var(--gradient-brand-blue);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: var(--text-muted);
  text-decoration: none;
  cursor: pointer;
  transition: color var(--transition);
}

.back-link:hover {
  color: var(--text-primary);
}

/* ============ 通栏 Hero ============ */
.profile-hero {
  position: relative;
  overflow: hidden;
}

.hero-bg-img {
  position: absolute;
  inset: 0;
  background-size: cover;
  background-position: center;
}

.hero-mask {
  position: absolute;
  inset: 0;
  background:
    linear-gradient(90deg, rgba(11, 13, 18, 0.95) 0%, rgba(200, 16, 46, 0.55) 55%, rgba(29, 66, 138, 0.75) 100%),
    linear-gradient(180deg, rgba(11, 13, 18, 0.5) 0%, rgba(11, 13, 18, 0.9) 100%);
}

.hero-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 40px;
  padding-top: 56px;
  padding-bottom: 56px;
}

.hero-left {
  display: flex;
  align-items: center;
  gap: 22px;
}

.avatar {
  width: 92px;
  height: 92px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.16);
  border: 2px solid rgba(255, 255, 255, 0.4);
  border-radius: 50%;
  font-family: var(--font-display);
  font-size: 38px;
  font-weight: 700;
  color: #fff;
}

.profile-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 14px;
  flex-wrap: wrap;
}

.name {
  font-family: var(--font-display);
  font-size: 36px;
  font-weight: 700;
  line-height: 1.1;
  color: #fff;
}

.level-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 14px;
  background: rgba(255, 255, 255, 0.16);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: var(--radius-full);
  font-size: 12px;
  color: #fff;
}

.profile-sub {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.72);
  margin: 0;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.hero-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 18px;
  border-radius: var(--radius-full);
  border: 1px solid rgba(255, 255, 255, 0.28);
  background: rgba(11, 13, 18, 0.4);
  color: rgba(255, 255, 255, 0.85);
  font-size: 13px;
  font-weight: 600;
  text-decoration: none;
  transition: all var(--transition);
}

.hero-btn:hover {
  border-color: #fff;
  color: #fff;
}

.hero-btn.primary {
  background: var(--gradient-brand);
  border-color: transparent;
  color: #fff;
  box-shadow: var(--shadow-brand);
}

.hero-stats {
  display: flex;
  align-items: stretch;
  gap: 14px;
  flex-shrink: 0;
}

.h-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  min-width: 120px;
  padding: 20px 16px;
  background: rgba(11, 13, 18, 0.55);
  border: 1px solid rgba(255, 255, 255, 0.14);
  border-radius: var(--radius-lg);
  backdrop-filter: blur(10px);
}

.h-value {
  font-family: var(--font-display);
  font-size: 30px;
  font-weight: 700;
  line-height: 1;
  color: #fff;
}

.h-highlight {
  font-size: 24px;
  color: #ff8a9c;
}

.h-label {
  font-size: 10px;
  letter-spacing: 1px;
  color: rgba(255, 255, 255, 0.6);
  text-align: center;
}

/* ============ 主体布局 ============ */
.main {
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 24px;
  padding-top: 32px;
  padding-bottom: 48px;
  align-items: start;
}

.sidebar {
  position: sticky;
  top: 92px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.menu {
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 14px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 13px 16px;
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 14px;
  transition: all var(--transition);
}

.menu-item:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.menu-item.active {
  background: var(--accent);
  color: #fff;
  font-weight: 600;
  box-shadow: var(--shadow-brand);
}

.menu-icon {
  font-size: 18px;
}

.menu-text {
  flex: 1;
}

.menu-arrow {
  font-size: 14px;
  opacity: 0.6;
}

.sidebar-help {
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.help-title {
  margin: 0 0 4px;
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 700;
  color: var(--text-primary);
}

.help-link {
  font-size: 13px;
  color: var(--text-muted);
  text-decoration: none;
  transition: color var(--transition);
}

.help-link:hover {
  color: var(--accent);
}

.content {
  min-width: 0;
  min-height: 720px;
}

@media (max-width: 1100px) {
  .hero-inner {
    flex-direction: column;
    align-items: flex-start;
  }

  .hero-stats {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 900px) {
  .main {
    grid-template-columns: 1fr;
  }

  .sidebar {
    position: static;
  }

  .hero-stats {
    flex-wrap: wrap;
  }

  .h-stat {
    flex: 1 1 40%;
  }
}
</style>
