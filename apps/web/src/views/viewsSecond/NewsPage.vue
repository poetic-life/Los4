<template>
  <div class="page">
    <div class="wrapper">
      <div class="page-head">
        <span class="section-tag">LATEST NEWS</span>
        <h1 class="section-title">新闻资讯</h1>
        <p class="section-desc">第一时间获取球队动态</p>
      </div>

      <!-- 赛季切换 -->
      <SeasonSwitcher @change="switchSeason" />

      <!-- 分类 Tab -->
      <div class="news-tabs">
        <button
          v-for="cat in categories"
          :key="cat"
          class="tab-item"
          :class="{ active: activeCategory === cat }"
          @click="changeCategory(cat)"
        >
          {{ cat }}
        </button>
      </div>

      <!-- 搜索 -->
      <div class="news-search">
        <el-icon><Search /></el-icon>
        <input v-model="searchQuery" placeholder="搜索新闻标题..." @keyup.enter="executeSearch" />
        <button class="search-btn" @click="executeSearch">搜索</button>
      </div>

      <!-- 头条新闻：大图横向卡片 -->
      <article class="headline-card card" v-if="filteredNews.length">
        <div class="headline-image">
          <img :src="filteredNews[0].imgSrc" :alt="filteredNews[0].title" />
          <span class="news-badge">头条</span>
        </div>
        <div class="headline-content">
          <span class="news-category">{{ filteredNews[0].category }}</span>
          <h2>{{ filteredNews[0].title }}</h2>
          <p>{{ filteredNews[0].content }}</p>
          <div class="headline-footer">
            <span class="news-date">{{ formatDate(filteredNews[0].date) }}</span>
            <button class="btn btn-primary" @click="viewNews(filteredNews[0].id)">
              查看详情 <ArrowRight />
            </button>
          </div>
        </div>
      </article>

      <!-- 其余新闻：列表行 -->
      <div class="news-list">
        <article
          class="news-row card"
          v-for="(news, index) in filteredNews.slice(1)"
          :key="index"
          @click="viewNews(news.id)"
        >
          <div class="news-thumb">
            <img :src="news.imgSrc" :alt="news.title" />
          </div>
          <div class="news-info">
            <div class="news-meta">
              <span class="news-category">{{ news.category }}</span>
              <span class="news-date">{{ formatDate(news.date) }}</span>
            </div>
            <h3>{{ news.title }}</h3>
            <p>{{ news.content }}</p>
            <span class="view-more">查看详情 <ArrowRight /></span>
          </div>
        </article>
      </div>

      <!-- 分页 -->
      <div v-if="total > pageSize" class="pagination-wrap">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :total="total"
          :page-size="pageSize"
          :current-page="page"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 新闻详情弹窗 -->
    <el-dialog v-model="newsDialogVisible" title="新闻详情" width="680px">
      <div v-if="currentNews" class="news-detail">
        <div class="news-detail-meta">
          <span class="news-category">{{ currentNews.category }}</span>
          <span class="news-date">{{ formatDate(currentNews.date) }}</span>
        </div>
        <h2 class="news-detail-title">{{ currentNews.title }}</h2>
        <img v-if="currentNews.image" :src="currentNews.image" :alt="currentNews.title" class="news-detail-img" />
        <p class="news-detail-content">{{ currentNews.content }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getNews } from '@/api'
import { useSeasons } from '@/composables/useSeasons'
import SeasonSwitcher from '@/components/SeasonSwitcher.vue'

const categories = ['全部', '球队动态', '交易签约', '伤情']
const { seasons, currentSeason: season, loadSeasons } = useSeasons()
const activeCategory = ref('全部')
const searchQuery = ref('')
const page = ref(1)
const pageSize = 6
const total = ref(0)

const newsList = ref([])
const newsDialogVisible = ref(false)
const currentNews = ref(null)

const fetchNews = async () => {
  try {
    const params = { page: page.value, size: pageSize, season: season.value }
    if (activeCategory.value !== '全部') params.category = activeCategory.value
    if (searchQuery.value.trim()) params.keyword = searchQuery.value.trim()
    const data = (await getNews(params)) || {}
    newsList.value = (data.list || []).map((n) => ({ ...n, imgSrc: n.image }))
    total.value = data.total || 0
  } catch (e) {
    newsList.value = []
    total.value = 0
  }
}

onMounted(async () => {
  await loadSeasons()
  fetchNews()
})

const filteredNews = computed(() => newsList.value)

const changeCategory = (cat) => {
  activeCategory.value = cat
  page.value = 1
  fetchNews()
}

const switchSeason = (s) => {
  season.value = s
  page.value = 1
  fetchNews()
}

const executeSearch = () => {
  page.value = 1
  fetchNews()
}

const handlePageChange = (p) => {
  page.value = p
  fetchNews()
}

const formatDate = (date) => {
  if (!date) return ''
  const parts = String(date).split('-')
  if (parts.length === 3) return `${parts[0]}-${parts[1]}-${parts[2]}`
  return date
}

const viewNews = (id) => {
  const news = newsList.value.find((n) => n.id === id)
  if (news) {
    currentNews.value = news
    newsDialogVisible.value = true
  }
}
</script>

<style scoped>
.page {
  padding: 48px 0 88px;
}

.page-head {
  text-align: center;
  margin-bottom: 36px;
}

/* ============ 分类 Tab ============ */
.news-tabs {
  display: flex;
  justify-content: center;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 36px;
}

.tab-item {
  padding: 9px 24px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  color: var(--text-secondary);
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all var(--transition);
}

.tab-item:hover {
  border-color: var(--border-strong);
  color: var(--text-primary);
}

.tab-item.active {
  background: var(--gradient-brand);
  border-color: transparent;
  color: #fff;
  box-shadow: var(--shadow-brand);
}

/* ============ 搜索 ============ */
.news-search {
  display: flex;
  align-items: center;
  gap: 10px;
  max-width: 460px;
  margin: 0 auto 28px;
  padding: 10px 16px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  color: var(--text-muted);
}

.news-search input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  color: var(--text-primary);
  font-size: 14px;
}

.news-search:focus-within {
  border-color: var(--brand-red);
}

.search-btn {
  padding: 5px 16px;
  background: var(--accent);
  border: none;
  border-radius: var(--radius-full);
  color: #fff;
  font-size: 13px;
  cursor: pointer;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 28px;
}

.news-detail-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
}

.news-detail-title {
  font-family: var(--font-display);
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 16px;
  line-height: 1.3;
}

.news-detail-img {
  width: 100%;
  border-radius: var(--radius-md);
  margin-bottom: 16px;
}

.news-detail-content {
  font-size: 15px;
  line-height: 1.9;
  color: var(--text-secondary);
  margin: 0;
}

/* ============ 头条卡片 ============ */
.headline-card {
  display: grid;
  grid-template-columns: 1.1fr 1fr;
  overflow: hidden;
  margin-bottom: 24px;
  cursor: pointer;
}

.headline-image {
  position: relative;
  min-height: 320px;
  overflow: hidden;
}

.headline-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.headline-card:hover .headline-image img {
  transform: scale(1.05);
}

.news-badge {
  position: absolute;
  top: 16px;
  left: 16px;
  padding: 5px 14px;
  background: var(--gradient-brand);
  border-radius: var(--radius-full);
  font-family: var(--font-display);
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 1px;
  color: #fff;
}

.headline-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 40px;
}

.news-category {
  font-family: var(--font-display);
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 2px;
  color: var(--accent);
  margin-bottom: 14px;
}

.headline-content h2 {
  font-family: var(--font-display);
  font-size: 30px;
  font-weight: 700;
  line-height: 1.25;
  color: var(--text-primary);
  margin: 0 0 16px;
}

.headline-content p {
  font-size: 15px;
  color: var(--text-muted);
  line-height: 1.7;
  margin: 0 0 24px;
}

.headline-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.news-date {
  font-size: 13px;
  color: var(--text-muted);
}

/* ============ 新闻列表行 ============ */
.news-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.news-row {
  display: flex;
  gap: 18px;
  padding: 16px;
  cursor: pointer;
}

.news-thumb {
  width: 160px;
  height: 112px;
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

.news-row:hover .news-thumb img {
  transform: scale(1.08);
}

.news-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.news-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 6px;
}

.news-meta .news-category {
  margin-bottom: 0;
  font-size: 12px;
}

.news-info h3 {
  font-size: 17px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 6px;
  line-height: 1.4;
  transition: color var(--transition);
}

.news-row:hover .news-info h3 {
  color: var(--accent);
}

.news-info p {
  font-size: 14px;
  color: var(--text-muted);
  line-height: 1.6;
  margin: 0 0 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.view-more {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  align-self: flex-start;
  font-size: 13px;
  font-weight: 600;
  color: var(--accent);
  transition: gap var(--transition);
}

.news-row:hover .view-more {
  gap: 10px;
}

/* ============ 响应式 ============ */
@media (max-width: 900px) {
  .headline-card {
    grid-template-columns: 1fr;
  }

  .headline-image {
    min-height: 220px;
  }

  .headline-content {
    padding: 28px;
  }
}

@media (max-width: 640px) {
  .news-row {
    flex-direction: column;
  }

  .news-thumb {
    width: 100%;
    height: 180px;
  }
}
</style>