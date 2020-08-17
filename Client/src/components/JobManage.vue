<template>
  <div>
    <div>
      <div style="height:500px; overflow:auto;">
        <div class="pageName"> JOB 관리</div>
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
              <th>JOB 명</th>
              <th>JOB 개요</th>
              <th>상세</th>
              <th>삭제</th>
            </tr>

            <tr v-for="p in jobArray" :key="p.id">
              <td>{{ p.id }}</td>
              <td>{{ p.name }}</td>
              <td>{{ p.comment }}</td>
              <td> <button style="width:100px;" v-on:click="jobDetailPopup(p.id)"> 보기 </button> </td>
              <td> <button style="width:100px;" v-on:click="removeJob(p.id)"> 삭제 </button> </td>
            </tr>
          </table>
        </div>
      </div>

      <table class="mainMngTable2" style="margin:auto; width:500px;">
        <tr>
          <td class="add_title"> JOB 명 </td>
          <td> <input style="width : 95%;" type="text" v-model="add_jobName" v-on:keyup.enter="addJob"> </td>
          <td rowspan="2"> <button class="addButton" v-on:click = "addJob" > 추가 </button> </td>
        </tr>
        <tr>
          <td class="add_title"> JOB 개요 </td>
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
      openWin : ''
    }
  },
  props:['currentuserid'],
  created() {
    this.svrAddr = this.svrConfig.hostserver;
    // this.loadJob();
    this.loadJobPost();
    // this.loadSchema();
  },

  methods: {
    // jobRun(index) {
    //   console.log("Job RUN");
    //   var params = {
    //     id : index
    //   }
    //   var api = "http://" + this.svrAddr + ":3000/users/jobs/run";
    //   axios
    //   .post(api, params)
    //   .then( response => { console.log(response) } )
    //   .catch( response => { console.log(response) } );
    // },
    // loadSchema() {
    //   var api = "http://" + this.svrAddr + ":3000/users/cell_schemas";
    //   axios
    //   .get(api)
    //   .then(response => {
    //     this.schemaArray = response.data;
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });
    // },

    openPop(){
      console.log("openPop")
      window.name = "parentForm";
      var url = "http://localhost:8080/taskdetail";
      var pop_title = "childForm"
      this.openWin = window.open(url, pop_title, "width=1130, height=880, resizable=no, scrollbars=no, titlebar=no, location=no");
      var frmData = document.frmData;
      frmData.target = pop_title;
      frmData.action = "http://localhost:8080/taskdetail";
      frmData.submit() ;
    },

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
      console.log("B: ", B)

      var de_C =A[0]+B[4]+A[1]+B[3]+A[2]+B[2]+A[3]+B[1]+A[4]+B[0]
      var url = "taskdetail?job="+de_C;

      this.openWin = window.open(url, "childForm", "width=1130, height=880, resizable=no, scrollbars=no, titlebar=no, location=no");
      console.log("click popup");
      this.openWin.document.title = "SIEVE2 Job Detail";
      // this.openWin.document.getElementById("job_id").value = index;

      // var url = "taskdetail";
      // window.open("", "pop", "width=1130, height=880"); // 먼저 빈 창을 pop 라는 이름으로 열어놓고
      // // document.pop2.input1.value = "111";    
      // document.pop2.action = url;
      // document.pop2.target = "pop"; // 이 부분이 핵심! 열어놓은 빈 창(pop)을 pop2가 날아갈 target으로 정한다.
      // document.pop2.method = "post"; // target에 submit할 방식을 post 방식으로 지정한다.
      // document.pop2.submit(); // target에 쏜다.
    },
    removeJob(index) {
      console.log("DELETE" + index);
      var params = {
        id : index
      }

      var api = "http://" + this.svrAddr + ":3000/users/jobs/delete";

      axios
      .post(api, params)
      .then( response => { console.log(response) } )
      .catch( response => { console.log(response) } );

      this.loadJobPost(this.currentuserid);
      this.loadJobPost(this.currentuserid);
    },

    addJob() {
      console.log("ADD JOB");
      console.log(this.add_jobName);
      console.log(this.add_jobComment);
      var params = {
        name: this.add_jobName,
        comment: this.add_jobComment,
        currentuserid: this.currentuserid
      }
      var api = "http://" + this.svrAddr + ":3000/users/jobs/add";

      axios
      .post(api, params)
      .then( response => { console.log(response) } )
      .catch( response => { console.log(response) } );

      this.clearInput();
      this.loadJobPost(this.currentuserid);
      this.loadJobPost(this.currentuserid);
      this.loadJobPost(this.currentuserid);
    },

    loadJob() {
      console.log("**** (4/5)LOAD JOB ****");
      var api = "http://" + this.svrAddr + ":3000/users/jobs";
      axios
      .get(api)
      .then(response => {
        this.jobArray = response.data;
      })
      .catch(err => { console.log(err); });
    },

    loadJobPost() {
      console.log("**** (4/5)LOAD JOB ****");
      var api = "http://" + this.svrAddr + ":3000/users/jobs2";
      var params={currentuserid:this.currentuserid};
      axios
      .post(api, params)
      .then(response => {
        this.jobArray = response.data;
      })
      .catch(err => { console.log(err); });
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

