<template>
  <!-- 哈登个人介绍 -->
  <div class="fresh" id="page-harden">
    <div class="wrapper">
      <!-- 标题 -->
      <div class="title">
        <div class="left">
          <h3>詹姆斯哈登</h3>
          <!-- <p>新鲜出炉 品质靠谱</p> -->
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
                <div class="pic"><img :src="intro.image" :alt="intro.title"></div>
                <div class="cover">
                  <p>{{ intro.stat1 }}</p>
                  <p>{{ intro.stat2 }}</p>
                  <p>{{ intro.stat3 }}</p>
                  <p>更多数据<span class="iconfont icon-arrow-right-bold"></span></p>
                </div>
                <div class="text">
                  <div class="info">
                    <h4>{{ intro.title }}</h4>
                    <p>{{ intro.honor1 }}</p>
                    <p>{{ intro.honor2 }}</p>
                  </div>
                  <p class="price">赛季薪资{{ intro.salary }}</p>
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
          title: '新秀赛季，所获荣誉',
          honor1: '2012-2013赛季最佳第六人',
          honor2: '入选最佳新秀阵容二阵',
          stat1: '新秀赛季场均得分9.9分',
          stat2: '季后赛场均出场时间22.9分钟',
          stat3: '',
          salary: '300万￥',
          image: require('../assets/uploads/哈登介绍1jpg.jpg')
        },
        {
          title: '2010-2011赛季',
          honor1: '带领球队打进季后赛',
          honor2: '当季战绩西部第三',
          stat1: '二年级场均得分12.2分',
          stat2: '季后赛场均出场时间26.7分钟',
          stat3: '',
          salary: '500万￥',
          image: require('../assets/uploads/哈登介绍2.jpg')
        },
        {
          title: '红色战袍',
          honor1: '当选西部最佳球员、全明星',
          honor2: '最佳阵容一阵',
          stat1: '季后赛场均27分',
          stat2: '季后赛场均出场时间43分钟',
          stat3: '',
          salary: '2210万￥',
          image: require('../assets/uploads/哈登介绍3.jpg')
        },
        {
          title: '休斯顿时间',
          honor1: '三次周最佳两次月最佳球员',
          honor2: '全明星替补，最佳阵容一阵',
          stat1: '季后赛场均得分27.2分',
          stat2: '季后赛场均出场时间38分钟',
          stat3: '',
          salary: '2210万￥',
          image: require('../assets/uploads/哈登介绍4.jpg')
        },
        {
          title: '航天城的英雄',
          honor1: '荣获MVP',
          honor2: '当选全明星首发，最佳一阵',
          stat1: '孤胆英雄',
          stat2: '季后赛场均出厂时间40分钟',
          stat3: '',
          salary: '3000万￥',
          image: require('../assets/uploads/哈登介绍5.jpg')
        },
        {
          title: '篮网三巨头',
          honor1: '三连得分王',
          honor2: '当选全明星首发',
          stat1: '东部第一战绩',
          stat2: '季后赛场均26分',
          stat3: '',
          salary: '4100万￥',
          image: require('../assets/uploads/哈登介绍6.jpg')
        },
        {
          title: '远赴费城',
          honor1: '当选助攻王',
          honor2: '场均21分10.2助攻',
          stat1: '西部半决赛单核带队',
          stat2: '季后赛场均出场时间38分钟',
          stat3: '',
          salary: '4100万￥',
          image: require('../assets/uploads/哈登介绍7.jpg')
        },
        {
          title: '最终登船',
          honor1: '场均16.7分',
          honor2: '常规赛西部第四',
          stat1: '季后赛场均得分25.2分',
          stat2: '季后赛场均出厂时间39.3分钟',
          stat3: '',
          salary: '3500万￥',
          image: require('../assets/uploads/哈登介绍8.jpg')
        }
      ]
    };
  }
};
</script>

<style scoped>
.fresh .title {
  margin-top: 60px;
  margin-bottom: 20px;
}
</style>
