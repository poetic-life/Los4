<template>
  <div class="BrAnd">
    <div class="wrapper">
      <div class="title">
        <h3>品牌合作</h3>
      </div>
      <div class="BrAnd-content">
        <div class="BrAnd-item" v-for="(brand, index) in brands" :key="index">
          <div class="BrAnd-item-image">
            <img :src="brand.imgSrc" :alt="brand.name" />
          </div>
          <div class="BrAnd-item-info">
            <h4>{{ brand.name }}</h4>
            <p>{{ brand.description }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import brand1 from '../assets/uploads/brand1.jpg'
import brand2 from '../assets/uploads/brand2.jpg'
import brand3 from '../assets/uploads/brand3.jpg'

const brands = [
  {
    id: 1,
    name: 'Nike',
    description: '全球领先的运动品牌',
    imgSrc: brand1
  },
  {
    id: 2,
    name: 'Adidas',
    description: '世界知名运动品牌',
    imgSrc: brand2
  },
  {
    id: 3,
    name: 'Under Armour',
    description: '专业运动装备品牌',
    imgSrc: brand3
  }
]
</script>

<style scoped>
.BrAnd {
  padding: 50px 0;
  background-color: #f9f9f9;
}

.wrapper {
  width: 1200px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 40px;
}

.title h3 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.BrAnd-content {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.BrAnd-item {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.BrAnd-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.BrAnd-item-image {
  height: 200px;
  overflow: hidden;
}

.BrAnd-item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.BrAnd-item:hover .BrAnd-item-image img {
  transform: scale(1.1);
}

.BrAnd-item-info {
  padding: 20px;
  text-align: center;
}

.BrAnd-item-info h4 {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.BrAnd-item-info p {
  color: #666;
  font-size: 14px;
}
</style>