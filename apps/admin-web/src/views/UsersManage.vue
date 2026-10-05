<template>
  <div class="adm-panel">
    <div class="list-head">
      <h3 class="title">用户管理</h3>
      <span class="sub">共 {{ filteredUsers.length }} 位用户</span>
    </div>

    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索用户名或邮箱" clearable style="width: 260px">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-select v-model="roleFilter" placeholder="全部角色" clearable style="width: 140px">
        <el-option label="普通用户" value="USER" />
        <el-option label="管理员" value="ADMIN" />
      </el-select>
    </div>

    <div class="card table-card">
      <el-table :data="filteredUsers" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="points" label="积分" width="90" />
        <el-table-column prop="level" label="等级" width="110" />
        <el-table-column label="角色" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.role === 'ADMIN' ? 'danger' : 'info'" effect="dark">
              {{ scope.row.role === 'ADMIN' ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="toggleRole(scope.row)">
              {{ scope.row.role === 'ADMIN' ? '设为普通用户' : '设为管理员' }}
            </el-button>
            <el-button size="small" type="danger" plain @click="remove(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminUsers, updateUserRole, deleteUser } from '@/api'

const users = ref([])
const loading = ref(false)
const keyword = ref('')
const roleFilter = ref('')

const filteredUsers = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  return users.value.filter((u) => {
    const matchKw = !kw || (u.username || '').toLowerCase().includes(kw) || (u.email || '').toLowerCase().includes(kw)
    const matchRole = !roleFilter.value || u.role === roleFilter.value
    return matchKw && matchRole
  })
})

const load = async () => {
  loading.value = true
  try {
    users.value = (await getAdminUsers()) || []
  } catch (e) {
    users.value = []
  } finally {
    loading.value = false
  }
}

const toggleRole = async (row) => {
  const next = row.role === 'ADMIN' ? 'USER' : 'ADMIN'
  try {
    await updateUserRole(row.id, next)
    ElMessage.success('角色已更新')
    load()
  } catch (e) {}
}

const remove = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除用户「${row.username}」及其帖子、评论吗？`, '删除确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })
  } catch (e) {
    return
  }
  try {
    await deleteUser(row.id)
    ElMessage.success('已删除')
    load()
  } catch (e) {}
}

onMounted(load)
</script>

<style scoped>
.list-head {
  display: flex;
  align-items: baseline;
  gap: 14px;
  margin-bottom: 20px;
}

.title {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.sub {
  font-size: 13px;
  color: var(--text-muted);
}

.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.table-card {
  padding: 8px 16px 16px;
}
</style>