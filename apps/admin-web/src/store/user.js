import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('admin-user', () => {
  const userInfo = ref(JSON.parse(localStorage.getItem('admin_user') || 'null'))

  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('admin_token', info.token)
    localStorage.setItem('admin_user', JSON.stringify(info))
  }

  const logout = () => {
    userInfo.value = null
    localStorage.removeItem('admin_token')
    localStorage.removeItem('admin_user')
  }

  return { userInfo, setUserInfo, logout }
})