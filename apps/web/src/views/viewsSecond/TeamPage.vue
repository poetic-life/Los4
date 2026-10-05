<template>
  <div class="page">
    <div class="wrapper">
      <div class="page-head">
        <span class="section-tag">OFFICIAL ROSTER</span>
        <h1 class="section-title">洛杉矶快船 <span class="text-gradient">LA CLIPPERS</span></h1>
        <p class="section-desc">{{ currentSeason.label }} 官方球员名单 · {{ currentSeason.record }} · 汇聚联盟顶级球星</p>
      </div>

      <!-- 赛季切换 -->
      <SeasonSwitcher @change="switchSeason" />

      <div class="roster-grid">
        <div
          class="player-card card clickable"
          v-for="(p, index) in teamPlayers"
          :key="index"
          @click="goDetail(p.id)"
        >
          <div class="player-photo">
            <img v-if="p.imgSrc" :src="p.imgSrc" :alt="p.name" />
            <div v-else class="photo-fallback">
              <span class="fallback-number">{{ p.number }}</span>
            </div>
            <span class="jersey-number">{{ p.number }}</span>
          </div>

          <div class="player-body">
            <div class="player-head">
              <div class="identity">
                <h3 class="player-name">{{ p.name }}</h3>
                <span class="player-en">{{ p.enName }}</span>
              </div>
              <span class="status-badge"><span class="dot"></span>现役</span>
            </div>

            <ul class="player-meta">
              <li>
                <span class="meta-label">位置</span>
                <span class="meta-value">{{ p.position }} · {{ p.positionEn }}</span>
              </li>
              <li>
                <span class="meta-label">身高</span>
                <span class="meta-value">{{ p.height }}</span>
              </li>
              <li>
                <span class="meta-label">体重</span>
                <span class="meta-value">{{ p.weight }}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getPlayers } from '@/api'
import { useSeasons } from '@/composables/useSeasons'
import SeasonSwitcher from '@/components/SeasonSwitcher.vue'

const router = useRouter()

const { seasons, currentSeason: season, loadSeasons } = useSeasons()
const currentSeason = computed(() => seasons.value.find((s) => s.value === season.value) || seasons.value[0] || {})

const teamPlayers = ref([])

const fetchPlayers = async () => {
  try {
    const data = (await getPlayers(season.value)) || []
    teamPlayers.value = data.map((p) => ({
      ...p,
      imgSrc: p.image,
      number: p.number === 0 ? '0' : String(p.number).padStart(2, '0')
    }))
  } catch (e) {
    // 加载失败时保持空列表
  }
}

const switchSeason = (s) => {
  season.value = s
  fetchPlayers()
}

onMounted(async () => {
  await loadSeasons()
  fetchPlayers()
})

const goDetail = (id) => {
  if (id) router.push(`/player/${id}`)
}
</script>

<style scoped>
.page {
  padding: 48px 0 88px;
}

.page-head {
  text-align: center;
  margin-bottom: 44px;
}

.roster-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.player-card {
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.player-photo {
  position: relative;
  height: 280px;
  overflow: hidden;
  background: var(--bg-hover);
}

.player-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center top;
  transition: transform 0.5s ease;
}

.clickable {
  cursor: pointer;
}

.photo-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-brand-blue);
}

.fallback-number {
  font-family: var(--font-display);
  font-size: 120px;
  font-weight: 700;
  color: transparent;
  -webkit-text-stroke: 2px rgba(255, 255, 255, 0.55);
}

.player-card:hover .player-photo img {
  transform: scale(1.06);
}

.jersey-number {
  position: absolute;
  left: 10px;
  bottom: -18px;
  font-family: var(--font-display);
  font-size: 96px;
  font-weight: 700;
  line-height: 1;
  letter-spacing: 2px;
  color: transparent;
  -webkit-text-stroke: 1.5px rgba(255, 255, 255, 0.55);
  pointer-events: none;
  user-select: none;
}

.player-body {
  padding: 18px 20px 20px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.player-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.identity {
  min-width: 0;
}

.player-name {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  line-height: 1.2;
}

.player-en {
  font-family: var(--font-display);
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 1.5px;
  text-transform: uppercase;
  color: var(--text-faint);
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  background: rgba(46, 204, 113, 0.12);
  border: 1px solid rgba(46, 204, 113, 0.3);
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 600;
  color: #2ecc71;
  white-space: nowrap;
}

.dot {
  width: 6px;
  height: 6px;
  background: #2ecc71;
  border-radius: 50%;
}

.player-meta {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.player-meta li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 8px 0;
  border-top: 1px solid var(--border);
  font-size: 13px;
}

.meta-label {
  color: var(--text-faint);
  letter-spacing: 1px;
}

.meta-value {
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 0.5px;
  color: var(--text-secondary);
  text-align: right;
}

@media (max-width: 992px) {
  .roster-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 560px) {
  .roster-grid {
    grid-template-columns: 1fr;
  }
}
</style>