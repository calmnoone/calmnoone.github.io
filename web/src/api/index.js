import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('trendshop_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const msg = error.response?.data?.message || '请求失败'
    if (error.response?.status === 401) {
      localStorage.removeItem('trendshop_token')
      localStorage.removeItem('trendshop_user')
      window.location.href = '/login'
    }
    ElMessage.error(msg)
    return Promise.reject(error)
  }
)

export default api
