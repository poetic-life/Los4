<template>
  <!-- 哈登个人介绍 -->
  <div class="cook" id="page-harden">
    <div class="wrapper">
      <!-- 标题 -->
      <div class="title">
        <div class="left">
          <h3>詹姆斯哈登</h3>
        </div>
        <div class="right">
          <ul>
            <li v-for="(period, index) in periods" :key="index">
              <a href="#" :class="{ active: index === 0 }">{{ period }}</a>
            </li>
          </ul>
          <a href="#" class="more">查看全部<span class="iconfont icon-arrow-right-bold"></span></a>
        </div>
      </div>
      <!-- 内容 -->
      <div class="content">
        <div class="left">
          <a href="#"><img src="../assets/uploads/哈登个人介绍.jpg" alt="詹姆斯哈登"></a>
        </div>
        <div class="right">
          <ul>
            <li v-for="(intro, index) in intros" :key="index">
              <a href="#">
                <div class="pic"><img :src="intro.imgSrc" :alt="'哈登介绍' + (index + 1)"></div>
                <div class="cover">
                  <p>{{ intro.coverText[0] }}</p>
                  <p>{{ intro.coverText[1] }}</p>
                  <p>{{ intro.coverText[2] }}</p>
                  <p>{{ intro.coverText[3] }}<span class="iconfont icon-arrow-right-bold"></span></p>
                </div>
                <div class="text">
                  <div class="info">
                    <h4>{{ intro.title }}</h4>
                    <p v-for="(detail, i) in intro.details" :key="i">{{ detail }}</p>
                  </div>
                  <p class="price">{{ intro.price }}</p>
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
      periods: [
        '快船时期',
        '76人时期',
        '火箭时期',
        '雷霆时期'
      ],
      intros: [
        {
          imgSrc: require('../assets/uploads/哈登介绍1.jpg'),
          coverText: ['新秀赛季场均得分9.9分', '', '季后赛场均出场时间22.9分钟', '更多数据'],
          title: '新秀赛季，所获荣誉',
          details: ['2012-2013赛季最佳第六人', '入选最佳新秀阵容二阵'],
          price: '赛季薪资300万￥',
        },
        {
          imgSrc: require('../assets/uploads/哈登介绍2.jpg'),
          coverText: ['二年级场均得分12.2分', '', '季后赛场均出场时间26.7分钟', '更多数据'],
          title: '2010-2011赛季',
          details: ['带领球队打进季后赛', '当季战绩西部第三'],
          price: '赛季薪资500万￥',
        },
        {
          imgSrc: require('../assets/uploads/哈登介绍3.jpg'),
          coverText: ['季后赛场均27分', '', '季后赛场均出场时间43分钟', '更多数据'],
          title: '红色战袍',
          details: ['当选西部最佳球员、全明星', '最佳阵容一阵'],
          price: '赛季薪资2210万￥',
        },
        {
          imgSrc: require('../assets/uploads/哈登介绍4.jpg'),
          coverText: ['季后赛场均得分27.2分', '', '季后赛场均出场时间38分钟', '更多数据'],
          title: '休斯顿时间',
          details: ['三次周最佳两次月最佳球员', '全明星替补，最佳阵容一阵'],
          price: '赛季薪资2210万￥',
        },
        {
          imgSrc: require('../assets/uploads/哈登介绍5.jpg'),
          coverText: ['孤胆英雄', '', '季后赛场均出场时间40分钟', '更多数据'],
          title: '航天城的英雄',
          details: ['荣获MVP', '当选全明星首发，最佳一阵'],
          price: '赛季薪资3000万￥',
        },
        {
          imgSrc: require('../assets/uploads/哈登介绍6.jpg'),
          coverText: ['东部第一战绩', '', '季后赛场均26分', '更多数据'],
          title: '篮网三巨头',
          details: ['三连得分王', '当选全明星首发'],
          price: '赛季薪资4100万￥',
        },
        {
          imgSrc: require('../assets/uploads/哈登介绍7.jpg'),
          coverText: ['西部半决赛单核带队', '', '季后赛场均出场时间38分钟', '更多数据'],
          title: '远赴费城',
          details: ['当选助攻王', '场均21分10.2助攻'],
          price: '赛季薪资4100万￥',
        },
        {
          imgSrc: require('../assets/uploads/哈登介绍8.jpg'),
          coverText: ['季后赛场均得分25.2分', '', '季后赛场均出场时间39.3分钟', '更多数据'],
          title: '最终登船',
          details: ['场均16.7分', '常规赛西部第四'],
          price: '赛季薪资3670万￥',
        }
      ]
    };
  }
};
</script>

<style scoped>
.cook .title {
  margin-top: 60px;
  margin-bottom: 20px;
}
</style>
