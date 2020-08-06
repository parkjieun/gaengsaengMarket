import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import router from './router'
import store from './store'
import Vuex from 'vuex'
import VueAxios from 'vue-axios'
// import { VueAuthenticate } from 'vue-authenticate'
import axios from 'axios'
import cookies from 'vue-cookies'
import "vuetify/dist/vuetify.min.css"
import GSignInButton from 'vue-google-signin-button'
import InfiniteLoading from 'vue-infinite-loading';

Vue.use(InfiniteLoading)
Vue.use(GSignInButton)
Vue.config.productionTip = false

Vue.use(Vuex)
Vue.use(VueAxios, axios)
Vue.use(cookies)

new Vue({
  created(){
 Kakao.init('2d0941ae8abd795345275774180c1dc2');
 
 },
  InfiniteLoading,
  vuetify,
  router,
  store, 
  render: h => h(App)
}).$mount('#app')
