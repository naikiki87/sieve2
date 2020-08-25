<template>
  <div>
    <br><br><br>
    <br><br><br><br>
    <br><br><br><br><br><br>
    <!-- <img src="./title3.png" /> -->

    <!-- <button v-on:click="dataTEST3"> TEST2 </button> -->

    <br><br><br>
    <div>
      <!-- <form action="http://localhost:3000/users/login" method="post" enctype="multipart/form-data"> -->
      <form action="http://165.132.105.40:50000/users/login" method="post" enctype="multipart/form-data">
        <table id="loginTable" style="margin:auto; width:430px;">
          <br>
          <tr>
            <td> <input placeholder="아이디" style="padding-left:10px; width:93%;" name="userID" type="text" v-model="loginID"/> </td>
          </tr>
          <br>
          <tr>
            <td> <input placeholder="비밀번호" style="padding-left:10px; width:93%;" name="userPW" type="password" v-model="loginPW"/> </td>
          </tr>
          <br><br>
          <tr style="height:45px;">
            <td> <button style="font-weight:700; height:48px; width:96%; background:darkblue; color:white; font-size:18px;" type="submit"> 로그인 </button> </td>
          </tr>
          <br><br>
        </table>
      </form>
      <br>

    </div>
  </div>
</template>

<script>

// import Header from './Header.vue'
import axios from 'axios'

export default {
  name: 'app',
  data() {
    return {
      loginID : '',
      loginPW : '',
      last_volume :0
    }
  },
  created() {
  },
  methods: {
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

<!-- /* <style>
body { text-align : center; background-color: #f6f6f8; }
input { border-style: groove; width : 200px; }
button { border-style: groove; }
.shadow { box-shadow: 5px 10px 10px rgba(0, 0, 0, 0.03) }
.tabmenu { float : left; padding : 0px; height : 48px; width : 105px; padding-top : 14px; color : white; font-size : 13px; }
.active { font-size : 14px; font-weight:bold; color : gold; }
p { color : white; font-weight : bold; }
th, td { height : 25px; padding : 0px; border : 0.1px solid #d8d8d8; background : #fafafa }
table { font-size : 14px; border : 1px solid; }
</style> */ -->
