import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import AdminLayout from '@/views/AdminLayout.vue'
import Dashboard from '@/views/Dashboard.vue'
import Profile from '@/views/Profile.vue'
import UsersManage from '@/views/UsersManage.vue'
import PostsManage from '@/views/PostsManage.vue'
import CommentsManage from '@/views/CommentsManage.vue'
import ProductsManage from '@/views/ProductsManage.vue'
import OrdersManage from '@/views/OrdersManage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: Login },
    {
      path: '/',
      component: AdminLayout,
      redirect: '/dashboard',
      children: [
        { path: '/dashboard', component: Dashboard },
        { path: '/profile', component: Profile },
        { path: '/users', component: UsersManage },
        { path: '/posts', component: PostsManage },
        { path: '/comments', component: CommentsManage },
        { path: '/products', component: ProductsManage },
        { path: '/orders', component: OrdersManage }
      ]
    }
  ]
})

router.beforeEach((to) => {
  const token = localStorage.getItem('admin_token')
  if (to.path !== '/login' && !token) {
    return '/login'
  }
  if (to.path === '/login' && token) {
    return '/users'
  }
  return true
})

export default router