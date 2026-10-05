<template>
  <div class="RaNk">
    <div class="wrapper">
      <div class="title">
        <h3>球队阵容/数据</h3>
      </div>
      <div class="RaNk-content">
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
  </div>
</template>

<script setup>
import { ref } from 'vue'

const players = ref([
  {
    name: '保罗乔治',
    position: '前锋',
    height: '2.03',
    weight: '99.8公斤',
    number: '13',
    birthday: '1990-05-02',
    experience: '13',
    previous: 'Fresno State/United States',
    nationality: '美国'
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
    nationality: '美国'
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
    nationality: '美国'
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
    nationality: '美国'
  }
])
</script>

<style scoped>
.RaNk {
  padding: 50px 0;
  background-color: #f9f9f9;
}

.wrapper {
  width: 1200px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 30px;
}

.title h3 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.RaNk-content {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background-color: #333;
  color: white;
}

th {
  padding: 15px 10px;
  text-align: left;
  font-weight: 600;
  font-size: 14px;
}

tbody tr {
  border-bottom: 1px solid #eee;
  transition: background-color 0.3s;
}

tbody tr:hover {
  background-color: #f9f9f9;
}

td {
  padding: 15px 10px;
  font-size: 14px;
  color: #666;
}

.more {
  text-align: center;
  margin-top: 30px;
}

.more a {
  display: inline-block;
  padding: 10px 30px;
  background-color: #ff6b6b;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.more a:hover {
  background-color: #ff4757;
}
</style>