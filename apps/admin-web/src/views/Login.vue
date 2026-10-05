<template>
  <div class="login-page">
    <div class="login-card">
      <div class="brand">
        <span class="logo-mark">🏀</span>
        <div class="brand-text">
          <span class="title">LA CLIPPERS</span>
          <span class="sub">管理后台</span>
        </div>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" @keyup.enter="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="管理员账号" size="large">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password>
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-button type="primary" size="large" class="submit" :loading="loading" @click="handleLogin">
          登 录
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)
const form = ref({ username: 'admin', password: '' })

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const data = await login({ username: form.value.username, password: form.value.password })
    const user = data.user || {}
    userStore.setUserInfo({ token: data.token, ...user })
    ElMessage.success('登录成功')
    router.push('/users')
  } catch (e) {
    // 错误提示已在拦截器统一处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background:
    radial-gradient(1000px 600px at 20% 0%, rgba(29, 66, 138, 0.35), transparent),
    radial-gradient(800px 500px at 90% 100%, rgba(200, 16, 46, 0.35), transparent),
    var(--bg-base);
  padding: 24px;
}

.login-card {
  width: 400px;
  max-width: 100%;
  padding: 40px 36px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-lg);
}

.brand {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 32px;
}

.logo-mark {
  font-size: 40px;
}

.brand-text {
  display: flex;
  flex-direction: column;
}

.title {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 1.5px;
  background: var(--gradient-brand-blue);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.sub {
  font-size: 13px;
  color: var(--text-muted);
}

.submit {
  width: 100%;
  margin-top: 8px;
}
</style>