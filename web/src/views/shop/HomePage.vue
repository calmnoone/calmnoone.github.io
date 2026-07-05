<template>
  <div class="home-page">
    <!-- Hero -->
    <section class="hero">
      <div class="hero-content container">
        <h1 class="hero-title">发现你的潮流风格</h1>
        <p class="hero-sub">精选好物，品质生活从这里开始</p>
        <div class="hero-btns">
          <button class="hero-btn-primary" @click="scrollToProducts">立即选购</button>
          <button class="hero-btn-outline" @click="$router.push('/orders')">查看订单</button>
        </div>
      </div>
    </section>

    <!-- Products Section -->
    <section class="container page-section" id="products-section">
      <!-- Search & Sort -->
      <div class="toolbar">
        <div class="search-wrap">
          <i class="fa fa-search"></i>
          <input v-model="keyword" placeholder="搜索商品..." @input="onSearch" />
        </div>
        <select v-model="sort" class="sort-select" @change="loadProducts">
          <option value="">默认排序</option>
          <option value="price-asc">价格从低到高</option>
          <option value="price-desc">价格从高到低</option>
          <option value="rating">评分最高</option>
          <option value="sales">销量最高</option>
        </select>
      </div>

      <!-- Categories -->
      <div class="category-chips">
        <button
          v-for="cat in categories"
          :key="cat"
          class="chip"
          :class="{ active: category === (cat === '全部' ? '' : cat) }"
          @click="category = (cat === '全部' ? '' : cat); loadProducts()"
        >
          {{ cat }}
        </button>
      </div>

      <!-- Products Grid -->
      <div v-if="products.length > 0" class="product-grid">
        <ProductCard
          v-for="p in products"
          :key="p.id"
          :product="p"
          @add-cart="handleAddToCart"
        />
      </div>
      <el-empty v-else description="暂无商品" />

      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination
          v-model:current-page="page"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="loadProducts"
          background
        />
      </div>
    </section>

    <CartDrawer v-model="cartVisible" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { productApi } from '../../api/product'
import { useAuthStore } from '../../stores/auth'
import { useCartStore } from '../../stores/cart'
import ProductCard from '../../components/shop/ProductCard.vue'
import CartDrawer from '../../components/shop/CartDrawer.vue'

const auth = useAuthStore()
const cart = useCartStore()

const products = ref([])
const categories = ref([])
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)
const category = ref('')
const keyword = ref('')
const sort = ref('')
const cartVisible = ref(false)

async function loadProducts() {
  const res = await productApi.list({
    page: page.value, pageSize: pageSize.value,
    category: category.value || undefined,
    keyword: keyword.value || undefined,
    sort: sort.value || undefined,
  })
  products.value = res.data.list || []
  total.value = res.data.pagination?.total || 0
}

async function loadCategories() {
  const res = await productApi.getCategories()
  categories.value = ['全部', ...res.data]
}

let searchTimer = null
function onSearch() {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(loadProducts, 400)
}

function handleAddToCart(product) {
  if (!auth.isLoggedIn) { ElMessage.warning('请先登录'); return }
  cart.addToCart(product.id, 1).then(() => { cartVisible.value = true })
}

function scrollToProducts() {
  document.getElementById('products-section')?.scrollIntoView({ behavior: 'smooth' })
}

onMounted(() => { loadProducts(); loadCategories() })
</script>

<style scoped>
/* Hero */
.hero {
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
  position: relative;
  overflow: hidden;
  padding: 100px 0;
  text-align: center;
}
.hero::before {
  content: '';
  position: absolute;
  width: 600px; height: 600px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255,107,107,0.3) 0%, transparent 70%);
  top: -200px; right: -100px;
  animation: floatBubble 8s ease-in-out infinite;
}
.hero::after {
  content: '';
  position: absolute;
  width: 400px; height: 400px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(78,205,196,0.25) 0%, transparent 70%);
  bottom: -100px; left: -50px;
  animation: floatBubble 10s ease-in-out infinite reverse;
}
@keyframes floatBubble {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33%      { transform: translate(30px, -20px) scale(1.1); }
  66%      { transform: translate(-20px, 15px) scale(0.9); }
}
.hero-content { position: relative; z-index: 1; }
.hero-title { font-size: 48px; font-weight: 800; color: #fff; margin-bottom: 16px; letter-spacing: -1px; }
.hero-sub { font-size: 18px; color: rgba(255,255,255,0.7); margin-bottom: 36px; }
.hero-btns { display: flex; gap: 16px; justify-content: center; }
.hero-btn-primary {
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
  color: #fff; border: none;
  padding: 14px 36px; border-radius: 30px;
  font-weight: 600; font-size: 15px; cursor: pointer;
  box-shadow: 0 4px 20px rgba(238,90,36,0.3);
  transition: all 0.3s;
}
.hero-btn-primary:hover { transform: translateY(-2px); box-shadow: 0 8px 30px rgba(238,90,36,0.45); }
.hero-btn-outline {
  background: transparent; color: #fff;
  border: 2px solid #fff;
  padding: 14px 36px; border-radius: 30px;
  font-weight: 600; font-size: 15px; cursor: pointer;
  transition: all 0.3s;
}
.hero-btn-outline:hover { background: #fff; color: #1a1a2e; }

/* Toolbar */
.toolbar { display: flex; gap: 12px; margin-bottom: 24px; }
.search-wrap {
  flex: 1; display: flex; align-items: center; gap: 10px;
  background: #fff; border: 1px solid #e0e0e0;
  border-radius: 30px; padding: 10px 20px;
  transition: all 0.3s;
}
.search-wrap:focus-within { border-color: #1a1a2e; box-shadow: 0 0 0 3px rgba(26,26,46,0.06); }
.search-wrap i { color: #aaa; }
.search-wrap input {
  flex: 1; border: none; outline: none;
  font-size: 14px; font-family: inherit; background: transparent;
}
.sort-select {
  padding: 10px 16px; border: 1px solid #e0e0e0;
  border-radius: 30px; font-size: 14px;
  background: #fff; cursor: pointer; outline: none;
  font-family: inherit;
}

/* Category Chips */
.category-chips { display: flex; flex-wrap: wrap; gap: 10px; margin-bottom: 32px; }
.chip {
  padding: 8px 22px; border-radius: 24px;
  font-size: 14px; font-weight: 500; cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e0e0e0; background: #fff; color: #666;
  font-family: inherit;
}
.chip:hover, .chip.active { background: #1a1a2e; color: #fff; border-color: #1a1a2e; }

/* Product Grid */
.product-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 24px; }
@media (max-width: 1024px) { .product-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 768px) { .product-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 480px) { .product-grid { grid-template-columns: 1fr; } }
.pagination-wrap { display: flex; justify-content: center; margin-top: 40px; }
</style>
