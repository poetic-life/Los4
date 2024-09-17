<template>
  <div class="member-center">
    <!-- 头部信息 -->
    <div class="header">
      <div class="user-info">
        <img class="avatar" :src="user.avatar" alt="用户头像" />
        <div class="details">
          <h3>{{ user.name }}</h3>
          <p>会员等级: {{ user.level }}</p>
          <p>积分: {{ user.points }}</p>
        </div>
      </div>
    </div>

    <!-- 主要功能 -->
    <div class="main">
      <div class="menu">
        <ul>
          <li v-for="(item, index) in menuItems" :key="index">
            <router-link :to="item.route">{{ item.name }}</router-link>
          </li>
        </ul>
      </div>

      <!-- 右侧内容区域 -->
      <div class="content">
        <!-- 这里可以根据不同的功能菜单切换显示内容 -->
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: {
        name: '陶兴义',
        avatar: 'https://example.com/avatar.png',
        level: '钻石会员',
        points: 3000,
      },
      menuItems: [
        { name: '个人信息', route: '/member-center/info' },
        { name: '我的订单', route: '/member-center/orders' },
        { name: '我的优惠券', route: '/member-center/coupons' },
        { name: '账户设置', route: '/member-center/settings' },
      ],
    };
  },
};
</script>

<style scoped>
.member-center {
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  padding: 20px;
}

.header {
  background-color: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-right: 20px;
}

.details {
  font-size: 16px;
}

.main {
  display: flex;
}

.menu {
  width: 20%;
  background-color: #fff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu ul li {
  margin-bottom: 20px;
}

.menu ul li a {
  text-decoration: none;
  color: #333;
}

.content {
  flex: 1;
  background-color: #fff;
  margin-left: 20px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>

