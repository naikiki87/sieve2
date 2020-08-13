import Vue from 'vue';
import App from './App';
import router from './router';
import VModal from 'vue-js-modal'

Vue.config.productionTip = false
Vue.use(VModal, {dynamic : true})

new Vue({
router,
render: h => h(App)
}).$mount('#app')
