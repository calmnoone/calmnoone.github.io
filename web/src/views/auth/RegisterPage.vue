<template>
  <div class="auth-page">
    <div class="auth-card">
      <h2><i class="fa fa-user-plus" style="color:#ff6b6b;margin-right:8px"></i>注册</h2>
      <p style="color:#999;text-align:center;margin-bottom:28px">创建你的 TrendShop 账号</p>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名（3-20位）" size="large" />
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" placeholder="邮箱" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码（6-32位）" size="large" show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleRegister">注 册</el-button>
        </el-form-item>
      </el-form>
      <div class="auth-link">已有账号？<router-link to="/login">立即登录</router-link></div>
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
const form = reactive({ username: '', email: '', password: '', confirmPassword: '' })

const rules = {
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度需要3-20位', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 6, max: 32, message: '密码长度需要6-32位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (r, v, cb) => v !== form.password ? cb(new Error('两次密码输入不一致')) : cb(), trigger: 'blur' },
  ],
}

async function handleRegister() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await auth.register({ username: form.username, email: form.email, password: form.password })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } finally { loading.value = false }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
}
.auth-card {
  width: 420px; background: #fff; padding: 40px;
  border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.2);
}
.auth-card h2 { text-align: center; font-size: 24px; font-weight: 800; }
.auth-link { text-align: center; font-size: 14px; color: #888; }
.auth-link a { color: #ee5a24; font-weight: 500; }
</style>
