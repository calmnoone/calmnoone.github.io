<template>
  <div>
    <h3 style="margin-bottom:16px">用户管理</h3>

    <div class="filters">
      <el-input v-model="keyword" placeholder="搜索用户名/邮箱" clearable style="width:220px" @input="search" />
      <el-select v-model="role" placeholder="角色" clearable style="width:120px" @change="loadUsers">
        <el-option label="管理员" value="admin" />
        <el-option label="用户" value="user" />
      </el-select>
      <el-select v-model="status" placeholder="状态" clearable style="width:120px" @change="loadUsers">
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
    </div>

    <el-table :data="users" stripe style="margin-top:12px">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="role" label="角色" width="80">
        <template #default="{ row }">
          <el-tag :type="row.role === 'admin' ? 'danger' : 'info'" size="small">
            {{ row.role === 'admin' ? '管理员' : '用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-popconfirm title="确定删除该用户？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap" v-if="total > 0">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadUsers"
        background
      />
    </div>

    <!-- Edit Dialog -->
    <el-dialog v-model="dialogVisible" title="编辑用户" width="450px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="editForm.role" style="width:100%">
            <el-option label="管理员" value="admin" />
            <el-option label="用户" value="user" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editForm.status" style="width:100%">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { userApi } from '../../api/user'

const users = ref([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const keyword = ref('')
const role = ref('')
const status = ref('')

const dialogVisible = ref(false)
const editForm = ref({})
const saving = ref(false)

let searchTimer = null

async function loadUsers() {
  const res = await userApi.list({
    page: page.value,
    pageSize: pageSize.value,
    keyword: keyword.value || undefined,
    role: role.value || undefined,
    status: status.value !== '' ? status.value : undefined,
  })
  users.value = res.data.list || []
  total.value = res.data.pagination?.total || 0
}

function search() {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(loadUsers, 400)
}

function openEdit(row) {
  editForm.value = { ...row }
  dialogVisible.value = true
}

async function saveEdit() {
  saving.value = true
  try {
    await userApi.update(editForm.value.id, editForm.value)
    ElMessage.success('更新成功')
    dialogVisible.value = false
    loadUsers()
  } finally {
    saving.value = false
  }
}

async function handleDelete(id) {
  await userApi.delete(id)
  ElMessage.success('删除成功')
  loadUsers()
}

onMounted(loadUsers)
</script>

<style scoped>
.filters {
  display: flex;
  gap: 12px;
}
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
