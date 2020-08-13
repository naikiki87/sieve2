<template>
  <div>
    <h2 v-model="taskID"> [Task 수정] Task ID : {{ taskID }} </h2>
    <br>
    <table style="margin:auto; width:190px; align:center;">
      <tr>
        <td style="width : 80px;" class="add_title"> 연산자 </td>
        <td>
          <select style="width : 99%;" v-model="task_id">
            <option v-for="(item, index) in taskListArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
          </select>
        </td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> 입력 스키마 </td>
        <td>
          <select style="width : 99%;" v-model="input_schema">
            <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
          </select>
        </td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> 리스닝 포트 </td>
        <td> <input style="width : 95%;" type="text" v-model="listening_port"></td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> 수행 서버 </td>
        <td>
          <select style="width : 99%;" v-model="ec_id">
            <option v-for="(item, index) in svrArray" :value="item.id"> {{ item.id }} </option>
          </select>
        </td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> 출력 타입 </td>
        <td>
          <select id="taskSelect" style="width : 99%; border:1px solid;" v-model="output_type">
            <option value = "0"> Socket Output </option>
            <option value = "1"> File Output </option>
            <option value = "2"> DB Output </option>
          </select>
        </td>
      </tr>

      <tr v-if="output_type === '0'"> <!-- socket output -->
        <td style="background:lightgrey;"> 출력 IP </td>
        <td> <input style="width : 95%;" type="text" v-model="otype_0_ip"></td>
      </tr>
      <tr v-if="output_type === '0'">
        <td style="background:lightgrey;"> 출력 포트 </td>
        <td> <input style="width : 95%;" type="text" v-model="otype_0_port"></td>
      </tr>

      <tr v-if="output_type === '1'"> <!-- file output -->
        <td style="background:lightgrey;"> 출력파일명 </td>
        <td> <input style="width : 95%;" type="text" v-model="otype_1_file"></td>
      </tr>

      <tr v-if="output_type === '2'"> <!-- DB output -->
        <td style="background:lightgrey;"> DB IP </td>
        <td> <input style="width : 95%;" type="text" v-model="otype_2_ip"></td>
      </tr>
      <tr v-if="output_type === '2'">
        <td style="background:lightgrey;"> DB 명 </td>
        <td> <input style="width : 95%;" type="text" v-model="otype_2_name"></td>
      </tr>
      <tr v-if="output_type === '2'">
        <td style="background:lightgrey;"> DB ID </td>
        <td> <input style="width : 95%;" type="text" v-model="otype_2_id"></td>
      </tr>
      <tr v-if="output_type === '2'">
        <td style="background:lightgrey;"> DB PW </td>
        <td> <input style="width : 95%;" type="text" v-model="otype_2_pw"></td>
      </tr>
      <tr v-if="output_type === '2'">
        <td style="background:lightgrey;"> Table 명 </td>
        <td> <input style="width : 95%;" type="text" v-model="otype_2_table"></td>
      </tr>

      <tr>
        <td style="width : 80px;" class="add_title"> CONFIG </td>
        <td> <input style="width : 95%;" type="text" v-model="config"> </td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> HB TASK </td>
        <td> <input style="width : 95%;" type="text" v-model="heartbeat_task"></td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> HB JOB </td>
        <td> <input style="width : 95%;" type="text" v-model="heartbeat_job"></td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> 출력 스키마 </td>
        <td>
          <select style="width : 99%;" v-model="output_schema">
            <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
          </select>
        </td>
      </tr>
    </table>
    <br> <br>
    <p v-model="position_x"> {{position_x}} </p>
    <p v-model="position_y"> {{position_y}} </p>
    <p v-model="linkto"> {{linkto}} </p>
    <button id="modifyButton" v-on:click="abcd"> SAVE </button>
  </div>
</template>

<script>
import axios from 'axios'
import serverConfig from "../assets/data/server_config.json";

export default {
  data() {
    return {
      taskID : '',
      otype_0_ip : '',
      otype_0_port : '',
      otype_1_file : '',
      otype_2_ip : '',
      otype_2_name : '',
      otype_2_id : '',
      otype_2_pw : '',
      otype_2_table : '',
      schemaArray : [],
      taskListArray : [],
      svrArray : [],
      svrConfig : serverConfig,
      svrAddr : '',
      columnArray : [],
      jobId : '',
      taskArray : [],
      mode : "CREATE",
      task_type : '',
      task_id : '',
      input_schema : '',
      output_schema : '',
      listening_port : '',
      ec_id : '',
      output_type : '',
      heartbeat_task : '',
      heartbeat_job : '',
      config : '',
      add_jobSchema : '',
      taskcnt : '',
      localcnt : '',
      div_pos_x : '0',
      div_pos_y : '0',
      tempID : 0
    }
  },
  created() {
    this.svrAddr = this.svrConfig.hostserver;
    var id = this.getParameterByName('taskID');
    this.taskID = id;
    this.loadTaskList();
    this.loadSchema();
    this.loadSvr();
    this.loadData();
  },
  methods : {
    abcd() {
      console.log("Hello");
      localStorage.removeItem("loglevel:webpack-dev-server");

      var params = {
        id : this.taskID,
        task_id : this.task_id,
        input_schema_id : this.input_schema,
        listening_port : this.listening_port,
        ec_id : this.ec_id,
        output_type : this.output_type,
        config : this.config,
        heartbeat_task_id : this.heartbeat_task,
        heartbeat_job_id : this.heartbeat_job,
        output_schema_id : this.output_schema,
        position_x : this.position_x,
        position_y : this.position_y,
        linkto : this.linkto
      }

      var api = "http://" + this.svrAddr + ":3000/users/job_tasks/update";

      axios
      .post(api, params)
      .then( response => {
      })
      .catch( response => { console.log(response) } );
      alert("Saved");
      close();
    },
    updateTask() {
      localStorage.removeItem("loglevel:webpack-dev-server");
      for(var i=0; i<localStorage.length; i++) {
        var output = localStorage.getItem(i);
        var arr = JSON.parse(output);
        var params = {
          id : arr[0],
          job_id : arr[1],
          task_id : arr[2],
          input_schema_id : arr[3],
          listening_port : arr[4],
          ec_id : arr[5],
          output_type : arr[6],
          config : arr[7],
          heartbeat_task_id : arr[8],
          heartbeat_job_id : arr[9],
          output_schema_id : arr[10],
          position_x : arr[11],
          position_y : arr[12],
          linkto : arr[13]
        }

        var api = "http://" + this.svrAddr + ":3000/users/job_tasks/update";

        axios
        .post(api, params)
        .then( response => {
        })
        .catch( response => { console.log(response) } );
      }
      alert("Saved");
    },
    loadData() {
      var output, arr;
      localStorage.removeItem("loglevel:webpack-dev-server");

      for(var i=0; i<localStorage.length; i++) {
        output = localStorage.getItem(i);
        arr = JSON.parse(output);

        if(arr[0] == this.taskID) {
          console.log(arr[0]);
          console.log(arr[1]);
          this.task_id = arr[2];
          this.input_schema = arr[3];
          this.listening_port = arr[4];
          this.ec_id = arr[5];
          this.output_type = arr[6];
          this.config = arr[7];
          this.heartbeat_task = arr[8];
          this.heartbeat_job = arr[9];
          this.output_schema = arr[10];
          this.position_x = arr[11];
          this.position_y = arr[12];
          this.linkto = arr[13];
        }
      }
    },
    loadSvr() {
      var api = "http://" + this.svrAddr + ":3000/users/engine_computer";
      axios
      .get(api)
      .then(response => {
        this.svrArray = response.data;
      })
      .catch(err => {
        console.log(err);
      });
    },
    loadTaskList() {
      //console.log("**** LOAD Task ****");
      var api = "http://" + this.svrAddr + ":3000/users/tasks";
      axios
      .get(api)
      .then(response => {

        this.taskListArray = response.data;
      })
      .catch(err => {
        console.log(err);
      });
    },
    loadSchema() {
      var api = "http://" + this.svrAddr + ":3000/users/cell_schemas";
      axios
      .get(api)
      .then(response => {
        this.schemaArray = response.data;
      })
      .catch(err => {
        console.log(err);
      });
    },
    getParameterByName(name) {
      name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
      var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
      results = regex.exec(location.search);
      return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    },
  }
}
</script>

<style src="./css/total.css"></style>
