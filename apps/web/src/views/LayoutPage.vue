<template>
  <div class="layout">
    <ShortCut />

    <nav class="site-nav" :class="{ scrolled: isScrolled }">
      <div class="wrapper nav-inner">
        <router-link to="/home" class="nav-logo">
          <span class="logo-mark">🏀</span>
          <span class="logo-text">LA CLIPPERS</span>
        </router-link>

        <div class="nav-links">
          <router-link
            v-for="(item, index) in role"
            :key="index"
            :to="getRoute(item)"
            class="nav-link"
            :class="{ active: index === activeIndex }"
            @click="setActive(index)"
          >
            {{ item }}
          </router-link>
        </div>

        <div class="nav-actions">
          <div class="search">
            <el-icon class="search-icon"><Search /></el-icon>
            <input
              @keyup.enter="handleSearch"
              type="text"
              v-model="searchQuery"
              placeholder="搜一搜"
            />
          </div>

          <div class="cart" @click="navigateToOrders">
            <el-icon class="cart-icon"><ShoppingCart /></el-icon>
            <span v-if="count > 0" class="cart-badge">{{ count }}</span>
          </div>
        </div>
      </div>
    </nav>

    <router-view />
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import ShortCut from '@/components/ShortCut.vue'

const router = useRouter()
const route = useRoute()

const role = ref(['首页', '购物', '新闻', '队员', '社区', '个人中心', '时间表'])
const count = ref(3)
const activeIndex = ref(0)
const searchQuery = ref('')
const isScrolled = ref(false)

const currentRoute = computed(() => route.path)

watch(currentRoute, (newRoute) => {
  activeIndex.value = role.value.findIndex((routeItem) => getRoute(routeItem) === newRoute)
})

const getRoute = (item) => {
  const map = {
    首页: '/home',
    购物: '/shopping',
    新闻: '/news',
    队员: '/team',
    社区: '/community',
    个人中心: '/member-center',
    时间表: '/schedule'
  }
  return map[item] || '/'
}

const setActive = (index) => {
  activeIndex.value = index
}

const navigateToOrders = () => {
  router.push('/orders')
}

const handleSearch = () => {
  const matchedRole = role.value.find((roleItem) => roleItem.includes(searchQuery.value))
  if (matchedRole) {
    router.push(getRoute(matchedRole))
    searchQuery.value = ''
  } else {
    router.push('/home')
  }
}

const handleScroll = () => {
  isScrolled.value = window.scrollY > 10
}

onMounted(() => {
  activeIndex.value = role.value.findIndex((routeItem) => getRoute(routeItem) === currentRoute.value)
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: var(--bg-base);
}

.site-nav {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(11, 13, 18, 0.86);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid transparent;
  transition: all 0.25s ease;
}

.site-nav.scrolled {
  border-bottom-color: var(--border);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.nav-inner {
  height: 68px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.nav-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}

.logo-mark {
  font-size: 24px;
}

.logo-text {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 1.5px;
  background: var(--gradient-brand-blue);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 4px;
}

.nav-link {
  position: relative;
  padding: 8px 16px;
  font-size: 15px;
  font-weight: 500;
  color: var(--text-secondary);
  border-radius: var(--radius-full);
  transition: all 0.2s ease;
}

.nav-link:hover {
  color: var(--text-primary);
  background: rgba(255, 255, 255, 0.06);
}

.nav-link.active {
  color: #fff;
  font-weight: 600;
}

.nav-link.active::after {
  content: '';
  position: absolute;
  left: 16px;
  right: 16px;
  bottom: 2px;
  height: 2px;
  border-radius: 2px;
  background: var(--brand-red);
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

.search {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 14px;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  transition: all 0.2s ease;
}

.search:focus-within {
  border-color: var(--brand-red);
  background: rgba(200, 16, 46, 0.06);
}

.search-icon {
  color: var(--text-muted);
  font-size: 15px;
}

.search input {
  background: transparent;
  border: none;
  outline: none;
  color: var(--text-primary);
  font-size: 14px;
  width: 130px;
}

.search input::placeholder {
  color: var(--text-faint);
}

.cart {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.2s ease;
}

.cart:hover {
  background: rgba(255, 255, 255, 0.08);
}

.cart-icon {
  font-size: 20px;
  color: var(--text-primary);
}

.cart-badge {
  position: absolute;
  top: 2px;
  right: 2px;
  min-width: 16px;
  height: 16px;
  padding: 0 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--brand-red);
  color: #fff;
  font-size: 11px;
  font-weight: 600;
  border-radius: var(--radius-full);
  border: 2px solid var(--bg-base);
}

@media (max-width: 900px) {
  .nav-links {
    display: none;
  }
}
</style>