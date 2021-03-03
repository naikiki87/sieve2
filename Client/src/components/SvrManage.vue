<template>
  <div>
    <div style="height:500px;">
      <div class="pageName"> Edge Server List </div>
        <!-- <button style="height:20px; width:70px; background:blue; color:white; font-size:10px;" v-on:click="watchdog_start"> W_DOG </button> -->
      
      <div style="height:450px; overflow:auto;">
        <table class="mainMngTable" style="width:98%;">
          <colgroup>
            <col style="width: 30px"> <!-- ID -->
            <col style="width: 100px">
            <col style="width: 100px"> <!-- IP -->
            <!-- <col style="width: 180px"> -->
            <col style="width: 100px">
            <col style="width: 100px">
            <col style="width: 100px">
            <col style="width: 100px">
          </colgroup>
          <tr>
            <th>ID</th>
            <th>Alias</th>
            <th>IP Address</th>
            <!-- <th>Description</th> -->
            <th>Alive</th>
            <th>Timestamp</th>
            <th>CPU_usage</th>
            <th>MEM_usage</th>
          </tr>
          <tr v-for="(p, index) in pageArray" :key="p.id">
            <td>{{ index + 1 }}</td>
            <td>{{ p.alias }}</td>
            <td>{{ p.ip_address }}</td>
            <!-- <td>{{ p.description }}</td> -->
            <!-- <td> <button v-if="p.userid == currentuserid" class="longBtn_DEL" v-on:click="removeSvr(p.id)"> DEL </button> </td> -->
            <td v-if="p.alive === 1" style="color:#04b431;"> {{ p.alive }} </td>
            <td v-if="p.alive === 0" style="color:red;"> {{ p.alive }} </td>
            <td> {{ p.timestamp }} </td>
            <!-- <td> {{ p.cpu_usage }} % </td> -->
            <td v-if="p.cpu_usage < 30" style="color:#04b431;"> {{ p.cpu_usage }} % </td>
            <td v-else-if="p.cpu_usage < 60" style="color:#ff8000;"> {{ p.cpu_usage }} % </td>
            <td v-else-if="p.cpu_usage >= 60" style="color:red;"> {{ p.cpu_usage }} % </td>

            <td> {{ p.mem_usage }} % </td>
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
      api_addr : '',
      prev_timestamp : [],
      dead_cnt : [],
      warning_list : []

    }
  },
  created () {
    this.api_addr = "http://" + this.svrConfig.dev.host + ':' + this.svrConfig.dev.sport;
    this.svrAddr = this.svrConfig.hostserver;
    this.loadSvr()
    if(this.currentuserid != null) {
      this.add_window = 1
    }
  },

  methods: {
    watchdog_start: function() {
      console.log("watchdog start")
      this.timer = setInterval(() => this.watchdog(), 1000);
    },
    async watchdog() {
      var api = this.api_addr + "/users/engine_computer"
      var check = (await axios.get(api)).data
      for(var i=0; i<this.pageArray.length; i++) {
        for(var j=0; j<check.length; j++) {
          if(check[j].ip_address == this.pageArray[i].ip_address) {
            if(this.prev_timestamp[i] == check[j].timestamp) {
              if(this.dead_cnt[i] == 3) {
                this.dead_cnt[i] = 0
                this.pageArray[i].cpu_usage = 0
                this.pageArray[i].mem_usage = 0
                this.pageArray[i].alive = 0
              }
              else {
                this.dead_cnt[i] = this.dead_cnt[i] + 1
              }
            }
            else {
              this.pageArray[i].alive = 1
              this.dead_cnt[i] = 0
              var myDate = new Date(check[j].timestamp * 1000)
              this.pageArray[i].timestamp = myDate.getHours() + ':' + myDate.getMinutes() + ':' + myDate.getSeconds()
              this.prev_timestamp[i] = check[j].timestamp
              this.pageArray[i].cpu_usage = check[j].cpu_usage
              this.pageArray[i].mem_usage = check[j].mem_usage

              if(check[j].cpu_usage >= 20) {
                // console.log("Warning : ", this.pageArray[i].ip_address)
                this.pageArray[i].warning_cnt = this.pageArray[i].warning_cnt + 1

                if(this.pageArray[i].warning_cnt == 5) {
                  console.log("prepare to scaling")
                  var api2 = this.api_addr + "/users/get_task_list"
                  var params = {
                    ip_address : this.pageArray[i].ip_address
                  }
                  var task_list = (await axios.post(api2, params)).data
                  console.log(task_list)
                }
              }
              else {
                this.pageArray[i].warning_cnt = 0
              }

            }
          }
        }
      }
    },
    // async watchdog() {
    //   // console.log("watchdog : ", this.pageArray[0].warning_cnt)
    //   var api = this.api_addr + "/users/watchdog"
    //   var check = (await axios.get(api)).data
    //   // console.log(check)
    //   // this.pageArray[0].abc = 100
    //   for(var i=0; i<this.pageArray.length; i++) {
    //     for(var j=0; j<check.length; j++) {
    //       if(check[j].ip_addr == this.pageArray[i].ip_address) {
    //         if(this.prev_timestamp[i] == check[j].timestamp) {
    //           if(this.dead_cnt[i] == 3) {
    //             this.dead_cnt[i] = 0
    //             this.pageArray[i].cpu_usage = 0
    //             this.pageArray[i].mem_usage = 0
    //             this.pageArray[i].alive = 0
    //           }
    //           else {
    //             this.dead_cnt[i] = this.dead_cnt[i] + 1
    //           }
    //         }
    //         else {
    //           this.pageArray[i].alive = 1
    //           this.dead_cnt[i] = 0
    //           var myDate = new Date(check[j].timestamp * 1000)
    //           this.pageArray[i].timestamp = myDate.getHours() + ':' + myDate.getMinutes() + ':' + myDate.getSeconds()
    //           this.prev_timestamp[i] = check[j].timestamp
    //           this.pageArray[i].cpu_usage = check[j].cpu_usage
    //           this.pageArray[i].mem_usage = check[j].mem_usage

    //           if(check[j].cpu_usage >= 20) {
    //             // console.log("Warning : ", this.pageArray[i].ip_address)
    //             this.pageArray[i].warning_cnt = this.pageArray[i].warning_cnt + 1

    //             if(this.pageArray[i].warning_cnt == 5) {
    //               console.log("prepare to scaling")
    //               var api2 = this.api_addr + "/users/get_task_list"
    //               var params = {
    //                 ip_address : this.pageArray[i].ip_address
    //               }
    //               var task_list = (await axios.post(api2, params)).data
    //               console.log(task_list)
    //             }
    //           }
    //           else {
    //             this.pageArray[i].warning_cnt = 0
    //           }

    //         }
    //       }
    //     }
    //   }
    // },
    async loadSvr() {
      var api = this.api_addr + "/users/engine_computer"
      this.pageArray = (await axios.get(api)).data
      for(var i=0; i<this.pageArray.length; i++) {
        this.dead_cnt.push(0)
      }
      this.watchdog_start()
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

      var api = this.api_addr + "/users/engine_computer/add"
      this.clearInput()
      await axios.post(api, params)
      await this.loadSvr()
    },
    async removeSvr(index) {
      var params = {
        id : index
      }
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
