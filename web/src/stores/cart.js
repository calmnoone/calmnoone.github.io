import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { cartApi } from '../api/cart'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])
  const total = ref(0)
  const count = computed(() => items.value.length)

  async function fetchCart() {
    try {
      const res = await cartApi.getCart()
      items.value = res.data.items || []
      total.value = res.data.total || 0
    } catch {
      items.value = []
      total.value = 0
    }
  }

  async function addToCart(productId, quantity = 1) {
    const res = await cartApi.addToCart(productId, quantity)
    items.value = res.data.items || []
    total.value = res.data.total || 0
  }

  async function updateQuantity(productId, quantity) {
    const res = await cartApi.updateCartItem(productId, quantity)
    items.value = res.data.items || []
    total.value = res.data.total || 0
  }

  async function removeItem(productId) {
    const res = await cartApi.removeCartItem(productId)
    items.value = res.data.items || []
    total.value = res.data.total || 0
  }

  async function clearCart() {
    const res = await cartApi.clearCart()
    items.value = []
    total.value = 0
  }

  return { items, total, count, fetchCart, addToCart, updateQuantity, removeItem, clearCart }
})
