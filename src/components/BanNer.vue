<template>
  <!-- banner区域 -->
  <div class="banner" @mouseover="stopAutoSlide" @mouseleave="startAutoSlide">
    <div class="wrapper">
      <ul class="pic" :style="picStyle">
        <li v-for="(item, index) in images" :key="index">
          <a :href="item.link">
            <img :src="item.src" :alt="'Banner image ' + (index + 1)" />
          </a>
        </li>
      </ul>
      <button v-if="showButtons" class="prev iconfont icon-arrow-left-bold " @click="prevSlide"></button>
      <button v-if="showButtons" class="next iconfont icon-arrow-right-bold" @click="nextSlide"></button>
      <ol class="slider-indicator">
        <li v-for="(item, index) in images" :key="index" :class="{ active: currentIndex === index }" @click="goToSlide(index)">
          <i></i>
        </li>
      </ol>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      images: [
        { src: require('../assets/uploads/fourtogether.jpg'), link: '#' },
        { src: require('../assets/uploads/fourtogether1.jpg'), link: '#' },
        { src: require('../assets/uploads/fourtogether2.jpg'), link: '#' }
      ],
      currentIndex: 0,
      intervalId: null,
      showButtons: true
    };
  },
  computed: {
    picStyle() {
      return {
        transform: `translateX(-${this.currentIndex * 100}%)`,
        transition: 'transform 0.5s ease-in-out'
      };
    }
  },
  mounted() {
    this.startAutoSlide();
  },
  beforeDestroy() {
    this.stopAutoSlide();
  },
  methods: {
  startAutoSlide() {
    this.intervalId = setInterval(() => {
      this.nextSlide();
    }, 1500); // 每1.5秒切换一次
  },
  stopAutoSlide() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  },
  nextSlide() {
    if (this.currentIndex === this.images.length - 1) {
      // 到达最后一张，瞬间切换到第一张
      this.currentIndex = 0;
      // 禁用过渡效果
      this.$nextTick(() => {
        this.picStyle.transition = 'none';
      });
    } else {
      // 正常切换到下一张
      this.currentIndex++;
      // 恢复过渡效果
      this.picStyle.transition = 'transform 0.5s ease-in-out';
    }
  },
  prevSlide() {
    this.currentIndex = (this.currentIndex - 1 + this.images.length) % this.images.length;
  },
  goToSlide(index) {
    this.currentIndex = index;
  }
}

};
</script>

<style scoped>
/* banner区域 */
.banner {
  position: relative;
  overflow: hidden;
}

.banner .wrapper {
  position: relative;
  width: 100%;
  height: 500px;
}

.banner .wrapper .pic {
  display: flex;
  width: 100%;
  height: 100%;
}

.banner .wrapper .pic li {
  flex: 0 0 100%;
  list-style: none;
}

.banner .wrapper .pic img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.5s ease-in-out;
}

button.prev, button.next {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  padding: 10px;
  cursor: pointer;
  z-index: 10;
  border-radius: 4px;
  display: none; /* 初始隐藏 */
}

button.prev {
  left: 10px;
}

button.next {
  right: 10px;
}

.banner:hover button.prev,
.banner:hover button.next {
  display: block; /* 鼠标悬停时显示按钮 */
}

.banner .wrapper .subnav {
  position: absolute;
  left: 0;
  top: 0;
  width: 250px;
  height: 100%;
  background-color: transparent; /* 移除背景色 */
}

.banner .wrapper .subnav ul {
  padding: 0;
  margin: 0;
  list-style: none;
}

.banner .wrapper .subnav ul li {
  height: 50px;
  line-height: 50px;
  display: flex;
  justify-content: space-between;
  padding-left: 30px;
  padding-right: 12px;
  color: #fff;
  cursor: pointer;
}

.banner .wrapper .subnav ul li:hover {
  background-color: #32a1d8be;
}

.banner .wrapper .subnav ul li a {
  margin-right: 5px;
  font-size: 14px;
  color: #fff;
}

.banner .wrapper .subnav ul li .classify {
  margin-right: 14px;
  font-size: 18px;
}

.banner .wrapper .subnav ul li a:hover {
  color: orange;
}

.banner .wrapper .subnav ul li .iconfont {
  font-size: 14px;
}

.banner .wrapper ol.slider-indicator {
  display: flex;
  position: absolute;
  bottom: 17px;
  right: 16px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.banner .wrapper ol.slider-indicator li {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  margin-left: 8px;
  cursor: pointer;
  background-color: rgba(255, 255, 255, 0.5);
}

.banner .wrapper ol.slider-indicator li.active {
  background-color: #fff;
}

.banner .wrapper ol.slider-indicator li i {
  display: block;
  width: 14px;
  height: 14px;
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  margin: 4px;
}
</style>
