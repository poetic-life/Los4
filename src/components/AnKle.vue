<template>
  <!-- 脚踝收集者 -->
  <div class="recommend wrapper" id="page-moment">
    <!-- title -->
    <div class="title">
      <div class="left">
        <h3>脚踝收集者</h3>
        <p>炸裂时刻 客场收割机</p>
      </div>
      <!-- <div class="right">
          <a href="#" class="more">查看全部<span class="iconfont icon-arrow-right-bold"></span></a>
      </div> -->
    </div>
    <!-- 内容 -->
    <div class="bd">
      <ul>
        <li v-for="(moment, index) in moments" :key="index">
          <a href="#">
            <div class="pic">
              <img :src="moment.image" :alt="moment.title">
            </div>
          </a>
          <div class="text">
            <h4>{{ moment.title }}</h4>
            <p>{{ moment.description }}</p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      moments: [
        {
          title: '无视防守',
          description: '等你站起来我在出手',
          image: require('../assets/uploads/hdhd.jpg')
        },
        {
          title: '活力四射',
          description: '力量与技术同在',
          image: require('../assets/uploads/wshd.jpg')
        },
        {
          title: '纵向丝滑',
          description: '帅气飘逸是目的',
          image: require('../assets/uploads/qzhd.jpg')
        },
        {
          title: '当代乔丹',
          description: '让人充满安全感',
          image: require('../assets/uploads/lndhd.jpg')
        }
      ]
    };
  }
};
</script>

<style scoped>

</style>
