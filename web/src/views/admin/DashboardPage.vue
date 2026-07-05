<template>
  <div>
    <h3 style="margin-bottom:16px">控制台</h3>
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card>
          <div class="stat">
            <i class="fa fa-users" style="font-size:32px;color:#409eff"></i>
            <div>
              <div class="stat-value">{{ stats.users }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat">
            <i class="fa fa-cubes" style="font-size:32px;color:#27ae60"></i>
            <div>
              <div class="stat-value">{{ stats.products }}</div>
              <div class="stat-label">商品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat">
            <i class="fa fa-list-alt" style="font-size:32px;color:#e67e22"></i>
            <div>
              <div class="stat-value">{{ stats.orders }}</div>
              <div class="stat-label">订单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '../../api/user'
import { productApi } from '../../api/product'
import { orderApi } from '../../api/order'

const stats = ref({ users: 0, products: 0, orders: 0 })

onMounted(async () => {
  try {
    const [userRes, prodRes, orderRes] = await Promise.all([
      userApi.list({ page: 1, pageSize: 1 }),
      productApi.getCount(),
      orderApi.getCount(),
    ])
    stats.value.users = userRes.data?.pagination?.total || 0
    stats.value.products = prodRes.data?.count || 0
    stats.value.orders = orderRes.data?.count || 0
  } catch {}
})
</script>

<style scoped>
.stat {
  display: flex;
  align-items: center;
  gap: 16px;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
}
.stat-label {
  font-size: 14px;
  color: var(--text-light);
}
</style>
