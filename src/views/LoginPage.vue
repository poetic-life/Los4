<template>
  <div class="wrapper">
    <!-- 返回首页按钮 -->
    <div class="home-button">
      <button @click="goHome">返回首页</button>
    </div>
    <div class="container">
      <div class="form-container">
        <!-- 登录表单 -->
        <form v-if="isLogin" @submit.prevent="handleLogin" class="form">
          <h2>登录</h2>
          <div class="input-group">
            <label for="loginUsername">用户名</label>
            <input type="text" id="loginUsername" v-model="loginUsername" placeholder="请输入用户名" required />
          </div>
          <div class="input-group">
            <label for="loginPassword">密码</label>
            <input type="password" id="loginPassword" v-model="loginPassword" placeholder="请输入密码" required />
          </div>
          <button type="submit">登录</button>
          <p>还没有账户？<a href="#" @click="toggleForm">立即注册</a></p>
        </form>

        <!-- 注册表单 -->
        <form v-else @submit.prevent="handleRegister" class="form">
          <h2>注册</h2>
          <div class="input-group">
            <label for="registerUsername">用户名</label>
            <input type="text" id="registerUsername" v-model="registerUsername" placeholder="请输入用户名" required />
          </div>
          <div class="input-group">
            <label for="registerEmail">邮箱</label>
            <input type="email" id="registerEmail" v-model="registerEmail" placeholder="请输入邮箱" required />
          </div>
          <div class="input-group">
            <label for="registerPassword">密码</label>
            <input type="password" id="registerPassword" v-model="registerPassword" placeholder="请输入密码" required />
          </div>
          <button type="submit">注册</button>
          <p>已有账户？<a href="#" @click="toggleForm">去登录</a></p>
        </form>
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
</template>

<script>
import router from '@/router';
export default {
  data() {
    return {
      isLogin: true,
      loginUsername: '',
      loginPassword: '',
      registerUsername: '',
      registerEmail: '',
      registerPassword: '',
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
  methods: {
  toggleForm() {
    this.isLogin = !this.isLogin;
  },
  handleLogin() {
    // 这里模拟一个登录成功的用户信息对象
    const userInfo = {
      token: 'exampleToken',
      userId: 'exampleUserId'
    };

    // 保存用户信息到 Vuex
    this.$store.commit('user/setUserInfo', userInfo);
    alert('登录成功')
    // 跳转到主页
    router.push('/home');
  },
  handleRegister() {
    console.log('注册中', this.registerUsername, this.registerEmail, this.registerPassword);
    alert("注册成功");
    this.toggleForm();
  },
  goHome() {
    router.push('/'); // 返回首页
  }
}

};
</script>

<style scoped>
/* 使页面整体布局居中，固定底部 */
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
}

.wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  position: relative; /* 为绝对定位提供参考 */
}

.home-button {
  position: absolute;
  top: 10px;
  right: 10px;
}

.home-button button {
  padding: 10px 20px;
  font-size: 16px;
  color: #007bff; /* 按钮文本颜色 */
  border: 1px solid #007bff; /* 边框颜色 */
  background-color: transparent; /* 去掉背景色 */
  border-radius: 4px;
  cursor: pointer;
}

.home-button button:hover {
  background-color: #e7f0ff; /* 悬停时背景颜色 */
}

.container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
}

.form-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 400px;
}

.form {
  display: flex;
  flex-direction: column;
}

.input-group {
  margin-bottom: 15px;
}

.input-group label {
  margin-bottom: 5px;
  font-size: 14px;
}

.input-group input {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 100%;
}

button {
  padding: 10px;
  font-size: 16px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

p {
  margin-top: 10px;
  font-size: 14px;
}

a {
  color: #007bff;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

.copyright {
  text-align: center;
  padding: 10px;
  background-color: #fff;
  border-top: 1px solid #ddd;
}

.copyright a {
  text-decoration: none;
  color: #007bff;
  margin: 0 5px;
}
</style>
