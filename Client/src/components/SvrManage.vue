<template>
  <div>
    <div style="height:500px;">
      <div class="pageName"> Edge Server List </div>
      <div style="height:450px; overflow:auto;">
        <table class="mainMngTable" style="width:98%;">
          <colgroup>
            <col style="width: 30px"> <!-- ID -->
            <col style="width: 100px">
            <col style="width: 100px"> <!-- IP -->
            <col style="width: 180px">
            <col style="width: 100px">
          </colgroup>
          <tr>
            <th>ID</th>
            <th>Alias</th>
            <th>IP Address</th>
            <th>Description</th>
            <th>Delete</th>
          </tr>
          <tr v-for="(p, index) in pageArray" :key="p.id">
            <td>{{ index + 1 }}</td>
            <td>{{ p.alias }}</td>
            <td>{{ p.ip_address }}</td>
            <td>{{ p.description }}</td>
            <td> <button v-if="p.userid == currentuserid" class="longBtn_DEL" v-on:click="removeSvr(p.id)"> DEL </button> </td>
          </tr>
        </table>
      </div>
    </div>
    <table v-if="add_window" class="mainMngTable2" style="margin:auto; width:800px;">
      <tr>
        <td class="add_title"> IP Address </td>
        <td> <input style="width : 95%;" type="text" v-model="add_ip" v-on:keyup.enter="addSvr"> </td>
        <td class="add_title"> Root ID </td>
        <td> <input style="width : 95%;" type="text" v-model="add_id" v-on:keyup.enter="addSvr"> </td>
        <td rowspan="2" style="width:20px;"> </td>
        <td rowspan="2"> <button class="addButton" v-on:click = "addSvr" > ADD </button> </td>
        <td rowspan="2" style="width:10px;"> </td>
      </tr>
      <tr>
        <td class="add_title"> Root PW </td>
        <td> <input style="width : 95%;" type="text" v-model="add_pw" v-on:keyup.enter="addSvr"> </td>
        <td class="add_title"> Alias </td>
        <td> <input style="width : 95%;" type="text" v-model="add_alias" v-on:keyup.enter="addSvr"> </td>
      </tr>
    </table>
  </div>
</template>

<script>
import axios from 'axios'
import userList from "../assets/data/users.json";
// import serverConfig from "../assets/data/server_config.json";
import serverConfig from '../../config/index'
import addInfo from '../../config/index'

export default {
  name: 'SvrManage',
  props : ['currentuserid'],
  data () {
    return {
      user : userList,
      svrConfig : serverConfig,
      svrAddr : '',
      pageArray: [],
      add_ip : '',
      add_id : '',
      add_pw : '',
      add_sievekey : '',
      add_alias : '',
      cookie_name : 'myHobby',
      cookie_value : 'game',
      cookie_days : 3,
      add_window : 0,
      host : addInfo.dev.host,
      f_port : addInfo.dev.port,
      b_port : addInfo.dev.sport,
      api_addr : ''

    }
  },
  created () {
    console.log("svar mananananan")
    this.api_addr = "http://" + this.svrConfig.dev.host + ':' + this.svrConfig.dev.sport;
    this.svrAddr = this.svrConfig.hostserver;
    this.loadSvr()
    if(this.currentuserid != null) {
      this.add_window = 1
    }
  },

  methods: {
    async loadSvr() {
      // console.log("**** (1/5)LOAD SERVER ****", this.svrAddr);
      // console.log("addr : " + this.svrAddr);
      // var api = "http://"
      // var api = "http://" + this.svrAddr + ":3000/users/engine_computer";
      var api = this.api_addr + "/users/engine_computer"
      console.log("svr mng : ", api)
      console.log("sss")
      this.pageArray = (await axios.get(api)).data
      console.log("svr mng : ", this.pageArray)
      
      // axios
      // .get(api)
      // .then(response => {
      //   // console.log(response);
      //   this.pageArray = response.data;
        
      // })
      // .catch(err => {
      //   console.log(err);
      // });
    },
    async addSvr() {
      var value_ip = this.add_ip && this.add_ip.trim();
      var value_id = this.add_id && this.add_id.trim();
      var value_pw = this.add_pw && this.add_pw.trim();
      var value_sievekey = this.add_sievekey && this.add_sievekey.trim();
      var value_alias = this.add_alias && this.add_alias.trim();
      var params = {
        ip_address: value_ip,
        root_id: value_id,
        root_passwd: value_pw,
        sieve_key: value_sievekey,
        alias : value_alias,
        currentuserid : this.currentuserid
      }

      // var api = "http://" + this.svrAddr + ":3000/users/engine_computer/add";
      var api = this.api_addr + "/users/engine_computer/add"
      this.clearInput()
      await axios.post(api, params)
      await this.loadSvr()
    },
    async removeSvr(index) {
      // console.log("DELETE" + index);
      var params = {
        id : index
      }
      // var api = "http://" + this.svrAddr + ":3000/users/engine_computer/delete";
      var api = this.api_addr + "/users/engine_computer/delete"
      await axios.post(api, params)
      await this.loadSvr()
    },
    clearInput() {
      this.add_ip = "";
      this.add_id = "";
      this.add_pw = "";
      this.add_sievekey = "";
    }
  }
}
</script>
<style src="./css/total.css"></style>
