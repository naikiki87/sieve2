import Vue from 'vue'
import Router from 'vue-router'
import MainPage from '@/components/MainPage'
import TaskDetail from '@/components/Taskdetail'
// import TaskModify from '@/components/taskModify'
// import Test from '@/components/test'
import Login from '@/components/Login'
import Chart from '@/components/Chart'
import Chart2 from '@/components/Chart2'
// import HelloWorld from '@/components/test/HelloWorld'

Vue.use(Router)
export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    // {
    //   path: '/',
    //   name: 'Chart',
    //   component: Chart
    // },
    {
      path: '/main',
      name: 'MainPage',
      component: MainPage
    },
    {
      path: '/taskdetail',
      name: 'TaskDetail',
      component: TaskDetail
    },
    // {
    //   path: '/taskmodify',
    //   name: 'TaskModify',
    //   component: TaskModify
    // },
    // {
    //   path: '/test',
    //   name: 'Test',
    //   component: Test
    // },

    // {
    //   path: '/helloworld',
    //   name: 'HelloWorld',
    //   component: HelloWorld
    // },
  ]
})
