<template>
  <div class="page">
    <div class="wrapper">
      <div class="page-head">
        <span class="section-tag">SCHEDULE · {{ currentSeason.value }}</span>
        <h1 class="section-title">快船赛程</h1>
        <p class="section-desc">洛杉矶快船 {{ currentSeason.label }} 常规赛完整赛程</p>
      </div>

      <!-- 赛季切换 -->
      <SeasonSwitcher @change="switchSeason" />

      <!-- 赛季概览 -->
      <div class="season-summary card">
        <div class="summary-left">
          <span class="summary-logo">LA</span>
          <div class="summary-meta">
            <span class="summary-name">洛杉矶快船</span>
            <span class="summary-sub">LA Clippers</span>
          </div>
        </div>
        <div class="summary-record">
          <span class="record-num">{{ currentSeason.wins }}</span><span class="record-unit">胜</span>
          <span class="record-sep">/</span>
          <span class="record-num">{{ currentSeason.losses }}</span><span class="record-unit">负</span>
        </div>
        <span class="summary-tip">{{ currentSeason.tip }}</span>
      </div>

      <!-- 月份切换（与赛季按钮同款下拉） -->
      <PillDropdown
        v-model="selectedMonth"
        :options="monthOptions"
        more-text="选择月份"
        placeholder="选择月份"
      />

      <!-- 比赛列表 -->
      <div class="schedule-list" v-if="filteredGames.length">
        <div class="game-card card" v-for="g in filteredGames" :key="g.id">
          <div class="game-date">
            <span class="date-main">{{ g.month }}-{{ g.day }}</span>
            <span class="date-time">{{ g.time }}</span>
          </div>

          <div class="game-center">
            <div class="game-title">
              <span class="home-away-badge" :class="g.isHome ? 'home' : 'away'">
                {{ g.isHome ? '主场' : '客场' }}
              </span>
              <span class="vs-text">快船 <b>VS</b> {{ g.opponent }}</span>
            </div>
            <span class="game-venue">
              <Location class="venue-icon" />{{ g.venue }}
            </span>
          </div>

          <div class="game-result">
            <template v-if="g.isFinished">
              <div class="score-line">
                <span class="score" :class="g.win ? 'win' : 'loss'">{{ g.clippersScore }}</span>
                <span class="score-sep">-</span>
                <span class="score muted">{{ g.oppScore }}</span>
              </div>
              <span class="result-badge" :class="g.win ? 'win' : 'loss'">{{ g.win ? '胜' : '负' }}</span>
            </template>
            <span v-else class="upcoming-text">未开始</span>
          </div>
        </div>
      </div>

      <div class="empty-state card" v-else>
        <Calendar class="empty-icon" />
        <p>该月暂无比赛安排</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getSchedule } from '@/api'
import { useSeasons } from '@/composables/useSeasons'
import SeasonSwitcher from '@/components/SeasonSwitcher.vue'
import PillDropdown from '@/components/PillDropdown.vue'

const teamNames = {
  'LA CLIPPERS': '洛杉矶快船',
  LAKERS: '洛杉矶湖人',
  NUGGETS: '丹佛掘金',
  WARRIORS: '金州勇士',
  SUNS: '菲尼克斯太阳',
  THUNDER: '俄克拉荷马雷霆',
  PELICANS: '新奥尔良鹈鹕',
  RAPTORS: '多伦多猛龙',
  ROCKETS: '休斯顿火箭',
  MAVERICKS: '达拉斯独行侠',
  KINGS: '萨克拉门托国王',
  SPURS: '圣安东尼奥马刺',
  BUCKS: '密尔沃基雄鹿',
  GRIZZLIES: '孟菲斯灰熊',
  TIMBERWOLVES: '明尼苏达森林狼',
  HEAT: '迈阿密热火',
  KNICKS: '纽约尼克斯',
  'TRAIL BLAZERS': '波特兰开拓者',
  '76ERS': '费城76人',
  CAVALIERS: '克利夫兰骑士',
  CELTICS: '波士顿凯尔特人',
  JAZZ: '犹他爵士',
  BULLS: '芝加哥公牛',
  NETS: '布鲁克林篮网',
  HORNETS: '夏洛特黄蜂',
  HAWKS: '亚特兰大老鹰',
  WIZARDS: '华盛顿奇才',
  PISTONS: '底特律活塞'
}

const { seasons, currentSeason: season, loadSeasons } = useSeasons()
const currentSeason = computed(() => seasons.value.find((s) => s.value === season.value) || seasons.value[0] || {})

const selectedMonth = ref('')

const games = ref([])

// 月份选项按比赛日期顺序动态生成（与赛季按钮同款下拉）
const monthOptions = computed(() => {
  const seen = []
  games.value.forEach((g) => {
    if (g.month && !seen.includes(g.month)) seen.push(g.month)
  })
  return seen.map((m) => ({ value: m, label: `${m}月`, activeLabel: '当前' }))
})

// 切换赛季后默认定位到下一场未赛比赛所在月份，否则取第一个月
const pickMonth = () => {
  if (selectedMonth.value && monthOptions.value.some((o) => o.value === selectedMonth.value)) return
  const next = games.value.find((g) => !g.isFinished)
  selectedMonth.value = next ? next.month : (monthOptions.value[0]?.value || '')
}

const fetchSchedule = async () => {
  try {
    const data = (await getSchedule(season.value)) || []
    games.value = data.map((g) => {
      const parts = String(g.date || '').split('-')
      const month = parts.length === 3 ? String(parseInt(parts[1], 10)) : ''
      const day = parts.length === 3 ? String(parseInt(parts[2], 10)) : ''
      const isHome = g.homeTeam === 'LA CLIPPERS'
      const opponent = isHome ? g.awayTeam : g.homeTeam
      const clippersScore = isHome ? g.homeScore : g.awayScore
      const oppScore = isHome ? g.awayScore : g.homeScore
      const isFinished = g.status === '已结束' && clippersScore != null && oppScore != null
      const win = isFinished && clippersScore > oppScore
      return {
        ...g,
        month,
        day,
        isHome,
        isFinished,
        opponent: teamNames[opponent] || opponent,
        clippersScore,
        oppScore,
        win
      }
    })
    pickMonth()
  } catch (e) {
    // 加载失败时保持空列表
  }
}

const switchSeason = (s) => {
  season.value = s
  selectedMonth.value = ''
  fetchSchedule()
}

onMounted(async () => {
  await loadSeasons()
  fetchSchedule()
})

const filteredGames = computed(() => games.value.filter((g) => g.month === selectedMonth.value))
</script>

<style scoped>
.page {
  padding: 48px 0 88px;
}

.page-head {
  text-align: center;
  margin-bottom: 36px;
}

/* ============ 赛季概览 ============ */
.season-summary {
  display: flex;
  align-items: center;
  gap: 22px;
  padding: 22px 28px;
  margin-bottom: 28px;
}

.summary-left {
  display: flex;
  align-items: center;
  gap: 14px;
}

.summary-logo {
  width: 52px;
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-brand);
  border-radius: 14px;
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: #fff;
}

.summary-meta {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.summary-name {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
}

.summary-sub {
  font-size: 12px;
  color: var(--text-faint);
  letter-spacing: 1px;
}

.summary-record {
  display: flex;
  align-items: baseline;
  gap: 4px;
  padding-left: 22px;
  border-left: 1px solid var(--border);
}

.record-num {
  font-family: var(--font-display);
  font-size: 30px;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1;
}

.record-unit {
  font-size: 13px;
  color: var(--text-muted);
}

.record-sep {
  font-size: 18px;
  color: var(--text-faint);
  margin: 0 6px;
}

.summary-tip {
  margin-left: auto;
  font-size: 13px;
  color: var(--accent);
  padding: 6px 14px;
  background: rgba(200, 16, 46, 0.1);
  border-radius: var(--radius-full);
}

/* ============ 比赛列表 ============ */
.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.game-card {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 18px 26px;
}

.game-date {
  flex: 0 0 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
  padding: 12px 0;
  background: var(--bg-hover);
  border-radius: var(--radius-md);
}

.date-main {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
}

.date-time {
  font-size: 12px;
  color: var(--text-muted);
}

.game-center {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.game-title {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.home-away-badge {
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 1px;
  white-space: nowrap;
}

.home-away-badge.home {
  color: #6f9cff;
  background: rgba(29, 66, 138, 0.16);
  border: 1px solid rgba(111, 156, 255, 0.4);
}

.home-away-badge.away {
  color: var(--text-muted);
  background: var(--bg-hover);
  border: 1px solid var(--border-strong);
}

.vs-text {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.vs-text b {
  font-family: var(--font-display);
  color: var(--accent);
  margin: 0 6px;
}

.game-venue {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
  color: var(--text-faint);
}

.venue-icon {
  width: 13px;
  height: 13px;
}

.game-result {
  flex: 0 0 130px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
}

.score-line {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.score {
  font-family: var(--font-display);
  font-size: 24px;
  font-weight: 700;
  line-height: 1;
}

.score.win {
  color: #2ecc71;
}

.score.loss {
  color: var(--accent);
}

.score.muted {
  color: var(--text-muted);
  font-weight: 600;
}

.score-sep {
  color: var(--text-faint);
  font-size: 16px;
}

.result-badge {
  font-size: 12px;
  font-weight: 700;
  padding: 2px 10px;
  border-radius: var(--radius-full);
}

.result-badge.win {
  color: #2ecc71;
  background: rgba(46, 204, 113, 0.12);
}

.result-badge.loss {
  color: var(--accent);
  background: rgba(200, 16, 46, 0.12);
}

.upcoming-text {
  font-size: 14px;
  color: var(--text-muted);
  padding: 8px 0;
}

/* ============ 空状态 ============ */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 64px 24px;
  color: var(--text-muted);
}

.empty-icon {
  width: 40px;
  height: 40px;
  color: var(--text-faint);
}

/* ============ 响应式 ============ */
@media (max-width: 700px) {
  .season-summary {
    flex-direction: column;
    align-items: flex-start;
  }

  .summary-record {
    border-left: none;
    padding-left: 0;
  }

  .summary-tip {
    margin-left: 0;
  }

  .game-card {
    gap: 16px;
    padding: 16px 20px;
    flex-wrap: wrap;
  }

  .game-result {
    flex: 1 1 100%;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }
}
</style>