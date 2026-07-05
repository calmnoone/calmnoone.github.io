import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '../api/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('trendshop_token') || '')
  const user = ref(JSON.parse(localStorage.getItem('trendshop_user') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'admin')

  function setAuth(t, u) {
    token.value = t
    user.value = u
    localStorage.setItem('trendshop_token', t)
    localStorage.setItem('trendshop_user', JSON.stringify(u))
  }

  function clearAuth() {
    token.value = ''
    user.value = null
    localStorage.removeItem('trendshop_token')
    localStorage.removeItem('trendshop_user')
  }

  async function login(username, password) {
    const res = await authApi.login({ username, password })
    setAuth(res.data.token, res.data.user)
    return res.data.user
  }

  async function register(form) {
    const res = await authApi.register(form)
    return res
  }

  function logout() {
    clearAuth()
  }

  return { token, user, isLoggedIn, isAdmin, login, register, logout, setAuth, clearAuth }
})
