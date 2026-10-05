<template>
  <div class="login-page">
    <div class="auth-bg">
      <div class="bg-glow glow-1"></div>
      <div class="bg-glow glow-2"></div>
    </div>

    <div class="wrapper">
      <router-link to="/home" class="home-link">
        <i class="iconfont icon-arrow-left"></i> 返回首页
      </router-link>

      <div class="login-card glass-card">
        <div class="card-head">
          <span class="section-tag">LA CLIPPERS</span>
          <h1 class="title">{{ isLogin ? '登录' : '注册' }}</h1>
          <p class="desc">{{ isLogin ? '欢迎回来，继续你的快船之旅' : '加入快船球迷大家庭' }}</p>
        </div>

        <el-form v-if="isLogin" :model="loginForm" :rules="loginRules" ref="loginFormRef" label-position="top" class="auth-form">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" size="large" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password size="large" />
          </el-form-item>
          <div class="form-options">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <a href="#" class="link" @click.prevent>忘记密码？</a>
          </div>
          <button type="button" class="btn btn-primary submit-btn" @click="handleLogin">登 录</button>
        </el-form>

        <el-form v-else :model="registerForm" :rules="registerRules" ref="registerFormRef" label-position="top" class="auth-form">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="registerForm.username" placeholder="请输入用户名" size="large" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="registerForm.email" placeholder="请输入邮箱" size="large" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password size="large" />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" show-password size="large" />
          </el-form-item>
          <el-checkbox v-model="agreeTerms">我已阅读并同意 <a href="#" class="link" @click.prevent>《用户协议》</a></el-checkbox>
          <button type="button" class="btn btn-primary submit-btn" @click="handleRegister">注 册</button>
        </el-form>

        <div class="toggle-row">
          <span>{{ isLogin ? '还没有账户？' : '已有账户？' }}</span>
          <a href="#" class="link toggle-link" @click.prevent="toggleForm">{{ isLogin ? '立即注册' : '去登录' }}</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/modules/user'
import { login as loginApi, register as registerApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()

const isLogin = ref(true)
const loginFormRef = ref()
const registerFormRef = ref()
const rememberMe = ref(false)
const agreeTerms = ref(false)

const loginForm = reactive({ username: '', password: '' })
const registerForm = reactive({ username: '', email: '', password: '', confirmPassword: '' })

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const registerRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码长度至少6位', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) callback(new Error('两次输入密码不一致'))
        else callback()
      },
      trigger: 'blur'
    }
  ]
}

const toggleForm = () => {
  isLogin.value = !isLogin.value
}

const handleLogin = async () => {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    const data = await loginApi({ username: loginForm.username, password: loginForm.password })
    const user = data.user || {}
    userStore.setUserInfo({
      token: data.token,
      userId: user.id,
      username: user.username,
      ...user
    })
    ElMessage.success('登录成功')
    router.push('/home')
  } catch (e) {
    // 错误提示已在拦截器统一处理
  }
}

const handleRegister = async () => {
  if (!agreeTerms.value) {
    ElMessage.warning('请先阅读并同意用户协议')
    return
  }
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    await registerApi({
      username: registerForm.username,
      email: registerForm.email,
      password: registerForm.password
    })
    ElMessage.success('注册成功，请登录')
    isLogin.value = true
    loginForm.username = registerForm.username
    registerForm.username = ''
    registerForm.email = ''
    registerForm.password = ''
    registerForm.confirmPassword = ''
  } catch (e) {
    // 错误提示已在拦截器统一处理
  }
}
</script>

<style scoped>
.login-page {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px 0;
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
  width: 100%;
  max-width: 440px;
  padding: 0 24px;
}

.home-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 20px;
  font-size: 14px;
  color: var(--text-muted);
  text-decoration: none;
  transition: color var(--transition);
}

.home-link:hover {
  color: var(--text-primary);
}

.login-card {
  padding: 40px 36px;
}

.card-head {
  text-align: center;
  margin-bottom: 28px;
}

.title {
  font-family: var(--font-display);
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 8px 0 6px;
}

.desc {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0;
}

.auth-form {
  display: flex;
  flex-direction: column;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.link {
  color: var(--accent);
  text-decoration: none;
  font-size: 13px;
}

.submit-btn {
  width: 100%;
  padding: 14px;
  margin-top: 4px;
  font-size: 15px;
}

.toggle-row {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 24px;
  font-size: 14px;
  color: var(--text-muted);
}

.toggle-link {
  font-weight: 600;
}
</style>