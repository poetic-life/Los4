<template>
  <div class="PuGe">
    <div class="wrapper">
      <div class="title">
        <h3>保罗·乔治</h3>
        <div class="more">
          <a href="#" @click="showMore">更多 <span class="iconfont icon-arrow-right"></span></a>
        </div>
      </div>
      <div class="PuGe-content">
        <div class="PuGe-image">
          <img :src="playerImage" :alt="playerName" />
        </div>
        <div class="PuGe-info">
          <h4>{{ playerName }}</h4>
          <p>{{ playerDescription }}</p>
          <div class="PuGe-stats">
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
import pg from '../assets/uploads/pg.jpg'

const playerName = '保罗·乔治'
const playerImage = pg
const playerDescription = '保罗·乔治（Paul George），1990年5月2日出生于美国加利福尼亚州帕姆代尔，美国职业篮球运动员，司职小前锋，效力于NBA洛杉矶快船队。乔治以其出色的防守能力和精准的三分球著称，是联盟中最全面的球员之一。'

const playerStats = [
  { name: '年龄', value: '34岁' },
  { name: '身高', value: '2.03米' },
  { name: '体重', value: '99.8公斤' },
  { name: '球衣号码', value: '13号' },
  { name: '位置', value: '小前锋' },
  { name: '职业生涯', value: '13年' }
]

const showMore = () => {
  console.log('显示更多乔治信息')
}

const viewMore = () => {
  console.log('查看乔治更多详情')
}
</script>

<style scoped>
.PuGe {
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

.PuGe-content {
  display: flex;
  gap: 40px;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.PuGe-image {
  flex: 0 0 400px;
  height: 500px;
  overflow: hidden;
}

.PuGe-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.PuGe-content:hover .PuGe-image img {
  transform: scale(1.1);
}

.PuGe-info {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.PuGe-info h4 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.PuGe-info p {
  font-size: 16px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 30px;
}

.PuGe-stats {
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