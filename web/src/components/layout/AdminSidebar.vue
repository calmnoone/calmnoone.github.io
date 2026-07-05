<template>
  <div class="admin-layout">
    <el-container style="min-height:100vh">
      <el-aside width="220px" style="background:#304156">
        <div class="admin-logo">
          <i class="fa fa-diamond" style="color:#409eff;margin-right:8px"></i>
          <span>TrendShop 后台</span>
        </div>
        <el-menu
          :default-active="route.path"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
          router
        >
          <el-menu-item index="/admin">
            <el-icon><i class="fa fa-dashboard"></i></el-icon>
            <span>控制台</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><i class="fa fa-users"></i></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/products">
            <el-icon><i class="fa fa-cubes"></i></el-icon>
            <span>商品管理</span>
          </el-menu-item>
          <el-menu-item index="/" style="margin-top:40px">
            <el-icon><i class="fa fa-arrow-left"></i></el-icon>
            <span>返回商城</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="background:#fff;display:flex;align-items:center;justify-content:space-between;box-shadow:0 1px 4px rgba(0,0,0,0.06)">
          <span style="font-size:16px;font-weight:600">管理后台</span>
          <el-button @click="auth.logout();$router.push('/')">退出</el-button>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { useAuthStore } from '../../stores/auth'

const route = useRoute()
const auth = useAuthStore()
</script>

<style scoped>
.admin-logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
.el-menu {
  border-right: none;
}
.el-menu-item i {
  margin-right: 8px;
}
</style>
