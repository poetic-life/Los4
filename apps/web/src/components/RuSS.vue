<template>
  <div class="RuSS">
    <div class="wrapper">
      <div class="title">
        <h3>拉塞尔·威斯布鲁克</h3>
        <div class="more">
          <a href="#" @click="showMore">更多 <span class="iconfont icon-arrow-right"></span></a>
        </div>
      </div>
      <div class="RuSS-content">
        <div class="RuSS-image">
          <img :src="playerImage" :alt="playerName" />
        </div>
        <div class="RuSS-info">
          <h4>{{ playerName }}</h4>
          <p>{{ playerDescription }}</p>
          <div class="RuSS-stats">
            <div class="stat-item" v-for="stat in playerStats" :key="stat.name">
              <span class="stat-name">{{ stat.name }}:</span>
              <span class="stat-value">{{ stat.value }}</span>
            </div>
          </div>
          <a href="#" @click="viewMore" class="view-more">查看更多</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import rw from '../assets/uploads/rw.jpg'

const playerName = '拉塞尔·威斯布鲁克'
const playerImage = rw
const playerDescription = '拉塞尔·威斯布鲁克（Russell Westbrook），1988年11月12日出生于美国加利福尼亚州长滩，美国职业篮球运动员，司职控球后卫，效力于NBA洛杉矶快船队。威斯布鲁克以其劲爆的身体素质和全面的技术著称，是NBA历史上最具爆发力的球员之一。'

const playerStats = [
  { name: '年龄', value: '35岁' },
  { name: '身高', value: '1.91米' },
  { name: '体重', value: '91公斤' },
  { name: '球衣号码', value: '0号' },
  { name: '位置', value: '控球后卫' },
  { name: '职业生涯', value: '15年' }
]

const showMore = () => {
  console.log('显示更多威少信息')
}

const viewMore = () => {
  console.log('查看威少更多详情')
}
</script>

<style scoped>
.RuSS {
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

.title h3 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.more a {
  color: #666;
  text-decoration: none;
  display: flex;
  align-items: center;
}

.more a:hover {
  color: #ff6b6b;
}

.RuSS-content {
  display: flex;
  gap: 40px;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.RuSS-image {
  flex: 0 0 400px;
  height: 500px;
  overflow: hidden;
}

.RuSS-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.RuSS-content:hover .RuSS-image img {
  transform: scale(1.1);
}

.RuSS-info {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.RuSS-info h4 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.RuSS-info p {
  font-size: 16px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 30px;
}

.RuSS-stats {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 30px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.stat-name {
  font-weight: bold;
  color: #333;
}

.stat-value {
  color: #666;
}

.view-more {
  align-self: flex-start;
  padding: 10px 20px;
  background-color: #ff6b6b;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.view-more:hover {
  background-color: #ff4757;
}
</style>