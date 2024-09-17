<template>
  <div class="shopping-cart">
    <!-- 购物车标题 -->
    <div class="cart-header">
      <h2>购物车</h2>
    </div>

    <!-- 购物车商品列表 -->
    <div class="cart-main">
      <table class="cart-table">
        <thead>
          <tr>
            <th>
              <input type="checkbox" v-model="selectAll" @change="selectAllItems">
              <span class="select-all-text">全选</span>
            </th>
            <th>商品信息</th>
            <th>单价</th>
            <th>数量</th>
            <th>小计</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in cartItems" :key="index">
            <td>
              <input type="checkbox" v-model="item.selected">
            </td>
            <td class="product-info">
              <img :src="item.image" alt="商品图片" />
              <div class="info">
                <p>{{ item.name }}</p>
                <p class="spec">规格: {{ item.spec }}</p>
              </div>
            </td>
            <td>{{ item.price | currency }}</td>
            <td>
              <div class="quantity">
                <button @click="decreaseQuantity(item)">-</button>
                <input type="number" v-model="item.quantity" min="1" />
                <button @click="increaseQuantity(item)">+</button>
              </div>
            </td>
            <td>{{ item.quantity * item.price | currency }}</td>
            <td><button @click="removeItem(index)" class="remove-btn">删除</button></td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 购物车结算部分 -->
    <div class="cart-footer">
      <div class="summary">
        <span>已选商品 {{ selectedItems.length }} 件</span>
        <span>合计：<strong>{{ totalPrice | currency }}</strong></span>
        <button @click="checkout" :disabled="!selectedItems.length" class="checkout-btn">去结算</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name:'OrderPage',
  data() {
    return {
      selectAll: false,
      cartItems: [
        {
          name: '苹果 iPhone 14 Pro',
          image: 'https://example.com/iphone.jpg',
          price: 7999,
          quantity: 1,
          spec: '256GB 黑色',
          selected: false
        },
        {
          name: '小米 11',
          image: 'https://example.com/xiaomi.jpg',
          price: 4999,
          quantity: 1,
          spec: '128GB 蓝色',
          selected: false
        },
        {
          name: '华为 Mate 40 Pro',
          image: 'https://example.com/huawei.jpg',
          price: 6499,
          quantity: 1,
          spec: '256GB 白色',
          selected: false
        }
      ]
    };
  },
  computed: {
    selectedItems() {
      return this.cartItems.filter(item => item.selected);
    },
    totalPrice() {
      return this.selectedItems.reduce((total, item) => {
        return total + item.price * item.quantity;
      }, 0);
    }
  },
  watch: {
    selectAll(newValue) {
      this.cartItems.forEach(item => {
        item.selected = newValue;
      });
    }
  },
  methods: {
    increaseQuantity(item) {
      item.quantity++;
    },
    decreaseQuantity(item) {
      if (item.quantity > 1) {
        item.quantity--;
      }
    },
    removeItem(index) {
      this.cartItems.splice(index, 1);
    },
    selectAllItems() {
      this.selectAll = !this.selectAll;
    },
    checkout() {
      alert('结算成功！');
    }
  },
  filters: {
    currency(value) {
      return '¥' + value.toFixed(2);
    }
  }
};
</script>

<style scoped>
.shopping-cart {
  width: 80%;
  margin: 20px auto;
  background-color: #fff;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.cart-header {
  padding-bottom: 15px;
  border-bottom: 2px solid #f5f5f5;
}

.cart-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.cart-main {
  margin-top: 20px;
}

.cart-table {
  width: 100%;
  border-collapse: collapse;
}

.cart-table th,
.cart-table td {
  padding: 10px;
  border-bottom: 1px solid #ddd;
  text-align: center;
}

.cart-table th {
  background-color: #f9f9f9;
  color: #333;
}

.cart-table .product-info {
  display: flex;
  align-items: center;
}

.cart-table .product-info img {
  width: 80px;
  height: 80px;
  margin-right: 10px;
  border-radius: 4px;
}

.cart-table .spec {
  color: #777;
  font-size: 14px;
}

.quantity {
  display: flex;
  align-items: center;
  justify-content: center;
}

.quantity button {
  padding: 5px 10px;
  border: none;
  background-color: #e0e0e0;
  color: #333;
  cursor: pointer;
  border-radius: 4px;
  font-size: 16px;
}

.quantity input {
  width: 40px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin: 0 5px;
}

.remove-btn {
  color: #ff4d4f;
  border: none;
  background: none;
  cursor: pointer;
}

.remove-btn:hover {
  text-decoration: underline;
}

.cart-footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  padding-top: 10px;
  border-top: 2px solid #f5f5f5;
}

.summary {
  display: flex;
  align-items: center;
}

.summary span {
  margin-right: 20px;
  font-size: 16px;
  color: #333;
}

.summary strong {
  color: #ff4d4f;
}

.checkout-btn {
  padding: 10px 20px;
  background-color: #ff4d4f;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.checkout-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
