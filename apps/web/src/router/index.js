import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/views/LoginPage.vue'
import MemberCenter from '@/views/MemberCenter.vue'
import Center from '@/views/Center.vue'
import Service from '@/views/Service.vue'
import Version from '@/views/Version.vue'
import Orders from '@/views/Orders.vue'
import HomePage from '@/views/viewsSecond/HomePage.vue'
import CommunityPage from '@/views/viewsSecond/CommunityPage.vue'
import NewsPage from '@/views/viewsSecond/NewsPage.vue'
import SchedulePage from '@/views/viewsSecond/SchedulePage.vue'
import ShoppingPage from '@/views/viewsSecond/ShoppingPage.vue'
import TeamPage from '@/views/viewsSecond/TeamPage.vue'
import UserProfilePage from '@/views/viewsSecond/UserProfilePage.vue'
import MessagesPage from '@/views/viewsSecond/MessagesPage.vue'
import LayoutPage from '@/views/LayoutPage.vue'
import ProfilePage from '@/views/viewsSecond/ProfilePage.vue'
import AddressPage from '@/views/viewsSecond/AddressPage.vue'
import CollectPage from '@/views/viewsSecond/CollectPage.vue'
import CountSetting from '@/views/viewsSecond/CountSetting.vue'
import JameHarden from '@/components/JameHarden.vue'
import XiaoKa from '@/components/XiaoKa.vue'
import PauGeo from '@/components/PauGeo.vue'
import WestBrook from '@/components/WestBrook.vue'
import DunkPage from '@/components/DunkPage.vue'
import ProductDetail from '@/views/viewsSecond/ProductDetail.vue'
import PlayerDetail from '@/views/viewsSecond/PlayerDetail.vue'
import OrderList from '@/views/viewsSecond/OrderList.vue'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior() {
    // 每次路由切换回到页面顶部，避免点击链接后停留在原滚动位置造成"闪屏/跳闪"
    return { top: 0 }
  },
  routes: [
    { path: '/orders', component: Orders },
    { path: '/login', component: LoginPage },
    {
      path: '/',
      component: LayoutPage,
      redirect: '/home',
      children: [
        { path: '/home', component: HomePage },
        { path: '/shopping', component: ShoppingPage },
        { path: '/product/:id', component: ProductDetail },
        { path: '/news', component: NewsPage },
        { path: '/team', component: TeamPage },
        { path: '/player/:id', component: PlayerDetail },
        { path: '/community', component: CommunityPage },
        { path: '/schedule', component: SchedulePage },
        { path: '/user/:id', component: UserProfilePage },
        { path: '/messages', component: MessagesPage }
      ]
    },
    {
      path: '/member-center',
      component: MemberCenter,
      redirect: '/member-center/profile',
      children: [
        { path: '/member-center/profile', component: ProfilePage },
        { path: '/member-center/orders', component: OrderList },
        { path: '/member-center/address', component: AddressPage },
        { path: '/member-center/collect', component: CollectPage },
        { path: '/member-center/count', component: CountSetting }
      ]
    },
    { path: '/help-center', component: Center },
    { path: '/customer-service', component: Service },
    { path: '/mobile', component: Version },
    { path: '/jamesharden', component: JameHarden },
    { path: '/kawhi', component: XiaoKa },
    { path: '/pg13', component: PauGeo },
    { path: '/RussWest', component: WestBrook },
    { path: '/DK', component: DunkPage }
  ]
})

export default router