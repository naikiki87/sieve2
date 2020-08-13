<template>
  <div>
    <!-- <button v-on:click="get_comp_name"> CORP LIST </button>
    <button v-on:click="setItems_OWN"> OWN </button> -->

    <!-- <button v-on:click="setItems_SUBTUNNEL"> sub </button> -->
    <!-- <button v-on:click="get_comp_name"> compName </button> -->
    <br>
    <br>
    Refresh(sec) : <input style="width:50px; height:20px; text-align:center" v-model="refreshSec" v-on:keyup.enter="startInterval"></input>
    <button style="font-weight:700; width:100px; height:30px; margin-left:20px; margin-right:20px;" v-on:click="startInterval"> START </button>
    <button style="font-weight:700; width:100px; height:30px;" v-on:click="stopInterval"> STOP </button>
    <button style="font-weight:700; width:100px; height:30px;" v-on:click="pythonrun"> PRUN </button>
    <div style="display:none;"> volume : {{ receivedData }}</div>
    <b align="left" style="display:; font-size:17px; font-weight:600; margin-left:30px;"> {{ timeHour }} : {{ timeMin }} : {{ timeSec }} </b>
    <b align="" style="display:; font-size:17px; font-weight:600; color:blue; margin-left:30px;"> {{ numSec[0] }} </b>
    <br><br><br>

    <div style="height:800px; overflow:auto;">
      <table class="mainMngTable" style=" margin : auto; width:98%;">
        <colgroup>
          <col style="width: 100px">
          <col style="width: 160px">
          <col style="width: 160px">
          <col style="width: 160px">
          <col style="width: 160px">
          <col style="width: 160px">
          <col style="width: 160px">

          <col style="width: 40px">

          <col style="width: 40px">
          <col style="width: 40px">
          <col style="width: 40px">
          <col style="width: 40px">

          <col style="width: 40px">
          <col style="width: 40px">
          <col style="width: 40px">
          <col style="width: 40px">

          <!-- <col style="width: 40px">
          <col style="width: 40px">
          <col style="width: 40px"> -->

          
        </colgroup>
        <tr>
          <th>ID</th>
          <th>Value</th>
          <th>Volume</th>
          <th style="background:black; color:white">Vol/s</th>
          <th>Per</th>
          <th style="background:black; color:white">Dif P</th>
          <th style="background:black; color:white">over 1KV/s</th>

          <th>Select</th>
          <th colspan="4" style="background:green;">slo(3)</th>
          <th colspan="4" style="background:orange;">slo(5)</th>
          
          <!-- <th colspan="4" style="background:brown;">slo(10)</th> -->

        </tr>
        <!-- <tr v-for="p in EventArray" v-if="p.difv>100 && p.cr<0"> -->
        <!-- <tr v-for="p in EventArray" v-if="p.cr<0"> -->
        <!-- <tr v-for="p in EventArray" v-if="p.lastslosh3!=0 || p.last2slosh3!=0 || p.last3slosh3!=0"> -->
          <tr v-for="p in EventArray" v-if="p.lastslosh3>=3">
        <!-- <tr v-for="p in EventArray"> -->
          <td>{{ p.cd }}</td>
          <td>{{ p.nv }}</td>
          <td>{{ p.aq }}</td>
          <td>{{ p.difv }}</td>
          <td>{{ p.cr }}</td>
          <td>{{ p.difp }}</td>
          <td>{{ p.times }}</td>

          <td>
            <input type="radio" name="optradio">
          </td>

          <td style="font-size:16px;">{{ p.last3slosh3 }}</td>
          <td style="font-size:16px;">{{ p.last2slosh3 }}</td>
          <td style="font-size:16px;">{{ p.lastslosh3 }}</td>
          <td style="font-size:20px; font-weight:700; color:red">{{ p.slosh3 }}</td>

          <td style="font-size:16px;">{{ p.last3slosh5 }}</td>
          <td style="font-size:16px;">{{ p.last2slosh5 }}</td>
          <td style="font-size:16px;">{{ p.lastslosh5 }}</td>
          <td style="font-size:20px; font-weight:700; color:red">{{ p.slosh5 }}</td>

          

          <!-- <td style="font-size:16px;">{{ p.last3slosh10 }}</td>
          <td style="font-size:16px;">{{ p.last2slosh10 }}</td>
          <td style="font-size:16px;">{{ p.lastslosh10 }}</td>
          <td style="font-size:20px; font-weight:700; color:red">{{ p.slosh10 }}</td> -->
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
    testtest(){
      console.log(this.refreshSec);
    },
    getCorpList() {
      console.log("A", this.itemList[0]);
      console.log("A", this.itemList[0][0].id);
    },
    get_comp_name() {
      axios.get("https://finance.naver.com/item/sise.nhn?code=005930")
      .then(html => {
        const $ = cheerio.load(html.data);
        var text = $(".wrap_company a").text().trim();
        var text2 = iconv.decode(text, "euc-kr");
        console.log("2", text2);
      })
    },
    setItems_OWN(){
      this.items=[];
      this.EventArray=[];
      // this.items = [
      //   "001250", "015760", "047050", "186230", "317120"
      // ]
      this.items = [
        "005930", "131370", "017180", "059090", "008350", "294140", "002720", "002360", "096530", "092190",
        "026150", "025550", 347140, 198080, 237820, 340350, "092190", 204270, 274090, 294630, 344050,
        213500, 210540, "060480", 176750, 206640, 208640, 193250 ,204630 ,200470 ,200670 ,200350 ,208710 ,189860 ,"067390" ,"080580" ,208350 ,160550 ,124500 ,"028260" ,208890 ,142280 ,187220 ,208370 ,208140 ,149980 ,"084650",
210120, 200710, 200780, 207760, 196170 ,206400, 178920, 140520,150440,173940,205470,112610,200230,194510,204840,204620,196490,"018260",149010,205500,205100,191420,192440,182690,143540,121060,203650,"041920",194480,204320,203690,192410,198440,202960,200880,187270,192400,177830,"090410","004650",192390,"059120",
200130,133820,203400,105550,136660,199150,199800,196450,"071850","027410",187790,192250,192820,138080,108790,"053300","084440",154040,"090850",140660,189330,
189540,189350,184230,178600,140290,"049080",150840,107640,"085810",185750,170030,"067570",134580,"006880",182360,171120,171010,161570,183410,138360,183190,"076610",168330,"064350",185190,131970,150900,183350,170920,119850,180640,"092040",181710,130500,170790,151860,175330,"089600",172580,114920,126340,158300,
"086460","064850",179720,101360,116100,950130,155660,141080,168490,170900,158310,"097800",104540,159580,114810,153490,
"099190",141020,159910,950110,"046970",113810,"013870",141070,155650,"037560",149950,163560,161890,161390,121850,149940,161000,159650,151910,"068400","097520",153460,
106520,155900,"014710",141000,153360,144620,"072950",152550,"079980",143240,126870,147830,"091590",145270,140410,104830,127120
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
    setItems_SUBTUNNEL() {
      this.items=[];
      this.EventArray=[];
      this.items = [
        "026150",
        "025550"
      ];

      for(var i=0; i<this.items.length; i++) {
        this.last_volume[i] = 0;
        this.times[i] = 0;

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
        
        var temp = "123";

        // this.EventArray[seq].fpvalue[0] = temp;
        // this.EventArray[seq].fpvalue[0] = temp;
        this.EventArray[seq].fpvalue = temp;

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

        // console.log("Refresh", this.refreshSec);

        if(this.numSec[seq] == this.refreshSec) {
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
        }
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