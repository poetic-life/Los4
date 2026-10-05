<template>
  <div class="help-page">
    <div class="auth-bg">
      <div class="bg-glow glow-1"></div>
      <div class="bg-glow glow-2"></div>
    </div>

    <div class="wrapper">
      <header class="topbar">
        <router-link to="/home" class="home-link">
          <el-icon><ArrowLeft /></el-icon> 返回首页
        </router-link>
        <span class="topbar-brand">帮助中心</span>
      </header>

      <section class="hero glass-card">
        <span class="section-tag">HELP CENTER</span>
        <h1 class="title">需要帮助吗？</h1>
        <p class="desc">搜索常见问题，或浏览下方分类快速找到答案。</p>
        <el-input
          v-model="keyword"
          class="search-input"
          size="large"
          placeholder="搜索问题，例如：如何退换商品"
          clearable
        >
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
      </section>

      <section class="categories">
        <div v-for="cat in categories" :key="cat.title" class="cat-card card" @click="activeCategory = cat.key">
          <el-icon class="cat-icon"><component :is="cat.icon" /></el-icon>
          <h3>{{ cat.title }}</h3>
          <p>{{ cat.desc }}</p>
        </div>
      </section>

      <section class="faq card">
        <div class="faq-head">
          <h2>{{ activeTitle }}</h2>
          <span class="faq-count">{{ filteredFaqs.length }} 个问题</span>
        </div>
        <el-collapse v-model="activeNames" accordion>
          <el-collapse-item v-for="(item, index) in filteredFaqs" :key="item.q" :name="index">
            <template #title>
              <span class="faq-q">{{ item.q }}</span>
            </template>
            <p class="faq-a">{{ item.a }}</p>
          </el-collapse-item>
        </el-collapse>
        <el-empty v-if="filteredFaqs.length === 0" description="没有找到相关问题" />
      </section>

      <section class="contact-cta glass-card">
        <h3>没找到想要的答案？</h3>
        <p>联系我们的客服团队，获取一对一帮助。</p>
        <router-link to="/customer-service" class="btn btn-primary">
          在线客服 <el-icon><ArrowRight /></el-icon>
        </router-link>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getFaqs } from '@/api'

const keyword = ref('')
const activeCategory = ref('merch')
const activeNames = ref([])

const categories = [
  { key: 'merch', icon: 'ShoppingBag', title: '周边商城', desc: '商品购买、配送、退换货' },
  { key: 'account', icon: 'User', title: '账户与会员', desc: '注册登录、会员权益' },
  { key: 'schedule', icon: 'Calendar', title: '赛程与观赛', desc: '赛程安排、现场观赛' }
]

const faqs = ref([])

onMounted(async () => {
  try {
    faqs.value = (await getFaqs()) || []
  } catch (e) {
    // 加载失败时保持空列表
  }
})

const activeTitle = computed(() => {
  if (keyword.value.trim()) return '搜索结果'
  const cat = categories.find(c => c.key === activeCategory.value)
  return cat ? cat.title : '常见问题'
})

const filteredFaqs = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  let list = faqs.value
  if (kw) {
    // 关键词搜索时跨全部分类检索
    list = list.filter(item => (item.question + item.answer).toLowerCase().includes(kw))
  } else {
    list = list.filter(item => item.category === activeCategory.value)
  }
  return list.map(item => ({ q: item.question, a: item.answer }))
})
</script>

<style scoped>
.help-page {
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
  margin-bottom: 28px;
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
  padding: 48px 36px;
  text-align: center;
  margin-bottom: 32px;
}

.title {
  font-family: var(--font-display);
  font-size: 40px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 12px 0 10px;
}

.desc {
  font-size: 15px;
  color: var(--text-muted);
  margin: 0 0 28px;
}

.search-input {
  max-width: 520px;
  margin: 0 auto;
}

.search-input :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  box-shadow: none;
  transition: all var(--transition);
}

.search-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--accent);
  background: rgba(200, 16, 46, 0.08);
  box-shadow: 0 0 0 3px rgba(200, 16, 46, 0.15);
}

.search-input :deep(.el-input__inner) {
  color: var(--text-primary);
}

.search-input :deep(.el-input__inner::placeholder) {
  color: var(--text-faint);
}

.categories {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}

.cat-card {
  padding: 28px 24px;
  cursor: pointer;
  text-align: center;
}

.cat-icon {
  font-size: 32px;
  color: var(--accent);
  margin-bottom: 14px;
}

.cat-card h3 {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px;
}

.cat-card p {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0;
}

.faq {
  padding: 32px;
}

.faq-head {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 20px;
}

.faq-head h2 {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.faq-count {
  font-size: 13px;
  color: var(--text-muted);
}

.faq-q {
  font-size: 15px;
  font-weight: 500;
  color: var(--text-primary);
}

.faq-a {
  font-size: 14px;
  line-height: 1.8;
  color: var(--text-muted);
  padding: 0 8px 16px 0;
  margin: 0;
}

.contact-cta {
  margin-top: 32px;
  padding: 40px 36px;
  text-align: center;
}

.contact-cta h3 {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 10px;
}

.contact-cta p {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0 0 22px;
}

.contact-cta .btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

@media (max-width: 900px) {
  .categories {
    grid-template-columns: repeat(2, 1fr);
  }
  .hero {
    padding: 36px 24px;
  }
  .title {
    font-size: 30px;
  }
}
</style>