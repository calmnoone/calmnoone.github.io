<template>
  <div class="container page-section">
    <div v-if="product" class="detail">
      <div class="detail-image">
        <img :src="product.image" :alt="product.name" />
      </div>
      <div class="detail-info">
        <span v-if="product.badge" class="badge" :class="product.badgeType === 'hot' ? 'badge-hot' : 'badge-new'">
          {{ product.badge }}
        </span>
        <h1>{{ product.name }}</h1>
        <div class="meta">
          <span class="stars"><i class="fa fa-star"></i> {{ product.rating }}</span>
          <span>{{ product.reviews }} 评论</span>
          <span>库存: {{ product.stock }}</span>
        </div>
        <div class="price-row">
          <span class="price">&yen;{{ product.price }}</span>
          <span v-if="product.originalPrice > product.price" class="original">&yen;{{ product.originalPrice }}</span>
        </div>
        <p class="desc">{{ product.description }}</p>
        <div class="actions">
          <el-input-number v-model="quantity" :min="1" :max="product.stock" size="large" />
          <button class="add-btn" @click="addToCart"><i class="fa fa-shopping-cart"></i> 加入购物车</button>
          <button class="wish-btn" @click="toggleWishlist">
            <i :class="['fa', isWishlisted ? 'fa-heart' : 'fa-heart-o']" :style="{ color: isWishlisted ? '#ee5a24' : '' }"></i>
          </button>
        </div>
      </div>
    </div>
    <div v-else style="text-align:center;padding:80px 0">
      <el-skeleton :rows="5" animated />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { productApi } from '../../api/product'
import { wishlistApi } from '../../api/wishlist'
import { useAuthStore } from '../../stores/auth'
import { useCartStore } from '../../stores/cart'

const route = useRoute()
const auth = useAuthStore()
const cart = useCartStore()

const product = ref(null)
const quantity = ref(1)
const isWishlisted = ref(false)

async function loadProduct() {
  const res = await productApi.getById(route.params.id)
  product.value = res.data
}

async function addToCart() {
  if (!auth.isLoggedIn) { ElMessage.warning('请先登录'); return }
  await cart.addToCart(product.value.id, quantity.value)
  ElMessage.success('已添加到购物车')
}

async function toggleWishlist() {
  if (!auth.isLoggedIn) { ElMessage.warning('请先登录'); return }
  if (isWishlisted.value) {
    await wishlistApi.remove(product.value.id)
    isWishlisted.value = false
    ElMessage.success('已取消收藏')
  } else {
    await wishlistApi.add(product.value.id)
    isWishlisted.value = true
    ElMessage.success('已收藏')
  }
}

onMounted(loadProduct)
</script>

<style scoped>
.detail {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 48px;
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}
@media (max-width: 768px) { .detail { grid-template-columns: 1fr; } }
.detail-image {
  background: #f8f8fa;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
}
.detail-image img { width: 100%; height: 100%; object-fit: cover; }
.detail-info { padding: 40px; }
.badge {
  display: inline-block;
  font-size: 11px; padding: 4px 10px; border-radius: 4px;
  font-weight: 600; color: #fff; margin-bottom: 12px;
}
.badge-hot { background: linear-gradient(135deg, #ff6b6b, #ee5a24); }
.badge-new { background: linear-gradient(135deg, #0be881, #05c46b); }
.detail-info h1 { font-size: 26px; font-weight: 700; margin-bottom: 12px; }
.meta { display: flex; gap: 16px; font-size: 14px; color: #999; margin-bottom: 16px; }
.stars { color: #f7ba2a; }
.price-row { display: flex; align-items: baseline; gap: 12px; margin-bottom: 20px; }
.price { font-size: 32px; font-weight: 800; color: #ee5a24; }
.original { font-size: 16px; color: #bbb; text-decoration: line-through; }
.desc { color: #666; line-height: 1.8; margin-bottom: 28px; }
.actions { display: flex; gap: 12px; align-items: center; }
.add-btn {
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
  color: #fff; border: none;
  padding: 12px 32px; border-radius: 30px;
  font-weight: 600; font-size: 15px; cursor: pointer;
  box-shadow: 0 4px 20px rgba(238,90,36,0.3);
  transition: all 0.3s;
  display: flex; align-items: center; gap: 6px;
}
.add-btn:hover { transform: translateY(-2px); box-shadow: 0 8px 30px rgba(238,90,36,0.45); }
.wish-btn {
  width: 46px; height: 46px;
  border-radius: 50%; border: 1.5px solid #e0e0e0;
  background: #fff; cursor: pointer;
  font-size: 18px; transition: all 0.3s;
  display: flex; align-items: center; justify-content: center;
}
.wish-btn:hover { border-color: #ee5a24; }
</style>
