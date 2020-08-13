<template>
  <div>
    <div>
      <div style="height:500px; overflow:auto;">
        <div class="pageName"> 연산자 관리 </div>
        <table class="mainMngTable" style="width:96%; ">
          <colgroup>
            <col style="width: 50px"> <!-- ID -->
            <col style="width: 80px"> <!-- name -->
            <col style="width: 80px"> <!-- program type -->
            <col style="width: 150px"> <!-- comment -->
            <col style="width: 40px"> <!-- delete -->
          </colgroup>
          <tr>
            <th>연산자 ID</th>
            <th>연산자 명</th>
            <th>실행언어</th>
            <th>설명</th>
            <th>삭제</th>
          </tr>

          <tr v-for="p in taskArray" :key="p.id">
            <td>{{ p.id }}</td>
            <td>{{ p.name }}</td>
            <td>{{ p.type_name }}</td>
            <td>{{ p.comment }}</td>
            <td> <button style="width:50px;" v-on:click="removeTask(p.id)"> 삭제 </button> </td>
          </tr>
        </table>
      </div>
    </div>

    <form action="http://localhost:3000/users/tasks/add" method="post" enctype="multipart/form-data">
      <!-- <form action="http://localhost:8080" method="post" enctype="multipart/form-data"> -->
      <table class="mainMngTable2" style="margin:auto; width:800px;">
        <tr>
          <td class="add_title"> 연산자 명 </td>
          <td> <input style="width : 95%;" name="name" type="text" v-model="addTName"> </td>
          <td class="add_title"> 실행언어 </td>
          <td>
            <select name="program_type" style="height:27px; width : 96%; border:1px solid;" v-model="addProType">
              <option value = "0"> Python </option>
              <option value = "1"> Java </option>
              <option value = "2"> Node </option>
              <option value = "3"> Sieve </option>
            </select>
          </td>
          <td rowspan="2" style="width:20px;"> </td>
          <td rowspan="2"> <button class="addButton" type="submit"> 추가 </button> </td>
          <td rowspan="2" style="width:20px;"> </td>
        </tr>
        <tr>
          <td class="add_title"> 연산자 설명 </td>
          <td> <input style="width:95%;" type="text" name="comment" v-model="addComment"/> </td>
          <td class="add_title"> 파일 업로드 </td>
          <td> <input style="width:95%; font-size:15px;" type="file" name="myFile"/> </td>
          <td style="display:none;"> <input style="width:95%;" type="text" name="currentuserid" v-model="currentuserid"/> </td>
        </tr>
      </table>
    </form>
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
    clearInput() {
      this.add_taskName = "";
      this.add_taskProg = "";
      this.add_taskComment = "";
    },
    removeTask(index) {
      console.log("DELETE" + index);
      var params = {
        id : index
      }

      var api = "http://" + this.svrAddr + ":3000/users/tasks/delete";

      axios
      .post(api, params)
      .then( response => { console.log(response) } )
      .catch( response => { console.log(response) } );

      this.loadTaskPost(this.currentuserid);
      this.loadTaskPost(this.currentuserid);
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
    loadTaskPost() {
      console.log("**** (5/5)LOAD TASK ****");
      var api = "http://" + this.svrAddr + ":3000/users/tasks2";
      var params = { currentuserid : this.currentuserid };
      axios
      .post(api, params)
      .then(response => {
        this.taskArray = response.data;
      })
      .catch(err => {
        console.log(err);
      });
    }
  }
}

</script>

<style src="./css/total.css"></style>
