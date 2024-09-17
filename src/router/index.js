import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginPage from '@/views/LoginPage.vue';
import MemberCenter from '@/views/MemberCenter.vue';
import Center from '@/views/Center.vue';
import Service from '@/views/Service.vue';
import Version from '@/views/Version.vue';
import Orders from '@/views/Orders.vue';
import HomePage from '@/views/viewsSecond/HomePage.vue';
import CommunityPage from '@/views/viewsSecond/CommunityPage.vue';
import NewsPage from '@/views/viewsSecond/NewsPage.vue';
import SchedulePage from '@/views/viewsSecond/SchedulePage.vue';
import ShoppingPage from '@/views/viewsSecond/ShoppingPage.vue';
import TeamPage from '@/views/viewsSecond/TeamPage.vue';
import TicketPage from '@/views/viewsSecond/TicketPage.vue';
import LayoutPage from '@/views/LayoutPage.vue';

Vue.use(VueRouter);

const router = new VueRouter({
  mode: 'history',
  routes: [
    {path:'/orders',component:Orders},
    { path: '/login', component: LoginPage },
    {
      redirect:'/home',
      path:'/',
      component:LayoutPage,
      children:[
        {
          path: '/home',
          component: HomePage,
        },
        {
          path: '/shopping',
          component: ShoppingPage,
        },
        {
          path: '/news',
          component: NewsPage,
        },
        {
          path: '/team',
          component: TeamPage,
        },
        {
          path: '/community',
          component: CommunityPage,
        },
        {
          path: '/tickets',
          component: TicketPage,
        },
        {
          path: '/schedule',
          component: SchedulePage,
        }
      ]
    },
    {path:'/member-center',component:MemberCenter},
    {path:'/help-center',component:Center},
    {path:'/customer-service',component:Service},
    {path:'/mobile',component:Version},
  ]
})

export default router
