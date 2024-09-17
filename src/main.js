import Vue from 'vue'
import App from './App.vue'
import '../style/base.css'
import '../style/common.css'
import '../style/index.css'
import './assets/iconfont/iconfont.css'
import store from './store'
import router from './router'
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
