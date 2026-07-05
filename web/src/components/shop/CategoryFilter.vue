<template>
  <div class="category-filter">
    <el-tag
      v-for="cat in categories"
      :key="cat"
      :type="cat === modelValue ? 'primary' : 'info'"
      :effect="cat === modelValue ? 'dark' : 'plain'"
      class="cat-tag"
      @click="$emit('update:modelValue', cat === modelValue ? '' : cat)"
    >
      {{ cat }}
    </el-tag>
  </div>
</template>

<script setup>
defineProps({
  categories: Array,
  modelValue: String,
})
defineEmits(['update:modelValue'])
</script>

<style scoped>
.category-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.cat-tag {
  cursor: pointer;
}
</style>
