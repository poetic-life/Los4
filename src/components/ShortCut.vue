<template>
  <div class="shortcut">
    <div class="title">
      <p style="color:darkcyan; font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif; font-size: 24px;">Clippers</p>
      <i class="iconfont icon-clippers"></i>
    </div>
    <div class="wrapper">
      <ul>
        <li v-if="!hasToken">
          <router-link :to="getLink(0)" :class="{ active: activeIndex === 0 }">请先登录</router-link>
        </li>
        <li v-if="hasToken">
          <a href="#" @click="logout" :class="{ active: activeIndex === 0 }">退出登录</a>
        </li>
        <li v-for="(item, index) in otherWords" :key="index">
          <router-link :to="getLink(index + 1)" :class="{ active: activeIndex === index + 1 }">{{ item }}</router-link>
        </li>
        <li>
          <router-link to="/mobile">
            <span class="iconfont icon-mobile"></span>手机版
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>


<script>
import { mapGetters} from 'vuex';

export default {
  data() {
    return {
      activeIndex: 0,
      otherWords: ['我的订单', '会员中心', '帮助中心', '在线客服']
    };
  },
  computed: {
    ...mapGetters(['token']),
    hasToken() {
      return !!this.token; // 判断 token 是否存在
    }
  },
  methods: {
    logout(){
      this.$store.dispatch('user/logout')
    },
    getLink(index) {
      switch (index) {
        case 0:
          return '/login';
        case 1:
          return '/orders';
        case 2:
          return '/member-center';
        case 3:
          return '/help-center';
        case 4:
          return '/customer-service';
        default:
          return '/';
      }
    }
  }
};
</script>

<style scoped>
.shortcut {
  display: flex;
  justify-content: space-between; /* 使 .title 和 .wrapper 对齐到容器的两边 */
  align-items: center; /* 垂直居中对齐 */
  background-color: #f4f4f4;
  padding: 10px;
  width: 100%; /* 确保容器宽度为100% */
  box-sizing: border-box; /* 包括内边距和边框在内的总宽度 */
}

.title {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  margin-left: 80px;
}

.title .iconfont {
  font-size: 24px;
  color: #007bff;
}

.wrapper {
  display: flex;
}

.wrapper ul {
  list-style: none;
  display: flex;
  padding: 0;
  margin: 0;
}

.wrapper ul li {
  margin-right: 15px;
}

.wrapper ul li a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
}

.wrapper ul li a.active {
  color: #007bff;
}

.iconfont {
  margin-left: 5px;
}
</style>


