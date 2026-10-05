<template>
  <div class="AnKle">
    <div class="wrapper">
      <div class="title">
        <h3>球星脚踝</h3>
        <div class="more">
          <a href="#" @click="showMore">更多 <span class="iconfont icon-arrow-right"></span></a>
        </div>
      </div>
      <div class="AnKle-content">
        <div class="AnKle-item" v-for="(player, index) in players" :key="index">
          <div class="AnKle-item-image">
            <img :src="player.imgSrc" :alt="player.name" />
          </div>
          <div class="AnKle-item-info">
            <h4>{{ player.name }}</h4>
            <p>{{ player.description }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import hddunk from '../assets/uploads/hddunk.jpg'
import wsdunk from '../assets/uploads/wsdunk.jpg'
import qzdunk from '../assets/uploads/qzdunk.jpg'
import lnddunk from '../assets/uploads/lnddunk.jpg'

const players = [
  {
    id: 1,
    name: '詹姆斯·哈登',
    description: '哈登的脚踝技术非常灵活，经常做出变向突破。',
    imgSrc: hddunk
  },
  {
    id: 2,
    name: '拉塞尔·威斯布鲁克',
    description: '威少的脚踝力量强大，爆发力十足。',
    imgSrc: wsdunk
  },
  {
    id: 3,
    name: '保罗·乔治',
    description: '乔治的脚踝稳定性好，防守时移动迅速。',
    imgSrc: qzdunk
  },
  {
    id: 4,
    name: '科怀·伦纳德',
    description: '伦纳德的脚踝力量均衡，步法扎实。',
    imgSrc: lnddunk
  }
]

const showMore = () => {
  console.log('显示更多球星脚踝信息')
}
</script>

<style scoped>
.AnKle {
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

.AnKle-content {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.AnKle-item {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.AnKle-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.AnKle-item-image {
  height: 250px;
  overflow: hidden;
}

.AnKle-item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.AnKle-item:hover .AnKle-item-image img {
  transform: scale(1.1);
}

.AnKle-item-info {
  padding: 20px;
  text-align: center;
}

.AnKle-item-info h4 {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.AnKle-item-info p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}
</style>