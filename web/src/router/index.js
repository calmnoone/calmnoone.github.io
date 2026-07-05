import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  {
    path: '/',
    component: () => import('../components/layout/ShopHeader.vue'),
    children: [
      { path: '', name: 'home', component: () => import('../views/shop/HomePage.vue'), meta: { title: 'TrendShop - 潮流商城' } },
      { path: 'product/:id', name: 'product', component: () => import('../views/shop/ProductDetail.vue'), meta: { title: '商品详情' } },
      { path: 'cart', name: 'cart', component: () => import('../views/shop/CartPage.vue'), meta: { auth: true, title: '购物车' } },
      { path: 'orders', name: 'orders', component: () => import('../views/shop/OrderPage.vue'), meta: { auth: true, title: '我的订单' } },
    ],
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/auth/LoginPage.vue'),
    meta: { guest: true, title: '登录' },
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/auth/RegisterPage.vue'),
    meta: { guest: true, title: '注册' },
  },
  {
    path: '/admin',
    component: () => import('../components/layout/AdminSidebar.vue'),
    meta: { auth: true, admin: true, title: '管理后台' },
    children: [
      { path: '', name: 'dashboard', component: () => import('../views/admin/DashboardPage.vue'), meta: { title: '控制台' } },
      { path: 'users', name: 'admin-users', component: () => import('../views/admin/UserListPage.vue'), meta: { title: '用户管理' } },
      { path: 'products', name: 'admin-products', component: () => import('../views/admin/ProductListPage.vue'), meta: { title: '商品管理' } },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()

  if (to.meta.auth && !auth.token) {
    return next('/login')
  }
  if (to.meta.guest && auth.token) {
    return next('/')
  }
  if (to.meta.admin && auth.user?.role !== 'admin') {
    return next('/')
  }

  next()
})

router.afterEach((to) => {
  document.title = to.meta.title || 'TrendShop'
})

export default router
