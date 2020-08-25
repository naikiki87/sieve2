import Vue from 'vue';
import App from './App';
import router from './router';
import VModal from 'vue-js-modal'
import VueCookie from 'vue-cookie'

Vue.config.productionTip = false
Vue.use(VModal, {dynamic : true})
Vue.use(VueCookie)

new Vue({
router,
render: h => h(App)
}).$mount('#app')
