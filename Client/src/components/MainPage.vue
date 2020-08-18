<template>
  <div id = "app">
    <Header v-bind:currentuserid="currentuserid"> </Header>
    <div id="tab-list" style="background : #0a1b2a; height : 50px; padding : 0px; margin:8px; margin-bottom : 20px;">
      <div id="tb_1" class="tabmenu active" v-on:click="tabChange('tb_1', 'svrManage')"> EDGE </div>
      <div id="tb_2" class="tabmenu" v-on:click="tabChange('tb_2', 'schemaManage')"> SCHEMA </div>
      <div id="tb_3" class="tabmenu" v-on:click="tabChange('tb_3', 'taskManage')"> TASK </div>
      <div id="tb_4" class="tabmenu" v-on:click="tabChange('tb_4', 'jobManage')"> JOB </div>
    </div>
    <div id="svrManage" class="tabcontent" style="display:;">
      <SvrManage v-bind:currentuserid="currentuserid"> </SvrManage>
    </div>
    <div id="schemaManage" class="tabcontent" style="display:none;">
      <SchemaManage v-bind:currentuserid="currentuserid"> </SchemaManage>
    </div>
    <div id="taskManage" class="tabcontent" style="display:none;">
      <TaskManage v-bind:currentuserid="currentuserid"> </TaskManage>
    </div>
    <div id="jobManage" class="tabcontent" style="display:none;">
      <JobManage v-bind:currentuserid="currentuserid"> </JobManage>
    </div>
  </div>
</template>

<script>
import Header from './Header.vue'
import SvrManage from './SvrManage.vue'
import SchemaManage from './SchemaManage.vue'
import JobManage from './JobManage.vue'
import TaskManage from './TaskManage.vue'
import axios from 'axios'

export default {
  name: 'app',
  data() {
    return {
      todoItems : [],
      currentuserid : ''
    }
  },
  created() {
    this.searchTarget();
    for(var i=0; i<localStorage.length; i++) {
      if(localStorage.key(i) != "loglevel:webpack-dev-server") {
        var val = localStorage.getItem(localStorage.key(i));
        var arr = JSON.parse(val);
        this.todoItems.push(arr);
      }
    }
    localStorage.removeItem("loglevel:webpack-dev-server");
  },
  methods: {
    searchTarget() {
      var text = "user0";
      var cookiedata = this.getCookie(text);
      // console.log(`쿠키 ${text}에 저장된 값: `+cookiedata);
      this.currentuserid = cookiedata;
    },
    getCookie(name) {
      // console.log("get Cookie");
      var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
      return value? value[2] : null;
    },
    tabChange(tab_id, tab_content) {
      var x = document.getElementsByClassName("tabcontent");
      var i;
      for (i = 0; i < x.length; i++) {
          x[i].style.display = 'none';
      }
      document.getElementById(tab_content).style.display = 'block';
      var x = document.getElementsByClassName("tabmenu");
      var i;
      for (i = 0; i < x.length; i++) {
          x[i].className = 'tabmenu';
      }
      document.getElementById(tab_id).className = 'tabmenu active';
    }
  },
  components: {
    'Header' : Header,
    'SvrManage' : SvrManage,
    'SchemaManage' : SchemaManage,
    'JobManage' : JobManage,
    'TaskManage' : TaskManage
  }
}

</script>

<style src="./css/total.css"></style>

<!-- /* <style>
body { text-align : center; background-color: #f6f6f8; }
input { border-style: groove; width : 200px; }
button { border-style: groove; }
.shadow { box-shadow: 5px 10px 10px rgba(0, 0, 0, 0.03) }
.tabmenu { float : left; padding : 0px; height : 48px; width : 105px; padding-top : 14px; color : white; font-size : 13px; }
.active { font-size : 14px; font-weight:bold; color : gold; }
p { color : white; font-weight : bold; }
/* th, td { height : 25px; padding : 0px; border : 0.1px solid #d8d8d8; background : #fafafa }
table { font-size : 14px; border : 1px solid; } */
</style> */ -->
