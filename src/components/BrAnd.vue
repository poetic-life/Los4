<template>
  <!-- 品牌 -->
    <div class="topic wrapper" id="page brand">
        <div class="title">
            <div class="left">
                <h3>品牌专区</h3>
                <!-- <p>新鲜出炉 品质靠谱</p> -->
            </div>
            <div class="right">
                <a href="#" class="more">查看全部<span class="iconfont icon-arrow-right-bold"></span></a>
            </div>
        </div>
        <div class="topic-bd">
            <ul>
                <li v-for="(item,index) in topics" :key="index">
                    <a href="item.herf">
                        <div class="pic">
                            <img :src="item.imgSrc" alt="">
                            <div class="cover">
                                <div class="left">
                                    <h4 style="color: aqua;">{{item.title}}</h4>
                                    <p style="color: aquamarine;">{{item.subtitle}}</p>
                                </div>
                                <div class="right">
                                    {{item.price}}
                                </div>
                            </div>
                        </div>
                        <div class="text">
                            <div class="left">
                                <p>
                                    <i class="iconfont icon-favorites-fill"></i>
                                    <span>{{item.favorites}}</span>
                                </p>
                                <p>
                                    <i class="iconfont icon-browse"></i>
                                    <span>{{item.browse}}</span>
                                </p>
                            </div>
                            <div class="right">
                                <p>
                                    <i class="iconfont icon-comment"></i>
                                    <span>{{item.browse}}</span>
                                </p>
                            </div>
                        </div>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
export default {
    data(){
        return {
      topics: [
        {
          href: '#',
          imgSrc: require('../assets/uploads/brand1.jpg'),
          title: '篮球运动不负自身',
          subtitle: 'JUST DO IT',
          titleColor: 'aqua',
          subtitleColor: 'aquamarine',
          price: '￥29.9起',
          favorites: '1220',
          browse: '1820',
          comments: '1820',
        },
        {
          href: '#',
          imgSrc: require('../assets/uploads/brand2.jpg'),
          title: '您需要知道一切',
          subtitle: 'NOTHING IS IMPOSSIBLE',
          titleColor: 'red',
          subtitleColor: '',
          price: '￥39.9起',
          favorites: '2330',
          browse: '2090',
          comments: '1820',
        },
        {
          href: '#',
          imgSrc: require('../assets/uploads/brand3.jpg'),
          title: '金牌梦之队',
          subtitle: '感受自然的力量',
          titleColor: '',
          subtitleColor: 'red',
          price: '￥29.9起',
          favorites: '1340',
          browse: '2320',
          comments: '1820',
        },
      ],
    };
    }
}
</script>

<style>

.brand {
  margin-top: 60px;
  height: 700px;
  
}
.brand .topic-bd{
  background-color: skyblue;
}
.brand .wrapper {
  /* overflow: hidden; */
  /* 解决外边距塌陷问题 */
  height: 468px;
  /* background-color: pink; */
}

.brand .wrapper .title {
  position: relative;
  /* margin-bottom: 20px; */
  height: 35px;
  line-height: 35px;
  /* margin-top: 40px; */
  /* justify-content: center; */

}

/* .brand .wrapper.h3 {} */

.brand .wrapper .button {
  position: absolute;
  right: 0;
  bottom: -25px;
  display: flex;
}

.brand .wrapper .button a {
  width: 20px;
  height: 20px;
  margin-left: 12px;
  text-align: center;
  line-height: 20px;
  color: #fff;
}

.brand .wrapper .button .prev {
  background-color: #ddd;
}

.brand .wrapper .button .next {
  background-color: rgba(29, 66, 138, 1);
}

.brand .bd {
  width: 1240px;
  height: 600px;
}

.brand .bd ul {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.brand .bd li {
  width: 620px;
  height: 300px;
  margin: 5px 0;
  background-color: #fff;
}
</style>