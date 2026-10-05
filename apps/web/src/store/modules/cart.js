import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])

  const addItem = (product) => {
    const existing = items.value.find(item => item.id === product.id)
    if (existing) {
      existing.quantity++
    } else {
      items.value.push({ ...product, quantity: 1 })
    }
    saveToStorage()
  }

  const removeItem = (productId) => {
    const index = items.value.findIndex(item => item.id === productId)
    if (index > -1) {
      items.value.splice(index, 1)
      saveToStorage()
    }
  }

  const updateQuantity = (productId, quantity) => {
    const item = items.value.find(item => item.id === productId)
    if (item) {
      item.quantity = quantity
      saveToStorage()
    }
  }

  const clearCart = () => {
    items.value = []
    saveToStorage()
  }

  const totalPrice = computed(() => {
    return items.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  })

  const totalItems = computed(() => {
    return items.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  const saveToStorage = () => {
    localStorage.setItem('cart', JSON.stringify(items.value))
  }

  const loadFromStorage = () => {
    const stored = localStorage.getItem('cart')
    if (stored) {
      try {
        items.value = JSON.parse(stored)
      } catch (e) {
        console.error('购物车数据解析失败', e)
      }
    }
  }

  return {
    items,
    addItem,
    removeItem,
    updateQuantity,
    clearCart,
    totalPrice,
    totalItems,
    loadFromStorage
  }
})