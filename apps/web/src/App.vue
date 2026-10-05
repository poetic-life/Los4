<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useCartStore } from '@/store/modules/cart'

const cartStore = useCartStore()

onMounted(() => {
  cartStore.loadFromStorage()
})
</script>

<style scoped>
</style>