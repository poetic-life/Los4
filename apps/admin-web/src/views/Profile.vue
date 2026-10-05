<template>
  <div class="page">
    <div class="profile-hero card">
      <div class="hero-main">
        <div class="hero-avatar">{{ avatarText }}</div>
        <div class="hero-info">
          <div class="hero-name-row">
            <h3 class="hero-name">{{ me.username }}</h3>
            <span class="badge badge-admin">超级管理员</span>
          </div>
          <p class="hero-bio">{{ me.bio || '这个人很低调，还没有填写简介。' }}</p>
          <div class="hero-meta">
            <span><el-icon><Message /></el-icon>{{ me.email || '未绑定邮箱' }}</span>
            <span><el-icon><Medal /></el-icon>{{ me.level || '银卡会员' }}</span>
            <span><el-icon><Coin /></el-icon>{{ me.points ?? 0 }} 积分</span>
            <span><el-icon><Clock /></el-icon>{{ formatTime(me.createdAt) }} 注册</span>
          </div>
        </div>
      </div>
    </div>

    <div class="stats-grid">
      <div v-for="s in stats" :key="s.label" class="stat-card">
        <span class="value">{{ s.value }}</span>
        <span class="label">{{ s.label }}</span>
      </div>
    </div>

    <div class="card section">
      <h4 class="section-title">账号信息</h4>
      <div class="info-list">
        <div class="info-row">
          <span class="info-key">用户 ID</span>
          <span class="info-val">{{ me.id }}</span>
        </div>
        <div class="info-row">
          <span class="info-key">用户名</span>
          <span class="info-val">{{ me.username }}</span>
        </div>
        <div class="info-row">
          <span class="info-key">邮箱</span>
          <span class="info-val">{{ me.email || '未绑定' }}</span>
        </div>
        <div class="info-row">
          <span class="info-key">角色</span>
          <span class="info-val">ADMIN · 超级管理员</span>
        </div>
        <div class="info-row">
          <span class="info-key">会员等级</span>
          <span class="info-val">{{ me.level || '银卡会员' }}</span>
        </div>
        <div class="info-row">
          <span class="info-key">注册时间</span>
          <span class="info-val">{{ formatTime(me.createdAt) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getDashboard } from '@/api'

const userStore = useUserStore()
const dash = ref({})

const me = computed(() => userStore.userInfo || {})
const avatarText = computed(() => (me.value.username || 'A').charAt(0).toUpperCase())

const stats = computed(() => {
  const d = dash.value || {}
  return [
    { label: '注册用户', value: d.userCount ?? 0 },
    { label: '社区帖子', value: d.postCount ?? 0 },
    { label: '评论总数', value: d.commentCount ?? 0 },
    { label: '在售商品', value: d.productCount ?? 0 },
    { label: '订单总数', value: d.orderCount ?? 0 },
    { label: '待处理发货', value: d.pendingShipCount ?? 0 }
  ]
})

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 16) : '')

onMounted(async () => {
  try {
    dash.value = (await getDashboard()) || {}
  } catch (e) {
    dash.value = {}
  }
})
</script>

<style scoped>
.page {
  max-width: 1400px;
}

.profile-hero {
  padding: 28px;
  margin-bottom: 24px;
}

.hero-main {
  display: flex;
  align-items: center;
  gap: 24px;
}

.hero-avatar {
  width: 88px;
  height: 88px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  background: var(--gradient-brand-blue);
  border-radius: 50%;
  box-shadow: var(--shadow-brand);
}

.hero-name-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 6px;
}

.hero-name {
  font-family: var(--font-display);
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.badge {
  padding: 3px 12px;
  font-size: 12px;
  font-weight: 600;
  border-radius: var(--radius-full);
}

.badge-admin {
  background: var(--gradient-brand);
  color: #fff;
}

.hero-bio {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0 0 14px;
}

.hero-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.hero-meta span {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-muted);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
}

.stat-card .value {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
}

.stat-card .label {
  font-size: 13px;
  color: var(--text-muted);
}

.section {
  padding: 22px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 16px;
}

.info-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 40px;
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 0;
  border-bottom: 1px solid var(--border);
}

.info-key {
  font-size: 14px;
  color: var(--text-muted);
}

.info-val {
  font-size: 14px;
  color: var(--text-primary);
  font-weight: 500;
}

@media (max-width: 900px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .info-list {
    grid-template-columns: 1fr;
  }
}
</style>