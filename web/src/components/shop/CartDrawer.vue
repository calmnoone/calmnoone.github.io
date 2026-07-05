<template>
  <el-drawer v-model="visible" title="购物车" size="400px" direction="rtl">
    <div v-if="cart.items.length === 0" class="empty-cart">
      <i class="fa fa-shopping-cart" style="font-size:48px;color:#dcdfe6"></i>
      <p style="margin-top:12px;color:var(--text-light)">购物车是空的</p>
    </div>
    <div v-else class="cart-items">
      <div v-for="item in cart.items" :key="item.productId" class="cart-item">
        <div class="item-info">
          <span class="item-name">{{ item.name }}</span>
          <span class="item-price">&yen;{{ item.price }} x {{ item.quantity }}</span>
        </div>
        <div class="item-actions">
          <el-input-number
            :model-value="item.quantity"
            :min="1"
            :max="99"
            size="small"
            @change="(v) => cart.updateQuantity(item.productId, v)"
          />
          <el-button type="danger" size="small" circle @click="cart.removeItem(item.productId)">
            <i class="fa fa-trash"></i>
          </el-button>
        </div>
      </div>
      <el-divider />
      <div class="cart-footer">
        <span>合计: <strong style="color:var(--danger);font-size:18px">&yen;{{ cart.total }}</strong></span>
        <el-button type="primary" @click="$router.push('/cart')">去结算</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<script setup>
import { computed } from 'vue'
import { useCartStore } from '../../stores/cart'

const props = defineProps({ modelValue: Boolean })
const emit = defineEmits(['update:modelValue'])
const cart = useCartStore()

const visible = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v),
})
</script>

<style scoped>
.empty-cart {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 120px;
}
.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--border);
}
.item-info {
  display: flex;
  flex-direction: column;
}
.item-name { font-size: 14px; font-weight: 500; }
.item-price { font-size: 12px; color: var(--text-light); margin-top: 4px; }
.item-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}
.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
