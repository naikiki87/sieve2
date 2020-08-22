<template>
  <div>
    <div>
      <div style="width:48%; height:500px; float:left;">
        <div class="pageName" style="margin-right:3px;"> PUBLIC </div>
        <div style="height:465px; background">
          <div style="height:450px; overflow:auto;">
            <table class="mainMngTable" style="width:97%;">
              <colgroup>
                <col style="width: 50px"> <!-- ID -->
                <col style="width: 100px"> <!-- IP -->
              	<!-- <col style="width: 100px"> -->
                <col style="width: 200px"> <!-- root ID -->
              </colgroup>
              <tr>
                <th>ID</th>
                <th>Task Name</th>
                <!-- <th>Language</th> -->
                <th>Description</th>
              </tr>
              <tr v-for="p in taskArrayS1" :key="p.id">
                <td>{{ p.id }}</td>
                <td>{{ p.name }}</td>
                <!-- <td>{{ p.type_name }}</td> -->
                <td>{{ p.comment }}</td>
              </tr>
            </table>
          </div>
        </div>
      </div>

      <div style="width:52%; height:500px; float:left;">
        <!-- <div class="pageName" style="margin-left:3px; background:brown; color:white;"> Custom Tasks </div> -->
        <div class="pageName3"> PRIVATE </div>
        <div style="height:465px;">
          <div style="height:450px; overflow:auto;">
            <table class="mainMngTable" style="width:97%;">
              <colgroup>
                <col style="width: 50px">
                <col style="width: 100px">
                <!-- <col style="width: 100px"> -->
              	<col style="width: 200px">
                <col style="width: 60px">
              </colgroup>
              <tr>
                <th>ID</th>
                <th>Task Name</th>
                <!-- <th>Language</th> -->
                <th>Description</th>
                <th>Delete</th>
              </tr>
              <tr v-for="p in taskArrayS2" :key="p.id">
                <td>{{ p.id }}</td>
                <td>{{ p.name }}</td>
                <!-- <td>{{ p.type_name }}</td> -->
                <td>{{ p.comment }}</td>
                <td> <button class="shortBtn" v-on:click="removeTask(p.id)"> DEL </button> </td>
              </tr>
            </table>
          </div>
        </div>
        <form action="http://localhost:3000/users/tasks/add" method="post" enctype="multipart/form-data">
          <table class="mainMngTable2" style="margin:auto; width:95%;">
            <tr>
              <td class="add_title"> Task Name </td>
              <td> <input style="width : 95%;" name="name" type="text" v-model="addTName"> </td>
              <td class="add_title"> Language </td>
              <td>
                <select name="program_type" style="height:27px; width : 96%; border:1px solid;" v-model="addProType">
                  <option value = "0"> Python </option>
                  <option value = "1"> Java </option>
                  <option value = "2"> Node </option>
                  <option value = "3"> Sieve </option>
                </select>
              </td>
              <td rowspan="2" style="width:3%;"> </td>
              <td rowspan="2"> <button class="addButton" type="submit"> ADD </button> </td>
              <td rowspan="2" style="width:3%;"> </td>
            </tr>
            <tr>
              <td class="add_title"> Description </td>
              <td> <input style="width:95%;" type="text" name="comment" v-model="addComment"/> </td>
              <td class="add_title"> File Upload </td>
              <td> <input style="width:94%; font-size:14px;" type="file" name="myFile"/> </td>
              <td style="display:none;"> <input style="width:95%;" type="text" name="currentuserid" v-model="currentuserid"/> </td>
            </tr>
          </table>
        </form>
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
      addTName : '',
      addProType : '',
      addComment : '',
      url:'https://78.media.tumblr.com/tumblr_m39nv7PcCU1r326q7o1_500.png'
    }
  },
  props : ['currentuserid'],
  created() {
    this.svrAddr = this.svrConfig.hostserver;
    this.loadTaskPost();
  },
  methods: {
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
      console.log("**** (5/5)LOAD TASK ****");
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
      console.log("**** (5/5)LOAD TASK ****");
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
    async removeTask(index) {
      console.log("DELETE" + index);
      var params = {
        id : index
      }

      var api = "http://" + this.svrAddr + ":3000/users/tasks/delete";
      await axios.post(api, params)
      await this.loadTaskPost(this.currentuserid)
    }
  }
}

</script>

<style src="./css/total.css"></style>
