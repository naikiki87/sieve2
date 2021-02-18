<template>
  <div>
    <table>
      <tr style="height:40px;">
        <td style="background:navy; width:2000px; color:white">
          DATA_VIEW / Task ID : <input type="text" v-model="taskID" value="ssss" style="background:navy; border:0px solid; color:white; font-size:20px; font-weight:600;">
        </td>
      </tr>
      
      <tr>
        <button class="longBtnSAVE" v-on:click="func_test"> VIEW </button>
      </tr>

      <tr style="height:40px;">
        <td style="border:1px solid; width:2000px; height:500px;">
          <input type="text" v-model="view_data" style="height:500px; width:450px; border:0px solid; font-size:15px; font-weight:600;">
        </td>
      </tr>


    </table>
    <br>
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

      svrArray : '',
      schemaArray : [],
      colArray : [],
      agg_HAV_array : [],

      temp_QUERY : '',

      running_edge : '',

      taskID : '',
      task_type_ID : '',
      temptask_name : 'sadfsadf',
      check_config : '',
      temptask_id : '',
      tempinput_schema : '',
      templistening_port : '',
      tempec_id : '',
      tempoutput_type : '',
      tempconfig : '',
      tempheartbeat_task : '',
      tempheartbeat_job : '',
      tempoutput_schema : '',
      tempposition_x : '',
      tempposition_y : '',
      templinkto : '',
      templinkfrom : '',
      tempdest_ip : '',
      tempdest_port : '',
      
      param_data : [],
      param_cnt : '',

      mod_win : -1,

      agg_GB_attr : '',
      agg_SEL_type : '',
      agg_SEL_attr : '',
      agg_OB_attr : '',
      agg_OB_order : '',
      agg_HAV_type : '',
      agg_HAV_attr : '',
      agg_HAV_op : '',
      agg_HAV_val : '',

      agg_GB_list : '',
      agg_SEL_list : '',
      agg_OB_list : '',
      agg_HAV_list : '',

      agg_GB_array : [],
      agg_SEL_array : [],
      agg_OB_array : [],
      agg_HAV_array : [],
      view_data : "data"
    }
  },
  async created() {
    this.api_addr = "http://" + this.svrConfig.dev.host + ':' + this.svrConfig.dev.sport;
    this.taskID = await this.getParameterByName('task')
    this.user_id = await this.getParameterByName('user_id')
    this.job_id = await this.getParameterByName('job_id')
    this.ec_id = await this.getParameterByName('ec_id')
    var type_and_check = await this.get_task_type(this.taskID)
    this.task_type_ID = type_and_check[0]
    this.check_config = type_and_check[1]

    if(this.check_config == -1) {
      await this.init_config(this.task_type_ID)
    }
    await this.loadSvr()
    await this.loadSchema()
    await this.loadModData(this.taskID)
    this.running_edge = await this.get_edge_ip()
  },
  methods : {
    sleep(ms) {
      console.log("sleep")
      var ts1 = new Date().getTime() + ms;
      var ts2 = new Date().getTime(); 
      while (ts2<ts1) {
        ts2 = new Date().getTime(); 
      }
    },
    async func_test() {
      var params = {
        user_id : this.user_id,
        ec_id : this.ec_id,
        lis_port : this.templistening_port
      }
      var api = this.api_addr + "/users/dataview";
      
      var data = (await axios.post(api, params)).data
      this.view_data = data.split('/')

      console.log(typeof(this.view_data))
      console.log(this.view_data.length)

      // // console.log("success : ", this.view_data)

      // this.view_data = "0 0"
      // console.log(this.view_data)
      // // }, 1000)
      // this.sleep(1000)
      // this.view_data = "1 1"

    },
    flush_agg_SEL() {
      this.agg_SEL_list = ''
      for(var i=0; i<this.agg_SEL_array.length; i++) {
        this.agg_SEL_array.pop()
      }
    },
    flush_agg_GB() {
      this.agg_GB_list = ''
      for(var i=0; i<this.agg_GB_array.length; i++) {
        this.agg_GB_array.pop()
      }
    },
    flush_agg_OB() {
      this.agg_OB_list = ''
      for(var i=0; i<this.agg_OB_array.length; i++) {
        this.agg_OB_array.pop()
      }
    },
    flush_agg_HAV() {
      this.agg_HAV_list = ''
      for(var i=0; i<this.agg_HAV_array.length; i++) {
        this.agg_HAV_array.pop()
      }
    },
    make_total_query() {
      console.log("func test1")
      var temp = "select "

      ///// Select clause
      if(this.agg_SEL_array.length == 0) {
        temp = temp + "*"
      }
      else {
        for(var i=0; i<this.agg_SEL_array.length; i++) {
          if(i == 0) {
            temp = temp + this.agg_SEL_array[i]
          }
          else {
            temp = temp + ', ' + this.agg_SEL_array[i]
          }
        }
      }

      temp = temp + ' ' + "from " + this.tempinput_schema
      temp = temp + ' '

      ///// group by clause
      if(this.agg_GB_array.length != 0) {
        temp = temp + "GROUP BY "
        for(var i=0; i<this.agg_GB_array.length; i++) {
          if(i == 0) {
            temp = temp + this.agg_GB_array[i]
          }
          else {
            temp = temp + ', ' + this.agg_GB_array[i]
          }
        }
      }

      ///// order by clause
      if(this.agg_OB_array.length != 0) {
        temp = temp + "ORDER BY "
        for(var i=0; i<this.agg_OB_array.length; i++) {
          if(i == 0) {
            temp = temp + this.agg_OB_array[i]
          }
          else {
            temp = temp + ', ' + this.agg_OB_array[i]
          }
        }
      }

      ///// order by clause
      if(this.agg_HAV_array.length != 0) {
        temp = temp + "HAVING "
        for(var i=0; i<this.agg_HAV_array.length; i++) {
          if(i == 0) {
            temp = temp + this.agg_HAV_array[i]
          }
          else {
            temp = temp + ', ' + this.agg_HAV_array[i]
          }
        }
      }

      console.log("ddd : ", temp)
      this.temp_QUERY = temp
    },
    add_agg_HAV() {
      console.log("func test1 : ", this.agg_HAV_type, this.agg_HAV_attr, this.agg_HAV_op, this.agg_HAV_val)
      if(this.agg_HAV_type != "" && this.agg_HAV_attr != "" && this.agg_HAV_op != "" && this.agg_HAV_val != "") {
        var temp = this.agg_HAV_type + ' ' + this.agg_HAV_attr + this.agg_HAV_op + this.agg_HAV_val
        this.agg_HAV_array.push(temp)
        this.agg_HAV_list = JSON.stringify(this.agg_HAV_array) 
      }
    },
    add_agg_OB() {
      console.log("func test1")
      console.log("this : ", this.agg_OB_attr, this.agg_OB_order)
      if(this.agg_OB_attr != "" && this.agg_OB_order != "") {
        var temp = this.agg_OB_attr + ' ' + this.agg_OB_order
        this.agg_OB_array.push(temp)
        this.agg_OB_list = JSON.stringify(this.agg_OB_array) 
      }
    },
    add_agg_SEL() {
      console.log("this : ", this.agg_SEL_type, this.agg_SEL_attr)
      if(this.agg_SEL_type != "" && this.agg_SEL_attr != "") {
        var temp = this.agg_SEL_type + '(' + this.agg_SEL_attr + ')'
        this.agg_SEL_array.push(temp)
        this.agg_SEL_list = JSON.stringify(this.agg_SEL_array) 
      }
    },
    add_agg_GB() {
      console.log("functest1")
      console.log("this. : ", this.agg_GB_attr)

      if(this.agg_GB_attr != "") {
        this.agg_GB_array.push(this.agg_GB_attr)
        this.agg_GB_list = JSON.stringify(this.agg_GB_array)
      }
    },
    async get_edge_ip() {
      var out;
      for(var i=0; i<this.svrArray.length; i++) {
        if(this.svrArray[i].id == this.tempec_id) {
          out = this.svrArray[i].ip_address
          break
        }
      }
      return out
    },
    async get_task_type(index) {
      var params = {
        id : index
      }
      var api = this.api_addr + "/users/get_taskinfo";
      var res = (await axios.post(api, params)).data[0]
      var res_arr = [res.task_id, res.config]

      return res_arr
    },
    async init_config(index) {
      console.log("init config : ", index)
      var params = {
        task_id : index
      }
      var api = this.api_addr + "/users/tasks/get_params";
      var res = (await axios.post(api, params)).data

      console.log("res : ", res)
      var temp_config = []
      for(var i=0; i<res.length; i++) {
        var temp = new Object()
        temp.name = res[i].name
        temp.val = ''
        temp_config.push(temp)
      }

      var params = {
        id : this.taskID,
        config : JSON.stringify(temp_config)
      }
      var api = this.api_addr + "/users/job_tasks/init_config";
      await axios.post(api, params)
    },
    set_out_schema() {
      var out;
      switch(this.temptask_id) {
        case 0 :
          out = this.tempinput_schema
          break
        default :
          out = -1
          break
      }
      return out
    },
    make_parameter() {
      var config = []
      for(var i=0; i<this.param_data.length; i++) {
        var temp = new Object()
        temp.name = this.param_data[i].name
        temp.val = this.param_data[i].val
        config.push(temp)
      }

      if(this.temptask_id == 4) {     // Aggregation
        var temp = new Object()
        temp.name = "QUERY"
        temp.val = this.temp_QUERY
        config.push(temp)
      }
      return JSON.stringify(config)
    },
    async update_task() {
      console.log("modi t r")
      var params = {
        id : this.taskID,
        task_id : this.temptask_id,
        input_schema_id : this.tempinput_schema,
        listening_port : this.templistening_port,
        ec_id : this.tempec_id,
        output_type : this.tempoutput_type,
        config : await this.make_parameter(),
        heartbeat_task_id : this.tempheartbeat_task,
        heartbeat_job_id : this.tempheartbeat_job,
        output_schema_id : await this.set_out_schema(),
        position_x : this.tempposition_x,
        position_y : this.tempposition_y,
        linkto : this.templinkto,
        linkfrom : this.templinkfrom,
        dest_ip : this.tempdest_ip,
        dest_port : this.tempdest_port
      }

      console.log('update : ', params)
      var api = this.api_addr + "/users/job_tasks/update";
      await axios.post(api, params)

      await this.loadModData(this.taskID)
    },
    async loadModData(tid) {
      var params = {
        id : tid
      }
      var api = this.api_addr + "/users/get_taskinfo";
      var res = (await axios.post(api, params)).data[0]

      this.mod_win = res.task_id

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

      await this.loadSchemaColumn(res.input_schema_id)

      // custom parameter
      this.param_data = JSON.parse(this.tempconfig)
      this.param_cnt = this.param_data.length
    },
    async loadSchemaColumn(index) {
      var params = {
        schema_id : index
      }
      var api = this.api_addr + "/users/cell_columns";
      this.colArray = (await axios.post(api, params)).data
    },
    async loadSvr() {
      var api = this.api_addr + "/users/engine_computer";
      this.svrArray = (await axios.get(api)).data
    },
    async loadSchema() {
      var api = this.api_addr + "/users/cell_schemas";
      this.schemaArray = (await axios.get(api)).data
    },
    getParameterByName(name) {
      name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
      var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
      results = regex.exec(location.search);
      return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    },
    tabChange(index, tab_content) {
      var x = document.getElementsByClassName("aggtabcontent");
      var i;
      for (i = 0; i < x.length; i++) {
          x[i].style.display = 'none';
      }
      document.getElementById(tab_content).style.display = 'block';
      var x = document.getElementsByClassName("tabmenu");
      var i;
      for (i = 0; i < x.length; i++) {
          x[i].className = 'tabmenu';
      }
      document.getElementById(index).className = 'tabmenu active';
    }
  },
  components: {
  }
}

</script>

<style src="../css/total.css"></style>
