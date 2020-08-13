<template>
  <div id = "app">
    <Header> </Header>
    <button v-on:click="setCookie"> click </button>
    <div id="tab-list" style="background : #0a1b2a; height : 50px; padding : 0px; margin:8px; margin-bottom : 20px;">
      <div id="tb_1" class="tabmenu active" v-on:click="tabChange('tb_1', 'svrManage')"> 연산서버 </div>
      <div id="tb_2" class="tabmenu" v-on:click="tabChange('tb_2', 'schemaManage')"> 데이터스키마 </div>
      <div id="tb_3" class="tabmenu" v-on:click="tabChange('tb_3', 'jobManage')"> JOB 생성/관리 </div>
      <div id="tb_4" class="tabmenu" v-on:click="tabChange('tb_4', 'taskManage')"> TASK 등록/관리 </div>
    </div>
    <div id="svrManage" class="tabcontent" style="width:95%; margin-left:2%; border : 1px solid;">
      <SvrManage> </SvrManage>
    </div>
    <div id="schemaManage" class="tabcontent" style="width:95%; margin-left:2%; border : 1px solid; display:none;">
      <SchemaManage> </SchemaManage>
    </div>
    <div id="jobManage" class="tabcontent" style="width:95%; margin-left:2%; border : 1px solid; display:none;">
      <JobManage> </JobManage>
    </div>
    <div id="taskManage" class="tabcontent" style="width:95%; margin-left:2%; border : 1px solid; display:none;">
      <TaskManage> </TaskManage>
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
      cookie_name : 'myHobby',
      cookie_value : 'game',
      cookie_days : 3
    }
  },
  created() {
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
    setCookie() {
      console.log("set Cookie");
      var exdate = new Date();
      exdate.setDate(exdate.getDate() + this.cookie_days);
      // 설정 일수만큼 현재시간에 만료값으로 지정

      var cookie_value = escape(this.cookie_value) + ((this.cookie_days == null) ? '' : ';    expires=' + exdate.toUTCString());
      document.cookie = this.cookie_name + '=' + cookie_value;
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

<style>
body { text-align : center; background-color: #f6f6f8; }
input { border-style: groove; width : 200px; }
button { border-style: groove; }
.shadow { box-shadow: 5px 10px 10px rgba(0, 0, 0, 0.03) }
.tabmenu { float : left; padding : 0px; height : 48px; width : 105px; padding-top : 14px; color : white; font-size : 13px; }
.active { font-size : 14px; font-weight:bold; color : gold; }
p { color : white; font-weight : bold; }
th, td { height : 25px; padding : 0px; border : 0.1px solid #d8d8d8; background : #fafafa }
table { font-size : 14px; border : 1px solid; }
</style>
