<template>
  <!-- 排行 -->
  <div class="wrapper" id="page rank">
    <div class="title">
      <div class="left">
        <h3>球队阵容/数据</h3>
      </div>
    </div>
    <div class="list">
      <table>
        <thead>
          <tr>
            <th>姓名</th>
            <th>位置</th>
            <th>身高</th>
            <th>体重</th>
            <th>球号</th>
            <th>生日</th>
            <th>经验</th>
            <th>进入NBA之前</th>
            <th>国籍</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(player, index) in players" :key="index">
            <td>{{ player.name }}</td>
            <td>{{ player.position }}</td>
            <td>{{ player.height }}</td>
            <td>{{ player.weight }}</td>
            <td>{{ player.number }}</td>
            <td>{{ player.birthday }}</td>
            <td>{{ player.experience }}</td>
            <td>{{ player.previous }}</td>
            <td>{{ player.nationality }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="more">
      <a href="#">查看更多</a>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      players: [
        {
          name: '保罗乔治',
          position: '前锋',
          height: '2.03',
          weight: '99.8公斤',
          number: '13',
          birthday: '1990-05-02',
          experience: '13',
          previous: 'Fresno State/United States',
          nationality: '美国',
        },
        {
          name: '科怀伦纳德',
          position: '前锋',
          height: '2.01',
          weight: '102.1公斤',
          number: '2',
          birthday: '1991-06-29',
          experience: '11',
          previous: 'San Diego State/United States',
          nationality: '美国',
        },
        {
          name: '詹姆斯哈登',
          position: '后卫',
          height: '1.96',
          weight: '99.8公斤',
          number: '1',
          birthday: '1989-08-26',
          experience: '14',
          previous: 'Arizona State/United States',
          nationality: '美国',
        },
        {
          name: '拉塞尔-威斯布鲁克',
          position: '后卫',
          height: '1.93',
          weight: '90.7公斤',
          number: '0',
          birthday: '1988-11-12',
          experience: '15',
          previous: 'UCLA/United States',
          nationality: '美国',
        },
      ],
    };
  },
};
</script>

<style scoped>
.wrapper {
  text-align: center;
}

.title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.title .left h3 {
  margin: 0;
}

.title .right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.title .more {
  margin-top: 10px;
}

.list {
  height: 380px;
  overflow-y: auto;
}

.list table {
  border-collapse: collapse;
  width: 100%;
}

.list thead th {
  color: #fff;
  background-color: rgba(3, 47, 79, 1);
  height: 60px;
}

.list tbody tr {
  height: 75px;
}

.list tbody tr:nth-child(odd) {
  background-color: rgba(244, 244, 244, 1);
}

.list tbody tr:hover {
  background-color: rgb(227, 227, 227);
}

.more {
  margin-top: 20px;
}
</style>
