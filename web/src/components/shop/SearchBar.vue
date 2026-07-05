<template>
  <div class="search-bar">
    <el-input
      v-model="keyword"
      placeholder="搜索商品..."
      clearable
      size="large"
      @input="onInput"
      @clear="$emit('search', '')"
    >
      <template #prefix>
        <i class="fa fa-search" style="color:#c0c4cc"></i>
      </template>
    </el-input>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['search'])
const keyword = ref('')

let timer = null
function onInput() {
  clearTimeout(timer)
  timer = setTimeout(() => emit('search', keyword.value), 400)
}
</script>
