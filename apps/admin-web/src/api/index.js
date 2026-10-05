import request from '@/utils/request'

// 认证
export const login = (data) => request.post('/auth/login', data)

// 仪表盘
export const getDashboard = () => request.get('/admin/dashboard')
export const getTrends = () => request.get('/admin/trends')

// 用户管理
export const getAdminUsers = () => request.get('/admin/users')
export const updateUserRole = (id, role) => request.put(`/admin/users/${id}/role`, { role })
export const deleteUser = (id) => request.delete(`/admin/users/${id}`)

// 帖子管理
export const getAdminPosts = () => request.get('/admin/posts')
export const deletePost = (id) => request.delete(`/admin/posts/${id}`)

// 评论管理
export const getAdminComments = () => request.get('/admin/comments')
export const deleteComment = (id) => request.delete(`/admin/comments/${id}`)

// 商品管理
export const getAdminProducts = () => request.get('/admin/products')
export const createProduct = (data) => request.post('/admin/products', data)
export const updateProduct = (id, data) => request.put(`/admin/products/${id}`, data)
export const deleteProduct = (id) => request.delete(`/admin/products/${id}`)

// 订单管理
export const getAdminOrders = () => request.get('/admin/orders')
export const shipOrderAdmin = (id) => request.post(`/admin/orders/${id}/ship`)
export const cancelOrderAdmin = (id) => request.post(`/admin/orders/${id}/cancel`)