<template>
  <div class="service-page">
    <div class="auth-bg">
      <div class="bg-glow glow-1"></div>
      <div class="bg-glow glow-2"></div>
    </div>

    <div class="wrapper">
      <header class="topbar">
        <router-link to="/home" class="home-link">
          <el-icon><ArrowLeft /></el-icon> 返回首页
        </router-link>
        <span class="topbar-brand">在线客服</span>
      </header>

      <section class="hero glass-card">
        <span class="section-tag">CUSTOMER SERVICE</span>
        <h1 class="title">我们随时为您服务</h1>
        <p class="desc">留下您的问题，客服团队会在 12 小时内回复。</p>
      </section>

      <div class="columns">
        <section class="contact-column">
          <h2 class="col-title">联系方式</h2>
          <div v-for="item in contacts" :key="item.label" class="contact-card card">
            <el-icon class="contact-icon"><component :is="item.icon" /></el-icon>
            <div class="contact-info">
              <span class="contact-label">{{ item.label }}</span>
              <span class="contact-value">{{ item.value }}</span>
            </div>
          </div>

          <h2 class="col-title faq-title">常见问题</h2>
          <div class="card faq-links">
            <router-link v-for="q in faqList" :key="q" :to="'/help-center'" class="faq-link">
              <el-icon><ArrowRight /></el-icon> {{ q }}
            </router-link>
          </div>
        </section>

        <section class="form-column card">
          <h2 class="col-title">留言反馈</h2>
          <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
            <el-form-item label="您的称呼" prop="name">
              <el-input v-model="form.name" placeholder="请输入您的称呼" />
            </el-form-item>
            <el-form-item label="联系方式" prop="contact">
              <el-input v-model="form.contact" placeholder="邮箱或手机号" />
            </el-form-item>
            <el-form-item label="问题类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择问题类型" style="width: 100%">
                <el-option label="赛程与观赛" value="schedule" />
                <el-option label="商品订单" value="order" />
                <el-option label="账户问题" value="account" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
            <el-form-item label="问题描述" prop="message">
              <el-input v-model="form.message" type="textarea" :rows="4" placeholder="请详细描述您遇到的问题" />
            </el-form-item>
            <button type="button" class="btn btn-primary submit-btn" @click="submit">提交反馈</button>
          </el-form>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { submitFeedback } from '@/api'

const formRef = ref()

const contacts = [
  { icon: 'Phone', label: '客服热线', value: '400-888-0000' },
  { icon: 'Message', label: '客服邮箱', value: 'support@laclippers.com' },
  { icon: 'Clock', label: '工作时间', value: '每日 9:00 - 21:00' }
]

const faqList = ['如何查看球队赛程？', '如何退换商品？', '如何重置密码？', '如何查看订单状态？']

const form = reactive({ name: '', contact: '', type: '', message: '' })

const rules = {
  name: [{ required: true, message: '请输入您的称呼', trigger: 'blur' }],
  contact: [{ required: true, message: '请输入联系方式', trigger: 'blur' }],
  type: [{ required: true, message: '请选择问题类型', trigger: 'change' }],
  message: [{ required: true, message: '请输入问题描述', trigger: 'blur' }]
}

const submit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      await submitFeedback({ ...form })
      ElMessage.success('反馈已提交，我们会尽快与您联系')
      form.name = ''
      form.contact = ''
      form.type = ''
      form.message = ''
    } catch (e) {
      // 错误已在拦截器统一提示
    }
  })
}
</script>

<style scoped>
.service-page {
  position: relative;
  min-height: 100vh;
  padding: 40px 0 80px;
  background-color: var(--bg-base);
  overflow: hidden;
}

.auth-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.bg-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(90px);
  opacity: 0.35;
}

.glow-1 {
  width: 420px;
  height: 420px;
  background: var(--accent);
  top: -10%;
  left: -10%;
}

.glow-2 {
  width: 360px;
  height: 360px;
  background: var(--accent-2);
  bottom: -10%;
  right: -8%;
}

.wrapper {
  position: relative;
  z-index: 2;
}

.topbar {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 28px;
}

.home-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: var(--text-muted);
  text-decoration: none;
  transition: color var(--transition);
}

.home-link:hover {
  color: var(--text-primary);
}

.topbar-brand {
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 1px;
  color: var(--text-muted);
}

.hero {
  padding: 44px 36px;
  text-align: center;
  margin-bottom: 32px;
}

.title {
  font-family: var(--font-display);
  font-size: 36px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 12px 0 10px;
}

.desc {
  font-size: 15px;
  color: var(--text-muted);
  margin: 0;
}

.columns {
  display: grid;
  grid-template-columns: 1fr 1.4fr;
  gap: 24px;
  align-items: start;
}

.col-title {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 16px;
}

.contact-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  margin-bottom: 16px;
}

.contact-icon {
  font-size: 26px;
  color: var(--accent);
  flex-shrink: 0;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.contact-label {
  font-size: 13px;
  color: var(--text-muted);
}

.contact-value {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.faq-title {
  margin-top: 28px;
}

.faq-links {
  padding: 8px;
}

.faq-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 14px;
  border-radius: var(--radius-sm);
  font-size: 14px;
  color: var(--text-muted);
  text-decoration: none;
  transition: all var(--transition);
}

.faq-link:hover {
  color: var(--accent);
  background: var(--bg-hover);
}

.form-column {
  padding: 28px;
}

.submit-btn {
  width: 100%;
  padding: 13px;
  font-size: 15px;
}

@media (max-width: 900px) {
  .columns {
    grid-template-columns: 1fr;
  }
  .hero {
    padding: 32px 24px;
  }
  .title {
    font-size: 28px;
  }
}
</style>