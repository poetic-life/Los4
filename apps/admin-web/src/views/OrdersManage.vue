<template>
  <div class="adm-panel">
    <div class="list-head">
      <h3 class="title">订单管理</h3>
      <span class="sub">共 {{ filteredOrders.length }} 笔订单</span>
    </div>

    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索订单号、收货人或电话" clearable style="width: 280px">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-select v-model="statusFilter" placeholder="全部状态" clearable style="width: 140px">
        <el-option label="待发货" value="待发货" />
        <el-option label="已发货" value="已发货" />
        <el-option label="已完成" value="已完成" />
        <el-option label="已取消" value="已取消" />
      </el-select>
    </div>

    <div class="card table-card">
      <el-table :data="filteredOrders" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" min-width="180" />
        <el-table-column prop="name" label="收货人" width="130" />
        <el-table-column prop="phone" label="电话" width="140" />
        <el-table-column label="金额" width="100">
          <template #default="scope">¥{{ scope.row.totalAmount }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="statusType(scope.row.status)" effect="dark">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" width="170">
          <template #default="scope">{{ formatTime(scope.row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.status === '待发货'" size="small" type="primary" @click="ship(scope.row)">发货</el-button>
            <el-button v-if="scope.row.status === '待发货' || scope.row.status === '已发货'" size="small" type="danger" plain @click="cancel(scope.row)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminOrders, shipOrderAdmin, cancelOrderAdmin } from '@/api'

const orders = ref([])
const loading = ref(false)
const keyword = ref('')
const statusFilter = ref('')

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 16) : '')

const statusType = (status) => {
  const map = { 待发货: 'warning', 已发货: 'primary', 已完成: 'success', 已取消: 'info' }
  return map[status] || 'info'
}

const filteredOrders = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  return orders.value.filter((o) => {
    const matchKw = !kw ||
      (o.orderNo || '').toLowerCase().includes(kw) ||
      (o.name || '').toLowerCase().includes(kw) ||
      (o.phone || '').toLowerCase().includes(kw)
    const matchStatus = !statusFilter.value || o.status === statusFilter.value
    return matchKw && matchStatus
  })
})

const load = async () => {
  loading.value = true
  try {
    orders.value = (await getAdminOrders()) || []
  } catch (e) {
    orders.value = []
  } finally {
    loading.value = false
  }
}

const ship = async (row) => {
  try {
    await shipOrderAdmin(row.id)
    ElMessage.success('已发货')
    load()
  } catch (e) {}
}

const cancel = async (row) => {
  try {
    await ElMessageBox.confirm(`确定取消订单「${row.orderNo}」吗？`, '取消确认', {
      type: 'warning',
      confirmButtonText: '取消订单',
      cancelButtonText: '返回'
    })
  } catch (e) {
    return
  }
  try {
    await cancelOrderAdmin(row.id)
    ElMessage.success('订单已取消')
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