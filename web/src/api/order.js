import api from './index'

export const orderApi = {
  create: (data) => api.post('/orders', data),
  list: (params) => api.get('/orders', { params }),
  getById: (id) => api.get(`/orders/${id}`),
  getCount: () => api.get('/orders/admin/count'),
}
