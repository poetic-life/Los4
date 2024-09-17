<template>
  <!-- 底部 -->
  <div class="footer">
    <div class="wrapper">
      <!-- 服务区域 -->
      <div class="serve">
        <ul>
          <li v-for="(item, index) in serveItems" :key="index">
            <a :href="item.href">
              <h5></h5>
              <p>{{ item.text }}</p>
            </a>
          </li>
        </ul>
      </div>

      <div class="help">
        <div class="left">
          <dl v-for="(section, index) in helpSections" :key="index">
            <dt>{{ section.title }}</dt>
            <dd v-for="(link, index) in section.links" :key="index">
              <a :href="link.href">{{ link.text }}<span v-if="link.icon" class="iconfont icon-customer-service"></span></a>
            </dd>
          </dl>
        </div>

        <div class="right">
          <ul>
            <li v-for="(item, index) in rightItems" :key="index">
              <p>{{ item.text }}</p>
              <div v-if="item.imgSrc" class="pic">
                <img :src="item.imgSrc" :alt="item.alt">
              </div>
            </li>
          </ul>
        </div>
      </div>

      <div class="copyright">
        <p>
          <a v-for="(link, index) in footerLinks" :key="index" :href="link.href">{{ link.text }}</a>
          <span v-if="index < footerLinks.length - 1">|</span>
        </p>
        <p>CopyRight © 陶兴义</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      serveItems: [
        { href: '#', text: '合作伙伴' },
        { href: '#', text: '商务交流' },
        { href: '#', text: '质量赛事' },
        { href: '#', text: '售后无忧' },
      ],
      helpSections: [
        {
          title: '关于我们',
          links: [
            { href: '#', text: '平台规则' },
            { href: '#', text: '联系我们' },
            { href: '#', text: '问题反馈' },
          ],
        },
        {
          title: '售后服务',
          links: [
            { href: '#', text: '售后政策' },
            { href: '#', text: '退款说明' },
            { href: '#', text: '取消订单' },
          ],
        },
        {
          title: '服务热线',
          links: [
            { href: '#', text: '在线客服', icon: true },
            { href: '#', text: '客服电话 400-0000-800' },
            { href: '#', text: '工作时间 周一至周日 8.00-18.00' },
          ],
        },
      ],
      rightItems: [
        { imgSrc: require('../assets/images/app.png'), alt: '', text: 'LOS微信公众号' },
        { imgSrc: require('../assets/images/wechat.png'), alt: '', text: '商业合作' },
      ],
      footerLinks: [
        { href: '#', text: '关于我们' },
        { href: '#', text: '帮助中心' },
        { href: '#', text: '配送与验收' },
        { href: '#', text: '售后服务' },
        { href: '#', text: '商务合作' },
        { href: '#', text: '搜索推荐' },
        { href: '#', text: '友情链接' },
      ],
    };
  },
};
</script>

<style scoped>
.footer {
  background-color: #f5f5f5;
}

.wrapper {
  padding: 20px;
}

.serve ul {
  display: flex;
  justify-content: space-around;
  list-style: none;
  padding: 0;
  margin: 0;
}

.serve li {
  text-align: center;
}

.serve li a {
  text-decoration: none;
  color: #333;
}

.help {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.help .left {
  width: 60%;
}

.help .right {
  width: 35%;
}

.help dl {
  margin-bottom: 15px;
}

.help dt {
  font-weight: bold;
}

.help dd {
  margin: 5px 0;
}

.help a {
  text-decoration: none;
  color: #333;
}

.help .right ul {
  list-style: none;
  padding: 0;
}

.help .right li {
  text-align: center;
  margin-bottom: 10px;
}

.help .right .pic img {
  width: 100px; /* 调整二维码图片的大小 */
  height: 100px;
  margin-top: 10px;
}

.footer .copyright {
  margin-top: 20px;
  text-align: center;
  padding: 10px; /* 添加 padding 以防止内容紧贴边缘 */
  background-color: #f5f5f5; /* 确保背景色一致 */
  max-width: 100%; /* 确保内容不会超出页面宽度 */
  box-sizing: border-box; /* 确保 padding 不会导致宽度超出父元素 */
  word-wrap: break-word; /* 如果内容过长，自动换行 */
}

.footer .copyright p {
  margin: 0;
}

.footer .copyright a {
  text-decoration: none;
  color: #007bff;
  margin: 0 5px;
}

</style>
