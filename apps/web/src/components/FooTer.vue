<template>
  <footer class="site-footer">
    <div class="wrapper">
      <div class="footer-grid">
        <div class="footer-brand">
          <router-link to="/home" class="brand-logo">
            <span class="logo-icon">🏀</span>
            <span class="logo-text">LA CLIPPERS</span>
          </router-link>
          <p class="brand-desc">承载着城市的篮球梦想，冲击总冠军。</p>
        </div>

        <div class="footer-col">
          <h4 class="col-title">快速导航</h4>
          <ul class="links-list">
            <li><router-link to="/home">首页</router-link></li>
            <li><router-link to="/team">球队阵容</router-link></li>
            <li><router-link to="/news">新闻资讯</router-link></li>
            <li><router-link to="/shopping">官方商城</router-link></li>
          </ul>
        </div>

        <div class="footer-col">
          <h4 class="col-title">球迷专区</h4>
          <ul class="links-list">
            <li><router-link to="/community">球迷社区</router-link></li>
            <li><router-link to="/member-center">个人中心</router-link></li>
            <li><router-link to="/schedule">比赛日程</router-link></li>
          </ul>
        </div>

        <div class="footer-col">
          <h4 class="col-title">关注我们</h4>
          <div class="social-rich">
            <a href="javascript:void(0)" class="social-item" title="抖音">
              <span class="social-icon douyin">
                <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor"><path d="M19.59 6.69a4.83 4.83 0 0 1-3.77-4.25V2h-3.45v13.67a2.89 2.89 0 0 1-5.2 1.74 2.89 2.89 0 0 1 2.31-4.64 2.93 2.93 0 0 1 .88.13V9.4a6.84 6.84 0 0 0-1-.05A6.33 6.33 0 0 0 5 20.1a6.34 6.34 0 0 0 10.86-4.43v-7a8.16 8.16 0 0 0 4.77 1.52v-3.4a4.85 4.85 0 0 1-1.04-.1z"/></svg>
              </span>
              <div class="social-text">
                <span class="social-name">抖音号</span>
                <span class="social-id">洛杉矶快船球迷会</span>
              </div>
            </a>
            <a href="javascript:void(0)" class="social-item" title="微信">
              <span class="social-icon wechat">
                <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor"><path d="M9.5 4C5.36 4 2 6.69 2 10c0 1.89 1.06 3.57 2.7 4.65l-.7 2.11 2.42-1.27c.65.18 1.35.28 2.08.28h.44c-.24-.61-.37-1.26-.37-1.94 0-3.05 3.06-5.52 6.83-5.52h.3C14.85 5.86 12.46 4 9.5 4zM7 8a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm5 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/><path d="M21.5 13.5c0-2.33-2.67-4.22-5.96-4.22s-5.96 1.89-5.96 4.22 2.67 4.22 5.96 4.22c.55 0 1.08-.07 1.58-.2l2.02 1.06-.58-1.76c1.74-.9 2.94-2.34 2.94-3.6z"/></svg>
              </span>
              <div class="social-text">
                <span class="social-name">微信公众号</span>
                <span class="social-id">洛杉矶快船</span>
              </div>
            </a>
          </div>
        </div>
      </div>

      <div class="footer-bottom">
        <p class="copyright">&copy; 2024 LA CLIPPERS. ALL RIGHTS RESERVED.</p>
        <div class="footer-links">
          <a href="#">隐私政策</a>
          <span class="link-divider">|</span>
          <a href="#">服务条款</a>
          <span class="link-divider">|</span>
          <a href="#">Cookie 设置</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { RouterLink } from 'vue-router'
</script>

<style scoped>
.site-footer {
  background: var(--bg-elevated);
  border-top: 1px solid var(--border);
  padding: 64px 0 28px;
}

.footer-grid {
  display: grid;
  grid-template-columns: 1.6fr 1fr 1fr 1fr;
  gap: 48px;
  padding-bottom: 48px;
}

.footer-brand {
  padding-right: 24px;
}

.brand-logo {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  margin-bottom: 20px;
}

.logo-icon {
  font-size: 32px;
}

.logo-text {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 2px;
  color: var(--text-primary);
}

.brand-desc {
  font-size: 14px;
  color: var(--text-muted);
  line-height: 1.7;
  max-width: 260px;
}

.footer-col .col-title {
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 2px;
  text-transform: uppercase;
  color: var(--text-primary);
  margin: 4px 0 20px;
  position: relative;
}

.links-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.links-list li {
  margin-bottom: 12px;
}

.links-list li a {
  font-size: 14px;
  color: var(--text-muted);
  text-decoration: none;
  transition: color var(--transition);
}

.links-list li a:hover {
  color: var(--accent);
}

.social-rich {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.social-item {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  color: inherit;
}

.social-icon {
  width: 42px;
  height: 42px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-md);
  color: #fff;
  transition: all var(--transition);
}

.social-icon.douyin {
  background: #161823;
  color: #fe2c55;
}

.social-icon.wechat {
  background: #161823;
  color: #07c160;
}

.social-item:hover .social-icon {
  transform: translateY(-3px);
  box-shadow: var(--shadow-brand);
}

.social-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.social-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary);
}

.social-id {
  font-size: 12px;
  color: var(--text-muted);
}

.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 28px;
  border-top: 1px solid var(--border);
}

.copyright {
  font-size: 12px;
  color: var(--text-faint);
  letter-spacing: 0.5px;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 14px;
}

.footer-links a {
  font-size: 12px;
  color: var(--text-muted);
  text-decoration: none;
  transition: color var(--transition);
}

.footer-links a:hover {
  color: var(--accent);
}

.link-divider {
  color: var(--border-strong);
}

@media (max-width: 900px) {
  .footer-grid {
    grid-template-columns: 1fr 1fr;
    gap: 32px;
  }

  .footer-brand {
    grid-column: span 2;
  }

  .footer-bottom {
    flex-direction: column;
    gap: 12px;
  }
}
</style>