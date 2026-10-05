import { ref } from 'vue'
import { getSeasons } from '@/api'

/**
 * 赛季数据源：统一从后端 /api/seasons 获取，三页（赛程/球员/新闻）共用，
 * 避免各页硬编码赛季列表。新增赛季只需在后端 seed 数据，无需改前端。
 */
const seasons = ref([])
const currentSeason = ref('')
const loaded = ref(false)
let loadPromise = null

async function loadSeasons() {
  if (loaded.value) return seasons.value
  if (loadPromise) return loadPromise
  loadPromise = (async () => {
    try {
      const data = (await getSeasons()) || []
      seasons.value = data.map((s) => {
        const wins = s.wins || 0
        const losses = s.losses || 0
        return {
          value: s.season,
          label: s.label || s.season,
          wins,
          losses,
          standing: s.standing || '',
          note: s.note || '',
          // 赛程页用的战绩摘要
          tip: [s.standing, s.note].filter(Boolean).join(' · '),
          // 球员页用的战绩描述
          record: `${wins}胜${losses}负${s.standing ? ' · ' + s.standing : ''}`
        }
      })
      if (!currentSeason.value && seasons.value.length) {
        currentSeason.value = seasons.value[0].value
      }
      loaded.value = true
    } catch (e) {
      seasons.value = []
    } finally {
      loadPromise = null
    }
  })()
  return loadPromise
}

export function useSeasons() {
  return { seasons, currentSeason, loadSeasons }
}