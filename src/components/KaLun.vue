<template>
  <!-- 伦纳德个人介绍 -->
  <div class="cook" id="page lunade">
    <div class="wrapper">
      <!-- 标题 -->
      <div class="title">
        <div class="left">
          <h3>伦纳德</h3>
        </div>
        <div class="right">
          <ul>
            <li><a href="#" class="active">快船时期</a></li>
            <li><a href="#">猛龙时期</a></li>
            <li><a href="#">马刺时期</a></li>
            <li><a href="#">总冠军赛季</a></li>
          </ul>
          <a href="#" class="more">查看全部<span class="iconfont icon-arrow-right-bold"></span></a>
        </div>
      </div>
      <!-- 内容 -->
      <div class="content">
        <div class="left">
          <a href="#"><img src="../assets/uploads/伦纳德个人介绍.jpg" alt="伦纳德"></a>
        </div>
        <div class="right">
          <ul>
            <li v-for="(item, index) in contentItems" :key="index">
              <a href="#">
                <div class="pic"><img :src="item.imgSrc" :alt="'伦纳德介绍' + (index + 1)"></div>
                <div class="cover">
                  <p>{{ item.coverText[0] }}</p>
                  <p>{{ item.coverText[1] }}</p>
                  <p>{{ item.coverText[2] }}</p>
                  <p>{{ item.coverText[3] }}<span class="iconfont icon-arrow-right-bold"></span></p>
                </div>
                <div class="text">
                  <div class="info">
                    <h4>{{ item.title }}</h4>
                    <p v-for="(detail, i) in item.details" :key="i">{{ detail }}</p>
                  </div>
                  <p class="price">{{ item.price }}</p>
                </div>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      contentItems: [
        {
          imgSrc: require('../assets/uploads/伦纳德介绍1.jpg'),
          coverText: ['新秀赛季场均得分8.9分', '', '季后赛场均出场时间23.9分钟', '更多数据'],
          title: '新秀赛季，所获荣誉',
          details: ['最佳新秀', '入选最佳新秀阵容一阵'],
          price: '赛季薪资230万￥',
        },
        {
          imgSrc: require('../assets/uploads/伦纳德介绍2.jpg'),
          coverText: ['场均得分19分', '', '季后赛场均出场时间24.9分钟', '更多数据'],
          title: 'GDP时刻',
          details: ['最佳防守阵容', '总决赛MVP'],
          price: '赛季薪资1400万￥',
        },
        {
          imgSrc: require('../assets/uploads/伦纳德介绍3.jpg'),
          coverText: ['马刺领军者', '', '季后赛场均出场时间30分钟', '更多数据'],
          title: '当赛季抢断王',
          details: ['最佳一阵', '入选最佳防守二阵'],
          price: '赛季薪资2000万￥',
        },
        {
          imgSrc: require('../assets/uploads/伦纳德介绍4.jpg'),
          coverText: ['赛季场均得分29.9分', '', '季后赛场均出场时间30.2分钟', '更多数据'],
          title: '无上荣誉',
          details: ['西部全明星首发', '总决赛MVP'],
          price: '赛季薪资3000万￥',
        },
        {
          imgSrc: require('../assets/uploads/伦纳德介绍5.jpg'),
          coverText: ['赛季场均得分28.9分', '', '季后赛场均出场时间33分钟', '更多数据'],
          title: '北境之王',
          details: ['全明星首发', '入选最佳防守一阵'],
          price: '赛季薪资4000万￥',
        },
        {
          imgSrc: require('../assets/uploads/伦纳德介绍6.jpg'),
          coverText: ['赛季场均得分29.9分', '', '季后赛场均出场时间36.9分钟', '更多数据'],
          title: '最佳领军者',
          details: ['总决赛MVP', '入选最佳防守二阵'],
          price: '赛季薪资4310万￥',
        },
        {
          imgSrc: require('../assets/uploads/伦纳德介绍7.jpg'),
          coverText: ['赛季场均得分27.9分', '', '季后赛场均出场时间36.9分钟', '更多数据'],
          title: '登至太平洋',
          details: ['西部全明星首发', '入选最佳阵容一阵'],
          price: '赛季薪资4500万￥',
        },
        {
          imgSrc: require('../assets/uploads/伦纳德介绍8.jpg'),
          coverText: ['赛季场均得分23.2分', '', '季后赛场均出场时间33.2分钟', '更多数据'],
          title: '携手洛城四巨',
          details: ['常规赛西部第四', '入选全明星替补'],
          price: '赛季薪资3900万￥',
        },
      ],
    };
  },
};
</script>

<style scoped>
/* 添加你的样式 */
</style>
