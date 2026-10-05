import axios from 'axios'
import { ElMessage } from 'element-plus'

// 统一 axios 实例：自动携带 JWT、统一解包 Result 响应
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截：附加 token
request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截：解包 { code, message, data }
request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res && res.code === 200) {
      return res.data
    }
    const msg = (res && res.message) || '请求失败'
    ElMessage.error(msg)
    return Promise.reject(new Error(msg))
  },
  (error) => {
    const res = error.response && error.response.data
    const msg = (res && res.message) || error.message || '网络异常，请稍后重试'
    ElMessage.error(msg)
    return Promise.reject(error)
  }
)

export default request