<template>
  <div class="adm-panel">
    <div class="list-head">
      <h3 class="title">评论管理</h3>
      <span class="sub">共 {{ filteredComments.length }} 条评论</span>
    </div>

    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索用户或评论内容" clearable style="width: 280px">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
    </div>

    <div class="card table-card">
      <el-table :data="filteredComments" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="postId" label="帖子ID" width="90" />
        <el-table-column prop="username" label="用户" width="130" />
        <el-table-column prop="content" label="内容" min-width="320" show-overflow-tooltip />
        <el-table-column label="时间" width="170">
          <template #default="scope">{{ formatTime(scope.row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
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
import { getAdminComments, deleteComment } from '@/api'

const comments = ref([])
const loading = ref(false)
const keyword = ref('')

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 16) : '')

const filteredComments = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return comments.value
  return comments.value.filter((c) =>
    (c.username || '').toLowerCase().includes(kw) ||
    (c.content || '').toLowerCase().includes(kw)
  )
})

const load = async () => {
  loading.value = true
  try {
    comments.value = (await getAdminComments()) || []
  } catch (e) {
    comments.value = []
  } finally {
    loading.value = false
  }
}

const remove = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该条评论吗？', '删除确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })
  } catch (e) {
    return
  }
  try {
    await deleteComment(row.id)
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