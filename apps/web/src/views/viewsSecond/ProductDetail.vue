<template>
  <div class="page">
    <div class="wrapper">
      <nav class="crumb">
        <router-link to="/shopping">商城</router-link>
        <span class="sep">/</span>
        <span class="current">{{ product.name || '商品详情' }}</span>
      </nav>

      <div v-if="product.id" class="detail card">
        <div class="gallery">
          <img :src="product.image" :alt="product.name" />
        </div>

        <div class="info">
          <div class="info-head">
            <span class="category-pill">{{ product.categoryName }}</span>
            <h1 class="name">{{ product.name }}</h1>
            <p class="stats">已售 <b>{{ product.sales }}</b> 件 · <b>{{ product.reviews }}</b> 条评价</p>
          </div>

          <div class="price-box">
            <span class="price-symbol">¥</span>
            <span class="price">{{ product.price }}</span>
            <span class="price-note">含税 · 支持 7 天无理由退换</span>
          </div>

          <p class="desc">{{ product.description }}</p>

          <div class="stock-row">
            <span class="stock-label">库存</span>
            <span class="stock-value" :class="{ low: product.stock < 20 }">
              {{ product.stock > 0 ? `现货 ${product.stock} 件` : '暂时缺货' }}
            </span>
          </div>

          <div class="buy-row">
            <div class="quantity">
              <button @click="decrease">−</button>
              <input v-model.number="quantity" type="number" min="1" />
              <button @click="increase">＋</button>
            </div>
            <button class="btn btn-primary btn-buy" :disabled="product.stock <= 0" @click="addToCart">
              <el-icon><ShoppingCart /></el-icon> 加入购物车
            </button>
            <button class="btn btn-outline btn-buy" :disabled="product.stock <= 0" @click="buyNow">
              立即购买
            </button>
          </div>

          <button class="fav-btn" :class="{ active: favorited }" @click="toggleFavorite">
            <el-icon><StarFilled v-if="favorited" /><Star v-else /></el-icon>
            {{ favorited ? '已收藏' : '收藏此商品' }}
          </button>
        </div>
      </div>

      <el-empty v-else description="商品不存在或已下架">
        <router-link to="/shopping" class="btn btn-primary">返回商城</router-link>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductDetail, addFavorite, removeFavorite, getFavorites } from '@/api'
import { useCartStore } from '@/store/modules/cart'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const product = ref({})
const quantity = ref(1)
const favorited = ref(false)

onMounted(async () => {
  try {
    product.value = (await getProductDetail(route.params.id)) || {}
    await checkFavorited()
  } catch (e) {
    product.value = {}
  }
})

const checkFavorited = async () => {
  try {
    const favs = (await getFavorites()) || []
    favorited.value = favs.some((p) => p.id === product.value.id)
  } catch (e) {
    favorited.value = false
  }
}

const toggleFavorite = async () => {
  const id = product.value.id
  if (!id) return
  try {
    if (favorited.value) {
      await removeFavorite(id)
      favorited.value = false
      ElMessage.success('已取消收藏')
    } else {
      await addFavorite(id)
      favorited.value = true
      ElMessage.success('已加入收藏夹')
    }
  } catch (e) {
    // 未登录等情况拦截器已提示
  }
}

const decrease = () => {
  if (quantity.value > 1) quantity.value--
}

const increase = () => {
  if (quantity.value < (product.value.stock || 99)) quantity.value++
}

const addToCart = () => {
  for (let i = 0; i < quantity.value; i++) {
    cartStore.addItem(product.value)
  }
  ElMessage.success(`${product.value.name} 已加入购物车`)
}

const buyNow = () => {
  addToCart()
  router.push('/orders')
}
</script>

<style scoped>
.page {
  padding: 32px 0 88px;
}

.crumb {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
  font-size: 14px;
  color: var(--text-muted);
}

.crumb a {
  color: var(--text-secondary);
  transition: color var(--transition);
}

.crumb a:hover {
  color: var(--accent);
}

.crumb .sep {
  color: var(--text-faint);
}

.crumb .current {
  color: var(--text-primary);
}

.detail {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 48px;
  padding: 32px;
}

.gallery {
  border-radius: var(--radius-lg);
  overflow: hidden;
  background: var(--bg-hover);
  aspect-ratio: 1;
}

.gallery img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.info-head {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.category-pill {
  align-self: flex-start;
  padding: 4px 14px;
  background: rgba(200, 16, 46, 0.14);
  border: 1px solid rgba(200, 16, 46, 0.3);
  border-radius: var(--radius-full);
  font-size: 12px;
  color: var(--accent);
}

.name {
  font-family: var(--font-display);
  font-size: 30px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  line-height: 1.2;
}

.stats {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0;
}

.stats b {
  color: var(--text-secondary);
}

.price-box {
  display: flex;
  align-items: baseline;
  gap: 6px;
  padding: 20px 0;
  border-top: 1px solid var(--border);
  border-bottom: 1px solid var(--border);
}

.price-symbol {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--accent);
}

.price {
  font-family: var(--font-display);
  font-size: 44px;
  font-weight: 700;
  color: var(--accent);
  line-height: 1;
}

.price-note {
  margin-left: 12px;
  font-size: 12px;
  color: var(--text-faint);
}

.desc {
  font-size: 15px;
  line-height: 1.8;
  color: var(--text-secondary);
  margin: 0;
}

.stock-row {
  display: flex;
  align-items: center;
  gap: 14px;
  font-size: 14px;
}

.stock-label {
  color: var(--text-faint);
}

.stock-value {
  color: #2ecc71;
  font-weight: 600;
}

.stock-value.low {
  color: #f39c12;
}

.buy-row {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-top: 8px;
}

.quantity {
  display: flex;
  align-items: center;
  border: 1px solid var(--border-strong);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.quantity button {
  width: 38px;
  height: 46px;
  background: var(--bg-hover);
  border: none;
  color: var(--text-primary);
  font-size: 18px;
  cursor: pointer;
}

.quantity input {
  width: 52px;
  height: 46px;
  background: transparent;
  border: none;
  text-align: center;
  color: var(--text-primary);
  font-size: 15px;
}

.quantity input::-webkit-inner-spin-button {
  display: none;
}

.btn-buy {
  flex: 1;
  height: 48px;
}

.btn-buy:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.fav-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 18px;
  background: transparent;
  border: 1px solid var(--border-strong);
  border-radius: var(--radius-full);
  color: var(--text-muted);
  font-size: 13px;
  cursor: pointer;
  transition: all var(--transition);
}

.fav-btn:hover {
  border-color: var(--accent);
  color: var(--accent);
}

.fav-btn.active {
  background: rgba(245, 179, 1, 0.12);
  border-color: #f5b301;
  color: #f5b301;
}

@media (max-width: 860px) {
  .detail {
    grid-template-columns: 1fr;
    gap: 28px;
  }
}
</style>