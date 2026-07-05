<template>
  <div class="shop-layout">
    <!-- Announcement Bar -->
    <div class="announcement-bar">
      <div class="announce-text">全场满299包邮 · 新用户首单享9折优惠 · 限时特惠进行中</div>
    </div>

    <!-- Glass Nav -->
    <header class="nav-glass">
      <div class="nav-inner container">
        <router-link to="/" class="logo">
          <i class="fa fa-diamond" style="color:#ff6b6b;font-size:22px"></i>
          <span class="logo-text">TrendShop</span>
        </router-link>

        <div class="nav-right">
          <template v-if="auth.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :size="32" style="background:linear-gradient(135deg, #ff6b6b, #ee5a24);font-size:14px">
                  {{ auth.user?.username?.charAt(0)?.toUpperCase() }}
                </el-avatar>
                <span class="username">{{ auth.user?.username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="orders"><i class="fa fa-list-alt" style="margin-right:6px"></i>我的订单</el-dropdown-item>
                  <el-dropdown-item v-if="auth.isAdmin" command="admin"><i class="fa fa-cog" style="margin-right:6px"></i>管理后台</el-dropdown-item>
                  <el-dropdown-item command="logout" divided><i class="fa fa-sign-out" style="margin-right:6px"></i>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-badge :value="cart.count" :hidden="cart.count === 0" class="cart-badge" @click="$router.push('/cart')">
              <i class="fa fa-shopping-bag" style="font-size:20px;color:#333"></i>
            </el-badge>
          </template>
          <template v-else>
            <button class="btn-outline-sm" @click="$router.push('/login')">登录</button>
            <button class="btn-primary-sm" @click="$router.push('/register')">注册</button>
          </template>
        </div>
      </div>
    </header>

    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { useCartStore } from '../../stores/cart'

const router = useRouter()
const auth = useAuthStore()
const cart = useCartStore()

onMounted(() => {
  if (auth.isLoggedIn) cart.fetchCart()
})

function handleCommand(cmd) {
  if (cmd === 'orders') router.push('/orders')
  else if (cmd === 'admin') router.push('/admin')
  else if (cmd === 'logout') { auth.logout(); router.push('/') }
}
</script>

<style scoped>
/* Announcement Bar */
.announcement-bar {
  background: linear-gradient(90deg, #1a1a2e, #16213e, #0f3460, #16213e, #1a1a2e);
  background-size: 200% 100%;
  animation: shimmer 4s ease infinite;
  color: #fff;
  text-align: center;
  padding: 8px 0;
  font-size: 13px;
  letter-spacing: 0.5px;
}
@keyframes shimmer {
  0%, 100% { background-position: 0% 50%; }
  50%      { background-position: 100% 50%; }
}

/* Glass Nav */
.nav-glass {
  background: rgba(255,255,255,0.85);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0,0,0,0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}
.nav-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}
.logo-text {
  font-size: 22px;
  font-weight: 800;
  background: linear-gradient(135deg, #1a1a2e, #302b63);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.nav-right {
  display: flex;
  align-items: center;
  gap: 16px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}
.username { font-size: 14px; font-weight: 500; color: #333; }
.cart-badge { cursor: pointer; }

/* Buttons */
.btn-primary-sm {
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
  color: #fff;
  border: none;
  padding: 8px 20px;
  border-radius: 30px;
  font-weight: 600;
  font-size: 13px;
  cursor: pointer;
  box-shadow: 0 4px 14px rgba(238,90,36,0.3);
  transition: all 0.3s;
}
.btn-primary-sm:hover { transform: translateY(-1px); box-shadow: 0 6px 20px rgba(238,90,36,0.4); }

.btn-outline-sm {
  background: transparent;
  color: #555;
  border: 1.5px solid #ddd;
  padding: 8px 20px;
  border-radius: 30px;
  font-weight: 600;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.3s;
}
.btn-outline-sm:hover { border-color: #1a1a2e; color: #1a1a2e; }
</style>
