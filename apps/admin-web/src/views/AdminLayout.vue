<template>
  <div class="admin-layout">
    <header class="admin-header">
      <div class="header-left">
        <button class="collapse-btn" @click="collapsed = !collapsed">
          <el-icon><Expand v-if="collapsed" /><Fold v-else /></el-icon>
        </button>
        <div class="brand">
          <span class="logo-mark">🏀</span>
          <div v-show="!collapsed" class="brand-text">
            <span class="logo-name">LA CLIPPERS</span>
            <span class="logo-sub">球迷社区 · 管理后台</span>
          </div>
        </div>
      </div>
      <div class="header-right">
        <span class="welcome">
          <el-icon><UserFilled /></el-icon>
          {{ userStore.userInfo?.username || 'admin' }}
        </span>
        <button class="logout-btn" @click="handleLogout">
          <el-icon><SwitchButton /></el-icon>
          <span>退出登录</span>
        </button>
      </div>
    </header>

    <div class="admin-body">
      <aside class="sidebar" :class="{ collapsed }">
        <nav class="menu">
          <router-link
            v-for="item in menuItems"
            :key="item.path"
            :to="item.path"
            class="menu-item"
            :class="{ active: route.path === item.path }"
            :title="item.name"
          >
            <el-icon class="menu-icon"><component :is="item.icon" /></el-icon>
            <span v-show="!collapsed" class="menu-text">{{ item.name }}</span>
          </router-link>
        </nav>

        <router-link to="/profile" class="sidebar-footer" :title="'个人主页'">
          <div class="avatar">{{ avatarText }}</div>
          <div v-show="!collapsed" class="profile">
            <span class="name">{{ userStore.userInfo?.username || 'admin' }}</span>
            <span class="role">超级管理员</span>
          </div>
        </router-link>
      </aside>

      <main class="content">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const collapsed = ref(false)

const menuItems = [
  { name: '仪表盘', path: '/dashboard', icon: 'DataAnalysis' },
  { name: '用户管理', path: '/users', icon: 'User' },
  { name: '帖子管理', path: '/posts', icon: 'ChatLineSquare' },
  { name: '评论管理', path: '/comments', icon: 'ChatDotRound' },
  { name: '商品管理', path: '/products', icon: 'Goods' },
  { name: '订单管理', path: '/orders', icon: 'Tickets' },
  { name: '个人主页', path: '/profile', icon: 'UserFilled' }
]

const avatarText = computed(() => {
  const name = userStore.userInfo?.username || 'A'
  return name.charAt(0).toUpperCase()
})

const handleLogout = () => {
  userStore.logout()
  router.replace('/login')
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  background: var(--bg-base);
}

.admin-header {
  position: sticky;
  top: 0;
  z-index: 100;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: var(--bg-elevated);
  border-bottom: 1px solid var(--border);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: var(--text-secondary);
  background: transparent;
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition);
}

.collapse-btn:hover {
  background: rgba(255, 255, 255, 0.08);
  color: var(--text-primary);
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  white-space: nowrap;
  overflow: hidden;
}

.logo-mark {
  font-size: 24px;
}

.brand-text {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
}

.logo-name {
  font-family: var(--font-display);
  font-size: 17px;
  font-weight: 700;
  letter-spacing: 1.5px;
  background: var(--gradient-brand-blue);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.logo-sub {
  font-size: 12px;
  color: var(--text-muted);
  letter-spacing: 0.5px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 18px;
}

.welcome {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: var(--text-secondary);
}

.logout-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 500;
  color: var(--text-primary);
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all var(--transition);
}

.logout-btn:hover {
  background: rgba(200, 16, 46, 0.14);
  border-color: var(--brand-red);
  color: #fff;
}

.admin-body {
  display: flex;
  min-height: calc(100vh - 60px);
}

.sidebar {
  width: 240px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  background: var(--bg-elevated);
  border-right: 1px solid var(--border);
  padding: 20px 14px;
  transition: width 0.22s ease;
  overflow: hidden;
}

.sidebar.collapsed {
  width: 64px;
  padding: 20px 10px;
}

.menu {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 13px 16px;
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  font-size: 14px;
  white-space: nowrap;
  transition: all var(--transition);
}

.sidebar.collapsed .menu-item {
  justify-content: center;
  padding: 13px 0;
}

.menu-item:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.menu-item.active {
  background: var(--gradient-brand);
  color: #fff;
  font-weight: 600;
  box-shadow: var(--shadow-brand);
}

.menu-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.menu-text {
  flex: 1;
}

.sidebar-footer {
  margin-top: auto;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  color: inherit;
  text-decoration: none;
  white-space: nowrap;
  overflow: hidden;
  transition: all var(--transition);
}

.sidebar-footer:hover {
  border-color: var(--border-strong);
}

.sidebar.collapsed .sidebar-footer {
  justify-content: center;
  padding: 12px 0;
}

.avatar {
  width: 38px;
  height: 38px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 700;
  color: #fff;
  background: var(--gradient-brand-blue);
  border-radius: 50%;
}

.profile {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
  overflow: hidden;
}

.profile .name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.profile .role {
  font-size: 12px;
  color: var(--text-muted);
}

.content {
  flex: 1;
  min-width: 0;
  padding: 24px;
}
</style>