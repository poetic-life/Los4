<template>
  <div class="FasCinating">
    <div class="wrapper">
      <div class="title">
        <div class="left">
          <h3>精彩集锦</h3>
          <p>视觉盛宴 不容错过</p>
        </div>
        <div class="button">
          <button class="prev" @click="prev">
            <i class="iconfont icon-arrow-left-bold"></i>
          </button>
          <button class="next" @click="next">
            <i class="iconfont icon-arrow-right-bold"></i>
          </button>
        </div>
      </div>
      <div class="bd">
        <ul>
          <li v-for="(video, index) in videos" :key="index">
            <video :src="video.src" controls muted loop></video>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import hdVideo from '../assets/uploads/哈登集锦.mp4'
import rwVideo from '../assets/uploads/威少集锦.mp4'
import pgVideo from '../assets/uploads/乔治集锦.mp4'
import lndVideo from '../assets/uploads/伦纳德集锦.mp4'

const videos = ref([
  { src: hdVideo },
  { src: rwVideo },
  { src: pgVideo },
  { src: lndVideo }
])

const prev = () => {
  console.log('上一个视频')
}

const next = () => {
  console.log('下一个视频')
}
</script>

<style scoped>
.FasCinating {
  padding: 50px 0;
  background-color: #f9f9f9;
}

.wrapper {
  width: 1200px;
  margin: 0 auto;
}

.title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.title .left h3 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.title .left p {
  margin: 5px 0 0;
  font-size: 14px;
  color: #888;
}

.title .button {
  display: flex;
  gap: 10px;
}

.title .button button {
  width: 40px;
  height: 40px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.title .button button:hover {
  background-color: #ff6b6b;
  color: white;
  border-color: #ff6b6b;
}

.bd ul {
  display: flex;
  gap: 20px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.bd ul li {
  flex: 1;
}

.bd ul li video {
  width: 100%;
  height: 300px;
  object-fit: cover;
  border-radius: 8px;
  background-color: #000;
}
</style>