<template>
  <!-- DUNK！ -->
  <div class="goods wrapper" id="page dunk">
    <!-- title -->
    <div class="title">
      <div class="left">
        <h3>DUNK！</h3>
        <p>暴力美学 精彩盛宴</p>
      </div>
      <div class="right">
        <a href="#" class="more">查看全部<span class="iconfont icon-arrow-right-bold"></span></a>
      </div>
    </div>
    <!-- 内容 -->
    <div class="bd">
      <ul>
        <li v-for="(item, index) in dunkItems" :key="index">
          <a href="#">
            <div class="pic"><img :src="item.imgSrc" :alt="item.altText"></div>
            <div class="text">
              <h4>{{ item.title }}</h4>
              <p>{{ item.description }}</p>
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
      dunkItems: [
  {
    imgSrc: require('../assets/uploads/hddunk.jpg'),
    altText: 'James Harden',
    title: 'James Harden 不惧岁月不飓风',
    description: '早期飞天流'
  },
  {
    imgSrc: require('../assets/uploads/wsdunk.jpg'),
    altText: '拉塞尔-威斯布鲁克',
    title: '拉塞尔-威斯布鲁克 神龟天降',
    description: '暴力补扣'
  },
  {
    imgSrc: require('../assets/uploads/qzdunk.jpg'),
    altText: '乔治大将军',
    title: '乔治大将军 今天是神',
    description: '又见将军拉满弓'
  },
  {
    imgSrc: require('../assets/uploads/lnddunk.jpg'),
    altText: '卡哇伊-伦纳德',
    title: '卡哇伊-伦纳德 炸裂隔扣',
    description: '冷血杀手'
  }
]

    };
  }
};
</script>

<style>

</style>
