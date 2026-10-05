<template>
  <div class="address-page">
    <div class="page-card card">
      <div class="card-head">
        <h2>配送地址</h2>
        <button class="btn btn-primary" @click="openDialog()">新增地址</button>
      </div>

      <el-empty v-if="addresses.length === 0" description="还没有收货地址" />
      <div v-else class="address-list">
        <div class="address-item" v-for="addr in addresses" :key="addr.id">
          <div class="addr-main">
            <div class="addr-line1">
              <span class="name">{{ addr.name }}</span>
              <span class="phone">{{ addr.phone }}</span>
              <span v-if="addr.isDefault" class="badge">默认</span>
            </div>
            <div class="addr-detail">{{ addr.address }}</div>
          </div>
          <div class="addr-actions">
            <a v-if="!addr.isDefault" href="#" class="link" @click.prevent="setDefault(addr.id)">设为默认</a>
            <a href="#" class="link" @click.prevent="openDialog(addr)">编辑</a>
            <a href="#" class="link danger" @click.prevent="removeAddress(addr.id)">删除</a>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑地址' : '新增地址'" width="520px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="收货人">
          <el-input v-model="form.name" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="收货地址">
          <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入详细收货地址" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="form.isDefault">设为默认地址</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAddresses, addAddress, updateAddress, removeAddress as removeAddressApi } from '@/api'

const dialogVisible = ref(false)
const editingId = ref(null)
const addresses = ref([])

const form = reactive({ name: '', phone: '', address: '', isDefault: false })

const loadAddresses = async () => {
  try {
    addresses.value = (await getAddresses()) || []
  } catch (e) {
    // 未登录或加载失败
  }
}

onMounted(loadAddresses)

const openDialog = (addr) => {
  if (addr) {
    editingId.value = addr.id
    Object.assign(form, { name: addr.name, phone: addr.phone, address: addr.address, isDefault: addr.isDefault })
  } else {
    editingId.value = null
    Object.assign(form, { name: '', phone: '', address: '', isDefault: false })
  }
  dialogVisible.value = true
}

const saveAddress = async () => {
  if (!form.name || !form.phone || !form.address) {
    ElMessage.warning('请完整填写地址信息')
    return
  }
  try {
    if (editingId.value) {
      await updateAddress(editingId.value, { ...form })
      ElMessage.success('地址已更新')
    } else {
      await addAddress({ ...form })
      ElMessage.success('地址已添加')
    }
    dialogVisible.value = false
    await loadAddresses()
  } catch (e) {
    // 错误已提示
  }
}

const setDefault = async (id) => {
  try {
    const target = addresses.value.find((a) => a.id === id)
    if (target) await updateAddress(id, { ...target, isDefault: true })
    await loadAddresses()
    ElMessage.success('默认地址已更新')
  } catch (e) {
    // 错误已提示
  }
}

const removeAddress = async (id) => {
  try {
    await removeAddressApi(id)
    addresses.value = addresses.value.filter((a) => a.id !== id)
    ElMessage.success('地址已删除')
  } catch (e) {
    // 错误已提示
  }
}
</script>

<style scoped>
.address-page {
  padding: 4px 4px 24px;
}

.page-card {
  padding: 24px;
}

.card-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-head h2 {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.address-item {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  padding: 18px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  transition: border-color var(--transition);
}

.address-item:hover {
  border-color: var(--border-strong);
}

.addr-line1 {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 6px;
}

.name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.phone {
  font-size: 14px;
  color: var(--text-muted);
}

.badge {
  padding: 2px 10px;
  background: rgba(200, 16, 46, 0.14);
  border-radius: var(--radius-full);
  font-size: 12px;
  color: var(--accent);
}

.addr-detail {
  font-size: 14px;
  color: var(--text-secondary);
}

.addr-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

.link {
  font-size: 13px;
  color: var(--text-muted);
  text-decoration: none;
  transition: color var(--transition);
}

.link:hover {
  color: var(--accent);
}

.link.danger:hover {
  color: #ff5b78;
}
</style>