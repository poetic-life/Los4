<template>
  <section class="skills">
    <div class="wrapper">
      <div class="section-header">
        <span class="section-tag">PLAYER SKILLS</span>
        <h2 class="section-title">球星技术特点</h2>
        <p class="section-desc">四大超巨，各怀绝技</p>
      </div>

      <div class="skills-grid">
        <article class="skill-card card" v-for="p in players" :key="p.id">
          <div class="skill-top">
            <div class="skill-img">
              <img :src="p.img" :alt="p.name" />
            </div>
            <div class="skill-body">
              <span class="skill-no">{{ p.no }}</span>
              <h3 class="skill-name">{{ p.name }}</h3>
              <span class="skill-en">{{ p.en }}</span>
              <span class="skill-role">{{ p.role }}</span>
              <p class="skill-desc">{{ p.desc }}</p>
            </div>
          </div>

          <div class="skill-bars">
            <div class="skill-bar" v-for="(s, i) in p.skills" :key="i">
              <div class="skill-bar-head">
                <span class="skill-bar-label">{{ s.name }}</span>
                <span class="skill-bar-score">{{ s.score }}</span>
              </div>
              <div class="skill-bar-track">
                <div class="skill-bar-fill" :style="{ width: s.score + '%' }"></div>
              </div>
            </div>
          </div>

          <div class="skill-video" v-if="activeId === p.id">
            <iframe
              :src="videoSrc(p)"
              title="视频集锦"
              scrolling="no"
              frameborder="0"
              allowfullscreen
            ></iframe>
          </div>

          <button class="skill-play" @click="toggle(p)">
            <i class="iconfont icon-play"></i>
            {{ activeId === p.id ? '收起视频' : '观看技术集锦' }}
          </button>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import harden from '@/assets/uploads/harden.png'
import westbrook from '@/assets/uploads/westbrook.png'
import george from '@/assets/uploads/george.png'
import kawhi from '@/assets/uploads/kawhi.png'

const players = ref([
  {
    id: 0,
    no: '01',
    name: '詹姆斯·哈登',
    en: 'James Harden',
    role: '组织后卫 / 得分后卫',
    img: harden,
    bvid: 'BV1BE7p6gE9r',
    desc: '历史级别持球大核心，用后撤步三分与造犯规艺术统治进攻端。',
    skills: [
      { name: '后撤步三分', score: 99 },
      { name: '造犯规艺术', score: 97 },
      { name: '组织串联', score: 92 }
    ]
  },
  {
    id: 1,
    no: '00',
    name: '拉塞尔·威斯布鲁克',
    en: 'Russell Westbrook',
    role: '组织后卫',
    img: westbrook,
    bvid: 'BV1SdKS6zEnv',
    desc: '后卫里的推土机，爆发力碾压防线，历史级三双机器。',
    skills: [
      { name: '暴力突破', score: 96 },
      { name: '三双全能', score: 94 },
      { name: '防守韧性', score: 88 }
    ]
  },
  {
    id: 2,
    no: '13',
    name: '保罗·乔治',
    en: 'Paul George',
    role: '得分后卫 / 小前锋',
    img: george,
    bvid: 'BV1zFaq6SE1V',
    desc: '丝滑干拔 + 攻防一体，乔大将军的关键球从不让人失望。',
    skills: [
      { name: '干拔跳投', score: 93 },
      { name: '攻防一体', score: 90 },
      { name: '关键球', score: 89 }
    ]
  },
  {
    id: 3,
    no: '02',
    name: '科怀·伦纳德',
    en: 'Kawhi Leonard',
    role: '小前锋',
    img: kawhi,
    bvid: 'BV17EB3BWEGn',
    desc: '死亡缠绕锁死对手，中距离机器，关键时刻最冷静的大心脏。',
    skills: [
      { name: '死亡缠绕', score: 99 },
      { name: '中距离跳投', score: 95 },
      { name: '大心脏', score: 96 }
    ]
  }
])

const activeId = ref(-1)

const videoSrc = (p) => `//player.bilibili.com/player.html?bvid=${p.bvid}&page=1&high_quality=1&danmaku=0&autoplay=1`

const toggle = (p) => {
  activeId.value = activeId.value === p.id ? -1 : p.id
}
</script>

<style scoped>
.skills {
  padding: 90px 0;
  background: var(--bg-base);
}

.section-header {
  text-align: center;
  margin-bottom: 56px;
}

.skills-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.skill-card {
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

.skill-top {
  display: flex;
  gap: 20px;
  align-items: center;
}

.skill-img {
  width: 130px;
  height: 200px;
  flex-shrink: 0;
  border-radius: var(--radius-md);
  overflow: hidden;
  background: var(--bg-hover);
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.skill-img img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  object-position: bottom center;
  filter: drop-shadow(0 10px 20px rgba(0, 0, 0, 0.4));
}

.skill-body {
  flex: 1;
  min-width: 0;
}

.skill-no {
  display: inline-block;
  font-family: var(--font-display);
  font-size: 40px;
  font-weight: 800;
  line-height: 1;
  color: transparent;
  -webkit-text-stroke: 1.5px rgba(255, 255, 255, 0.55);
  margin-bottom: 8px;
}

.skill-name {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 2px;
}

.skill-en {
  display: block;
  font-family: var(--font-display);
  font-size: 12px;
  letter-spacing: 2px;
  text-transform: uppercase;
  color: var(--text-muted);
  margin-bottom: 8px;
}

.skill-role {
  display: inline-block;
  padding: 3px 10px;
  background: rgba(200, 16, 46, 0.12);
  border: 1px solid rgba(200, 16, 46, 0.3);
  border-radius: var(--radius-full);
  font-size: 11px;
  color: var(--accent);
  margin-bottom: 12px;
}

.skill-desc {
  font-size: 13px;
  line-height: 1.6;
  color: var(--text-muted);
  margin: 0;
}

.skill-bars {
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin-top: 20px;
}

.skill-bar-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.skill-bar-label {
  font-size: 13px;
  color: var(--text-secondary);
}

.skill-bar-score {
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 700;
  color: var(--accent);
}

.skill-bar-track {
  height: 6px;
  border-radius: 4px;
  background: var(--bg-hover);
  overflow: hidden;
}

.skill-bar-fill {
  height: 100%;
  border-radius: 4px;
  background: var(--gradient-brand);
  transition: width 1s ease;
}

.skill-video {
  margin-top: 20px;
  border-radius: var(--radius-md);
  overflow: hidden;
  background: #000;
}

.skill-video iframe {
  display: block;
  width: 100%;
  aspect-ratio: 16 / 9;
  border: 0;
}

.skill-play {
  margin-top: 18px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 20px;
  background: rgba(200, 16, 46, 0.12);
  border: 1px solid rgba(200, 16, 46, 0.32);
  border-radius: var(--radius-full);
  font-size: 13px;
  font-weight: 600;
  color: var(--accent);
  cursor: pointer;
  transition: all var(--transition);
}

.skill-play:hover {
  background: var(--accent);
  color: #fff;
}

@media (max-width: 992px) {
  .skills-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 560px) {
  .skill-top {
    flex-direction: column;
    align-items: flex-start;
  }

  .skill-img {
    width: 100%;
    height: 220px;
  }
}
</style>