<template>
  <div class="auth-page">
    <div class="auth-card">
      <h2><i class="fa fa-diamond" style="color:#ff6b6b;margin-right:8px"></i>TrendShop</h2>
      <p style="color:#999;text-align:center;margin-bottom:28px">登录你的账号</p>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleLogin">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="auth-link">还没有账号？<router-link to="/register">立即注册</router-link></div>
      <div class="auth-link" style="margin-top:8px"><router-link to="/">← 返回商城</router-link></div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const auth = useAuthStore()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try { await auth.login(form.username, form.password); ElMessage.success('登录成功'); router.push('/') }
  finally { loading.value = false }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
}
.auth-card {
  width: 400px; background: #fff; padding: 40px;
  border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.2);
}
.auth-card h2 { text-align: center; font-size: 24px; font-weight: 800; }
.auth-link { text-align: center; font-size: 14px; color: #888; }
.auth-link a { color: #ee5a24; font-weight: 500; }
</style>
