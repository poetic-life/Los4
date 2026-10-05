<template>
  <div class="order-list">
    <div class="list-head">
      <h3 class="title">我的订单</h3>
      <span class="sub">共 {{ orders.length }} 笔订单</span>
    </div>

    <el-empty v-if="orders.length === 0" description="暂无订单">
      <router-link to="/shopping" class="btn btn-primary">去逛逛</router-link>
    </el-empty>

    <div v-else class="order-cards">
      <div class="order-card card" v-for="order in orders" :key="order.id">
        <div class="order-header">
          <div class="order-meta">
            <span class="order-no">订单号 {{ order.orderNo }}</span>
            <span class="order-date">{{ formatTime(order.createdAt) }}</span>
          </div>
          <span class="status-badge" :class="statusClass(order.status)">{{ order.status }}</span>
        </div>

        <div class="order-body">
          <div class="order-sum">
            <span class="sum-label">实付金额</span>
            <span class="sum-value">¥{{ order.totalAmount }}</span>
          </div>
          <div class="order-pay">
            <span class="pay-label">支付方式</span>
            <span class="pay-value">{{ payName(order.paymentMethod) }}</span>
          </div>
        </div>

        <div class="order-footer">
          <div class="actions">
            <button class="btn btn-ghost" @click="openDetail(order.id)">查看详情</button>
            <button v-if="order.status === '待发货'" class="btn btn-outline" @click="cancel(order.id)">取消订单</button>
            <button v-if="order.status === '待发货'" class="btn btn-outline" @click="simulateShip(order.id)">模拟发货</button>
            <button v-if="order.status === '已发货'" class="btn btn-primary" @click="confirm(order.id)">确认收货</button>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="detailVisible" title="订单详情" width="720px">
      <div v-if="detail.order" class="detail-wrap">
        <div class="detail-status">
          <span class="status-badge" :class="statusClass(detail.order.status)">{{ detail.order.status }}</span>
          <span class="detail-no">订单号：{{ detail.order.orderNo }}</span>
        </div>

        <el-table :data="detail.items" style="width: 100%">
          <el-table-column label="商品" min-width="220">
            <template #default="scope">
              <div class="item-cell">
                <img v-if="scope.row.productImage" :src="scope.row.productImage" :alt="scope.row.productName" />
                <span>{{ scope.row.productName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="单价" width="90">
            <template #default="scope"> ¥{{ scope.row.price }} </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="70" />
          <el-table-column label="小计" width="100">
            <template #default="scope"> ¥{{ scope.row.price * scope.row.quantity }} </template>
          </el-table-column>
        </el-table>

        <div class="detail-info">
          <div class="info-line"><span>收货人</span>{{ detail.order.name }}（{{ detail.order.phone }}）</div>
          <div class="info-line"><span>收货地址</span>{{ detail.order.address }}</div>
          <div class="info-line"><span>支付方式</span>{{ payName(detail.order.paymentMethod) }}</div>
          <div class="info-line total"><span>实付金额</span><b>¥{{ detail.order.totalAmount }}</b></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrders, getOrderDetail, cancelOrder, shipOrder, confirmOrder } from '@/api'

const orders = ref([])
const detailVisible = ref(false)
const detail = ref({ order: null, items: [] })

const statusClass = (status) => {
  const map = {
    待发货: 'pending',
    已发货: 'shipped',
    已完成: 'done',
    已取消: 'cancelled'
  }
  return map[status] || 'pending'
}

const payName = (m) => ({ alipay: '支付宝', wechat: '微信支付', bank: '银行卡' }[m] || m)

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 16) : '')

const load = async () => {
  try {
    orders.value = (await getOrders()) || []
  } catch (e) {
    orders.value = []
  }
}

const openDetail = async (id) => {
  try {
    const data = (await getOrderDetail(id)) || {}
    detail.value = { order: data.order, items: data.items || [] }
    detailVisible.value = true
  } catch (e) {
    // 错误已在拦截器提示
  }
}

const cancel = async (id) => {
  try {
    await cancelOrder(id)
    ElMessage.success('订单已取消')
    load()
  } catch (e) {}
}

const simulateShip = async (id) => {
  try {
    await shipOrder(id)
    ElMessage.success('已发货（模拟）')
    load()
  } catch (e) {}
}

const confirm = async (id) => {
  try {
    await confirmOrder(id)
    ElMessage.success('确认收货成功')
    load()
  } catch (e) {}
}

onMounted(load)
</script>

<style scoped>
.order-list {
  min-height: 60vh;
}

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

.order-cards {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-card {
  padding: 20px 24px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 14px;
  border-bottom: 1px solid var(--border);
}

.order-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.order-no {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.order-date {
  font-size: 12px;
  color: var(--text-faint);
}

.status-badge {
  padding: 4px 14px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 600;
}

.status-badge.pending {
  background: rgba(243, 156, 18, 0.14);
  color: #f39c12;
}

.status-badge.shipped {
  background: rgba(41, 128, 185, 0.16);
  color: #3498db;
}

.status-badge.done {
  background: rgba(46, 204, 113, 0.14);
  color: #2ecc71;
}

.status-badge.cancelled {
  background: rgba(255, 255, 255, 0.08);
  color: var(--text-faint);
}

.order-body {
  display: flex;
  gap: 48px;
  padding: 16px 0;
}

.order-sum,
.order-pay {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.sum-label,
.pay-label {
  font-size: 12px;
  color: var(--text-faint);
}

.sum-value {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--accent);
}

.pay-value {
  font-size: 14px;
  color: var(--text-secondary);
}

.order-footer {
  display: flex;
  justify-content: flex-end;
}

.actions {
  display: flex;
  gap: 10px;
}

.actions .btn {
  padding: 8px 20px;
  font-size: 13px;
}

.detail-wrap {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.detail-status {
  display: flex;
  align-items: center;
  gap: 14px;
}

.detail-no {
  font-size: 13px;
  color: var(--text-muted);
}

.item-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.item-cell img {
  width: 48px;
  height: 48px;
  object-fit: cover;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border);
}

.detail-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 16px;
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  font-size: 14px;
}

.info-line {
  display: flex;
  gap: 16px;
  color: var(--text-secondary);
}

.info-line span {
  min-width: 72px;
  color: var(--text-faint);
}

.info-line.total b {
  font-family: var(--font-display);
  font-size: 20px;
  color: var(--accent);
}
</style>