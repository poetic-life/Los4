<template>
    <!-- 精彩集锦 -->
    <div class="brand" id="page-fans">
      <div class="wrapper">
        <!-- title区域 -->
        <div class="title">
          <div class="left">
            <h3>精彩集锦</h3>
            <p>视觉盛宴 不容错过</p>
          </div>
          <div class="button">
            <a href="#" class="prev">
              <i class="iconfont icon-arrow-left-bold"></i>
            </a>
            <a href="#" class="next">
              <i class="iconfont icon-arrow-right-bold"></i>
            </a>
          </div>
        </div>
        <!-- 内容 -->
        <div class="bd">
          <ul>
            <li><video src="../assets/uploads/哈登集锦.mp4" controls muted loop></video></li>
            <li><video src="../assets/uploads/威少集锦.mp4" controls muted loop></video></li>
            <li><video src="../assets/uploads/乔治集锦.mp4" controls muted loop></video></li>
            <li><video src="../assets/uploads/伦纳德集锦.mp4" controls muted loop></video></li>
          </ul>
        </div>
      </div>
    </div>
  </template>
  

<script>
export default {

}
</script>

<style scoped>
/* 整体品牌区域 */
.brand {
  padding: 20px;
  background-color: #f5f5f5; /* 背景颜色可根据需要调整 */
}

/* 标题区域 */
.title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-align: center;
  /* margin-bottom: 20px; */
}

.title .left h3 {
  font-size: 24px;
  margin-right: 35px;
}

.title .left p {
  margin: 5px 0 0;
  font-size: 16px;
  color: #888;
}

.title .button {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.title .button a {
  height: 10px;
  text-align: center;
  display: inline-block;
  margin: 0 ;
  font-size: 24px;
  color: #333;
  text-decoration: none;
  align-content: center;
}

.title .button a i {
  text-align: center;
  align-items: center;
  display: flex;
  justify-items: center;
}

.title .button a:hover {
  color: #007bff; /* 按钮悬停颜色 */
}

/* 内容区域 */
.bd {
  overflow: hidden;
}

.bd ul {
  display: flex;
  padding: 0;
  margin: 0;
  list-style: none;
}

.bd ul li {
  flex: 1;
  margin-right: 20px;
}

.bd ul li:last-child {
  margin-right: 0;
}

.bd video {
  width: 100%;
  height: auto;
  display: block;
  border-radius: 8px; /* 圆角边框可选 */
}
</style>
