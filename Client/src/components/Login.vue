<template>
  <div>
    <br><br><br>
    <br><br><br><br>
    <br><br><br><br><br><br>
    <img src="./title3.png" />

    <!-- <button v-on:click="dataTEST3"> TEST2 </button> -->

    <br><br><br>
    <div>
      <!-- <form action="http://localhost:3000/users/login" method="post" enctype="multipart/form-data"> -->
      <!-- <form action="http://165.132.105.40:50000/users/login" method="post" enctype="multipart/form-data"> -->
        <table id="loginTable" style="margin:auto; width:430px;">
          <br>
          <tr>
            <td> <input placeholder="ID" style="padding-left:10px; width:93%;" name="userID" type="text" v-model="loginID"/> </td>
          </tr>
          <br>
          <tr>
            <td> <input placeholder="Password" style="padding-left:10px; width:93%;" name="userPW" type="password" v-model="loginPW"/> </td>
          </tr>
          <br><br>
          <tr style="height:45px;">
            <td> <button style="font-weight:700; height:48px; width:96%; background:darkblue; color:white; font-size:18px;" v-on:click="sign_in"> Sign IN </button> </td>
            <!-- <td> <button style="font-weight:700; height:48px; width:96%; background:darkblue; color:white; font-size:18px;" v-on:click="watchdog"> TEST </button> </td> -->
          </tr>
          <br><br>
        </table>
      <!-- </form> -->
      <br>
      <div> <button class="regBtn" v-on:click="new_membership"> New Membership </button> </div>
      <br>
      <div v-if="membership">
        <table id="registerTable" style="margin:auto; width:430px; padding:5px;">
          <tr>
            <td> ID : </td>
            <td> <input placeholder="ID" style="padding-left:10px; width:93%;" name="userID" type="text" v-model="registerID"/> </td>
            <td> <button class="midBtn" v-on:click="name_check"> CHECK </button> </td>
            <td> <a style="color:green" v-if="name_usable === 1"> OK </a> <a style="color:red" v-if="name_usable === 2"> Not OK </a></td>
          </tr>

          <tr>
            <td> PW : </td>
            <td> <input placeholder="Password" style="padding-left:10px; width:93%;" name="userID" type="password" v-model="registerPW"/> </td>
            <td> </td>
            <td> </td>
          </tr>

          <tr>
            <td> retype PW : </td>
            <td> <input v-on:click="check_regPW" placeholder="Check Password" style="padding-left:10px; width:93%;" name="userID" type="password" v-model="registerPW2"/> </td>
            <td colspan="2"> <a style="color:green" v-if="regPW_check"> Same </a> </td>
            
          </tr>
          <tr>
            <td colspan="4"> <button class="longBtn" style="font-weight:700; height:28px; width:75%; background:; color:white; font-size:18px;" v-on:click="new_register"> Register </button> </td>
          </tr>
            
        </table>

      </div>
    </div>
  </div>
</template>

<script>

// import Header from './Header.vue'
import axios from 'axios'
import serverConfig from '../../config/index'

export default {
  name: 'app',
  data() {
    return {
      svrConfig : serverConfig,
      loginID : '',
      loginPW : '',
      registerID : '',
      registerPW : '',
      registerPW2 : '',
      last_volume : 0,
      svrHost : '',
      f_port : '',
      b_port : '',
      api_addr : '',
      membership : 0,
      name_usable : 0,
      check_interval : '',
      regPW_check : 0
    }
  },
  created() {
    this.svrHost = this.svrConfig.dev.host;
    this.f_port = this.svrConfig.dev.port;
    this.b_port = this.svrConfig.dev.sport;
    this.api_addr = "http://" + this.svrConfig.dev.host + ':' + this.svrConfig.dev.sport;
  },
  methods: {
    check() {
      console.log("adasdf")
      if(this.registerPW == this.registerPW2) {
        this.regPW_check = 1
        clearInterval(this.check_interval)
      }
    },
    async check_regPW(){
      this.check_interval = setInterval(this.check, 1000)

    },
    async new_register() {
      console.log("new register")
      console.log("status : ", this.name_usable, this.regPW_check)
      if(this.name_usable != 1) {
        console.log("ID is not acceptable")
      }
      if(this.regPW_check != 1) {
        console.log("PW is not acceptable")
      }

      if(this.name_usable == 1 && this.regPW_check == 1) {
        var params = {
          regid : this.registerID,
          regpw : this.registerPW
        }
        var api = this.api_addr + "/users/new_register"
      }

      var success = (await axios.post(api, params)).data.success
      if(success) {
        location.href = "http://" + this.svrHost + ':' + this.f_port
      }
    },
    async name_check() {
      if(this.registerID == "") {
      }
      else {
        console.log(this.registerID)
        var params = {
          regid : this.registerID
        }
        var api = this.api_addr + "/users/regID_check"
        var check = (await axios.post(api, params)).data.same
        if(check == 0) {
          this.name_usable = 1
        }
        else {
          this.name_usable = 2
        }
      }

    },
    async watchdog() {
      console.log("watch dog")
      var api = this.api_addr + "/users/watchdog"
      
      var check = (await axios.get(api)).data
      console.log("check : ", check)

    },
    new_membership() {
      this.membership = 1
    },
    async sign_in() {
      var params = {
        userID : this.loginID,
        userPW : this.loginPW
      }

      var api = this.api_addr + "/users/login"

      console.log("api222 : ", api)
      var login_res = (await axios.post(api, params)).data
      var success = login_res.success

      console.log("succe : ", success)

      if(success) {
        this.$cookie.set("user0", login_res.userid, (new Date(Date.now() + 3600000)))
        location.href = "http://" + this.svrHost + ':' + this.f_port + "/main"
      }
      else {
        location.href = "http://" + this.svrHost + ':' + this.f_port
      }
    },
    dataTEST2() {
      // var url = `https://finance.daum.net/api/quotes/A${item}?summary=false&changeStatistics=true`;
      var url = `https://finance.daum.net/api/quotes/A008350?summary=false&changeStatistics=true`;
      var Interval_num = setInterval(function(){
        axios.get(url, {
          headers: {
            authority: "finance.daum.net",
            method: "GET",
            path: "/api/quotes/A005930?summary=false&changeStatistics=true",
            scheme: "https",
            accept: "application/json, text/javascript, */*; q=0.01",
            'accept-encoding': "gzip, deflate, br",
            'accept-language': "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7",
            cookie: "webid=d46730276e634ffaa5192c52a065e43a; SLEVEL=1; _TI_NID=iok9mjEFfnWoNqMjDA5hhZXcdWiWGmEAqFI+ku1SWnKso2aKSyJqQlbi2x0X7wUhZESA4O+tJ9qeqeHe3gi7tg==; ssab=823_1; _ga=GA1.2.742888705.1585551741; _gid=GA1.2.745483432.1585551741; KAKAO_STOCK_CHART_ENABLED_INDICATORS=[%22sma%22%2C%22column%22]; KAKAO_STOCK_RECENT=[%22A005930%22%2C%22A059090%22]; TIARA=eEjETbrkEXEWQNaOpVY6HIGHvnpVmY.jAdH-ZPTPYT6lfJqeJ6Z2dWN8DP4VlL6Pi7jIQl_ENkCRB_uOM9.6n7vO1J2f7rFe; _gat_gtag_UA_128578811_1=1; webid_sync=1585638058122; _dfs=Tll3Y2ZxRkdCWWZIMXp5K2w1NlJDSTZ1RWdzZVRlbWR4aXJMeUVrMU9mRDhvckhYU3pqVUFEeTRQZ055M2hXMjNYT3VDUS9KaXhrNmowUkJFelo0OFE9PS0tYUhDMFRxYUVRTUNaU0xRSHo2czNjZz09--db62e4303b7b5ead35e336c2a7112f994cc5284f",
            'if-none-match': `W/"41c52e677d2db77a499a831a5a51e77c"`,
            referer: "https://finance.daum.net/quotes/A005930",
            'sec-fetch-dest': "empty",
            'sec-fetch-mode': "cors",
            'sec-fetch-site': "same-origin",
            'user-agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36",
            'x-requested-with': "XMLHttpRequest"
          }
        })
        .then(response => {
          console.log(response.data.accTradeVolume);
        })
      }, 1000);
    },
    dataTEST3() {
      var url = 'http://polling.finance.naver.com/api/realtime.nhn?query=SERVICE_ITEM:005930|SERVICE_RECENT_ITEM:005930&_callback=';

      // axios.get(url)

      var Interval_num = setInterval(function(){

      axios.get(url, {
        headers: {
          'Accept-Language' : "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4",
          'Accept-Encoding' : "gzip, deflate, sdch",
          'Connection' : "keep-alive",
          'Accept' : "*/*",
          'User-Agent' : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36",
          'Host' : "polling.finance.naver.com",
          'Referer' : "http://finance.naver.com/item/main.nhn?code=035420"
        }
      })
      .then(response => {
        var current_volume = response.data.result.areas[1].datas[0].aq;

        // console.log("Current : " + current_volume);

        // console.log("Last : " + this.last_volume);
        var diff_volume = current_volume - this.last_volume;
        // console.log(diff_volume);

        if(diff_volume > 0) {
          console.log(response.data.result.areas[1].datas[0].aq);
        }
        this.last_volume = current_volume;
      })
    }, 500);
    }
  },
  components: {
    // 'Header' : Header
  }
}

</script>

<style src="./css/total.css"></style>
