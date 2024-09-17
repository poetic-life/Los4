<template>
  <!-- 洛城四巨头 -->
  <div class="goods wrapper" id="page-los">
    <!-- title -->
    <div class="title">
      <div class="left">
        <h3>洛杉矶四巨头</h3>
        <p>绚丽夺目 精彩绝伦</p>
      </div>
      <div class="right">
        <a href="#" class="more">查看全部<span class="iconfont icon-arrow-right-bold"></span></a>
      </div>
    </div>
    <!-- 内容 -->
    <div class="bd">
      <ul>
        <li v-for="(star, index) in stars" :key="index">
          <a href="#">
            <div class="pic">
              <img :src="star.image" :alt="star.name">
            </div>
            <div class="text">
              <h4>{{ star.title }}</h4>
              <p>{{ star.description }}</p>
            </div>
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      stars: [
        {
          name: 'James Harden',
          title: 'James Harden 再现当年火箭登',
          description: '历史双能卫',
          image: require('../assets/uploads/star hd.jpg')
        },
        {
          name: 'Russell Westbrook',
          title: '拉塞尔-威斯布鲁克 神龟天降',
          description: '暴力美学',
          image: require('../assets/uploads/star ws.jpg')
        },
        {
          name: 'Paul George',
          title: '乔治大将军 万里无一',
          description: '神鬼二象性',
          image: require('../assets/uploads/star qz.jpg')
        },
        {
          name: 'Kawhi Leonard',
          title: '卡哇伊-伦纳德 表情管理',
          description: '机器人属性',
          image: require('../assets/uploads/star lnd.jpg')
        }
      ]
    };
  }
};
</script>

<style scoped>

</style>
