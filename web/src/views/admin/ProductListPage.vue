<template>
  <div>
    <h3 style="margin-bottom:16px">商品管理</h3>

    <el-table :data="products" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="image" label="图片" width="80">
        <template #default="{ row }">
          <img :src="row.image" style="width:40px;height:40px;border-radius:4px;object-fit:cover" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="category" label="分类" width="80" />
      <el-table-column prop="price" label="价格" width="90">
        <template #default="{ row }">&yen;{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="70" />
      <el-table-column prop="rating" label="评分" width="70" />
      <el-table-column prop="badge" label="标签" width="80">
        <template #default="{ row }">
          <el-tag v-if="row.badge" :type="row.badgeType === 'hot' ? 'danger' : 'success'" size="small">
            {{ row.badge }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="80">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap" v-if="total > 0">
      <el-pagination
        v-model:current-page="page" :page-size="pageSize" :total="total"
        layout="prev, pager, next" @current-change="loadProducts" background
      />
    </div>

    <!-- Edit Dialog -->
    <el-dialog v-model="visible" title="编辑商品" width="550px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width:100%">
            <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
          </el-select>
        </el-form-item>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="价格">
              <el-input-number v-model="form.price" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原价">
              <el-input-number v-model="form.originalPrice" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="库存">
              <el-input-number v-model="form.stock" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="图片URL">
              <el-input v-model="form.image" placeholder="/images/products/xxx.svg" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" />
        </el-form-item>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="标签文字">
              <el-input v-model="form.badge" placeholder="热卖/新品" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签类型">
              <el-select v-model="form.badgeType" style="width:100%">
                <el-option label="热卖" value="hot" />
                <el-option label="新品" value="new" />
                <el-option label="无" value="" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { productApi } from '../../api/product'

const products = ref([])
const categories = ref([])
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)

const visible = ref(false)
const form = ref({})
const saving = ref(false)

async function loadProducts() {
  const res = await productApi.list({ page: page.value, pageSize: pageSize.value })
  products.value = res.data.list || []
  total.value = res.data.pagination?.total || 0
}

async function loadCategories() {
  const res = await productApi.getCategories()
  categories.value = res.data || []
}

function openEdit(row) {
  form.value = { ...row }
  visible.value = true
}

async function save() {
  saving.value = true
  try {
    await productApi.update(form.value.id, form.value)
    ElMessage.success('更新成功')
    visible.value = false
    loadProducts()
  } finally { saving.value = false }
}

onMounted(() => { loadProducts(); loadCategories() })
</script>

<style scoped>
.pagination-wrap { display: flex; justify-content: center; margin-top: 20px; }
</style>
