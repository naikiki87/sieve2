<template>
  <div>
    <div>
      <div style="height:500px; overflow:auto;">
        <div class="pageName"> JOB List</div>
        <div style="height:450px; overflow:auto;">
          <table class="mainMngTable" style=" margin : auto; width:98%;">
            <colgroup>
              <col style="width: 50px"> <!-- ID -->
              <col style="width: 120px"> <!-- Name -->
              <col style="width: 200px"> <!-- comment -->
              <col style="width: 100px"> <!-- detail -->
              <col style="width: 100px"> <!-- delete -->
            </colgroup>
            <tr>
              <th>ID</th>
              <th>JOB Name</th>
              <th>Description</th>
              <th>Modify</th>
              <th>Delete</th>
            </tr>

            <tr v-for="p in jobArray" :key="p.id">
              <td>{{ p.id }}</td>
              <td>{{ p.name }}</td>
              <td>{{ p.comment }}</td>
              <!-- <td> <button style="width:100px; cursor:pointer; border:0px; background : lightblue;" v-on:click="jobDetailPopup(p.id)"> MODIFY </button> </td> -->
              <td> <button class="longBtn" v-on:click="jobDetailPopup(p.id)"> MODIFY </button> </td>
              <td> <button class="longBtn" v-on:click="removeJob(p.id)"> DEL </button> </td>
            </tr>
          </table>
        </div>
      </div>

      <table v-if="add_window" class="mainMngTable2" style="margin:auto; width:500px;">
        <tr>
          <td class="add_title"> JOB Name </td>
          <td> <input style="width : 95%;" type="text" v-model="add_jobName" v-on:keyup.enter="addJob"> </td>
          <td rowspan="2"> <button class="addButton" v-on:click = "addJob" > ADD </button> </td>
        </tr>
        <tr>
          <td class="add_title"> Description </td>
          <td> <input style="width : 95%;" type="text" v-model="add_jobComment" v-on:keyup.enter="addJob"> </td>
        </tr>
      </table>
    </div>

  </div>
</template>

<script>
import axios from 'axios'
import serverConfig from "../assets/data/server_config.json";

export default {
  data() {
    return {
      // schemaArray : [],
      svrConfig : serverConfig,
      svrAddr : '',
      jobArray : [],
      add_jobName : '',
      add_jobSchema : '',
      add_jobComment : '',
      openWin : '',
      add_window : 0
    }
  },
  props:['currentuserid'],
  created() {
    this.svrAddr = this.svrConfig.hostserver;
    // this.loadJob();
    this.loadJobPost();
    // this.loadSchema();
    if(this.currentuserid != null) {
      this.add_window = 1
    }
  },

  methods: {
    gen_random(min, max) {
      var ranNum = Math.floor(Math.random()*(max-min+1)) + min;
      return ranNum;
    },
        
    jobDetailPopup(index) {
      window.name = "parentForm";
      var add = this.gen_random(100000, 200000)
      var add_add = add + 5555
      var A = add_add.toString(16)
      var id = index
      var id_add = id + add
      var B = id_add.toString(16)
      // console.log("B: ", B)

      var de_C =A[0]+B[4]+A[1]+B[3]+A[2]+B[2]+A[3]+B[1]+A[4]+B[0]
      // var url = "taskdetail?job="+de_C;
      var url = "jobDetail?job="+de_C+"&case="+this.currentuserid;

      this.openWin = window.open(url, "childForm", "width=1130, height=880, resizable=no, scrollbars=no, titlebar=no, location=no");
      // console.log("click popup");
      this.openWin.document.title = "SIEVE2 Job Detail";
    },
    async removeJob(index) {
      // console.log("DELETE" + index);
      var params = {
        id : index
      }

      var api = "http://" + this.svrAddr + ":3000/users/jobs/delete";

      await axios.post(api, params)
      await this.loadJobPost(this.currentuserid)
    },

    async addJob() {
      // console.log("ADD JOB");
      // console.log(this.add_jobName);
      // console.log(this.add_jobComment);
      var params = {
        name: this.add_jobName,
        comment: this.add_jobComment,
        currentuserid: this.currentuserid
      }
      var api = "http://" + this.svrAddr + ":3000/users/jobs/add";

      await axios.post(api, params)
      this.clearInput()
      await this.loadJobPost(this.currentuserid)
    },

    loadJob() {
      // console.log("**** (4/5)LOAD JOB ****");
      var api = "http://" + this.svrAddr + ":3000/users/jobs";
      axios
      .get(api)
      .then(response => {
        this.jobArray = response.data;
      })
      .catch(err => { console.log(err); });
    },

    async loadJobPost() {
      // console.log("**** (4/5)LOAD JOB ****");
      var api = "http://" + this.svrAddr + ":3000/users/jobs2";
      var params={currentuserid:this.currentuserid};

      this.jobArray = (await axios.post(api, params)).data
    },
    clearInput() {
      this.add_jobName = "";
      this.add_jobSchema = "";
      this.add_jobComment = "";
    }

  }
}
</script>

<style src="./css/total.css"></style>


