<template>
  <div class="page">
    <div class="wrapper">
      <nav class="crumb">
        <router-link to="/team">球队阵容</router-link>
        <span class="sep">/</span>
        <span class="current">{{ player.name || '球员详情' }}</span>
      </nav>

      <div v-if="player.id" class="hero card">
        <div class="hero-photo">
          <img v-if="player.image" :src="player.image" :alt="player.name" />
          <div v-else class="photo-fallback">
            <span>{{ number }}</span>
          </div>
        </div>

        <div class="hero-info">
          <span class="pos-pill">{{ player.position }} · {{ player.positionEn }}</span>
          <h1 class="name">{{ player.name }}</h1>
          <p class="en-name">{{ player.enName }}</p>

          <ul class="facts">
            <li><span>球衣号码</span><b>#{{ number }}</b></li>
            <li><span>身高</span><b>{{ player.height }}</b></li>
            <li><span>体重</span><b>{{ player.weight }}</b></li>
            <li><span>NBA 年限</span><b>{{ player.experience || '—' }}</b></li>
            <li><span>毕业院校</span><b>{{ player.college || '—' }}</b></li>
            <li><span>选秀</span><b>{{ player.draft || '—' }}</b></li>
          </ul>
        </div>
      </div>

      <div v-if="player.id" class="stats card">
        <div class="stats-head">
          <span class="section-tag">SEASON AVERAGE</span>
          <h2 class="stats-title">本赛季场均数据</h2>
        </div>

        <div class="stats-grid">
          <div class="stat-item">
            <span class="stat-value">{{ fmt(player.pointsAvg) }}</span>
            <span class="stat-label">得分</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ fmt(player.reboundsAvg) }}</span>
            <span class="stat-label">篮板</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ fmt(player.assistsAvg) }}</span>
            <span class="stat-label">助攻</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ fmt(player.stealsAvg) }}</span>
            <span class="stat-label">抢断</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ fmt(player.blocksAvg) }}</span>
            <span class="stat-label">盖帽</span>
          </div>
        </div>

        <div class="pct-row">
          <div class="pct-item">
            <span class="pct-value">{{ pct(player.fieldGoalPct) }}</span>
            <span class="pct-label">投篮命中率</span>
          </div>
          <div class="pct-item">
            <span class="pct-value">{{ pct(player.threePct) }}</span>
            <span class="pct-label">三分命中率</span>
          </div>
          <div class="pct-item">
            <span class="pct-value">{{ pct(player.freeThrowPct) }}</span>
            <span class="pct-label">罚球命中率</span>
          </div>
        </div>
      </div>

      <div v-if="player.id && player.bio" class="bio card">
        <span class="section-tag">PLAYER PROFILE</span>
        <p class="bio-text">{{ player.bio }}</p>
      </div>

      <el-empty v-else description="球员不存在" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getPlayerDetail } from '@/api'

const route = useRoute()
const player = ref({})

const number = computed(() => String(player.value.number ?? 0).padStart(2, '0'))

onMounted(async () => {
  try {
    player.value = (await getPlayerDetail(route.params.id)) || {}
  } catch (e) {
    player.value = {}
  }
})

const fmt = (v) => (v === null || v === undefined ? '—' : v.toFixed(1))

const pct = (v) => (v === null || v === undefined ? '—' : v.toFixed(1) + '%')
</script>

<style scoped>
.page {
  padding: 32px 0 88px;
}

.crumb {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
  font-size: 14px;
  color: var(--text-muted);
}

.crumb a {
  color: var(--text-secondary);
  transition: color var(--transition);
}

.crumb a:hover {
  color: var(--accent);
}

.crumb .sep {
  color: var(--text-faint);
}

.crumb .current {
  color: var(--text-primary);
}

.hero {
  display: grid;
  grid-template-columns: 380px 1fr;
  overflow: hidden;
  margin-bottom: 24px;
}

.hero-photo {
  height: 460px;
  background: var(--bg-hover);
}

.hero-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center top;
}

.photo-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-brand-blue);
}

.photo-fallback span {
  font-family: var(--font-display);
  font-size: 160px;
  font-weight: 700;
  color: transparent;
  -webkit-text-stroke: 3px rgba(255, 255, 255, 0.6);
}

.hero-info {
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.pos-pill {
  align-self: flex-start;
  padding: 5px 16px;
  background: rgba(200, 16, 46, 0.14);
  border: 1px solid rgba(200, 16, 46, 0.3);
  border-radius: var(--radius-full);
  font-size: 13px;
  color: var(--accent);
  letter-spacing: 1px;
}

.name {
  font-family: var(--font-display);
  font-size: 40px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 16px 0 4px;
  line-height: 1.1;
}

.en-name {
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 3px;
  text-transform: uppercase;
  color: var(--text-faint);
  margin: 0 0 28px;
}

.facts {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 14px 32px;
}

.facts li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--border);
  font-size: 14px;
}

.facts span {
  color: var(--text-faint);
}

.facts b {
  font-family: var(--font-display);
  font-weight: 600;
  color: var(--text-secondary);
}

.stats {
  padding: 32px;
  margin-bottom: 24px;
}

.stats-head {
  margin-bottom: 28px;
}

.stats-title {
  font-family: var(--font-display);
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 8px 0 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
  margin-bottom: 28px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 22px 12px;
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
}

.stat-value {
  font-family: var(--font-display);
  font-size: 30px;
  font-weight: 700;
  color: var(--text-primary);
}

.stat-label {
  font-size: 13px;
  color: var(--text-muted);
}

.pct-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.pct-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px 20px;
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
}

.pct-value {
  font-family: var(--font-display);
  font-size: 24px;
  font-weight: 700;
  color: var(--accent);
}

.pct-label {
  font-size: 13px;
  color: var(--text-muted);
}

.bio {
  padding: 32px;
}

.bio-text {
  font-size: 15px;
  line-height: 1.9;
  color: var(--text-secondary);
  margin: 16px 0 0;
}

@media (max-width: 860px) {
  .hero {
    grid-template-columns: 1fr;
  }

  .hero-photo {
    height: 320px;
  }

  .stats-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}
</style>