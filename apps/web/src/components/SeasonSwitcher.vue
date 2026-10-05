<template>
  <PillDropdown
    :model-value="season"
    :options="seasonOptions"
    more-text="更多赛季"
    placeholder="选择赛季"
    @change="onChange"
  />
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useSeasons } from '@/composables/useSeasons'
import PillDropdown from '@/components/PillDropdown.vue'

const emit = defineEmits(['change'])

const { seasons, currentSeason: season, loadSeasons } = useSeasons()

const seasonOptions = computed(() =>
  seasons.value.map((s) => ({
    value: s.value,
    label: s.label,
    activeLabel: '当前'
  }))
)

const onChange = (v) => {
  season.value = v
  emit('change', v)
}

onMounted(loadSeasons)
</script>
