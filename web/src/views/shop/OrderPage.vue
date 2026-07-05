<template>
  <div class="container page-section">
    <h2 style="margin-bottom:20px">
      <i class="fa fa-list-alt" style="color:var(--primary);margin-right:8px"></i>我的订单
    </h2>

    <div v-if="orders.length === 0">
      <el-empty description="暂无订单">
        <el-button type="primary" @click="$router.push('/')">去逛逛</el-button>
      </el-empty>
    </div>

    <div v-else class="order-list">
      <el-card v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span>订单号: {{ order.id }}</span>
          <el-tag :type="order.status === 'pending' ? 'warning' : 'success'" size="small">
            {{ order.status === 'pending' ? '待处理' : order.status }}
          </el-tag>
          <span class="order-date">{{ order.createdAt }}</span>
        </div>

        <el-table :data="order.items" style="margin-top:12px">
          <el-table-column prop="name" label="商品" />
          <el-table-column prop="price" label="单价" width="100">
            <template #default="{ row }">&yen;{{ row.price }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column label="小计" width="100">
            <template #default="{ row }">&yen;{{ row.subtotal }}</template>
          </el-table-column>
        </el-table>

        <div class="order-footer">
          <span v-if="order.address"><i class="fa fa-map-marker"></i> {{ order.address }}</span>
          <span class="order-total">合计: <strong>&yen;{{ order.total }}</strong></span>
        </div>
      </el-card>

      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination
          v-model:current-page="page"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="loadOrders"
          background
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { orderApi } from '../../api/order'

const orders = ref([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

async function loadOrders() {
  const res = await orderApi.list({ page: page.value, pageSize: pageSize.value })
  orders.value = res.data.list || []
  total.value = res.data.pagination?.total || 0
}

onMounted(loadOrders)
</script>

<style scoped>
.order-card {
  margin-bottom: 16px;
}
.order-header {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
}
.order-date {
  margin-left: auto;
  color: var(--text-light);
  font-size: 13px;
}
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid var(--border);
  font-size: 14px;
}
.order-total strong {
  color: var(--danger);
  font-size: 18px;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
