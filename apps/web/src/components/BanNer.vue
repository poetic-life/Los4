<template>
  <section class="banner" @mouseover="stopAutoSlide" @mouseleave="startAutoSlide">
    <div class="wrapper">
      <div class="section-header">
        <span class="section-tag">TOP MOMENTS</span>
        <h2 class="section-title">精彩瞬间</h2>
        <p class="section-desc">每一帧都是热血的见证</p>
      </div>

      <div class="banner-stage">
        <transition name="fade" mode="out-in">
          <div class="slide" :key="currentIndex">
            <img
              :src="images[currentIndex].src"
              :alt="bannerTitles[currentIndex]"
            />
            <div class="slide-overlay"></div>
            <div class="slide-info">
              <span class="slide-tag">{{ currentIndex + 1 }} / {{ images.length }}</span>
              <h3 class="slide-title">{{ bannerTitles[currentIndex] }}</h3>
              <p class="slide-subtitle">{{ bannerSubtitles[currentIndex] }}</p>
            </div>
          </div>
        </transition>

        <button class="nav-btn prev" @click="prevSlide" aria-label="上一张">
          <i class="iconfont icon-arrow-left-bold"></i>
        </button>
        <button class="nav-btn next" @click="nextSlide" aria-label="下一张">
          <i class="iconfont icon-arrow-right-bold"></i>
        </button>

        <div class="indicators">
          <span
            v-for="(item, index) in images"
            :key="index"
            :class="{ active: currentIndex === index }"
            @click="goToSlide(index)"
          ></span>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import hardenImg from '../assets/uploads/harden.png'
import westbrookImg from '../assets/uploads/westbrook.png'
import georgeImg from '../assets/uploads/george.png'
import kawhiImg from '../assets/uploads/kawhi.png'

const images = ref([
  { src: hardenImg },
  { src: westbrookImg },
  { src: georgeImg },
  { src: kawhiImg }
])

const bannerTitles = ['JAMES HARDEN', 'RUSSELL WESTBROOK', 'PAUL GEORGE', 'KAWHI LEONARD']
const bannerSubtitles = [
  '哈登 · 变幻莫测的进攻大师',
  '威少 · 势不可挡的爆发力',
  '乔治 · 攻防一体的全能锋线',
  '伦纳德 · 冷血无情的终结者'
]

const currentIndex = ref(0)
const intervalId = ref(null)

const startAutoSlide = () => {
  intervalId.value = setInterval(() => nextSlide(), 5000)
}

const stopAutoSlide = () => {
  if (intervalId.value) clearInterval(intervalId.value)
}

const nextSlide = () => {
  currentIndex.value = (currentIndex.value + 1) % images.value.length
}

const prevSlide = () => {
  currentIndex.value = (currentIndex.value - 1 + images.value.length) % images.value.length
}

const goToSlide = (index) => {
  currentIndex.value = index
}

onMounted(startAutoSlide)
onBeforeUnmount(stopAutoSlide)
</script>

<style scoped>
.banner {
  padding: 90px 0;
  background: var(--bg-base);
}

.section-header {
  text-align: center;
  margin-bottom: 48px;
}

.banner-stage {
  position: relative;
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
}

.slide {
  position: relative;
  height: 480px;
  overflow: hidden;
  background: radial-gradient(ellipse at 50% 40%, #2a2f3a 0%, #14171e 55%, #0b0d12 100%);
}

.slide img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  object-position: center bottom;
  display: block;
}

.slide-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(11, 13, 18, 0.1) 0%, rgba(11, 13, 18, 0.25) 50%, rgba(11, 13, 18, 0.85) 100%);
}

.slide-info {
  position: absolute;
  left: 40px;
  bottom: 40px;
}

.slide-tag {
  display: inline-block;
  padding: 5px 14px;
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(8px);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-full);
  font-family: var(--font-display);
  font-size: 12px;
  letter-spacing: 2px;
  color: #fff;
  margin-bottom: 14px;
}

.slide-title {
  font-family: var(--font-display);
  font-size: 3rem;
  font-weight: 700;
  letter-spacing: 1px;
  text-transform: uppercase;
  color: #fff;
  margin: 0 0 8px;
}

.slide-subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.nav-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(22, 26, 35, 0.6);
  backdrop-filter: blur(12px);
  border: 1px solid var(--glass-border);
  border-radius: 50%;
  color: #fff;
  font-size: 18px;
  cursor: pointer;
  transition: all var(--transition);
}

.nav-btn:hover {
  background: var(--accent);
  border-color: var(--accent);
}

.nav-btn.prev { left: 20px; }
.nav-btn.next { right: 20px; }

.indicators {
  position: absolute;
  left: 50%;
  bottom: 24px;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
}

.indicators span {
  width: 24px;
  height: 4px;
  border-radius: 2px;
  background: rgba(255, 255, 255, 0.35);
  cursor: pointer;
  transition: all var(--transition);
}

.indicators span.active {
  background: var(--accent);
  width: 40px;
}

/* 切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
  .slide {
    height: 300px;
  }

  .slide-info {
    left: 24px;
    bottom: 24px;
  }

  .slide-title {
    font-size: 2rem;
  }
}
</style>