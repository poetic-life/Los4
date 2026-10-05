<template>
  <div class="adm-panel">
    <div class="list-head">
      <h3 class="title">帖子管理</h3>
      <span class="sub">共 {{ filteredPosts.length }} 篇帖子</span>
    </div>

    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索标题、作者或标签" clearable style="width: 280px">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
    </div>

    <div class="card table-card">
      <el-table :data="filteredPosts" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="title" label="标题" min-width="220" show-overflow-tooltip />
        <el-table-column prop="username" label="作者" width="130" />
        <el-table-column prop="tag" label="标签" width="100" />
        <el-table-column prop="likes" label="点赞" width="80" />
        <el-table-column prop="comments" label="评论" width="80" />
        <el-table-column prop="views" label="浏览" width="80" />
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
import { getAdminPosts, deletePost } from '@/api'

const posts = ref([])
const loading = ref(false)
const keyword = ref('')

const filteredPosts = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return posts.value
  return posts.value.filter((p) =>
    (p.title || '').toLowerCase().includes(kw) ||
    (p.username || '').toLowerCase().includes(kw) ||
    (p.tag || '').toLowerCase().includes(kw)
  )
})

const load = async () => {
  loading.value = true
  try {
    posts.value = (await getAdminPosts()) || []
  } catch (e) {
    posts.value = []
  } finally {
    loading.value = false
  }
}

const remove = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除帖子「${row.title}」及其全部评论吗？`, '删除确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })
  } catch (e) {
    return
  }
  try {
    await deletePost(row.id)
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