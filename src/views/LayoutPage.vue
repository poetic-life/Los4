<template>
  <div>
    <ShortCut></ShortCut>
    <!-- 头部 -->
    <div class="header wrapper">
      <!-- logo -->
      <div class="logo">
        <h1><a href="#"></a></h1>
      </div>
      <!-- 导航 -->
      <div class="nav">
        <ul>
          <li v-for="(item, index) in role" :key="index">
            <router-link 
              :to="getRoute(item)" 
              :class="{ active: index === activeIndex }"
              @click.native="setActive(index)"
            >
              {{ item }}
            </router-link>
          </li>
        </ul>
      </div>
      <div class="search">
        <a href="#">
          <span class="iconfont icon-search"></span>
        </a>
        <input type="text" placeholder="搜一搜">
      </div>
      <!-- 购物车图标 -->
      <div class="cart">
        <a href="#">
          <span class="iconfont icon-fabulous"></span>
        </a>
        <i>{{ count }}</i>
      </div>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
import ShortCut from '@/components/ShortCut.vue';

export default {
  components: {
    ShortCut
  },
  data() {
    return {
      role: ['首页', '购物', '新闻', '队员', '社区', '门票', '时间表'],
      count: 3,
      activeIndex: 0 // 默认为第一个选项高亮
    };
  },
  methods: {
    getRoute(item) {
      switch (item) {
        case '首页':
          return '/home';
        case '购物':
          return '/shopping';
        case '新闻':
          return '/news';
        case '队员':
          return '/team';
        case '社区':
          return '/community';
        case '门票':
          return '/tickets';
        case '时间表':
          return '/schedule';
        default:
          return '/';
      }
    },
    setActive(index) {
      this.activeIndex = index;
    }
  }
}
</script>


<style scoped>
/* 添加你的样式 */
.nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
}

.nav ul li {
  margin-right: 15px;
}

.nav ul li a {
  text-decoration: none;
  color: #333;
  padding: 10px;
}

.nav ul li a.active {
  color: #007bff; /* 高亮颜色 */
  font-weight: bold;
}
</style>
