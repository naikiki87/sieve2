import Vue from 'vue'
import Router from 'vue-router'
import MainPage from '@/components/MainPage'
// import TaskDetail from '@/components/Taskdetail'
import JobDetail from '@/components/jobDetail'
import modBS from '@/components/modpop/basestream'
import task_modify from '@/components/modpop/task_modify'
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
    // {
    //   path: '/taskdetail',
    //   name: 'TaskDetail',
    //   component: TaskDetail
    // },
    {
      path: '/jobDetail',
      name: 'JobDetail',
      component: JobDetail
    },
    {
      path: '/modify/BS',
      name: 'modBS',
      component: modBS
    },
    {
      path: '/modify',
      name: 'modify',
      component: task_modify
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
