<template>
  <div class="pill-dropdown">
    <div class="pill-current" :class="{ open }" @click="toggle">
      <span class="pill-current-label">{{ currentLabel }}</span>
      <el-icon class="caret" :class="{ open }"><ArrowDown /></el-icon>
    </div>

    <button v-if="options.length > 1" type="button" class="pill-more" @click="toggle">
      {{ moreText }}
    </button>

    <div v-if="open" class="pill-overlay" @click="open = false"></div>
    <transition name="pill-fade">
      <div v-if="open" class="pill-menu">
        <button
          v-for="o in options"
          :key="o.value"
          type="button"
          class="pill-option"
          :class="{ active: o.value === modelValue }"
          @click="select(o.value)"
        >
          <span>{{ o.label }}</span>
          <span v-if="o.value === modelValue && o.activeLabel" class="pill-check">{{ o.activeLabel }}</span>
        </button>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  modelValue: { type: [String, Number], default: '' },
  options: { type: Array, default: () => [] },
  moreText: { type: String, default: '更多' },
  placeholder: { type: String, default: '请选择' }
})

const emit = defineEmits(['update:modelValue', 'change'])

const open = ref(false)

const currentLabel = computed(() => {
  const o = props.options.find((x) => x.value === props.modelValue)
  return (o && o.label) || props.placeholder
})

const toggle = () => {
  open.value = !open.value
}

const select = (v) => {
  if (v !== props.modelValue) {
    emit('update:modelValue', v)
    emit('change', v)
  }
  open.value = false
}
</script>

<style scoped>
.pill-dropdown {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.pill-current {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 9px 22px;
  background: var(--gradient-brand);
  border: 1px solid transparent;
  border-radius: var(--radius-full);
  color: #fff;
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: var(--shadow-brand);
  transition: all var(--transition);
  user-select: none;
}

.pill-current:hover {
  filter: brightness(1.05);
}

.caret {
  font-size: 14px;
  transition: transform var(--transition);
}

.caret.open {
  transform: rotate(180deg);
}

.pill-more {
  padding: 9px 22px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  color: var(--text-secondary);
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition);
}

.pill-more:hover {
  border-color: var(--border-strong);
  color: var(--text-primary);
}

.pill-overlay {
  position: fixed;
  inset: 0;
  z-index: 30;
  background: transparent;
}

.pill-menu {
  position: absolute;
  top: calc(100% + 10px);
  left: 50%;
  transform: translateX(-50%);
  z-index: 31;
  min-width: 200px;
  padding: 6px;
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-md);
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.pill-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 11px 16px;
  background: transparent;
  border: none;
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  font-size: 14px;
  cursor: pointer;
  transition: all var(--transition);
  text-align: left;
}

.pill-option:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.pill-option.active {
  background: rgba(200, 16, 46, 0.12);
  color: var(--accent);
  font-weight: 600;
}

.pill-check {
  font-size: 11px;
  color: var(--accent);
  padding: 2px 8px;
  border: 1px solid rgba(200, 16, 46, 0.3);
  border-radius: var(--radius-full);
  white-space: nowrap;
}

.pill-fade-enter-active,
.pill-fade-leave-active {
  transition: opacity 0.15s ease, transform 0.15s ease;
}

.pill-fade-enter-from,
.pill-fade-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(-6px);
}
</style>
