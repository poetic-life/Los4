<template>
  <section class="stars">
    <div class="wrapper">
      <div class="section-header">
        <span class="section-tag">STAR PLAYERS</span>
        <h2 class="section-title">四大球星</h2>
        <p class="section-desc">汇聚联盟顶级球星，冲击总冠军</p>
      </div>

      <div class="stars-grid">
        <router-link
          v-for="(player, index) in players"
          :key="index"
          :to="player.path"
          class="star-card card"
        >
          <div class="card-image">
            <img :src="player.imgSrc" :alt="player.name" />
            <span class="player-number">{{ getPlayerNumber(player.name) }}</span>
          </div>

          <div class="card-content">
            <div class="card-header">
              <h3 class="player-name">{{ player.name }}</h3>
              <span class="position-tag">{{ getPosition(player.name) }}</span>
            </div>

            <div class="player-stats">
              <div class="stat-item">
                <span class="stat-value">{{ getPlayerStats(player.name).pts }}</span>
                <span class="stat-label">得分</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ getPlayerStats(player.name).reb }}</span>
                <span class="stat-label">篮板</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ getPlayerStats(player.name).ast }}</span>
                <span class="stat-label">助攻</span>
              </div>
            </div>

            <ul class="highlights">
              <li v-for="(highlight, i) in getPlayerHighlights(player.name)" :key="i">
                {{ highlight }}
              </li>
            </ul>
          </div>
        </router-link>
      </div>
    </div>
  </section>
</template>

<script setup>
import hd1 from '../assets/uploads/hd1.jpg'
import rw from '../assets/uploads/rw.jpg'
import pg from '../assets/uploads/pg.jpg'
import lnd from '../assets/uploads/lnd.jpg'

const players = [
  { id: 1, name: '詹姆斯·哈登', imgSrc: hd1, path: '/jamesharden' },
  { id: 2, name: '拉塞尔·威斯布鲁克', imgSrc: rw, path: '/RussWest' },
  { id: 3, name: '保罗·乔治', imgSrc: pg, path: '/pg13' },
  { id: 4, name: '科怀·伦纳德', imgSrc: lnd, path: '/kawhi' }
]

const playerNumbers = {
  '詹姆斯·哈登': '01',
  '拉塞尔·威斯布鲁克': '00',
  '保罗·乔治': '13',
  '科怀·伦纳德': '02'
}

const playerHighlights = {
  '詹姆斯·哈登': ['10次全明星', '1次MVP', '3次得分王'],
  '拉塞尔·威斯布鲁克': ['9次全明星', '2次全明星MVP', '2次得分王'],
  '保罗·乔治': ['8次全明星', '1次抢断王', '1次最佳一阵'],
  '科怀·伦纳德': ['2次总冠军', '2次FMVP', '2次最佳防守球员']
}

const playerStats = {
  '詹姆斯·哈登': { pts: '24.6', reb: '5.8', ast: '10.1' },
  '拉塞尔·威斯布鲁克': { pts: '22.2', reb: '8.0', ast: '7.4' },
  '保罗·乔治': { pts: '23.8', reb: '6.6', ast: '5.2' },
  '科怀·伦纳德': { pts: '24.8', reb: '6.5', ast: '3.9' }
}

const playerPositions = {
  '詹姆斯·哈登': '组织后卫',
  '拉塞尔·威斯布鲁克': '组织后卫',
  '保罗·乔治': '小前锋',
  '科怀·伦纳德': '小前锋'
}

const getPlayerNumber = (name) => playerNumbers[name] || '00'
const getPlayerHighlights = (name) => playerHighlights[name] || []
const getPlayerStats = (name) => playerStats[name] || { pts: '0', reb: '0', ast: '0' }
const getPosition = (name) => playerPositions[name] || '前锋'
</script>

<style scoped>
.stars {
  padding: 90px 0;
  background: var(--bg-elevated);
}

.section-header {
  text-align: center;
  margin-bottom: 48px;
}

.stars-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.star-card {
  display: block;
  text-decoration: none;
  color: inherit;
  overflow: hidden;
  transition: transform var(--transition), box-shadow var(--transition);
}

.star-card:hover {
  transform: translateY(-6px);
}

.card-image {
  position: relative;
  height: 300px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center top;
  transition: transform 0.5s ease;
}

.star-card:hover .card-image img {
  transform: scale(1.1);
}

.player-number {
  position: absolute;
  top: 14px;
  left: 14px;
  width: 46px;
  height: 46px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(8px);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-md);
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: #fff;
}

.card-content {
  padding: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 16px;
}

.player-name {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.position-tag {
  padding: 4px 12px;
  background: rgba(200, 16, 46, 0.12);
  border-radius: var(--radius-full);
  font-size: 12px;
  color: var(--accent);
  white-space: nowrap;
}

.player-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  padding: 14px 0;
  border-top: 1px solid var(--border);
  border-bottom: 1px solid var(--border);
  margin-bottom: 14px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.stat-value {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
}

.stat-label {
  font-size: 12px;
  color: var(--text-muted);
}

.highlights {
  list-style: none;
  padding: 0;
  margin: 0;
}

.highlights li {
  position: relative;
  padding-left: 16px;
  font-size: 13px;
  color: var(--text-muted);
  margin-bottom: 6px;
}

.highlights li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 6px;
  height: 6px;
  background: var(--accent);
  border-radius: 50%;
}

@media (max-width: 992px) {
  .stars-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 560px) {
  .stars-grid {
    grid-template-columns: 1fr;
  }
}
</style>