<template>
  <div class="count-page">
    <div class="page-card card">
      <div class="card-head">
        <h2>账户设置</h2>
      </div>

      <el-form :model="form" label-width="100px" class="setting-form">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">保存修改</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="page-card card danger-zone">
      <div class="card-head">
        <h2>危险操作</h2>
      </div>
      <p class="danger-desc">退出登录后，需要重新输入账号密码才能访问。</p>
      <el-button type="danger" plain @click="logout">退出登录</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import { updateProfile } from '@/api'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({ username: '', email: '' })

const save = async () => {
  const email = form.email.trim()
  if (!email) {
    ElMessage.warning('请输入邮箱')
    return
  }
  try {
    const saved = await updateProfile({ email })
    userStore.setUserInfo({ ...userStore.userInfo, email: saved.email })
    ElMessage.success('账户信息已保存')
  } catch (e) {
    // 错误已提示
  }
}

const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '退出',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/login')
    })
    .catch(() => {})
}

onMounted(() => {
  const info = userStore.getUserInfo()
  form.username = info?.username || '球迷'
  form.email = info?.email || ''
})
</script>

<style scoped>
.count-page {
  padding: 4px 4px 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-card {
  padding: 24px;
}

.card-head {
  margin-bottom: 20px;
}

.card-head h2 {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.setting-form {
  max-width: 460px;
}

.danger-zone {
  border-color: rgba(200, 16, 46, 0.25);
}

.danger-desc {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0 0 16px;
}
</style>