<template>
  <div>
    <table>
      <tr style="height:40px;">
        <td style="background:navy; width:2000px; color:white">
          MODIFY / Task ID : <input type="text" v-model="taskID" value="ssss" style="background:navy; border:0px solid; color:white; font-size:20px; font-weight:600;">
        </td>
        <td style="background:; width:250px;">
          <button class="longBtnSAVE" v-on:click="update_task"> SAVE </button>
        </td>
        <!-- <td>
          <button class="longBtn" v-on:click="func_test"> TEST </button>
        </td> -->
      </tr>
    </table>
    <br>

    <div style="background:; width:35%; height:480px; float:left;">
      <div style="background:; width:100%; height:180px; float:left;">
        <div class="add_title" style="color:blue;"> BASE Parameter : </div>
        <br>
        <table id="base_table" style="margin:auto; align:center;">
          <tr>
            <td> <input class="mod_base_title" disabled value="Task Type : "> </td>
            <td> <input class="mod_base_value" type="text" v-model="temptask_name" disabled> </td>
          </tr>
          <tr>
            <td> <input class="mod_base_title" disabled value="Running Server : "> </td>
            <td> <input class="mod_base_value" type="text" v-model="running_edge" disabled></td>
          </tr>
          <tr>
            <td> <input class="mod_base_title" disabled value="Listening Port : "> </td>
            <td> <input class="mod_base_value" type="text" v-model="templistening_port" disabled></td>
          </tr>
          <tr>
            <td> <input class="mod_base_title" disabled value="Destination IP : "> </td>
            <td> <input class="mod_base_value" type="text" v-model="tempdest_ip" disabled></td>
          </tr>

          <tr>
            <td> <input class="mod_base_title" disabled value="Destination Port : "> </td>
            <td> <input class="mod_base_value" type="text" v-model="tempdest_port" disabled></td>
          </tr>

          <tr v-if="mod_win == 0">
            <td> <input class="mod_base_title" disabled value="IN Schema : "> </td>
            <td>
              <select style="width : 99%;" v-model="tempinput_schema">
                <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
              </select>
            </td>
          </tr>
          <!-- <tr>
            <td style="width : 80px;" class="add_title"> OUT Schema </td>
            <td> 
              <select style="width : 99%;" v-model="tempoutput_schema" disabled>
                <option v-for="(item, index) in schemaArray" :value="item.id"> {{ item.id }} - {{ item.name }} </option>
              </select>
            </td>
          </tr> -->
        </table>
      </div>

      <div style="background:; width:100%; height:300px; float:left;">
        <div v-model="tempinput_schema" class="add_title"> [ Input Schema : {{ tempinput_schema}} ] </div>
        <br>
        <table class="modify_schema_table" style="margin:auto; width:50%;">
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>type</th>
          </tr>
            
          <tr v-for="n in colArray.length">
            <td> <input v-model="colArray[n-1].id" style="width:80px; border:0px;" disabled> </td>
            <td> <input v-model="colArray[n-1].name" style="width:100px; border:0px;" disabled> </td>
            <td> <input v-model="colArray[n-1].type_name" style="width:100px; border:0px;" disabled> </td>
          </tr>
        </table>
      </div>
    </div>

    <div style="background:; width:63%; height:480px; float:left;">
      <div class="add_title" style="width:150px; color:blue"> CUSTOM Parameter : </div>
      <br>
      <table id="param_table" style="margin:auto; align:center;">
        <tr v-for="n in param_cnt">
          <td> <input class="mod_base_title" v-model="param_data[n-1].name" disabled> </td>
          <td> <input class="mod_base_value" v-model="param_data[n-1].val"> </td>
        </tr>
      </table>

      <br><br>
      <div v-if="mod_win == 4" class="add_title" style="width:100%"> MAKE Query </div>
      <br>
      <table v-if="mod_win == 4" class="make_query_table" style="margin:auto; width:100%;">
        <tr>
          <th colspan="2"> CATEGORY </th>
          <th> VAL </th>
          <th> ADD </th>
          <th colspan="2"> LIST </th>
        </tr>
        <tr>
          <td rowspan="2"> <input class="make_query_title_1" disabled value="SELECT"> </td>
          <td> <input class="make_query_title_2" disabled value="TYPE"> </td>
          <td> 
            <select class="make_query_value" v-model="agg_SEL_type">
              <option value = "Group"> Group </option>
              <option value = "MAX"> MAX </option>
              <option value = "MIN"> MIN </option>
              <option value = "SUM"> SUM </option>
              <option value = "AVG"> AVG </option>
              <option value = "Count"> Count </option>
              <option value = "Renew"> Renew </option>
            </select>
          </td>
          <td rowspan="2"> <button class="shortBtnQuery" v-on:click="add_agg_SEL"> ADD </button> </td>
          <td rowspan="2"> <input class="make_query_list" v-model="agg_SEL_list"> </td>
          <td rowspan="2"> <button class="shortBtnQueryFlush" v-on:click="flush_agg_SEL"> FLUSH </button> </td>
        </tr>

        <tr>
          <td> <input class="make_query_title_2" disabled value="ATTR"> </td>
          <td> 
            <select class="make_query_value" v-model="agg_SEL_attr">
              <option v-for="(item, index) in colArray" :value="item.name"> {{ item.name }} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td> <input class="make_query_title_1" disabled value="GROUP BY"> </td>
          <td> <input class="make_query_title_2" disabled value="ATTR"> </td>
          <td> 
            <select class="make_query_value" v-model="agg_GB_attr">
              <option v-for="(item, index) in colArray" :value="item.name"> {{ item.name }} </option>
            </select>
          </td>
          <td> <button class="shortBtnQuery" v-on:click="add_agg_GB"> ADD </button> </td>
          <td> <input class="make_query_list" v-model="agg_GB_list"> </td>
          <td> <button class="shortBtnQueryFlush" v-on:click="flush_agg_GB"> FLUSH </button> </td>
        </tr>

        <tr>
          <td rowspan="2"> <input class="make_query_title_1" disabled value="ORDER BY"> </td>
          <td> <input class="make_query_title_2" disabled value="ATTR"> </td>
          <td> 
            <select class="make_query_value" v-model="agg_OB_attr">
              <option v-for="item in agg_SEL_array" :value="item"> {{ item }} </option>
            </select>
          </td>
          <td rowspan="2"> <button class="shortBtnQuery" v-on:click="add_agg_OB"> ADD </button> </td>
          <td rowspan="2"> <input class="make_query_list" v-model="agg_OB_list"> </td>
          <td rowspan="2"> <button class="shortBtnQueryFlush" v-on:click="flush_agg_OB"> FLUSH </button> </td>
        </tr>

        <tr>
          <td> <input class="make_query_title_2" disabled value="ORDER"> </td>
          <td> 
            <select class="make_query_value" v-model="agg_OB_order">
              <option value = "ASC"> ASC </option>
              <option value = "DESC"> DESC </option>
            </select>
          </td>
        </tr>

        <tr>
          <td rowspan="4"> <input class="make_query_title_1" disabled value="HAVING"> </td>
          <td> <input class="make_query_title_2" disabled value="TYPE"> </td>
          <td> 
            <select class="make_query_value" v-model="agg_HAV_type">
              <option value = "Group"> Group </option>
              <option value = "MAX"> MAX </option>
              <option value = "MIN"> MIN </option>
              <option value = "SUM"> SUM </option>
              <option value = "AVG"> AVG </option>
              <option value = "Count"> Count </option>
              <option value = "Renew"> Renew </option>
            </select>
          </td>
          <td rowspan="4"> <button class="shortBtnQuery" v-on:click="add_agg_HAV"> ADD </button> </td>
          <td rowspan="4"> <input class="make_query_list" v-model="agg_HAV_list"> </td>
          <td rowspan="4"> <button class="shortBtnQueryFlush" v-on:click="flush_agg_HAV"> FLUSH </button> </td>
        </tr>

        <tr>
          <td> <input class="make_query_title_2" disabled value="ATTR"> </td>
          <td> 
            <select class="make_query_value" v-model="agg_HAV_attr">
              <option v-for="item in agg_GB_array" :value="item"> {{ item }} </option>
            </select>
          </td>
        </tr>

        <tr>
          <td> <input class="make_query_title_2" disabled value="OP"> </td>
          <td> 
            <select class="make_query_value" v-model="agg_HAV_op">
              <option value = "="> = </option>
              <option value = "<"> < </option>
              <option value = "<="> <= </option>
              <option value = ">"> > </option>
              <option value = ">="> >= </option>
            </select>
          </td>
        </tr>

        <tr>
          <td> <input class="make_query_title_2" disabled value="VAL"> </td>
          <td> <input class="make_query_value" type="text" v-model="agg_HAV_val"> </td>
        </tr>
        <tr style="height:40px;">
          <td colspan="6"> <button class="longBtnMakeQ" v-on:click="make_total_query"> MAKE QUERY </button> </td>
        </tr>
        <tr> 
          <td> <input class="mod_base_title" value="QUERY" disabled> </td>
          <td colspan="5"> <input class="mod_base_value" style="height:30px;" v-model="temp_QUERY"> </td>
        </tr>
      </table>
    </div>
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
      agg_HAV_array : []
    }
  },
  async created() {
    this.api_addr = "http://" + this.svrConfig.dev.host + ':' + this.svrConfig.dev.sport;
    this.taskID = await this.getParameterByName('task')
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
    func_test() {
      console.log("flush called")
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
