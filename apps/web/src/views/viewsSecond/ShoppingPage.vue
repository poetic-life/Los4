<template>
  <div class="page">
    <!-- 促销轮播 banner -->
    <div class="wrapper banner-wrap">
      <el-carousel height="280px" :interval="4000" arrow="hover" class="promo-carousel">
        <el-carousel-item v-for="slide in promos" :key="slide.title">
          <div class="promo-slide" :style="{ background: slide.bg }">
            <div class="promo-text">
              <span class="promo-tag">{{ slide.tag }}</span>
              <h2 class="promo-title">{{ slide.title }}</h2>
              <p class="promo-sub">{{ slide.sub }}</p>
              <button class="btn btn-primary promo-btn" @click="goToCategory(slide.categoryId)">立即抢购</button>
            </div>
            <img :src="slide.image" class="promo-img" :alt="slide.title" />
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div class="wrapper">
      <!-- 分类 tab -->
      <div class="category-nav">
        <button
          v-for="category in categories"
          :key="category.id ?? 'all'"
          :class="{ active: selectedCategory === category.id }"
          @click="filterProducts(category.id)"
        >
          {{ category.name }}
        </button>
      </div>

      <!-- 搜索 + 排序 -->
      <div class="toolbar">
        <div class="search">
          <el-icon><Search /></el-icon>
          <input v-model="searchQuery" placeholder="搜索商品..." @keyup.enter="executeSearch" />
        </div>
        <div class="sort-bar">
          <button
            v-for="opt in sortOptions"
            :key="opt.value"
            :class="{ active: sortBy === opt.value }"
            @click="sortBy = opt.value"
          >
            {{ opt.label }}
          </button>
        </div>
      </div>

      <!-- 商品网格 -->
      <el-empty v-if="sortedProducts.length === 0" description="暂无商品" />
      <div v-else class="product-grid">
        <div class="product card" v-for="product in sortedProducts" :key="product.id">
          <div class="product-image" @click="goToDetail(product.id)">
            <img :src="product.image" :alt="product.name" />
          </div>
          <div class="product-info">
            <h3 class="product-name clickable" @click="goToDetail(product.id)">{{ product.name }}</h3>
            <p class="product-stats">已售 {{ product.sales }} · {{ product.reviews }} 条评价</p>
            <div class="price-row">
              <span class="price-symbol">¥</span>
              <span class="price">{{ product.price }}</span>
            </div>
            <button class="add-cart" @click.stop="addToCart(product)">
              <el-icon><ShoppingCart /></el-icon>
              <span>加入购物车</span>
            </button>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="total > pageSize" class="pagination-wrap">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :total="total"
          :page-size="pageSize"
          :current-page="page"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 右下悬浮购物车入口 -->
    <button class="cart-fab" @click="goToCart" title="购物车">
      <el-badge :value="cartStore.totalItems" :hidden="cartStore.totalItems === 0" :max="99">
        <el-icon><ShoppingCart /></el-icon>
      </el-badge>
    </button>

    <el-dialog v-model="cartDialogVisible" title="我的购物车" width="640px" class="cart-dialog">
      <el-table :data="cartStore.items" style="width: 100%">
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope"> ¥{{ scope.row.price }} </template>
        </el-table-column>
        <el-table-column label="数量" width="150">
          <template #default="scope">
            <el-input-number v-model="scope.row.quantity" :min="1" @change="updateQuantity(scope.row.id, scope.row.quantity)" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="100">
          <template #default="scope"> ¥{{ scope.row.price * scope.row.quantity }} </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template #default="scope">
            <el-button type="danger" @click="removeFromCart(scope.row.id)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <div class="cart-footer">
          <span class="total">总计: <b>¥{{ cartStore.totalPrice }}</b></span>
          <button class="btn btn-primary" @click="goToCheckout">去结算</button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/store/modules/cart'
import { getProducts } from '@/api'

const router = useRouter()
const cartStore = useCartStore()

const categories = ref([
  { id: null, name: '全部' },
  { id: 1, name: '篮球' },
  { id: 2, name: '篮球鞋' },
  { id: 3, name: '篮球服' },
  { id: 4, name: '篮球配件' }
])

const products = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = 12

const fetchProducts = async () => {
  try {
    const params = { page: page.value, size: pageSize }
    if (selectedCategory.value !== null) params.categoryId = selectedCategory.value
    if (searchQuery.value.trim()) params.keyword = searchQuery.value.trim()
    const data = (await getProducts(params)) || {}
    products.value = data.list || []
    total.value = data.total || 0
  } catch (e) {
    products.value = []
    total.value = 0
  }
}

onMounted(fetchProducts)

const promos = [
  { title: '快船装备焕新 5 折起', tag: '限时促销', sub: '全场满 299 减 50，新赛季装备一站购齐', bg: 'linear-gradient(120deg, #c8102e 0%, #6b0f1f 100%)', image: '/uploads/basketball.jpg', categoryId: 1 },
  { title: '球星同款实战战靴', tag: '新品首发', sub: '季后赛同款篮球鞋，现货直发最快次日达', bg: 'linear-gradient(120deg, #1d428a 0%, #0f1f3d 100%)', image: '/uploads/shoe.jpg', categoryId: 2 },
  { title: '经典球衣专区', tag: '热卖推荐', sub: '球迷同款球衣，经典配色百搭出街', bg: 'linear-gradient(120deg, #232833 0%, #11141b 100%)', image: '/uploads/clothes.jpg', categoryId: 3 }
]

const sortOptions = [
  { value: 'default', label: '综合' },
  { value: 'sales', label: '销量' },
  { value: 'price-asc', label: '价格 ↑' },
  { value: 'price-desc', label: '价格 ↓' }
]

const selectedCategory = ref(null)
const searchQuery = ref('')
const sortBy = ref('default')
const cartDialogVisible = ref(false)

const sortedProducts = computed(() => {
  const list = [...products.value]
  if (sortBy.value === 'sales') {
    list.sort((a, b) => b.sales - a.sales)
  } else if (sortBy.value === 'price-asc') {
    list.sort((a, b) => a.price - b.price)
  } else if (sortBy.value === 'price-desc') {
    list.sort((a, b) => b.price - a.price)
  }
  return list
})

const addToCart = (product) => {
  cartStore.addItem(product)
  ElMessage.success(`${product.name} 已加入购物车`)
}

const removeFromCart = (id) => {
  cartStore.removeItem(id)
}

const updateQuantity = (id, quantity) => {
  cartStore.updateQuantity(id, quantity)
}

const filterProducts = (categoryId) => {
  selectedCategory.value = categoryId
  page.value = 1
  fetchProducts()
}

const goToCategory = (categoryId) => {
  filterProducts(categoryId)
}

const executeSearch = () => {
  page.value = 1
  fetchProducts()
}

const handlePageChange = (p) => {
  page.value = p
  fetchProducts()
}

const goToDetail = (id) => {
  router.push(`/product/${id}`)
}

const goToCart = () => {
  cartDialogVisible.value = true
}

const goToCheckout = () => {
  if (cartStore.items.length === 0) {
    ElMessage.warning('购物车是空的')
    return
  }
  router.push('/orders')
}
</script>

<style scoped>
.page {
  padding: 32px 0 88px;
}

/* ---------- 促销轮播 ---------- */
.banner-wrap {
  margin-bottom: 32px;
}

.promo-carousel {
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-md);
}

.promo-slide {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 36px 56px;
  overflow: hidden;
}

.promo-text {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
}

.promo-tag {
  display: inline-block;
  padding: 4px 14px;
  background: rgba(255, 255, 255, 0.16);
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: var(--radius-full);
  font-size: 12px;
  letter-spacing: 2px;
  color: #fff;
}

.promo-title {
  font-family: var(--font-display);
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  margin: 0;
}

.promo-sub {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.promo-btn {
  margin-top: 8px;
}

.promo-img {
  position: relative;
  z-index: 2;
  width: 380px;
  height: 220px;
  object-fit: cover;
  border-radius: var(--radius-lg);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.4);
  transform: rotate(-3deg);
}

/* ---------- 分类 tab ---------- */
.category-nav {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 18px;
}

.category-nav button {
  padding: 9px 24px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  color: var(--text-secondary);
  font-size: 14px;
  cursor: pointer;
  transition: all var(--transition);
}

.category-nav button:hover {
  border-color: var(--border-strong);
  color: var(--text-primary);
}

.category-nav button.active {
  background: var(--accent);
  border-color: var(--accent);
  color: #fff;
}

/* ---------- 搜索 + 排序 ---------- */
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
}

.search {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  max-width: 420px;
  padding: 10px 16px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  color: var(--text-muted);
}

.search input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  color: var(--text-primary);
  font-size: 14px;
}

.sort-bar {
  display: flex;
  gap: 4px;
  padding: 4px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
}

.sort-bar button {
  padding: 6px 16px;
  background: transparent;
  border: none;
  border-radius: var(--radius-full);
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all var(--transition);
}

.sort-bar button:hover {
  color: var(--text-primary);
}

.sort-bar button.active {
  background: var(--accent);
  color: #fff;
}

/* ---------- 商品网格 ---------- */
.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.product {
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.product:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
  border-color: var(--border-strong);
}

.product-image {
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.product:hover .product-image img {
  transform: scale(1.06);
}

.product-info {
  display: flex;
  flex-direction: column;
  padding: 16px;
  flex: 1;
}

.product-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-stats {
  font-size: 12px;
  color: var(--text-faint);
  margin: 0 0 10px;
}

.price-row {
  display: flex;
  align-items: baseline;
  gap: 2px;
  margin-bottom: 12px;
  margin-top: auto;
}

.price-symbol {
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 700;
  color: var(--accent);
}

.price {
  font-family: var(--font-display);
  font-size: 26px;
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
  padding: 10px 0;
  background: var(--accent);
  border: none;
  border-radius: var(--radius-md);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition);
}

.add-cart:hover {
  background: var(--brand-red-hover);
  box-shadow: var(--shadow-brand);
}

.product-image {
  cursor: pointer;
}

.clickable {
  cursor: pointer;
  transition: color var(--transition);
}

.clickable:hover {
  color: var(--accent);
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

/* ---------- 悬浮购物车入口 ---------- */
.cart-fab {
  position: fixed;
  right: 32px;
  bottom: 40px;
  z-index: 50;
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-brand);
  border: none;
  border-radius: 50%;
  color: #fff;
  font-size: 24px;
  cursor: pointer;
  box-shadow: var(--shadow-brand);
  transition: all var(--transition);
}

.cart-fab:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 28px rgba(200, 16, 46, 0.4);
}

/* ---------- 购物车弹窗底部 ---------- */
.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total {
  font-size: 15px;
  color: var(--text-secondary);
}

.total b {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--accent);
}

@media (max-width: 992px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .search {
    max-width: none;
  }

  .promo-img {
    display: none;
  }
}

@media (max-width: 560px) {
  .product-grid {
    grid-template-columns: 1fr;
  }
}
</style>