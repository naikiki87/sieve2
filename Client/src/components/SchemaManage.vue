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
                <col style="width: 100px"> <!-- IP -->
              	<col style="width: 120px"> <!-- root ID -->
                <col style="width: 80px"> <!-- root ID -->
                <col style="width: 80px"> <!-- root ID -->
              </colgroup>
              <tr>
                <th class="svrMngColName">ID</th>
                <th class="svrMngColName">Schema</th>
                <th class="svrMngColName">Description</th>
                <th class="svrMngColName">Columns</th>
                <th class="svrMngColName">Delete</th>
              </tr>
              <tr v-for="(p, index) in schemaArray" :key="p.id">
                <td>{{ index + 1 }}</td>
                <td>{{ p.name }}</td>
                <td>{{ p.comment }}</td>
                <td> <button class="longBtn" v-on:click="loadSchemaColumn(p.id)"> VIEW </button> </td>
                <td> <button v-if="p.userid == currentuserid" class="longBtn_DEL" v-on:click="removeSchema(p.id)"> DEL </button> </td>
              </tr>
            </table>
          </div>
        </div>
        <div>
          <table v-if="add_window" class="mainMngTable2" style="margin:auto; width:97%;">
            <tr>
              <td class="add_title"> Schema Name </td>
              <td> <input style="width : 95%; cursor:pointer;" type="text" v-model="add_schemaName" v-on:keyup.enter="addSchema"> </td>
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
        <div class="pageName3" v-model="add_ColSchema"> Column List : {{ add_ColSchema }} </div>
        <div style="height:465px;">
          <div style="height:450px; overflow:auto;">
            <table class="mainMngTable" style="width:97%;">
              <colgroup>
                <col style="width: 40px">
                <col style="width: 100px">
              	<col style="width: 80px">
                <col style="width: 50px">
              </colgroup>
              <tr>
                <th class="svrMngColName">ID</th>
                <th class="svrMngColName">Column</th>
                <th class="svrMngColName">Type</th>
                <th class="svrMngColName">Delete</th>
              </tr>
              <tr v-for="(p, index) in columnArray" :key="p.id">
                <td>{{ index + 1 }}</td>
                <td>{{ p.name }}</td>
                <td>{{ p.type_name }}</td>
                <td> <button v-if="p.userid == currentuserid" class="shortBtn_DEL" v-on:click="removeColumn3(p.id)"> DEL </button> </td>
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
              <td rowspan="2"> <button class="addButton" v-on:click = "addColumn3" > ADD </button> </td>
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
// import serverConfig from "../assets/data/server_config.json";
import serverConfig from '../../config/index'

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
      add_ColType : '',
      add_window : 0,
      api_addr : ''
    }
  },
  created () {
    // console.log("this curr : ", this.currentuserid)
    this.api_addr = "http://" + this.svrConfig.dev.host + ':' + this.svrConfig.dev.sport;
    this.svrAddr = this.svrConfig.hostserver;
    // this.loadSchemaPost();
    this.loadSchema();
    this.loadAllColumns();
    if(this.currentuserid != null) {
      this.add_window = 1
    }

  },
  methods: {
    async loadSchemaColumn(index) {
      this.ok = 1;
      this.add_ColSchema = index;
      var params = {
        schema_id: index
      }
      // var api = "http://" + this.svrAddr + ":3000/users/cell_columns";
      var api = this.api_addr + "/users/cell_columns"
      this.columnArray = (await axios.post(api, params)).data
    },
    loadAllColumns() {
      // console.log("**** (3/5)LOAD SCH COLs ****" );

      // var api = "http://" + this.svrAddr + ":3000/users/cell_columns/all";
      var api = this.api_addr + "/users/cell_columns/all"
      axios.get(api)
      .then( response => {
        this.columnArray2 = response.data;
        // console.log(this.columnArray2);
      })
      .catch( response => { console.log(response) } );
    },
    addColumn() {
      // console.log("ADD COLUMN");
      var params = {
        name: this.add_ColName,
        schema_id: this.add_ColSchema,
        type_id: this.add_ColType
      }

      // var api = "http://" + this.svrAddr + ":3000/users/cell_columns/add";
      var api = this.api_addr + "/users/cell_columns/add"
      axios.post(api, params)
      .then( response => {
        // console.log("1", response.data.success);
      })
      .catch( response => { console.log(response) } );

      this.loadSchemaColumn(this.add_ColSchema);
      this.loadSchemaColumn(this.add_ColSchema);
      this.loadSchemaColumn(this.add_ColSchema);

      this.loadAllColumns();

      this.ok = 0;

      this.clearInput();
    },
    async addColumn3() {
      var schema_id = this.add_ColSchema
      var params = {
        name: this.add_ColName,
        schema_id: this.add_ColSchema,
        type_id: this.add_ColType,
        user_id: this.currentuserid
      }

      // console.log("addcol3 : ", params)

      // var api = "http://" + this.svrAddr + ":3000/users/cell_columns/add";
      var api = this.api_addr + "/users/cell_columns/add"
      await axios.post(api, params)
      this.clearInput()
      await this.loadSchemaColumn(schema_id)


    },
    addColumn2() {
      var colSchema = this.add_ColSchema;
      var params = {
        name: this.add_ColName,
        schema_id: this.add_ColSchema,
        type_id: this.add_ColType
      }

      // var api = "http://" + this.svrAddr + ":3000/users/cell_columns/add";
      var api = this.api_addr + "/users/cell_columns/add"
      axios.post(api, params)
      .then( response => {
        var params2 = {schema_id:colSchema};
        // var api2 = "http://" + this.svrAddr + ":3000/users/cell_columns";
        var api2 = this.api_addr + "/users/cell_columns"
        axios.post(api2, params2)
        .then(response=> {
          this.columnArray = response.data;
          // var api3 = "http://" + this.svrAddr + ":3000/users/cell_columns/all";
          var api3 = this.api_addr + "/users/cell_columns/all"
          axios.get(api3)
          .then(response => {
            this.columnArray2 = response.data;
            var params4 = {
              schemas:this.schemaArray,
              attrs:this.columnArray2
            }
            // var api4 = "http://" + this.svrAddr + ":3000/users/cell_schemas/xml2";
            var api4 = this.api_addr + "/users/cell_schemas/xml2"
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

    async removeColumn3(index) {
      var params = { id : index }
      // var api = "http://" + this.svrAddr + ":3000/users/cell_columns/delete";
      var api = this.api_addr + "/users/cell_columns/delete"
      await axios.post(api, params)
      this.columnArray = []
      await this.loadSchemaColumn(this.add_ColSchema)
    },
    removeColumn2(index, schema_id) {
      var colSchema = schema_id;
      var params = { id : index }

      // var api = "http://" + this.svrAddr + ":3000/users/cell_columns/delete";
      var api = this.api_addr + "/users/cell_columns/delete"
      axios.post(api, params)
      .then( response => {
        var params2 = {schema_id:colSchema};
        // var api2 = "http://" + this.svrAddr + ":3000/users/cell_columns";
        var api2 = this.api_addr + "/users/cell_columns"
        axios.post(api2, params2)
        .then(response=> {
          this.columnArray = response.data;
          // var api3 = "http://" + this.svrAddr + ":3000/users/cell_columns/all";
          var api3 = this.api_addr + "/users/cell_columns/all"
          axios.get(api3)
          .then(response => {
            this.columnArray2 = response.data;
            var params4 = {
              schemas:this.schemaArray,
              attrs:this.columnArray2
            }
            // var api4 = "http://" + this.svrAddr + ":3000/users/cell_schemas/xml2";
            var api4 = this.api_addr + "/users/cell_schemas/xml2"
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
      // console.log("DELETE" + index);
      var params = {
        id : index
      }

      // var api = "http://" + this.svrAddr + ":3000/users/cell_columns/delete";
      var api = this.api_addr + "/users/cell_columns/delete"

      axios
      .post(api, params)
      .then( response => { console.log(response) } )
      .catch( response => { console.log(response) } );

      this.loadSchemaColumn(schema_id);
      this.loadSchemaColumn(schema_id);
      this.loadSchemaColumn(schema_id);
    },
    loadSchemaPost() {
      // console.log("**** (2/5)LOAD SCHEMA ****" + this.currentuserid);
      // var api = "http://" + this.svrAddr + ":3000/users/cell_schemas2";
      var api = this.api_addr + "/users/cell_schemas2"
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
      // console.log("**** (2/5)LOAD SCHEMA ****");
      // var api = "http://" + this.svrAddr + ":3000/users/cell_schemas";
      var api = this.api_addr + "/users/cell_schemas"
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
    async addSchema() {
      // console.log("ADD SCHEMA");
      var params = {
        name: this.add_schemaName,
        comment: this.add_schemaCom,
        currentuserid : this.currentuserid
      }

      // var api = "http://" + this.svrAddr + ":3000/users/cell_schemas/add";
      var api = this.api_addr + "/users/cell_schemas/add"
      this.clearInput();
      await axios.post(api, params)
      await this.loadSchema()
    },
    async removeSchema(index) {
      // console.log("DELETE" + index);
      var params = {
        id : index
      }

      // var api = "http://" + this.svrAddr + ":3000/users/cell_schemas/delete";
      var api = this.api_addr + "/users/cell_schemas/delete"
      await axios.post(api, params)
      await this.loadSchema()
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
