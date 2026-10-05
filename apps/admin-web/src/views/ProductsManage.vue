<template>
  <div class="adm-panel">
    <div class="list-head">
      <h3 class="title">商品管理</h3>
      <div class="head-right">
        <span class="sub">共 {{ filteredProducts.length }} 件商品</span>
        <el-button type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon> 新增商品
        </el-button>
      </div>
    </div>

    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索商品名称或分类" clearable style="width: 280px">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
    </div>

    <div class="card table-card">
      <el-table :data="filteredProducts" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column label="图片" width="80">
          <template #default="scope">
            <img v-if="scope.row.image" :src="scope.row.image" class="thumb" :alt="scope.row.name" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" min-width="200" show-overflow-tooltip />
        <el-table-column label="价格" width="100">
          <template #default="scope">¥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="110" />
        <el-table-column prop="sales" label="销量" width="80" />
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="openEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" plain @click="remove(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑商品' : '新增商品'" width="640px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :min="0" />
        </el-form-item>
        <el-form-item label="图片">
          <el-input v-model="form.image" placeholder="/uploads/xxx.jpg" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" style="width: 200px" @change="syncCategoryName">
            <el-option label="篮球" :value="1" />
            <el-option label="篮球鞋" :value="2" />
            <el-option label="篮球服" :value="3" />
            <el-option label="篮球配件" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminProducts, createProduct, updateProduct, deleteProduct } from '@/api'

const products = ref([])
const loading = ref(false)
const keyword = ref('')
const dialogVisible = ref(false)
const editingId = ref(null)
const form = ref({})

const categoryNames = { 1: '篮球', 2: '篮球鞋', 3: '篮球服', 4: '篮球配件' }

const filteredProducts = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return products.value
  return products.value.filter((p) =>
    (p.name || '').toLowerCase().includes(kw) ||
    (p.categoryName || '').toLowerCase().includes(kw)
  )
})

const syncCategoryName = () => {
  form.value.categoryName = categoryNames[form.value.categoryId] || ''
}

const load = async () => {
  loading.value = true
  try {
    products.value = (await getAdminProducts()) || []
  } catch (e) {
    products.value = []
  } finally {
    loading.value = false
  }
}

const openCreate = () => {
  editingId.value = null
  form.value = { name: '', price: 0, image: '', categoryId: 1, categoryName: '篮球', stock: 100, description: '' }
  dialogVisible.value = true
}

const openEdit = (row) => {
  editingId.value = row.id
  form.value = { ...row }
  dialogVisible.value = true
}

const save = async () => {
  try {
    if (editingId.value) {
      await updateProduct(editingId.value, form.value)
      ElMessage.success('已更新')
    } else {
      await createProduct(form.value)
      ElMessage.success('已新增')
    }
    dialogVisible.value = false
    load()
  } catch (e) {}
}

const remove = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除商品「${row.name}」吗？`, '删除确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })
  } catch (e) {
    return
  }
  try {
    await deleteProduct(row.id)
    ElMessage.success('已删除')
    load()
  } catch (e) {}
}

onMounted(load)
</script>

<style scoped>
.list-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.head-right {
  display: flex;
  align-items: center;
  gap: 16px;
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

.thumb {
  width: 44px;
  height: 44px;
  object-fit: cover;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border);
}
</style>