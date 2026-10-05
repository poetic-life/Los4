import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

const loadStoredUser = () => {
  const stored = localStorage.getItem('userInfo')
  if (stored && stored !== 'undefined') {
    try {
      return JSON.parse(stored)
    } catch (e) {
      return null
    }
  }
  return null
}

export const useUserStore = defineStore('user', () => {
  // 初始化时就从 localStorage 恢复登录态，避免刷新/直达个人中心时 userInfo 为空导致「我的主页」误跳到登录页
  const userInfo = ref(loadStoredUser())
  const token = ref(localStorage.getItem('token') || '')

  const setUserInfo = (info) => {
    userInfo.value = info
    if (info.token) {
      token.value = info.token
      localStorage.setItem('token', info.token)
    }
    if (info.userId) {
      localStorage.setItem('userId', info.userId)
    }
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const getUserInfo = () => {
    if (!userInfo.value) {
      const stored = localStorage.getItem('userInfo')
      if (stored && stored !== 'undefined') {
        try {
          userInfo.value = JSON.parse(stored)
        } catch (e) {
          console.error('用户信息解析失败', e)
        }
      }
    }
    return userInfo.value
  }

  const logout = () => {
    userInfo.value = null
    token.value = ''
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
  }

  const isLoggedIn = computed(() => !!token.value)

  return {
    userInfo,
    token,
    setUserInfo,
    getUserInfo,
    logout,
    isLoggedIn
  }
})