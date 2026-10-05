<template>
  <div class="profile-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>个人中心</h3>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="profile">
          <el-form :model="profileForm" :rules="profileRules" ref="profileFormRef" label-width="100px">
            <el-form-item label="用户名">
              <el-input v-model="profileForm.username" placeholder="请输入昵称（用户名）" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="个人简介">
              <el-input
                v-model="profileForm.bio"
                type="textarea"
                :rows="3"
                maxlength="200"
                show-word-limit
                placeholder="介绍一下自己，让球迷更了解你"
              />
            </el-form-item>
            <el-form-item label="会员等级">
              <span class="level-text">{{ userStore.userInfo?.level || '银卡会员' }}</span>
            </el-form-item>
            <el-form-item label="积分">
              <span class="level-text">{{ userStore.userInfo?.points ?? 0 }}</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 头像设置 -->
        <el-tab-pane label="头像设置" name="avatar">
          <div class="avatar-section">
            <el-avatar :size="100" :src="avatarUrl || undefined">
              {{ (userStore.userInfo?.username || '球').charAt(0) }}
            </el-avatar>
            <el-upload
              class="avatar-upload"
              action="#"
              :show-file-list="false"
              :auto-upload="false"
              :on-change="handleAvatarChange"
              accept="image/*"
            >
              <el-button size="small">选择图片</el-button>
            </el-upload>
            <el-button type="primary" size="small" @click="saveAvatar">保存头像</el-button>
          </div>
        </el-tab-pane>

        <!-- 账户安全 -->
        <el-tab-pane label="账户安全" name="security">
          <el-form :model="securityForm" label-width="100px">
            <el-form-item label="当前密码">
              <el-input v-model="securityForm.oldPassword" type="password" placeholder="请输入当前密码" show-password />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="securityForm.newPassword" type="password" placeholder="请输入新密码" show-password />
            </el-form-item>
            <el-form-item label="确认新密码">
              <el-input v-model="securityForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="changePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/modules/user'
import { getMe, updateProfile, changePassword as changePasswordApi } from '@/api'

const userStore = useUserStore()

const activeTab = ref('profile')
const profileFormRef = ref()

const profileForm = reactive({
  username: '',
  email: '',
  bio: ''
})

const avatarUrl = ref('')

const pendingAvatar = ref('')

const securityForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const profileRules = {
  email: [{ type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }]
}

const loadUser = async () => {
  try {
    const user = await getMe()
    if (user) {
      profileForm.username = user.username || ''
      profileForm.email = user.email || ''
      profileForm.bio = user.bio || ''
      avatarUrl.value = user.avatar || ''
      // 同步到本地 store
      userStore.setUserInfo({ ...userStore.userInfo, username: user.username, email: user.email, avatar: user.avatar, bio: user.bio })
    }
  } catch (e) {
    // 未登录等情况由拦截器提示
  }
}

const saveProfile = async () => {
  if (!profileFormRef.value) return
  profileFormRef.value.validate(async (valid) => {
    if (!valid) return
    if (!profileForm.username || profileForm.username.trim().length < 2) {
      ElMessage.warning('用户名至少 2 个字符')
      return
    }
    try {
      const saved = await updateProfile({ username: profileForm.username.trim(), email: profileForm.email.trim(), bio: profileForm.bio.trim() })
      userStore.setUserInfo({ ...userStore.userInfo, username: saved.username, email: saved.email, avatar: saved.avatar, bio: saved.bio })
      ElMessage.success('个人信息已保存')
    } catch (e) {
      // 拦截器已提示
    }
  })
}

const handleAvatarChange = (file) => {
  const raw = file.raw
  if (!raw || !raw.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件')
    return
  }
  const reader = new FileReader()
  reader.onload = (e) => {
    avatarUrl.value = e.target.result
    pendingAvatar.value = e.target.result
  }
  reader.readAsDataURL(raw)
}

const saveAvatar = async () => {
  if (!pendingAvatar.value) {
    ElMessage.warning('请先选择图片')
    return
  }
  try {
    const saved = await updateProfile({ avatar: pendingAvatar.value })
    userStore.setUserInfo({ ...userStore.userInfo, avatar: saved.avatar })
    pendingAvatar.value = ''
    ElMessage.success('头像已更新')
  } catch (e) {
    // 拦截器已提示
  }
}

const changePassword = async () => {
  if (!securityForm.oldPassword) {
    ElMessage.warning('请输入当前密码')
    return
  }
  if (!securityForm.newPassword || securityForm.newPassword.length < 6) {
    ElMessage.warning('新密码长度至少 6 位')
    return
  }
  if (securityForm.newPassword !== securityForm.confirmPassword) {
    ElMessage.warning('两次输入的新密码不一致')
    return
  }
  try {
    await changePasswordApi({ oldPassword: securityForm.oldPassword, newPassword: securityForm.newPassword })
    ElMessage.success('密码修改成功')
    securityForm.oldPassword = ''
    securityForm.newPassword = ''
    securityForm.confirmPassword = ''
  } catch (e) {
    // 拦截器已提示
  }
}

onMounted(loadUser)
</script>

<style scoped>
.profile-page {
  padding: 4px 4px 24px;
}

.card-header h3 {
  margin: 0;
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.level-text {
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 600;
  color: var(--text-secondary);
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 30px;
}

.avatar-upload {
  text-align: center;
}
</style>