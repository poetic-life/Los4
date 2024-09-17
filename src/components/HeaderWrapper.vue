<template>
  <!-- 头部 -->
    <div class="header wrapper">
        <!-- logo -->
        <div class="logo">
            <h1><a href="#"></a></h1>
        </div>
        <!-- 导航 -->
        <div class="nav">
            <ul>
                <li v-for="(item,index) in role" :key="index"><a href="#" :class="{active:index===0}">{{ item }}</a></li>
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
        <span class="iconfont icon-cart-full"></span>
      </a>
      <i>{{ count }}</i>
    </div>
    </div>
</template>

<script>
export default {
    data(){
        return {
            role:['首页','购物','新闻','队员','社区','门票','时间表'],
            count:3
        }
    }
}
</script>

<style scoped>

</style>