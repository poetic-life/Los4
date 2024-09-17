<template>
  <!-- 威少个人介绍 -->
  <div class="cloths" id="page-west">
    <div class="wrapper">
      <!-- 标题 -->
      <div class="title">
        <div class="left">
          <h3>维斯布鲁克</h3>
        </div>
        <div class="right">
          <ul>
            <li><a href="#" class="active">快船时期</a></li>
            <li><a href="#">湖人时期</a></li>
            <li><a href="#">奇才时期</a></li>
            <li><a href="#">火箭时期</a></li>
            <li><a href="#">雷霆时期</a></li>
          </ul>
          <a href="#" class="more">查看全部<span class="iconfont icon-arrow-right-bold"></span></a>
        </div>
      </div>
      <!-- 内容 -->
      <div class="content">
        <div class="left">
          <a href="#"><img src="../assets/uploads/威少个人介绍.jpg" alt="威少个人介绍"></a>
        </div>
        <div class="right">
          <ul>
            <li v-for="(season, index) in seasons" :key="index">
              <a href="#">
                <div class="pic"><img :src="season.image" :alt="season.title"></div>
                <div class="cover">
                  <p>{{ season.stats }}</p>
                  <p></p>
                  <p>{{ season.playoffStats }}</p>
                  <p>更多数据<span class="iconfont icon-arrow-right-bold"></span></p>
                </div>
                <div class="text">
                  <div class="info">
                    <h4>{{ season.title }}</h4>
                    <p v-for="honor in season.honors" :key="honor">{{ honor }}</p>
                  </div>
                  <p class="price">赛季薪资{{ season.salary }}￥</p>
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
      seasons: [
        {
          image: require('../assets/uploads/威少介绍1.jpg'),
          title: '新秀赛季，所获荣誉',
          stats: '新秀赛季场均得分15.3分',
          playoffStats: '季后赛场均出场时间30分钟',
          honors: ['最佳新秀', '入选最佳新秀阵容一阵'],
          salary: '700万'
        },
        {
          image: require('../assets/uploads/威少介绍2.jpg'),
          title: '超音速之子',
          stats: '场均贡献22.7分',
          playoffStats: '季后赛场均出场时间32分钟',
          honors: ['常规赛战绩西部第三', '当选全明星首发'],
          salary: '710万'
        },
        {
          image: require('../assets/uploads/威少介绍3.jpg'),
          title: '加盟火箭',
          stats: '绝杀当选MVP',
          playoffStats: '常规赛达成成就场均三双',
          honors: ['组成双MVP组合', '当选月最佳球员'],
          salary: '2900万'
        },
        {
          image: require('../assets/uploads/威少介绍4.jpg'),
          title: '后场双枪组合',
          stats: '季后赛场均26.7分',
          playoffStats: '季后赛场均出场时间32.9分钟',
          honors: ['常规赛西部第四', '西部全明星首发'],
          salary: '3030万'
        },
        {
          image: require('../assets/uploads/威少介绍5.jpg'),
          title: '雷霆新王',
          stats: '三林鼎立',
          playoffStats: '季后赛场均出场时间38分钟',
          honors: ['当赛季得分王，助攻王', '入选最佳阵容一阵、全明星'],
          salary: '4012万'
        },
        {
          image: require('../assets/uploads/威少介绍6.jpg'),
          title: '辗转奇才',
          stats: '季后赛场均得分30分',
          playoffStats: '季后赛场均出场时间39.4分钟',
          honors: ['单核打进季后赛', '成为历史三双王'],
          salary: '4001万'
        },
        {
          image: require('../assets/uploads/威少介绍7.jpg'),
          title: '360组合',
          stats: '生涯总得分历史第十一',
          playoffStats: '生涯助攻数历史第十',
          honors: ['场均贡献20分', '入选历史75大巨星'],
          salary: '4300万'
        },
        {
          image: require('../assets/uploads/威少介绍8.jpg'),
          title: '寻得伯牙',
          stats: '场均得分12.4分',
          playoffStats: '季后赛场均出场时间18.9分钟',
          honors: ['降薪加盟快船', '求圆梦总冠军'],
          salary: '500万'
        }
      ]
    }
  }
}
</script>

<style scoped>

</style>
