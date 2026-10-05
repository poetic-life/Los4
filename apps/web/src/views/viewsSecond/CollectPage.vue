<template>
  <div class="collect-page">
    <div class="page-card card">
      <div class="card-head">
        <h2>收藏夹</h2>
        <span class="count">共 {{ items.length }} 件</span>
      </div>

      <el-empty v-if="items.length === 0" description="还没有收藏任何商品" />
      <div v-else class="collect-grid">
        <div class="collect-item" v-for="item in items" :key="item.id">
          <div class="item-image">
            <img :src="item.image" :alt="item.name" />
            <button class="remove-fav" title="取消收藏" @click="removeItem(item.id)">
              <el-icon><Delete /></el-icon>
            </button>
          </div>
          <div class="item-info">
            <h3>{{ item.name }}</h3>
            <div class="price-row">
              <span class="price-symbol">¥</span>
              <span class="price">{{ item.price }}</span>
            </div>
            <button class="add-cart" @click="addToCart(item)">
              <el-icon><ShoppingCart /></el-icon>
              <span>加入购物车</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/store/modules/cart'
import { getFavorites, removeFavorite } from '@/api'

const cartStore = useCartStore()

const items = ref([])

const loadFavorites = async () => {
  try {
    items.value = (await getFavorites()) || []
  } catch (e) {
    // 未登录或加载失败
  }
}

onMounted(loadFavorites)

const addToCart = (item) => {
  cartStore.addItem(item)
  ElMessage.success(`${item.name} 已加入购物车`)
}

const removeItem = async (id) => {
  try {
    await removeFavorite(id)
    items.value = items.value.filter((i) => i.id !== id)
    ElMessage.success('已取消收藏')
  } catch (e) {
    // 错误已提示
  }
}
</script>

<style scoped>
.collect-page {
  padding: 4px 4px 24px;
}

.page-card {
  padding: 24px;
}

.card-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-head h2 {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.count {
  font-size: 13px;
  color: var(--text-muted);
}

.collect-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
}

.collect-item {
  overflow: hidden;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  transition: transform var(--transition), border-color var(--transition), box-shadow var(--transition);
}

.collect-item:hover {
  transform: translateY(-4px);
  border-color: var(--border-strong);
  box-shadow: var(--shadow-md);
}

.item-image {
  position: relative;
  height: 160px;
  overflow: hidden;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.collect-item:hover .item-image img {
  transform: scale(1.06);
}

.remove-fav {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(11, 13, 18, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  color: #fff;
  cursor: pointer;
  opacity: 0;
  transition: all var(--transition);
}

.collect-item:hover .remove-fav {
  opacity: 1;
}

.remove-fav:hover {
  background: var(--accent);
  border-color: var(--accent);
}

.item-info {
  padding: 14px 14px 16px;
  display: flex;
  flex-direction: column;
}

.item-info h3 {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.price-row {
  display: flex;
  align-items: baseline;
  gap: 2px;
  margin-bottom: 12px;
}

.price-symbol {
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 700;
  color: var(--accent);
}

.price {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--accent);
  line-height: 1;
}

.add-cart {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  width: 100%;
  padding: 9px 0;
  background: var(--accent);
  border: none;
  border-radius: var(--radius-sm);
  color: #fff;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition);
}

.add-cart:hover {
  background: var(--brand-red-hover);
  box-shadow: var(--shadow-brand);
}

@media (max-width: 1100px) {
  .collect-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .collect-grid {
    grid-template-columns: 1fr;
  }
}
</style>