<template>
  <div>
    <div>
      <div style="width:48%; height:500px; float:left;">
        <!-- <div class="pageName" style="margin-right:3px;"> 스키마 목록 </div> -->
        <div class="pageName" style="margin-right:3px;"> Schema List </div>
        <div style="height:465px; background">
          <div style="height:450px; overflow:auto;">
            <table class="mainMngTable" style="width:97%;">
              <colgroup>
                <col style="width: 50px"> <!-- ID -->
                <col style="width: 120px"> <!-- IP -->
              	<col style="width: 120px"> <!-- root ID -->
                <col style="width: 50px"> <!-- root ID -->
                <col style="width: 50px"> <!-- root ID -->
              </colgroup>
              <tr>
                <!-- <th class="svrMngColName">ID</th>
                <th class="svrMngColName">스키마명</th>
                <th class="svrMngColName">설명</th>
                <th class="svrMngColName">삭제</th>
                <th class="svrMngColName">컬럼</th> -->
                <th class="svrMngColName">ID</th>
                <th class="svrMngColName">Schema</th>
                <th class="svrMngColName">Description</th>
                <th class="svrMngColName">Delete</th>
                <th class="svrMngColName">Columns</th>
              </tr>
              <tr v-for="p in schemaArray" :key="p.id">
                <td>{{ p.id }}</td>
                <td>{{ p.name }}</td>
                <td>{{ p.comment }}</td>
                <td> <button style="width:50px;" v-on:click="removeSchema(p.id)"> DEL </button> </td>
                <td> <button style="width:100px;" v-on:click="loadSchemaColumn(p.id)"> COLUMN </button> </td>
              </tr>
            </table>
          </div>
        </div>
        <div>
          <table class="mainMngTable2" style="margin:auto; width:97%;">
            <tr>
              <td class="add_title"> Schema Name </td>
              <td> <input style="width : 95%;" type="text" v-model="add_schemaName" v-on:keyup.enter="addSchema"> </td>
              <td rowspan="2"> <button class="addButton" v-on:click = "addSchema" > ADD </button> </td>
            </tr>
            <tr>
              <td class="add_title"> Comment </td>
              <td> <input style="width : 95%;" type="text" v-model="add_schemaCom" v-on:keyup.enter="addSchema"> </td>
            </tr>
          </table>
        </div>
      </div>
      
      <div style="width:52%; height:500px; float:left;" margin="auto">
        <!-- <div class="pageName" style="margin-left:3px; background:brown; color:white;"> 컬럼 목록 </div> -->
        <div class="pageName" style="margin-left:3px; background:brown; color:white;"> Column List </div>
        <div style="height:465px;">
          <div style="height:450px; overflow:auto;">
            <table class="mainMngTable" style="width:97%;">
              <colgroup>
                <col style="width: 50px"> <!-- ID -->
                <col style="width: 70px"> <!-- sch ID -->
                <col style="width: 150px"> <!-- name -->
              	<col style="width: 80px"> <!-- type ID -->
                <col style="width: 50px"> <!-- del -->
              </colgroup>
              <tr>
                <!-- <th class="svrMngColName">ID</th>
                <th class="svrMngColName">스키마</th>
                <th class="svrMngColName">컬럼명</th>
                <th class="svrMngColName">자료형</th>
                <th class="svrMngColName">삭제</th> -->
                <th class="svrMngColName">ID</th>
                <th class="svrMngColName">Schema</th>
                <th class="svrMngColName">Column</th>
                <th class="svrMngColName">Data Type</th>
                <th class="svrMngColName">Delete</th>
              </tr>
              <tr v-for="p in columnArray" :key="p.id">
                <td>{{ p.id }}</td>
                <td>{{ p.schema_id }}</td>
                <td>{{ p.name }}</td>
                <td>{{ p.type_name }}</td>
                <td> <button style="width:50px;" v-on:click="removeColumn2(p.id, p.schema_id)"> DEL </button> </td>
              </tr>
            </table>
          </div>
        </div>
        <div>
          <table class="mainMngTable2" style="margin:auto; width:97%;" v-if="ok">
            <tr>
              <td style="width:50px;" rowspan="2"> ID : <a style="font-size:16px; font-weight:700; color:red;" v-model="add_ColSchema"> {{ add_ColSchema }} </a> </td>
              <td class="add_title"> Column Name </td>
              <td> <input style="width : 95%;" type="text" v-model="add_ColName" v-on:keyup.enter="addSchema"> </td>
              <td rowspan="2"> <button class="addButton" v-on:click = "addColumn2" > ADD </button> </td>
            </tr>
            <tr>
              <td class="add_title"> Data Type </td>
              <td>
                <select style="width:96%;" v-model="add_ColType">
                  <option value = "1"> string </option>
                  <option value = "2"> integer </option>
                  <option value = "3"> double </option>
                  <option value = "4"> long </option>
                </select>
              </td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import serverConfig from "../assets/data/server_config.json";

export default {
  name: 'SchemaManage',
  props:['currentuserid'],
  data () {
    return {
      ok : 0,
      svrConfig : serverConfig,
      svrAddr : '',
      schemaArray: [],
      columnArray: [],
      columnArray2: [],
      add_schemaName : '',
      add_schemaCom : '',
      add_ColName : '',
      add_ColSchema : '',
      add_ColType : ''

    }
  },
  created () {
    this.svrAddr = this.svrConfig.hostserver;
    this.loadSchemaPost();
    this.loadAllColumns();

  },
  methods: {
    loadSchemaColumn(index) {
      this.ok = 1;
      this.add_ColSchema = index;
      var params = {
        schema_id: index
      }
      var api = "http://" + this.svrAddr + ":3000/users/cell_columns";
      axios.post(api, params)
      .then( response => {
        this.columnArray = response.data;
      })
      .catch( response => { console.log(response) } );
    },
    loadAllColumns() {
      console.log("**** (3/5)LOAD SCH COLs ****" );

      var api = "http://" + this.svrAddr + ":3000/users/cell_columns/all";
      axios.get(api)
      .then( response => {
        this.columnArray2 = response.data;
        // console.log(this.columnArray2);
      })
      .catch( response => { console.log(response) } );
    },
    addColumn() {
      console.log("ADD COLUMN");
      var params = {
        name: this.add_ColName,
        schema_id: this.add_ColSchema,
        type_id: this.add_ColType
      }

      var api = "http://" + this.svrAddr + ":3000/users/cell_columns/add";
      axios.post(api, params)
      .then( response => {
        console.log("1", response.data.success);
      })
      .catch( response => { console.log(response) } );

      this.loadSchemaColumn(this.add_ColSchema);
      this.loadSchemaColumn(this.add_ColSchema);
      this.loadSchemaColumn(this.add_ColSchema);

      this.loadAllColumns();

      this.ok = 0;

      this.clearInput();
    },
    addColumn2() {
      var colSchema = this.add_ColSchema;
      var params = {
        name: this.add_ColName,
        schema_id: this.add_ColSchema,
        type_id: this.add_ColType
      }

      var api = "http://" + this.svrAddr + ":3000/users/cell_columns/add";
      axios.post(api, params)
      .then( response => {
        var params2 = {schema_id:colSchema};
        var api2 = "http://" + this.svrAddr + ":3000/users/cell_columns";
        axios.post(api2, params2)
        .then(response=> {
          this.columnArray = response.data;
          var api3 = "http://" + this.svrAddr + ":3000/users/cell_columns/all";
          axios.get(api3)
          .then(response => {
            this.columnArray2 = response.data;
            var params4 = {
              schemas:this.schemaArray,
              attrs:this.columnArray2
            }
            var api4 = "http://" + this.svrAddr + ":3000/users/cell_schemas/xml2";
            axios.post(api4, params4)
            .then(r => {
            })
          })
        })
      })
      .catch( response => { console.log(response) } );

      this.ok = 0;
      this.clearInput();
    },
    removeColumn2(index, schema_id) {
      var colSchema = schema_id;
      var params = { id : index }

      var api = "http://" + this.svrAddr + ":3000/users/cell_columns/delete";
      axios.post(api, params)
      .then( response => {
        var params2 = {schema_id:colSchema};
        var api2 = "http://" + this.svrAddr + ":3000/users/cell_columns";
        axios.post(api2, params2)
        .then(response=> {
          this.columnArray = response.data;
          var api3 = "http://" + this.svrAddr + ":3000/users/cell_columns/all";
          axios.get(api3)
          .then(response => {
            this.columnArray2 = response.data;
            var params4 = {
              schemas:this.schemaArray,
              attrs:this.columnArray2
            }
            var api4 = "http://" + this.svrAddr + ":3000/users/cell_schemas/xml2";
            axios.post(api4, params4)
            .then(r => {
            })
          })
        })
      })
      .catch( response => { console.log(response) } );

      this.ok = 0;
      this.clearInput();

    },
    removeColumn(index, schema_id) {
      console.log("DELETE" + index);
      var params = {
        id : index
      }

      var api = "http://" + this.svrAddr + ":3000/users/cell_columns/delete";

      axios
      .post(api, params)
      .then( response => { console.log(response) } )
      .catch( response => { console.log(response) } );

      this.loadSchemaColumn(schema_id);
      this.loadSchemaColumn(schema_id);
      this.loadSchemaColumn(schema_id);
    },
    loadSchemaPost() {
      console.log("**** (2/5)LOAD SCHEMA ****" + this.currentuserid);
      var api = "http://" + this.svrAddr + ":3000/users/cell_schemas2";
      var params = { currentuserid : this.currentuserid }
      axios
      .post(api, params)
      .then(response => {
        this.schemaArray = response.data;
      })
      .catch(err => {
        console.log(err);
      });

    },
    loadSchema() {
      console.log("**** (2/5)LOAD SCHEMA ****");
      var api = "http://" + this.svrAddr + ":3000/users/cell_schemas";
      axios
      .get(api)
      .then(response => {
        this.schemaArray = response.data;
        // console.log(this.schemaArray);
      })
      .catch(err => {
        console.log(err);
      });
    },
    addSchema() {
      console.log("ADD SCHEMA");
      var params = {
        name: this.add_schemaName,
        comment: this.add_schemaCom,
        currentuserid : this.currentuserid
      }

      var api = "http://" + this.svrAddr + ":3000/users/cell_schemas/add";

      axios
      .post(api, params)
      .then( response => {
        this.loadSchemaPost(this.currentuserid);
      })
      .catch( response => { console.log(response) } );

      this.clearInput();
      // this.loadSchemaPost(this.currentuserid);
      // this.loadSchemaPost(this.currentuserid);
      // this.loadSchemaPost(this.currentuserid);
    },
    removeSchema(index) {
      console.log("DELETE" + index);
      var params = {
        id : index
      }

      var api = "http://" + this.svrAddr + ":3000/users/cell_schemas/delete";

      axios
      .post(api, params)
      .then( response => { console.log(response) } )
      .catch( response => { console.log(response) } );

      this.loadSchemaPost(this.currentuserid);
      this.loadSchemaPost(this.currentuserid);
      this.loadSchemaPost(this.currentuserid);
    },
    clearInput() {
      this.add_schemaCom = "";
      this.add_schemaName = "";
      this.add_ColName = "",
      this.add_ColSchema = "",
      this.add_ColType = ""
    }
  }
}
</script>

<style>
h1 { color: #454545; text-align: center; }
.svrMngColName { background : lightblue; }
</style>
