<template>
  <div>
    <div class="modify_window_title">
      <h2 v-model="taskID"> Task ID : {{ taskID }} </h2>
    </div>
    <br>
    <table style="margin:auto; align:center;">
      <tr>
        <td style="width : 80px;" class="add_title"> 연산자 </td>
        <td>
          <h3 v-model="temptask_name" class="modify_window_op_title"> {{ temptask_name }} </h3>
        </td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> 수행 서버 </td>
        <td>
          <select style="width : 99%;" v-model="tempec_id" disabled>
            <option v-for="(item, index) in svrArray" :value="item.id"> ({{ item.id }}) {{item.ip_address}} </option>
          </select>
        </td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> 수행 포트 </td>
        <td> <input style="width : 95%;" type="text" v-model="templistening_port" disabled></td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> 목적 IP </td>
        <td> <input style="width : 95%;" type="text" v-model="tempdest_ip" disabled></td>
      </tr>

      <tr>
        <td style="width : 80px;" class="add_title"> 목적 Port </td>
        <td> <input style="width : 95%;" type="text" v-model="tempdest_port" disabled></td>
      </tr>

      <tr>
        <td style="width : 80px;" class="add_title"> IN Schema </td>
        <td>
          <select style="width : 99%;" v-model="tempinput_schema">
            <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
          </select>
        </td>
      </tr>
      <tr>
        <td style="width : 80px;" class="add_title"> OUT Schema </td>
        <td> 
          <select style="width : 99%;" v-model="tempoutput_schema" disabled>
            <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
          </select>
        </td>
      </tr>
    </table>
    <br> <br>
    <button class="longBtn" v-on:click="modify_task"> SAVE </button>
  </div>
</template>

<script>
import axios from 'axios'
import serverConfig from '../../../config/index'
export default {
  data() {
    return {
      svrConfig : serverConfig,
      api_addr : '',
      taskID : '',
      temptask_name : '',
      svrArray : '',
      templistening_port : '',
      tempdest_ip : '',
      tempdest_port : '',
      tempinput_schema : '',
      tempec_id : '',
      schemaArray : []
    }
  },
  async created() {
    this.taskID = await this.getParameterByName('task')
    this.api_addr = "http://" + this.svrConfig.dev.host + ':' + this.svrConfig.dev.sport;
    await this.loadSvr()
    await this.loadSchema()
    await this.loadModData(this.taskID)
  },
  methods : {
    set_out_schema() {
      var out;
      if(this.temptask_id == 0) {
        out = this.tempinput_schema
      }
      return out
    },
    async modify_task() {
      console.log("modi t r")
      var params = {
        id : this.taskID,
        task_id : this.temptask_id,
        input_schema_id : this.tempinput_schema,
        listening_port : this.templistening_port,
        ec_id : this.tempec_id,
        output_type : this.tempoutput_type,
        config : this.tempconfig,
        heartbeat_task_id : this.tempheartbeat_task,
        heartbeat_job_id : this.tempheartbeat_job,
        // output_schema_id : this.tempoutput_schema,
        output_schema_id : await this.set_out_schema(),
        position_x : this.tempposition_x,
        position_y : this.tempposition_y,
        linkto : this.templinkto,
        linkfrom : this.templinkfrom,
        dest_ip : this.tempdest_ip,
        dest_port : this.tempdest_port
      }
      var api = this.api_addr + "/users/job_tasks/update";
      await axios.post(api, params)
      close()
    },
    async loadModData(tid) {
      var params = {
        id : tid
      }
      var api = this.api_addr + "/users/get_taskinfo";
      
      var res = (await axios.post(api, params)).data[0]

      this.temptask_name = res.name
      this.temptask_id = res.task_id
      this.tempinput_schema = res.input_schema_id
      this.templistening_port = res.listening_port
      this.tempec_id = res.ec_id
      this.tempoutput_type = res.output_type
      this.tempconfig = res.config
      this.tempheartbeat_task = res.heartbeat_task_id
      this.tempheartbeat_job = res.heartbeat_job_id
      this.tempoutput_schema = res.output_schema_id
      this.tempposition_x = res.position_x
      this.tempposition_y = res.position_y
      this.templinkto = res.linkto
      this.templinkfrom = res.linkfrom
      this.tempdest_ip = res.dest_ip
      this.tempdest_port = res.dest_port
    },
    async loadSvr() {
      var api = this.api_addr + "/users/engine_computer";
      this.svrArray = (await axios.get(api)).data
    },
    async loadSchema() {
      var api = this.api_addr + "/users/cell_schemas";
      this.schemaArray = (await axios.get(api)).data
      console.log("schema : ", this.schemaArray)
    },
    getParameterByName(name) {
      name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
      var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
      results = regex.exec(location.search);
      return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }
  }
}

</script>

<style src="../css/total.css"></style>
