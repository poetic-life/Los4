<template>
  <div class="orders-page">
    <header class="header">
      <div class="wrapper header-inner">
        <div class="logo" @click="goHome">LA CLIPPERS</div>
        <el-steps :active="currentStep" align-center class="steps">
          <el-step title="购物车" />
          <el-step title="确认订单" />
          <el-step title="完成" />
        </el-steps>
        <button class="btn btn-outline keep-shopping" @click="goShopping">继续购物</button>
      </div>
    </header>

    <main class="content wrapper">
      <!-- 购物车步骤 -->
      <div v-if="currentStep === 0" class="step-content">
        <div class="panel-card">
          <div class="panel-head">
            <span class="panel-title">我的购物车</span>
            <span class="panel-sub">共 {{ cartStore.items.length }} 件商品</span>
          </div>
          <el-table ref="tableRef" :data="cartStore.items" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column label="商品" width="320">
              <template #default="scope">
                <div class="product-cell">
                  <img :src="scope.row.image" :alt="scope.row.name" />
                  <span class="cell-name">{{ scope.row.name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="price" label="单价">
              <template #default="scope"> <span class="money">¥{{ scope.row.price }}</span> </template>
            </el-table-column>
            <el-table-column label="数量" width="150">
              <template #default="scope">
                <el-input-number v-model="scope.row.quantity" :min="1" @change="updateQuantity(scope.row.id, scope.row.quantity)" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="小计">
              <template #default="scope"> <span class="money">¥{{ scope.row.price * scope.row.quantity }}</span> </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="scope">
                <el-button type="danger" @click="removeItem(scope.row.id)" size="small">删除</el-button>
              </template>
            </el-table-column>
            <template #empty>
              <el-empty description="购物车还是空的">
                <button class="btn btn-primary" @click="goShopping">去逛逛</button>
              </el-empty>
            </template>
          </el-table>
        </div>

        <div class="settle-bar">
          <div class="settle-left">
            <el-checkbox v-model="selectAll" @change="handleSelectAll">全选</el-checkbox>
            <el-button type="text" @click="clearSelected" :disabled="selectedItems.length === 0">删除选中</el-button>
          </div>
          <div class="settle-right">
            <span class="settle-count">已选 <b>{{ selectedItems.length }}</b> 件</span>
            <span class="settle-total">合计: <b class="money-lg">¥{{ totalPrice }}</b></span>
            <button class="btn btn-primary btn-lg" :disabled="selectedItems.length === 0" @click="goToConfirm">去结算</button>
          </div>
        </div>
      </div>

      <!-- 确认订单步骤 -->
      <div v-if="currentStep === 1" class="step-content">
        <div class="panel-card">
          <div class="panel-head">
            <span class="panel-title"><el-icon><Location /></el-icon> 收货地址</span>
            <el-button type="text" @click="showAddressDialog">添加新地址</el-button>
          </div>
          <div v-if="selectedAddress" class="address-info">
            <div class="address-person">
              <span class="addr-name">{{ selectedAddress.name }}</span>
              <span class="addr-phone">{{ selectedAddress.phone }}</span>
              <span class="addr-tag">默认</span>
            </div>
            <div class="addr-detail">{{ selectedAddress.address }}</div>
          </div>
          <el-empty v-else description="请选择收货地址" />
        </div>

        <div class="panel-card">
          <div class="panel-head">
            <span class="panel-title">商品信息</span>
            <span class="panel-sub">共 {{ selectedItems.length }} 件</span>
          </div>
          <el-table :data="selectedItems" style="width: 100%">
            <el-table-column label="商品" width="320">
              <template #default="scope">
                <div class="product-cell">
                  <img :src="scope.row.image" :alt="scope.row.name" />
                  <span class="cell-name">{{ scope.row.name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="price" label="单价">
              <template #default="scope"> <span class="money">¥{{ scope.row.price }}</span> </template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" />
            <el-table-column label="小计">
              <template #default="scope"> <span class="money">¥{{ scope.row.price * scope.row.quantity }}</span> </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="panel-card">
          <div class="panel-head">
            <span class="panel-title">支付方式</span>
          </div>
          <el-radio-group v-model="paymentMethod">
            <el-radio value="alipay">支付宝支付</el-radio>
            <el-radio value="wechat">微信支付</el-radio>
            <el-radio value="bank">银行卡支付</el-radio>
          </el-radio-group>
        </div>

        <div class="settle-bar">
          <div class="settle-left">
            <button class="btn btn-outline" @click="currentStep = 0">返回购物车</button>
          </div>
          <div class="settle-right">
            <span class="settle-total">实付款: <b class="money-lg">¥{{ totalPrice }}</b></span>
            <button class="btn btn-primary btn-lg" @click="submitOrder">提交订单</button>
          </div>
        </div>
      </div>

      <!-- 完成步骤 -->
      <div v-if="currentStep === 2" class="step-content">
        <div class="success-box">
          <div class="success-icon"><el-icon><CircleCheckFilled /></el-icon></div>
          <h2>订单提交成功！</h2>
          <p class="order-no">订单号：{{ orderNumber }}</p>
          <p class="success-sub">感谢您的购买，商品将尽快为您发出</p>
          <div class="success-actions">
            <button class="btn btn-primary" @click="goToOrders">查看订单</button>
            <button class="btn btn-outline" @click="goShopping">继续购物</button>
          </div>
        </div>
      </div>
    </main>

    <!-- 地址对话框 -->
    <el-dialog v-model="addressDialogVisible" title="添加收货地址" width="500px">
      <el-form :model="addressForm" :rules="addressRules" ref="addressFormRef" label-width="100px">
        <el-form-item label="收货人" prop="name">
          <el-input v-model="addressForm.name" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="addressForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="收货地址" prop="address">
          <el-input v-model="addressForm.address" type="textarea" placeholder="请输入详细地址" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addressDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/store/modules/cart'
import { createOrder, getAddresses, addAddress } from '@/api'

const router = useRouter()
const cartStore = useCartStore()

const currentStep = ref(0)
const selectedItems = ref([])
const selectAll = ref(false)
const tableRef = ref()
const paymentMethod = ref('alipay')
const orderNumber = ref('')
const addressDialogVisible = ref(false)
const addressFormRef = ref()

const selectedAddress = ref(null)

const addressForm = reactive({
  name: '',
  phone: '',
  address: ''
})

const addressRules = {
  name: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  address: [{ required: true, message: '请输入收货地址', trigger: 'blur' }]
}

const addresses = ref([])

onMounted(async () => {
  try {
    const list = (await getAddresses()) || []
    addresses.value = list
    selectedAddress.value = list.find((a) => a.isDefault) || list[0] || null
  } catch (e) {
    // 未登录或加载失败时保持默认空地址，结算时引导登录
  }
})

const totalPrice = computed(() => {
  return selectedItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const handleSelectionChange = (items) => {
  selectedItems.value = items
  selectAll.value = items.length === cartStore.items.length
}

const handleSelectAll = (val) => {
  cartStore.items.forEach((item) => {
    tableRef.value?.toggleRowSelection(item, val)
  })
}

const updateQuantity = (productId, quantity) => {
  cartStore.updateQuantity(productId, quantity)
}

const removeItem = (productId) => {
  cartStore.removeItem(productId)
  ElMessage.success('已从购物车移除')
}

const clearSelected = () => {
  selectedItems.value.forEach(item => {
    cartStore.removeItem(item.id)
  })
  selectedItems.value = []
  ElMessage.success('已删除选中商品')
}

const goToConfirm = () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请选择商品')
    return
  }
  currentStep.value = 1
}

const saveAddress = () => {
  if (!addressFormRef.value) return
  addressFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const saved = await addAddress({ ...addressForm })
      addresses.value.push(saved)
      selectedAddress.value = saved
    } catch (e) {
      // 未登录时仅保存在本地会话
      const local = { id: Date.now(), ...addressForm }
      addresses.value.push(local)
      selectedAddress.value = local
    }
    addressDialogVisible.value = false
    ElMessage.success('地址保存成功')
  })
}

const submitOrder = async () => {
  if (!selectedAddress.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  try {
    const order = await createOrder({
      name: selectedAddress.value.name,
      phone: selectedAddress.value.phone,
      address: selectedAddress.value.address,
      paymentMethod: paymentMethod.value,
      items: selectedItems.value.map((item) => ({ productId: item.id, quantity: item.quantity }))
    })
    orderNumber.value = order.orderNo
    currentStep.value = 2
    selectedItems.value.forEach((item) => cartStore.removeItem(item.id))
    ElMessage.success('订单提交成功')
  } catch (e) {
    // 未登录（401）等错误已在拦截器提示
  }
}

const goToOrders = () => {
  router.push('/member-center/orders')
}

const goShopping = () => {
  router.push('/shopping')
}

const goHome = () => {
  router.push('/home')
}

const showAddressDialog = () => {
  addressForm.name = ''
  addressForm.phone = ''
  addressForm.address = ''
  addressDialogVisible.value = true
}
</script>

<style scoped>
.orders-page {
  min-height: 100vh;
  background-color: var(--bg-base);
}

.header {
  position: sticky;
  top: 0;
  z-index: 10;
  background: rgba(17, 20, 27, 0.85);
  backdrop-filter: blur(16px);
  border-bottom: 1px solid var(--border);
}

.header-inner {
  height: 68px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.logo {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 1px;
  color: var(--text-primary);
  cursor: pointer;
  white-space: nowrap;
}

.steps {
  flex: 1;
  max-width: 480px;
}

.keep-shopping {
  padding: 8px 20px;
  font-size: 13px;
}

.content {
  padding-top: 32px;
  padding-bottom: 64px;
}

.step-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.panel-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 24px;
}

.panel-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border);
}

.panel-title {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
}

.panel-sub {
  font-size: 13px;
  color: var(--text-muted);
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-cell img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border);
}

.cell-name {
  font-size: 14px;
  color: var(--text-primary);
}

.money {
  font-family: var(--font-display);
  font-weight: 700;
  color: var(--text-primary);
}

.money-lg {
  font-family: var(--font-display);
  font-size: 26px;
  font-weight: 700;
  color: var(--accent);
}

/* 结算栏 */
.settle-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: var(--bg-elevated);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  position: sticky;
  bottom: 16px;
}

.settle-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.settle-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.settle-count {
  font-size: 14px;
  color: var(--text-secondary);
}

.settle-count b {
  color: var(--accent);
}

.settle-total {
  font-size: 15px;
  color: var(--text-primary);
}

.btn-lg {
  padding: 14px 40px;
  font-size: 16px;
}

.btn-lg:disabled {
  cursor: not-allowed;
  opacity: 0.55;
  background: var(--bg-hover);
  box-shadow: none;
}

.address-person {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 8px;
}

.addr-name {
  font-size: 17px;
  font-weight: 700;
  color: var(--text-primary);
}

.addr-phone {
  font-size: 14px;
  color: var(--text-muted);
}

.addr-tag {
  padding: 1px 12px;
  background: rgba(200, 16, 46, 0.14);
  color: var(--accent);
  border-radius: var(--radius-full);
  font-size: 12px;
}

.addr-detail {
  font-size: 14px;
  color: var(--text-secondary);
}

/* 成功页 */
.success-box {
  text-align: center;
  padding: 60px 30px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
}

.success-icon {
  font-size: 72px;
  color: #67c23a;
  margin-bottom: 16px;
}

.success-box h2 {
  font-family: var(--font-display);
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 12px;
}

.order-no {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0 0 4px;
}

.success-sub {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0;
}

.success-actions {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 20px;
}
</style>