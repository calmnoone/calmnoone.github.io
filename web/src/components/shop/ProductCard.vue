<template>
  <div class="product-card" @click="$router.push(`/product/${product.id}`)">
    <div class="img-wrap">
      <img :src="product.image" :alt="product.name" class="card-img" />
      <span v-if="product.badge" class="badge" :class="product.badgeType === 'hot' ? 'badge-hot' : 'badge-new'">
        {{ product.badge }}
      </span>
      <div class="quick-view" @click.stop="$emit('add-cart', product)">
        <i class="fa fa-shopping-cart" style="margin-right:6px"></i>加入购物车
      </div>
    </div>
    <div class="card-body">
      <h3 class="card-name">{{ product.name }}</h3>
      <div class="card-meta">
        <span class="stars"><i class="fa fa-star"></i> {{ product.rating }}</span>
        <span class="reviews">{{ product.reviews }} 评论</span>
      </div>
      <div class="card-price">
        <span class="price">&yen;{{ product.price }}</span>
        <span v-if="product.originalPrice > product.price" class="original">&yen;{{ product.originalPrice }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({ product: Object })
defineEmits(['add-cart'])
</script>

<style scoped>
.product-card {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0,0,0,0.04);
}
.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 24px 60px rgba(0,0,0,0.12), 0 8px 20px rgba(0,0,0,0.06);
}

.img-wrap {
  position: relative;
  overflow: hidden;
  height: 220px;
  background: #f8f8fa;
  display: flex;
  align-items: center;
  justify-content: center;
}
.card-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}
.product-card:hover .card-img { transform: scale(1.08); }

.badge {
  position: absolute;
  top: 12px;
  right: 12px;
  font-size: 11px;
  padding: 4px 10px;
  border-radius: 4px;
  font-weight: 600;
  color: #fff;
}
.badge-hot { background: linear-gradient(135deg, #ff6b6b, #ee5a24); }
.badge-new { background: linear-gradient(135deg, #0be881, #05c46b); }

.quick-view {
  position: absolute;
  bottom: 12px;
  left: 50%;
  transform: translateX(-50%) translateY(20px);
  background: rgba(0,0,0,0.75);
  color: #fff;
  padding: 8px 20px;
  border-radius: 24px;
  font-size: 13px;
  white-space: nowrap;
  opacity: 0;
  transition: all 0.3s;
  cursor: pointer;
}
.product-card:hover .quick-view {
  opacity: 1;
  transform: translateX(-50%) translateY(0);
}
.quick-view:hover { background: rgba(0,0,0,0.9); }

.card-body { padding: 16px; }
.card-name {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.card-meta {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
}
.stars { color: #f7ba2a; }
.card-price { display: flex; align-items: baseline; gap: 8px; }
.price { font-size: 20px; font-weight: 700; color: #ee5a24; }
.original { font-size: 13px; color: #bbb; text-decoration: line-through; }
</style>
