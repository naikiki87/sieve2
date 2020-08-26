<template>
  <div>
    <h1 style="height:20px">  </h1>
    <img src="./title3.png" />

    <div align="right" style="font-size:18px; font-weight:700;">
      <table>
        <tr>
          <td class="usericon" style="width:35px; display:none;"> <img src="./pengsoo3.jpg" width="28px" height="28px"> </td>
          <td class="usericon" style="width:35px; display:none;"> <img src="./pengsoo.jpg" width="28px" height="28px"> </td>
          <td class="usericon" style="width:35px; display:none;"> <img src="./pengsoo2.jpg" width="28px" height="28px"> </td>
          <td> <b v-if="this.currentuserid"> Current User :  </b> </td>
          <td> <a style="font-size:20px; color:blue;"> {{ currentusername }} </a> </td>
          <td>
            <button class="loginBtn" v-on:click="to_login" v-if="!this.currentuserid"> LogIN </button>
            <button class="loginBtn" v-on:click="to_login" v-if="this.currentuserid"> LogOUT </button>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import serverConfig from '../../config/index'
export default {
  data() {
    return {
      icon : '',
      svrConfig : serverConfig,
      svrAddr : '',
      currentusername : '',
      api_addr : '',
      svrHost : '',
      f_port : '',
      b_port : ''
    }
  },
  props : ['currentuserid'],
  async created() {
    this.api_addr = "http://" + this.svrConfig.dev.host + ':' + this.svrConfig.dev.sport;
    // this.svrAddr = this.svrConfig.hostserver;
    await this.getUserName();
    this.icon = Math.floor(Math.random() * 3); // 0 ~ (max - 1) 까지의 정수 값을 생성
    await this.showIcon(this.icon)
    this.svrHost = this.svrConfig.dev.host;
    this.f_port = this.svrConfig.dev.port;
    this.b_port = this.svrConfig.dev.sport;
  },
  methods : {
    to_login() {
      console.log("to login")
      this.$cookie.delete("user0")
      location.href = "http://" + this.svrHost + ':' + this.f_port
    },
    showIcon(index){
      var x = document.getElementsByClassName("usericon");
      for(var i = 0; i < x.length; i++) {
          x[i].style.display = 'none';
      }
      x[index].style.display = 'block';
    },
    async getUserName() {
      try {
        var api = this.api_addr + "/users/get_username";
        var params = { id:this.currentuserid }
        this.currentusername = (await axios.post(api, params)).data
      }
      catch(e) {}
    }
  }
}

</script>

<style src="./css/total.css"></style>
