<template>
  <div>
    <br><br>
    <br>
    <div style="background:;">
    <img src="./title3.png" />

    </div>

    <div align="right" style="font-size:18px; font-weight:700;">
      <form action="http://165.132.105.40:3000/users/logout" method="get" enctype="multipart/form-data">
        <table>
          <tr>
            <td class="usericon" style="width:35px; display:none;"> <img src="./pengsoo3.jpg" width="28px" height="28px"> </td>
            <td class="usericon" style="width:35px; display:none;"> <img src="./pengsoo.jpg" width="28px" height="28px"> </td>
            <td class="usericon" style="width:35px; display:none;"> <img src="./pengsoo2.jpg" width="28px" height="28px"> </td>
            <td> <b v-if="this.currentuserid"> Welcome </b> </td>
            <td> <a style="font-size:20px; color:blue;"> {{ currentusername }} </a> </td>
            
            <td>
              <button class="loginBtn" type="submit" v-if="!this.currentuserid"> 로그인 </button>
              <button class="loginBtn" type="submit" v-if="this.currentuserid"> 로그아웃 </button>
            </td>
          </tr>
        </table>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import serverConfig from "../assets/data/server_config.json";
export default {
  data() {
    return {
      icon : '',
      svrConfig : serverConfig,
      svrAddr : '',
      currentusername : ''
    }
  },
  props : ['currentuserid'],
  created() {
    this.svrAddr = this.svrConfig.hostserver;
    this.getUserName();
    this.icon = Math.floor(Math.random() * 3); // 0 ~ (max - 1) 까지의 정수 값을 생성
  },
  updated() {
    this.showIcon(this.icon);
  },
  methods : {
    showIcon(index){
      var x = document.getElementsByClassName("usericon");
      // console.log("Length:", x.length)
      for(var i = 0; i < x.length; i++) {
          x[i].style.display = 'none';
      }
      x[index].style.display = 'block';
    },
    getUserName() {
      // console.log("init", this.currentuserid);
      var api = "http://" + this.svrAddr + ":3000/users/get_username";
      var params = {id:this.currentuserid};
      axios
      .post(api, params)
      .then(response => {
        this.currentusername = response.data;
      })
      .catch(err => {
        console.log(err);
      })
    }
  }
}

</script>

<style src="./css/total.css"></style>
