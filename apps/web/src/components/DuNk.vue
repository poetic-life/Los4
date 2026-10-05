<template>
  <section class="dunk">
    <div class="wrapper">
      <div class="section-header">
        <span class="section-tag">DUNK SHOW</span>
        <h2 class="section-title">扣篮展示</h2>
        <p class="section-desc">飞天遁地，震撼全场</p>
      </div>

      <div class="dunk-grid">
        <router-link
          v-for="(item, index) in DunkData"
          :key="index"
          :to="item.path"
          class="dunk-card card"
        >
          <div class="dunk-image">
            <img :src="item.imgSrc" :alt="item.title" />
            <div class="dunk-overlay"></div>
            <span class="dunk-number">{{ String(index + 1).padStart(2, '0') }}</span>
            <div class="dunk-info">
              <h3 class="dunk-title">{{ item.title }}</h3>
            </div>
            <button class="play-button" @click.stop.prevent="playDunk(index)" aria-label="播放">
              <i class="iconfont icon-play"></i>
            </button>
          </div>
        </router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import hddunk from '../assets/uploads/hddunk.jpg'
import wsdunk from '../assets/uploads/wsdunk.jpg'
import qzdunk from '../assets/uploads/qzdunk.jpg'
import lnddunk from '../assets/uploads/lnddunk.jpg'

const DunkData = ref([
  { id: 1, imgSrc: hddunk, title: '哈登暴扣', path: '/DK' },
  { id: 2, imgSrc: wsdunk, title: '威少暴扣', path: '/DK' },
  { id: 3, imgSrc: qzdunk, title: '乔治暴扣', path: '/DK' },
  { id: 4, imgSrc: lnddunk, title: '伦纳德暴扣', path: '/DK' }
])

const playDunk = (index) => {
  console.log(`Playing dunk ${index + 1}: ${DunkData.value[index].title}`)
}
</script>

<style scoped>
.dunk {
  padding: 90px 0;
  background: var(--bg-base);
}

.section-header {
  text-align: center;
  margin-bottom: 48px;
}

.dunk-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.dunk-card {
  display: block;
  text-decoration: none;
  color: inherit;
  overflow: hidden;
}

.dunk-image {
  position: relative;
  height: 320px;
  overflow: hidden;
}

.dunk-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.dunk-card:hover .dunk-image img {
  transform: scale(1.08);
}

.dunk-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(11, 13, 18, 0.05) 0%, rgba(11, 13, 18, 0.15) 40%, rgba(11, 13, 18, 0.9) 100%);
}

.dunk-number {
  position: absolute;
  top: 14px;
  left: 14px;
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.6);
}

.dunk-info {
  position: absolute;
  left: 16px;
  bottom: 16px;
}

.dunk-title {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  margin: 0;
}

.play-button {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) scale(0.9);
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(200, 16, 46, 0.9);
  border: none;
  border-radius: 50%;
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  opacity: 0;
  transition: all var(--transition);
}

.dunk-card:hover .play-button {
  opacity: 1;
  transform: translate(-50%, -50%) scale(1);
}

.play-button:hover {
  background: var(--brand-red-hover);
}

@media (max-width: 992px) {
  .dunk-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 560px) {
  .dunk-grid {
    grid-template-columns: 1fr;
  }
}
</style>