<template>
  <div id="app">
    <!-- 顶部导航栏 -->
    <header class="header" style="background-color:rgba(0, 0, 0, 0.1)">
      <div class="logo" style="color: red;">TTShopping</div>
      <div class="search-cart">
        <input 
          type="text" 
          class="search-box" 
          placeholder="搜索商品..." 
          v-model="searchQuery"
          @input="searchProducts"
          @keyup.enter="executeSearch"
        />
        <div class="cart-icon">
          <i class="fa fa-shopping-cart"></i>
        </div>
      </div>
    </header>

    <!-- 分类导航栏 -->
    <nav class="category-nav">
      <ul>
        <li
          v-for="category in categories"
          :key="category.id"
          @click="filterProducts(category.id)"
          :class="{ active: selectedCategory === category.id }"
        >
          {{ category.name }}
        </li>
      </ul>
    </nav>

    <!-- 商品展示区 -->
    <main class="product-list">
      <div class="product" v-for="product in filteredProducts" :key="product.id">
        <img :src="product.image" :alt="product.name" />
        <div class="product-info">
          <h2>{{ product.name }}</h2>
          <p class="price">{{ product.price }} 元</p>
          <button @click="addToCart(product)">加入购物车</button>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 商品分类
      categories: [
        { id: null, name: "全部" }, // 添加“全部”分类
        { id: 1, name: "篮球" },
        { id: 2, name: "篮球鞋" },
        { id: 3, name: "篮球服" },
        { id: 4, name: "篮球配件" },
      ],
      // 商品列表
      products: [
        { id: 1, name: "专业篮球", price: 299, image: "https://via.placeholder.com/200x200", categoryId: 1 },
        { id: 2, name: "运动篮球鞋", price: 799, image: "https://via.placeholder.com/200x200", categoryId: 2 },
        { id: 3, name: "篮球训练服", price: 299, image: "https://via.placeholder.com/200x200", categoryId: 3 },
        { id: 4, name: "篮球护具", price: 199, image: "https://via.placeholder.com/200x200", categoryId: 4 },
        // 更多商品...
      ],
      selectedCategory: null, // 默认显示所有商品
      searchQuery: "", // 搜索框的值
    };
  },
  computed: {
    filteredProducts() {
      let filtered = this.products;
      if (this.selectedCategory !== null) {
        filtered = filtered.filter(product => product.categoryId === this.selectedCategory);
      }
      if (this.searchQuery) {
        filtered = filtered.filter(product => product.name.toLowerCase().includes(this.searchQuery.toLowerCase()));
      }
      return filtered;
    },
  },
  methods: {
    addToCart(product) {
      alert(`${product.name} 已加入购物车`);
    },
    filterProducts(categoryId) {
      this.selectedCategory = categoryId;
    },
    searchProducts() {
      // 实时搜索功能
    },
    executeSearch() {
      // 可以在此处添加搜索完成后的处理逻辑，如果有需要
      console.log("搜索内容:", this.searchQuery);
    },
  },
};
</script>

<style scoped>
/* 页面整体样式 */
#app {
  font-family: 'Helvetica Neue', Arial, sans-serif;
  margin: 0;
  padding: 0;
}

/* 顶部导航栏样式 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
  background-color: #282c34;
  color: white;
}

.logo {
  font-size: 24px;
  font-weight: bold;
}

.search-cart {
  display: flex;
  align-items: center;
}

.search-box {
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 15px;
}

.cart-icon {
  font-size: 24px;
  cursor: pointer;
}

/* 分类导航栏样式 */
.category-nav {
  background-color: #f5f5f5;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.category-nav ul {
  display: flex;
  justify-content: center;
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-nav li {
  margin: 0 15px;
  font-size: 16px;
  cursor: pointer;
  font-weight: 500;
  padding: 5px 10px;
  border-radius: 4px;
}

.category-nav li.active {
  background-color: #007bff;
  color: white;
}

/* 商品展示区样式 */
.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  padding: 20px;
}

.product {
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.product img {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 8px;
}

.product-info {
  margin-top: 10px;
}

.product-info h2 {
  font-size: 18px;
  margin: 10px 0;
}

.product-info .price {
  font-size: 16px;
  color: #333;
  margin: 10px 0;
}

button {
  padding: 10px 15px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
