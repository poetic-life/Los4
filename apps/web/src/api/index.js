import request from '@/utils/request'

// ---------- 认证 ----------
export const login = (data) => request.post('/auth/login', data)
export const register = (data) => request.post('/auth/register', data)

// ---------- 用户 ----------
export const getMe = () => request.get('/user/me')
export const updateProfile = (data) => request.put('/user/me', data)
export const changePassword = (data) => request.put('/user/password', data)
export const getUserProfile = (id) => request.get(`/user/${id}`)

// ---------- 商品 ----------
export const getProducts = (params) => request.get('/products', { params })
export const getProductDetail = (id) => request.get(`/products/${id}`)

// ---------- 新闻 ----------
export const getNews = (params) => request.get('/news', { params })

// ---------- 球员 ----------
export const getPlayers = (season) => request.get('/players', { params: season ? { season } : {} })
export const getPlayerDetail = (id) => request.get(`/players/${id}`)

// ---------- 赛程 ----------
export const getSchedule = (season) => request.get('/schedule', { params: season ? { season } : {} })

// ---------- 赛季 ----------
export const getSeasons = () => request.get('/seasons')

// ---------- 帮助中心 FAQ ----------
export const getFaqs = () => request.get('/faqs')

// ---------- 购物车 ----------
export const getCart = () => request.get('/carts')
export const addCart = (data) => request.post('/carts', data)
export const updateCart = (id, quantity) => request.put(`/carts/${id}`, null, { params: { quantity } })
export const removeCart = (id) => request.delete(`/carts/${id}`)
export const clearCart = () => request.delete('/carts')

// ---------- 地址 ----------
export const getAddresses = () => request.get('/addresses')
export const addAddress = (data) => request.post('/addresses', data)
export const updateAddress = (id, data) => request.put(`/addresses/${id}`, data)
export const removeAddress = (id) => request.delete(`/addresses/${id}`)

// ---------- 收藏 ----------
export const getFavorites = () => request.get('/favorites')
export const addFavorite = (productId) => request.post('/favorites', { productId })
export const removeFavorite = (productId) => request.delete(`/favorites/${productId}`)

// ---------- 订单 ----------
export const getOrders = () => request.get('/orders')
export const createOrder = (data) => request.post('/orders', data)
export const getOrderDetail = (id) => request.get(`/orders/${id}`)
export const shipOrder = (id) => request.post(`/orders/${id}/ship`)
export const confirmOrder = (id) => request.post(`/orders/${id}/confirm`)
export const cancelOrder = (id) => request.post(`/orders/${id}/cancel`)

// ---------- 社区 ----------
export const getPosts = () => request.get('/community/posts')
export const createPost = (data) => request.post('/community/posts', data)
export const likePost = (id) => request.post(`/community/posts/${id}/like`)
export const getComments = (id) => request.get(`/community/posts/${id}/comments`)
export const addComment = (id, data) => request.post(`/community/posts/${id}/comments`, data)
export const getUserPosts = (id) => request.get(`/community/posts/user/${id}`)

// ---------- 私信 ----------
export const getConversations = () => request.get('/messages/conversations')
export const getMessages = (userId) => request.get(`/messages/${userId}`)
export const sendMessage = (data) => request.post('/messages', data)

// ---------- 反馈 ----------
export const submitFeedback = (data) => request.post('/feedbacks', data)