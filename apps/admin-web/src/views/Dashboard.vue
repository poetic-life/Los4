<template>
  <div class="page">
    <div class="list-head">
      <div>
        <h3 class="title">仪表盘</h3>
        <p class="sub">快船球迷社区运营数据总览</p>
      </div>
      <span class="update-time">数据每 5 秒自动刷新</span>
    </div>

    <div class="stats-grid">
      <div v-for="s in stats" :key="s.label" class="stat-card">
        <div class="stat-icon" :style="{ background: s.color }">
          <el-icon><component :is="s.icon" /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ s.value }}</span>
          <span class="stat-label">{{ s.label }}</span>
        </div>
      </div>
    </div>

    <div class="charts-grid">
      <div class="card chart-card">
        <h4 class="chart-title">一周用户增长</h4>
        <VChart :option="userGrowthOption" height="300px" />
      </div>
      <div class="card chart-card">
        <h4 class="chart-title">近 7 日营收趋势</h4>
        <VChart :option="revenueOption" height="300px" />
      </div>
      <div class="card chart-card">
        <h4 class="chart-title">商品销量 TOP6</h4>
        <VChart :option="topProductsOption" height="300px" />
      </div>
      <div class="card chart-card">
        <h4 class="chart-title">订单状态分布</h4>
        <VChart :option="statusDistOption" height="300px" />
      </div>
      <div class="card chart-card">
        <h4 class="chart-title">用户角色分布</h4>
        <VChart :option="roleDistOption" height="300px" />
      </div>
      <div class="card chart-card">
        <h4 class="chart-title">商品分类分布</h4>
        <VChart :option="categoryDistOption" height="300px" />
      </div>
    </div>

    <div class="card section">
      <div class="section-head">
        <h4 class="section-title">最近订单</h4>
        <router-link to="/orders" class="more">查看全部</router-link>
      </div>
      <el-table :data="recentOrders" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" min-width="180" />
        <el-table-column prop="name" label="收货人" width="130" />
        <el-table-column label="金额" width="110">
          <template #default="scope">¥{{ scope.row.totalAmount }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="statusType(scope.row.status)" effect="dark">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" width="170">
          <template #default="scope">{{ formatTime(scope.row.createdAt) }}</template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getDashboard, getTrends } from '@/api'
import VChart from '@/components/VChart.vue'

const data = ref({})
const trends = ref({})
const loading = ref(false)
let timer = null

const PALETTE = ['#c8102e', '#1d428a', '#13c2c2', '#faad14', '#9254de', '#52c41a', '#ff7a45', '#2f54eb']

const stats = computed(() => {
  const d = data.value || {}
  return [
    { label: '用户总数', value: d.userCount ?? 0, icon: 'User', color: '#c8102e' },
    { label: '帖子总数', value: d.postCount ?? 0, icon: 'ChatLineSquare', color: '#1d428a' },
    { label: '评论总数', value: d.commentCount ?? 0, icon: 'ChatDotRound', color: '#fa8c16' },
    { label: '商品总数', value: d.productCount ?? 0, icon: 'Goods', color: '#13c2c2' },
    { label: '订单总数', value: d.orderCount ?? 0, icon: 'Tickets', color: '#9254de' },
    { label: '待发货', value: d.pendingShipCount ?? 0, icon: 'Van', color: '#faad14' },
    { label: '总营收(元)', value: d.totalRevenue ?? 0, icon: 'Wallet', color: '#52c41a' }
  ]
})

const t = computed(() => trends.value || {})
const dates = computed(() => t.value.dates || [])

const axisStyle = {
  axisLine: { lineStyle: { color: '#333a47' } },
  axisLabel: { color: '#9aa0ab' },
  splitLine: { lineStyle: { color: '#232833' } }
}

const userGrowthOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  legend: { data: ['累计用户', '新增用户'], textStyle: { color: '#9aa0ab' }, top: 0 },
  grid: { left: 40, right: 20, top: 40, bottom: 30 },
  xAxis: { type: 'category', data: dates.value, ...axisStyle },
  yAxis: { type: 'value', ...axisStyle },
  series: [
    {
      name: '累计用户',
      type: 'line',
      smooth: true,
      data: t.value.userGrowth || [],
      lineStyle: { color: '#c8102e', width: 3 },
      itemStyle: { color: '#c8102e' },
      areaStyle: { color: 'rgba(200, 16, 46, 0.2)' }
    },
    {
      name: '新增用户',
      type: 'bar',
      data: t.value.userNew || [],
      barWidth: 16,
      itemStyle: { color: '#1d428a' }
    }
  ]
}))

const revenueOption = computed(() => ({
  tooltip: { trigger: 'axis', valueFormatter: (v) => '¥' + v },
  grid: { left: 60, right: 20, top: 30, bottom: 30 },
  xAxis: { type: 'category', data: dates.value, ...axisStyle },
  yAxis: { type: 'value', ...axisStyle },
  series: [
    {
      name: '营收',
      type: 'line',
      smooth: true,
      data: t.value.revenueTrend || [],
      lineStyle: { color: '#13c2c2', width: 3 },
      itemStyle: { color: '#13c2c2' },
      areaStyle: { color: 'rgba(19, 194, 194, 0.18)' }
    }
  ]
}))

const topProductsOption = computed(() => {
  const list = (t.value.topProducts || []).slice().reverse()
  return {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: 120, right: 40, top: 10, bottom: 24 },
    xAxis: { type: 'value', ...axisStyle },
    yAxis: { type: 'category', data: list.map((i) => i.name), ...axisStyle, splitLine: { show: false } },
    series: [
      {
        type: 'bar',
        data: list.map((i) => i.sales),
        barWidth: 14,
        label: { show: true, position: 'right', color: '#9aa0ab' },
        itemStyle: { color: '#c8102e', borderRadius: [0, 7, 7, 0] }
      }
    ]
  }
})

const doughnutOption = (items) => ({
  tooltip: { trigger: 'item' },
  color: PALETTE,
  legend: { bottom: 0, textStyle: { color: '#9aa0ab' }, itemWidth: 12, itemHeight: 12 },
  series: [
    {
      type: 'pie',
      radius: ['48%', '70%'],
      center: ['50%', '42%'],
      label: { show: false },
      itemStyle: { borderColor: '#0b0d12', borderWidth: 2 },
      data: items
    }
  ]
})

const statusDistOption = computed(() => doughnutOption(t.value.statusDist || []))
const roleDistOption = computed(() => doughnutOption(t.value.roleDist || []))
const categoryDistOption = computed(() => doughnutOption(t.value.categoryDist || []))

const recentOrders = computed(() => data.value?.recentOrders || [])

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 16) : '')
const statusType = (s) => ({ 待发货: 'warning', 已发货: 'primary', 已完成: 'success', 已取消: 'info' }[s] || 'info')

const refresh = async () => {
  try {
    const [d, tr] = await Promise.all([getDashboard(), getTrends()])
    data.value = d || {}
    trends.value = tr || {}
  } catch (e) {
    /* ignore */
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loading.value = true
  refresh()
  timer = setInterval(refresh, 5000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.page {
  max-width: 1400px;
}

.list-head {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 22px;
}

.title {
  font-family: var(--font-display);
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.sub {
  font-size: 13px;
  color: var(--text-muted);
  margin: 4px 0 0;
}

.update-time {
  font-size: 12px;
  color: var(--text-faint);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  transition: all var(--transition);
}

.stat-card:hover {
  border-color: var(--border-strong);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.stat-icon {
  width: 48px;
  height: 48px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: #fff;
  border-radius: var(--radius-md);
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1.1;
}

.stat-label {
  font-size: 13px;
  color: var(--text-muted);
}

.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 24px;
}

.chart-card {
  padding: 20px;
}

.chart-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 12px;
}

.section {
  padding: 20px;
}

.section-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.more {
  font-size: 13px;
  color: var(--accent);
}

@media (max-width: 1100px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }
}
</style>