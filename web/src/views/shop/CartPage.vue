<template>
  <div class="container page-section">
    <h2 style="margin-bottom:20px">
      <i class="fa fa-shopping-cart" style="color:var(--primary);margin-right:8px"></i>购物车
    </h2>

    <div v-if="cart.items.length === 0" style="text-align:center;padding:80px 0">
      <el-empty description="购物车是空的">
        <el-button type="primary" @click="$router.push('/')">去逛逛</el-button>
      </el-empty>
    </div>

    <div v-else class="cart-page">
      <div class="cart-list">
        <div v-for="item in cart.items" :key="item.productId" class="cart-item">
          <div class="item-main">
            <span class="item-name">{{ item.name }}</span>
            <span class="item-price">&yen;{{ item.price }}</span>
          </div>
          <div class="item-actions">
            <el-input-number
              :model-value="item.quantity"
              :min="1"
              :max="99"
              @change="(v) => cart.updateQuantity(item.productId, v)"
            />
            <span class="item-subtotal">&yen;{{ (item.price * item.quantity).toFixed(2) }}</span>
            <el-button type="danger" size="small" circle @click="cart.removeItem(item.productId)">
              <i class="fa fa-trash"></i>
            </el-button>
          </div>
        </div>
      </div>

      <el-card class="cart-summary">
        <div class="summary-row">
          <span>商品数量</span>
          <span>{{ cart.count }} 件</span>
        </div>
        <div class="summary-row">
          <span>合计</span>
          <span class="total-price">&yen;{{ cart.total }}</span>
        </div>
        <el-button type="primary" size="large" style="width:100%;margin-top:16px" @click="showOrderDialog = true">
          去结算
        </el-button>
      </el-card>
    </div>

    <!-- Order Dialog -->
    <el-dialog v-model="showOrderDialog" title="确认订单" width="500px">
      <el-form :model="orderForm" label-width="80px">
        <el-form-item label="收货地址">
          <el-input v-model="orderForm.address" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="orderForm.note" type="textarea" placeholder="订单备注（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showOrderDialog = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitOrder">确认下单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCartStore } from '../../stores/cart'
import { orderApi } from '../../api/order'

const router = useRouter()
const cart = useCartStore()
const showOrderDialog = ref(false)
const submitting = ref(false)

const orderForm = ref({ address: '', note: '' })

onMounted(() => cart.fetchCart())

async function submitOrder() {
  submitting.value = true
  try {
    const items = cart.items.map(i => ({ productId: i.productId, quantity: i.quantity }))
    await orderApi.create({ items, address: orderForm.value.address, note: orderForm.value.note })
    ElMessage.success('下单成功')
    showOrderDialog.value = false
    cart.clearCart()
    router.push('/orders')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.cart-page {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 24px;
}
@media (max-width: 768px) { .cart-page { grid-template-columns: 1fr; } }
.cart-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.cart-item {
  background: #fff;
  padding: 16px 20px;
  border-radius: var(--radius);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.item-main {
  display: flex;
  flex-direction: column;
}
.item-name { font-size: 15px; font-weight: 500; }
.item-price { font-size: 13px; color: var(--text-light); margin-top: 4px; }
.item-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}
.item-subtotal { font-weight: 600; min-width: 70px; text-align: right; }
.cart-summary {
  height: fit-content;
  position: sticky;
  top: 76px;
}
.summary-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 15px;
}
.total-price {
  font-size: 20px;
  font-weight: 700;
  color: var(--danger);
}
</style>
