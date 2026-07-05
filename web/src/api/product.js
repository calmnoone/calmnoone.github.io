import api from './index'

export const productApi = {
  list: (params) => api.get('/products', { params }),
  getById: (id) => api.get(`/products/${id}`),
  getCategories: () => api.get('/products/categories'),
  update: (id, data) => api.put(`/products/${id}`, data),
  getCount: () => api.get('/products/admin/count'),
}
