<template>
  <div>
    <div style="background:; width:70%; height:600px; float:left"> 
      <div style="background:; width:100%; height:500px; float:left"> 
        <div style="background:; width:50%; height:500px; float:left"> 
          <div class="pageName" style="margin-right:3px;"> PUBLIC </div>
            <div style="height:465px; background">
              <div style="height:450px; overflow:auto;">
                <table class="mainMngTable" style="width:97%;">
                  <colgroup>
                    <col style="width: 50px">
                    <col style="width: 100px">
                    <col style="width: 200px">
                    <col style="width: 60px">
                  </colgroup>
                  <tr>
                    <th>ID</th>
                    <th>Task Name</th>
                    <th>Description</th>
                    <th>Params</th>
                  </tr>
                  <tr v-for="(p, index) in taskArrayS1" :key="p.id">
                    <td>{{ index + 1 }}</td>
                    <td>{{ p.name }}</td>
                    <td>{{ p.comment }}</td>
                    <td> <button class="shortBtn" v-on:click="show_params(p.id)"> VIEW </button> </td>
                  </tr>
                </table>
              </div>
            </div>
          </div>
        <div style="background:; width:50%; height:500px; float:left"> 
          <div class="pageName3"> PRIVATE </div>
          <div style="height:465px;">
            <div style="height:450px; overflow:auto;">
              <table class="mainMngTable" style="width:97%;">
                <colgroup>
                  <col style="width: 50px">
                  <col style="width: 100px">
                  <col style="width: 150px">
                  <col style="width: 60px">
                  <col style="width: 60px">
                </colgroup>
                <tr>
                  <th>ID</th>
                  <th>Task Name</th>
                  <th>Description</th>
                  <th>Params</th>
                  <th>Delete</th>
                </tr>
                <tr v-for="(p, index) in taskArrayS2" :key="p.id">
                  <td>{{ index + 1 }}</td>
                  <td>{{ p.name }}</td>
                  <td>{{ p.comment }}</td>
                  <td> <button class="shortBtn" v-on:click="show_params(p.id)"> VIEW </button> </td>
                  <td> <button class="shortBtn_DEL" v-on:click="removeTask(p.id)"> DEL </button> </td>
                </tr>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div style="background:; width:100%; height:100px; float:left">
        <table class="mainMngTable2" style="margin:auto; width:75%; border-bottom : 0px">
          <tr>
            <td class="add_title"> Task Name : </td>
            <td style="width:35%; background:"> <input style="width:100%; align:left;" name="name" type="text" v-model="addTName"> </td>
            <td style="background:"> <button style="width:130px" class="midBtn" v-on:click="taskname_check"> Name CHECK </button> </td>
            <td style="width:15%; background:"> <a v-if="nameok == 2" style="color:green"> OK </a> <a v-if="nameok == 1" style="color:red"> Not OK </a></td>
          </tr>
        </table>
        <form v-if="add_window" action="http://localhost:3000/users/tasks/add" method="post" enctype="multipart/form-data">
          <table class="mainMngTable2" style="margin:auto; border-top:0px; width:75%;">
            <tr>
              <td style="display:none;"> <input style="width :93%;" name="name" type="text" v-model="addTName"> </td>
              <td style="display:none;"> <input style="width:98%;" type="text" name="currentuserid" v-model="currentuserid"/> </td>
              <td class="add_title"> Description : </td>
              <td> <input style="width:95%;" type="text" name="comment" v-model="addComment"/> </td>
              <td class="add_title"> File : </td>
              <td> <input style="width:95%; font-size:14px;" type="file" name="myFile"/> </td>
              
              <td style="width:1%;"> </td>
              <td> <button style="height:35px;" class="addButton" type="submit"> ADD </button> </td>
              <td style="width:1%;"> </td>
            </tr>
          </table>
        </form>
      </div>

    </div>
    <div style="background:; width:30%; height:600px; float:left"> 
      <div style="background:; width:100%; height:500px; float:left"> 
        <div class="pageName4"> PARAMETER </div>
        <div style="height:465px;">
          <div style="height:450px; overflow:auto;">
            <table class="mainMngTable" style="width:97%;">
              <colgroup>
                <col style="width: 50px">
                <col style="width: 100px">
              	<col style="width: 200px">
                <col style="width: 60px">
              </colgroup>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Delete</th>
              </tr>
              <tr v-for="(p, index) in task_params" :key="p.id">
                <td>{{ index + 1 }}</td>
                <td>{{ p.name }}</td>
                <td>{{ p.comment }}</td>
                <td> <button class="shortBtn_DEL" v-on:click="remove_param(p.id)"> DEL </button> </td>
              </tr>
            </table>
          </div>
        </div>
      </div>
      <div style="background:; width:100%; height:100px; float:left">
        <table v-if="table_add_param" class="mainMngTable2" style="margin:auto; width:95%;">
          <tr>
            <td class="add_title"> Name </td>
            <td> <input style="width : 95%;" type="text" v-model="add_param_name" v-on:keyup.enter="addSvr"> </td>
            <td rowspan="2" style="width:20px;"> </td>
            <td rowspan="2"> <button class="addButton" v-on:click = "add_param" > ADD </button> </td>
            <td rowspan="2" style="width:10px;"> </td>
          </tr>
          <tr>
            <td class="add_title"> Description </td>
            <td> <input style="width : 95%;" type="text" v-model="add_param_des" v-on:keyup.enter="addSvr"> </td>
          </tr>
    </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import serverConfig from "../assets/data/server_config.json";

export default {
  data() {
    return {
      svrConfig : serverConfig,
      svrAddr : '',
      taskArray : [],
      taskArrayS1 : [],
      taskArrayS2 : [],
      task_params : [],
      addTName : '',
      addProType : '',
      addComment : '',
      url:'https://78.media.tumblr.com/tumblr_m39nv7PcCU1r326q7o1_500.png',
      add_window : 0,
      add_params : [],
      nameok : 0,
      table_add_param : 0,
      add_param_name : '',
      add_param_des : '',
      temp_tid : ''
    }
  },
  props : ['currentuserid'],
  created() {
    this.svrAddr = this.svrConfig.hostserver;
    this.loadTaskPost();
    if(this.currentuserid != null) {
      this.add_window = 1
    }
  },
  methods: {
    async add_param() {
      var param_name = this.add_param_name && this.add_param_name.trim();
      var param_des = this.add_param_des && this.add_param_des.trim();
      var params = {
        param_name : param_name,
        param_des: param_des,
        task_id : this.temp_tid,
        currentuserid : this.currentuserid
      }
      var api = "http://" + this.svrAddr + ":3000/users/tasks/add_param";
      await axios.post(api, params)
      await this.show_params(this.temp_tid)
    },
    async remove_param(index) {
      var params = {
        p_id : index
      }

      console.log("index : ", index)
      var api = "http://" + this.svrAddr + ":3000/users/tasks/remove_param";
      await axios.post(api, params)
      this.task_params = []
      await this.show_params(this.temp_tid)
      // await this.loadTaskPost(this.currentuserid)
      
    },
    addRow() {
      this.add_params.push({
        one : '',
        two : ''
      })
    },
    deleteRow(index) {
      this.add_params.splice(index,1)
    },
    async show_params(index) {
      console.log("show params : ", index)
      this.table_add_param = 1
      this.temp_tid = index
      var params = {
        task_id : index
      }
      var api = "http://" + this.svrAddr + ":3000/users/tasks/get_params";

      this.task_params = (await axios.post(api, params)).data
    },
    forceFileDownload(response){
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', 'file.png') //or any other extension
      document.body.appendChild(link)
      link.click()
    },
    downloadWithVueResource() {
      this.$http({
        method: 'get',
        url: this.url,
        responseType: 'arraybuffer'
      })
      .then(response => {
        this.forceFileDownload(response)
      })
      .catch(() => console.log('error occured'))

    },
    downloadWithAxios(){
      axios({
        method: 'get',
        url: this.url,
        responseType: 'arraybuffer'
      })
      .then(response => {

        this.forceFileDownload(response)

      })
      .catch(() => console.log('error occured'))
    },

    loadTask() {
      // console.log("**** (5/5)LOAD TASK ****");
      var api = "http://" + this.svrAddr + ":3000/users/tasks";
      axios
      .get(api)
      .then(response => {
        this.taskArray = response.data;
      })
      .catch(err => {
        console.log(err);
      });
    },
    async loadTaskPost() {
      // console.log("**** (5/5)LOAD TASK ****");
      var api = "http://" + this.svrAddr + ":3000/users/tasks2";
      var params = { currentuserid : this.currentuserid };
      this.taskArrayS1 = []
      this.taskArrayS2 = []

      this.taskArray = (await axios.post(api, params)).data
      for(var i=0; i<this.taskArray.length; i++) {
        if(this.taskArray[i].sieve2 == 0) {
          this.taskArrayS1.push(this.taskArray[i]);
        }
        else {
          this.taskArrayS2.push(this.taskArray[i]);
        }
      }
    },
    clearInput() {
      this.add_taskName = "";
      this.add_taskProg = "";
      this.add_taskComment = "";
    },
    async taskname_check() {
      console.log("taskname check")
      if(this.addTName == "") {
        console.log("nullllll")
      }
      else {
        console.log("this.add : ", this.addTName)
        var params = {
          t_name : this.addTName
        }
        var api = "http://" + this.svrAddr + ":3000/users/tasks/name_check";

        var same = (await axios.post(api, params)).data.same

        if(same == 1) {
          alert("Same Name Exist")
          this.nameok = 1
        }
        else {
          this.nameok = 2
        }
      }
    },
    async removeTask(index) {
      var params = {
        id : index
      }
      var api = "http://" + this.svrAddr + ":3000/users/tasks/delete";
      await axios.post(api, params)
      await this.loadTaskPost(this.currentuserid)
      this.task_params = []
    }
  }
}

</script>

<style src="./css/total.css"></style>
