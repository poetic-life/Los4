<template>
  <!-- 乔治个人介绍 -->
  <div class="cook" id="page-paul">
    <div class="wrapper">
      <!-- 标题 -->
      <div class="title">
        <div class="left">
          <h3>保罗乔治</h3>
        </div>
        <div class="right">
          <ul>
            <li><a href="#" class="active">快船时期</a></li>
            <li><a href="#">雷霆赛季</a></li>
            <li><a href="#">步行者时期</a></li>
          </ul>
          <a href="#" class="more">查看全部<span class="iconfont icon-arrow-right-bold"></span></a>
        </div>
      </div>
      <!-- 内容 -->
      <div class="content">
        <div class="left">
          <a href="#"><img src="../assets/uploads/乔治个人介绍.jpg" alt="乔治个人介绍"></a>
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
          image: require('../assets/uploads/乔治介绍1.jpg'),
          title: '新秀赛季，所获荣誉',
          stats: '新秀赛季场均得分12分',
          playoffStats: '季后赛场均出场时间21.9分钟',
          honors: ['2012-2013赛季最佳防守', '入选最佳新秀阵容二阵'],
          salary: '340万'
        },
        {
          image: require('../assets/uploads/乔治介绍2.jpg'),
          title: '步行者的神',
          stats: '季后赛场均12.1分',
          playoffStats: '季后赛场均出场时间24分钟',
          honors: ['入选全明星', '扣篮大赛第一名'],
          salary: '500万'
        },
        {
          image: require('../assets/uploads/乔治介绍3.jpg'),
          title: '季后赛硬刚热火三巨头',
          stats: '场均得分27.3分',
          playoffStats: '场均篮板6.7个',
          honors: ['场均出场37.6分钟', '入选最佳防守一阵'],
          salary: '1900万'
        },
        {
          image: require('../assets/uploads/乔治介绍4.jpg'),
          title: '加盟雷霆',
          stats: '场均得分30.3分',
          playoffStats: '场均篮板9.7个',
          honors: ['场均出场37.6分钟', '入选最佳防守一阵'],
          salary: '3000万'
        },
        {
          image: require('../assets/uploads/乔治介绍5.jpg'),
          title: '双锋线组合',
          stats: '场均得分30.2分',
          playoffStats: '场均篮板9.1个',
          honors: ['场均出场39.2分钟', '入选最佳阵容一阵'],
          salary: '3300万'
        },
        {
          image: require('../assets/uploads/乔治介绍6.jpg'),
          title: '快船国度',
          stats: '快船国度',
          playoffStats: '季后赛场均出场时间38.3分钟',
          honors: ['联手FMVP', '全明星首发'],
          salary: '3900万'
        },
        {
          image: require('../assets/uploads/乔治介绍7.jpg'),
          title: '得分王候选',
          stats: '场均贡献29.9分',
          playoffStats: '季后赛场均出场时间38分钟',
          honors: ['MVP候选人', '入选西部全明星'],
          salary: '4100万'
        },
        {
          image: require('../assets/uploads/乔治介绍8.jpg'),
          title: '西部最佳球员',
          stats: '场均28.9分',
          playoffStats: '季后赛场均出场时间38.9分钟',
          honors: ['总得分来到历史第20', '入选西部全明星'],
          salary: '4300万'
        }
      ]
    }
  }
}
</script>

<style scoped>

</style>
