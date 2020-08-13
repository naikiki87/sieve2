<template>
  <div>
    <br>
    <br>
    <!-- Refresh(sec) : <input style="width:50px; height:20px; text-align:center" v-model="refreshSec" v-on:keyup.enter="startInterval"></input> -->
    <button style="font-weight:700; width:100px; height:30px; margin-left:20px; margin-right:20px;" v-on:click="startInterval"> START </button>
    <button style="font-weight:700; width:100px; height:30px;" v-on:click="stopInterval"> STOP </button>
    <!-- <button style="font-weight:700; width:100px; height:30px;" v-on:click="pythonrun"> PRUN </button> -->
    <div style="display:none;"> volume : {{ receivedData }}</div>
    <b align="left" style="display:; font-size:17px; font-weight:600; margin-left:30px;"> {{ timeHour }} : {{ timeMin }} : {{ timeSec }} </b>
    <b align="" style="display:; font-size:17px; font-weight:600; color:blue; margin-left:30px;"> {{ numSec[0] }} </b>
    <br><br><br>

    <div style="height:800px; overflow:auto;">
      <table class="mainMngTable" style=" margin : auto; width:98%;">
        <colgroup>
          <col style="width: 100px">
          <col style="width: 160px">
          <!-- <col style="width: 160px">
          <col style="width: 160px">
          <col style="width: 160px">
          <col style="width: 160px">
          <col style="width: 160px"> -->

          <col style="width: 40px">

          <!-- <col style="width: 40px">
          <col style="width: 40px">
          <col style="width: 40px">
          <col style="width: 40px">

          <col style="width: 40px">
          <col style="width: 40px">
          <col style="width: 40px">
          <col style="width: 40px"> -->
          
        </colgroup>
        <tr>
          <th>ID</th>
          <th>current Value</th>
          <!-- <th>Volume</th>
          <th style="background:black; color:white">Vol/s</th>
          <th>Per</th>
          <th style="background:black; color:white">Dif P</th>
          <th style="background:black; color:white">over 1KV/s</th> -->

          <th>Select</th>
          <!-- <th colspan="4" style="background:green;">slo(3)</th>
          <th colspan="4" style="background:orange;">slo(5)</th> -->
          
        </tr>
        
        <tr v-for="p in EventArray">
          <td>{{ p.cd }}</td>
          <td>{{ p.nv }}</td>
          <!-- <td>{{ p.aq }}</td>
          <td>{{ p.difv }}</td>
          <td>{{ p.cr }}</td>
          <td>{{ p.difp }}</td>
          <td>{{ p.times }}</td> -->

          <td>
            <input type="radio" name="optradio">
          </td>

          <!-- <td style="font-size:16px;">{{ p.last3slosh3 }}</td>
          <td style="font-size:16px;">{{ p.last2slosh3 }}</td>
          <td style="font-size:16px;">{{ p.lastslosh3 }}</td>
          <td style="font-size:20px; font-weight:700; color:red">{{ p.slosh3 }}</td>

          <td style="font-size:16px;">{{ p.last3slosh5 }}</td>
          <td style="font-size:16px;">{{ p.last2slosh5 }}</td>
          <td style="font-size:16px;">{{ p.lastslosh5 }}</td>
          <td style="font-size:20px; font-weight:700; color:red">{{ p.slosh5 }}</td> -->
          
        </tr>
      </table>
    </div>

    <br><br><br>

  </div>
</template>

<script>

import axios from 'axios'
import iconv from 'iconv-lite'
import cheerio from 'cheerio'
import request from 'request'
import lodash from 'lodash'
import pythonshell from 'python-shell'
import serverConfig from "../assets/data/server_config.json";
import pyrun from '../service/pyrun'

export default {
  name: 'app',
  data() {
    return {
      last_volume :[],
      last_percent : [],
      EventArray : [],
      receivedData : '0',
      interval : '',
      times :0,
      items:[],
      tempArr:[],
      itemList:[],
      times : [],
      timeHour : '',
      timeMin : '',
      timeSec : '',
      slosh3 : [],
      slosh5 : [],
      slosh10 : [],
      numSec : [],
      refreshSec : '',
      last_slosh3 : [],
      last_slosh5 : [],
      last_slosh10 : [],
      last2_slosh3 : [],
      last2_slosh5 : [],
      last2_slosh10 : [],
      last3_slosh3 : [],
      last3_slosh5 : [],
      last3_slosh10 : [],
      value_times : []
    }
  },
  created() {
    this.setItems_OWN();
  },
  computed() {

  },
  methods: {
    pythonrun() {
      console.log("P RUN sieve")
      // pyrun.pyrun();
      // setInterval(() => {
        pyrun.pyrun();
      // }, 1000);
    },
    setItems_OWN(){
      this.items=[];
      this.EventArray=[];
      this.items = [
        "001250", "015760", "047050", "186230", "317120"
      ]
      for(var i=0; i<this.items.length; i++) {
        this.last_volume[i] = 0;
        this.last_percent[i] = 0;
        this.times[i] = 0;
        this.last_slosh3[i] = 0;
        this.last_slosh5[i] = 0;
        this.last_slosh10[i] = 0;
        this.last2_slosh3[i] = 0;
        this.last2_slosh5[i] = 0;
        this.last2_slosh10[i] = 0;
        this.last3_slosh3[i] = 0;
        this.last3_slosh5[i] = 0;
        this.last3_slosh10[i] = 0;
        this.slosh3[i] = 0;
        this.slosh5[i] = 0;
        this.slosh10[i] = 0;
        this.numSec[i] = 0;
      }

      console.log("Set Items");
    },
    startInterval: function () {
                   this.interval = setInterval(() => {
                     var len = this.EventArray.length;
                     for(var i=0; i<this.items.length; i++) {
                       this.getData(i, this.items[i]);
                     }

                     console.log("EA", this.EventArray);
                   }, 1000);
                 },
    stopInterval() {
      clearInterval(this.interval);
    },

    getData(seq, item) {
      var url = `http://polling.finance.naver.com/api/realtime.nhn?query=SERVICE_ITEM:${item}|SERVICE_RECENT_ITEM:${item}&_callback=`;
      axios.get(url, {
        headers: {
          'Accept-Language' : `ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4`,
          'Accept-Encoding' : "gzip, deflate, sdch",
          'Connection' : "keep-alive",
          'Accept' : "*/*",
          'User-Agent' : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36",
          'Host' : "polling.finance.naver.com",
          'Referer' : "http://finance.naver.com/item/main.nhn?code=035420"
        }
      })
      .then(response => {
        var now = new Date();
        var current_volume = response.data.result.areas[1].datas[0].aq;
        var current_percent = response.data.result.areas[1].datas[0].cr;
        this.receivedData = response.data.result.areas[1].datas[0].aq;
        this.EventArray[seq] = response.data.result.areas[1].datas[0];
        
        if(this.EventArray[seq].nv < this.EventArray[seq].sv) {
          this.EventArray[seq].cr = this.EventArray[seq].cr * -1;
        }

        var diff_volume = current_volume - this.last_volume[seq];
        var diff_percent = current_percent - this.last_percent[seq];

        this.EventArray[seq].difv = diff_volume;
        this.EventArray[seq].difp = diff_percent.toFixed(2);

        if(this.EventArray[seq].difv > 1000) {
          this.times[seq]++;
        }

        // if(this.numSec[seq] == this.refreshSec) {
          this.numSec[seq] = 0;

          this.last3_slosh3[seq] = this.last2_slosh3[seq];
          this.last3_slosh5[seq] = this.last2_slosh5[seq];
          this.last3_slosh10[seq] = this.last2_slosh10[seq];

          this.last2_slosh3[seq] = this.last_slosh3[seq];
          this.last2_slosh5[seq] = this.last_slosh5[seq];
          this.last2_slosh10[seq] = this.last_slosh10[seq];

          this.last_slosh3[seq] = this.slosh3[seq];
          this.last_slosh5[seq] = this.slosh5[seq];
          this.last_slosh10[seq] = this.slosh10[seq];

          this.slosh3[seq] = 0;
          this.slosh5[seq] = 0;
          this.slosh10[seq] = 0;
        // }
        if(diff_percent <= -0.25 || diff_percent >= 0.25) {
          this.slosh3[seq]++;
        }
        if(diff_percent <= -0.5 || diff_percent >= 0.5) {
          this.slosh5[seq]++;
        }
        if(diff_percent <= -1 || diff_percent >= 1) {
          this.slosh10[seq]++;
        }

        this.EventArray[seq].times = this.times[seq];
        this.EventArray[seq].numSec = this.numSec[seq];

        this.EventArray[seq].slosh3 = this.slosh3[seq];
        this.EventArray[seq].slosh5 = this.slosh5[seq];
        this.EventArray[seq].slosh10 = this.slosh10[seq];

        this.EventArray[seq].lastslosh3 = this.last_slosh3[seq];
        this.EventArray[seq].lastslosh5 = this.last_slosh5[seq];
        this.EventArray[seq].lastslosh10 = this.last_slosh10[seq];

        this.EventArray[seq].last2slosh3 = this.last2_slosh3[seq];
        this.EventArray[seq].last2slosh5 = this.last2_slosh5[seq];
        this.EventArray[seq].last2slosh10 = this.last2_slosh10[seq];

        this.EventArray[seq].last3slosh3 = this.last3_slosh3[seq];
        this.EventArray[seq].last3slosh5 = this.last3_slosh5[seq];
        this.EventArray[seq].last3slosh10 = this.last3_slosh10[seq];


        this.EventArray[seq].sec = now.getSeconds();
        this.timeHour = now.getHours();
        this.timeMin = now.getMinutes();
        this.timeSec = now.getSeconds();

        this.last_volume[seq] = response.data.result.areas[1].datas[0].aq;
        this.last_percent[seq] = current_percent;
        this.numSec[seq]++;
      })
      _.sortBy(this.EventArray, 'aq');
    }
  },
  components: {
  }
}

</script>

<style src="./css/total.css"></style>