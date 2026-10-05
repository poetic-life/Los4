<template>
  <div class="shortcut">
    <div class="wrapper shortcut-inner">
      <div class="shortcut-badge">
        <i class="iconfont icon-clippers"></i>
        <span>Clippers</span>
      </div>
      <nav class="shortcut-links">
        <router-link to="/login" v-if="!isLoggedIn">请先登录</router-link>
        <template v-else>
          <span class="welcome">你好，{{ userStore.userInfo?.username || '球迷' }}</span>
          <a href="#" @click.prevent="logout">退出登录</a>
        </template>
        <router-link
          v-for="(item, index) in navItems"
          :key="index"
          :to="item.link"
        >{{ item.name }}</router-link>
        <router-link to="/mobile" class="mobile-link">
          <i class="iconfont icon-mobile-phone"></i>手机版
        </router-link>
      </nav>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/modules/user'

const router = useRouter()
const userStore = useUserStore()

const isLoggedIn = computed(() => userStore.isLoggedIn)

const navItems = ref([
  { name: '会员中心', link: '/member-center' },
  { name: '帮助中心', link: '/help-center' },
  { name: '在线客服', link: '/customer-service' }
])

const logout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.shortcut {
  background: var(--bg-elevated);
  border-bottom: 1px solid var(--border);
}

.shortcut-inner {
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.shortcut-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 700;
  letter-spacing: 0.5px;
  color: var(--text-primary);
}

.shortcut-badge .iconfont {
  font-size: 16px;
  color: var(--brand-red);
}

.shortcut-links {
  display: flex;
  align-items: center;
  gap: 4px;
}

.shortcut-links a,
.shortcut-links .welcome {
  padding: 0 12px;
  font-size: 13px;
  color: var(--text-muted);
  border-right: 1px solid var(--border);
  line-height: 14px;
  transition: color 0.2s ease;
}

.shortcut-links a:last-child {
  border-right: none;
}

.shortcut-links a:hover {
  color: var(--brand-red);
}

.shortcut-links .welcome {
  color: var(--text-secondary);
}

.mobile-link {
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.mobile-link .iconfont {
  font-size: 14px;
}
</style>