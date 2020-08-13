<template>
  <div>
    <div style="height:500px;">
      <div class="pageName"> - 연산서버 관리 - </div>
      <!-- <button v-on:click="setCookie"> click </button>
      <button v-on:click="dataTEST"> TEST </button>
      <button v-on:click="dataTEST2"> TEST2 </button> -->
      <div style="height:450px; overflow:auto;">
        <table class="mainMngTable" style="width:98%;">
          <colgroup>
            <col style="width: 50px"> <!-- ID -->
            <col style="width: 120px"> <!-- IP -->
          	<col style="width: 120px"> <!-- root ID -->
          	<col style="width: 120px"> <!-- root password -->
          	<col style="width: 120px"> <!-- sieve key -->
            <col style="width: 80px"> <!-- delete -->
          </colgroup>
          <tr>
            <th>ID</th>
            <th>IP 주소</th>
            <th>Root ID</th>
            <th>Root PW</th>
            <th>SIEVE KEY</th>
            <th>삭제</th>
          </tr>
          <tr v-for="p in pageArray" :key="p.id">
            <td>{{ p.id }}</td>
            <td>{{ p.ip_address }}</td>
            <td>{{ p.root_id }}</td>
            <td>{{ p.root_passwd }}</td>
            <td>{{ p.sieve_key }}</td>
            <td> <button style="width:50px;" v-on:click="removeSvr(p.id)"> 삭제 </button> </td>
          </tr>
        </table>
      </div>
    </div>
    <table class="mainMngTable2" style="margin:auto; width:800px;">
      <tr>
        <td class="add_title"> IP 주소 </td>
        <td> <input style="width : 95%;" type="text" v-model="add_ip" v-on:keyup.enter="addSvr"> </td>
        <td class="add_title"> Root ID </td>
        <td> <input style="width : 95%;" type="text" v-model="add_id" v-on:keyup.enter="addSvr"> </td>
        <td rowspan="2" style="width:20px;"> </td>
        <td rowspan="2"> <button class="addButton" v-on:click = "addSvr" > 추가 </button> </td>
        <td rowspan="2" style="width:10px;"> </td>
        <!-- <td rowspan="2"> <button class="addButton" v-on:click = "clearSvr" > 전체 삭제 </button> </td> -->
        <!-- <td rowspan="2" style="width:10px;"> </td> -->
      </tr>
      <tr>
        <td class="add_title"> Root PW </td>
        <td> <input style="width : 95%;" type="text" v-model="add_pw" v-on:keyup.enter="addSvr"> </td>
        <td class="add_title"> SIEVE KEY </td>
        <td> <input style="width : 95%;" type="text" v-model="add_sievekey" v-on:keyup.enter="addSvr"> </td>
      </tr>
    </table>
    <!-- <button class="addButton" v-on:click = "setCookie" > test1 </button> </td> -->
    <!-- <button class="addButton" v-on:click = "searchTarget" > test </button> -->
  </div>
</template>

<script>
import axios from 'axios'
import userList from "../assets/data/users.json";
import serverConfig from "../assets/data/server_config.json";

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
      cookie_name : 'myHobby',
      cookie_value : 'game',
      cookie_days : 3,
      currentuserid : ''
    }
  },
  created () {
    this.svrAddr = this.svrConfig.hostserver;
    this.loadSvrPost();
  },

  methods: {
    dataTEST2() {
      // var url = `https://finance.daum.net/api/quotes/A${item}?summary=false&changeStatistics=true`;
      var url = `https://finance.daum.net/api/quotes/A005930?summary=false&changeStatistics=true`;
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
          console.log(response);
        })
      }, 1000);
    },
    dataTEST() {
      var params = {
        ServiceKey : "NcrKKA%2BLkC4A%2FV8UoNrpkHw0V7fQp6No1NGaN69RF7TrjGR9hSP%2FvvrUvgCZ9kLf4q4UMhBvavYZmFHWPhyDDw%3D%3D",
        sidoName : "서울",
        searchCondition : "DAILY",
        _returnType : "json"
      }
      var api = `http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?ServiceKey=${params.ServiceKey}&sidoName=${params.sidoName}&_returnType=${params._returnType}&searchCondition=${params.searchCondition}`;

      axios.get(api)
      .then(response => {
        console.log(response);
      })
      .catch(err => {
        console.log(err);
      });
    },
    setCookie() {
      console.log("set Cookie");
      var exdate = new Date();
      exdate.setDate(exdate.getDate() + this.cookie_days);
      // 설정 일수만큼 현재시간에 만료값으로 지정

      var cookie_value = escape(this.cookie_value) + ((this.cookie_days == null) ? '' : ';    expires=' + exdate.toUTCString());
      document.cookie = this.cookie_name + '=' + cookie_value;
    },

    loadSvr() {
      console.log("**** (1/5)LOAD SERVER ****", this.svrAddr);

      // console.log("addr : " + this.svrAddr);
      var api = "http://" + this.svrAddr + ":3000/users/engine_computer";
      axios
      .get(api)
      .then(response => {
        // console.log(response);
        this.pageArray = response.data;
        
      })
      .catch(err => {
        console.log(err);
      });
    },
    loadSvrPost() {
      console.log("**** (1/5)LOAD SERVER **** : ", this.currentuserid);

      // console.log("addr : " + this.svrAddr);
      var api = "http://" + this.svrAddr + ":3000/users/engine_computer2";
      var params = {
        searchTarget : this.currentuserid
      }

      axios
      .post(api, params)
      .then( response => {
        this.pageArray = response.data;
        console.log(response.data)
      })
      .catch( response => { console.log(response) } );
    },

    addSvr() {
      var value_ip = this.add_ip && this.add_ip.trim();
      var value_id = this.add_id && this.add_id.trim();
      var value_pw = this.add_pw && this.add_pw.trim();
      var value_sievekey = this.add_sievekey && this.add_sievekey.trim();
      var params = {
        ip_address: value_ip,
        root_id: value_id,
        root_passwd: value_pw,
        sieve_key: value_sievekey,
        currentuserid : this.currentuserid
      }

      var api = "http://" + this.svrAddr + ":3000/users/engine_computer/add";

      axios
      .post(api, params)
      .then( response => { console.log(response) } )
      .catch( response => { console.log(response) } );

      this.clearInput();
      // this.loadSvr();
      // this.loadSvr();
      // this.loadSvr();
      this.loadSvrPost(this.currentuserid);
      this.loadSvrPost(this.currentuserid);
      this.loadSvrPost(this.currentuserid);
    },

    clearSvr() {
      console.log("CLEAR SERVER");
      var api = "http://" + this.svrAddr + ":3000/users/engine_computer/delete_all";
      axios
      .post(api)
      .then( response => { console.log(response) } )
      .catch( response => { console.log(response) } );
      this.loadSvr();
      this.loadSvr();
      this.loadSvr();
    },

    removeSvr(index) {
      console.log("DELETE" + index);
      var params = {
        id : index
      }
      var api = "http://" + this.svrAddr + ":3000/users/engine_computer/delete";
      axios
      .post(api, params)
      .then( response => { console.log(response) } )
      .catch( response => { console.log(response) } );

      this.loadSvrPost(this.currentuserid);
      this.loadSvrPost(this.currentuserid);
      this.loadSvrPost(this.currentuserid);
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
