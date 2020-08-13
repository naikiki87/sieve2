<template>
  <div>
    <div id="HJConfig_title" v-model="taskID"> TRANSACTION : {{ taskID }} </div>
    <div>
      <table id="BJConfig_table" style=" margin : auto; width:97%;">
        <colgroup>
          <col style="width: 200px"> <!-- Name -->
          <col style="width: 200px"> <!-- value -->
          <col style="width: 200px"> <!-- description -->
        </colgroup>

        <th>Name</th>
        <th>Value</th>
        <th>Description</th>

        <tr>
          <td> Input Queue Size </td>
          <td> <input class="AggGeneralInput" type="text" v-model="val_inputQueueSize"> </td>
          <td> <input style="width : 95%;" type="text" v-model="des_inputQueueSize"> </td>
        </tr>
        <tr>
          <td> Duration </td>
          <td> <input class="AggGeneralInput" type="text" v-model="val_duration"> </td>
          <td> <input style="width : 95%;" type="text" v-model="des_duration"> </td>
        </tr>
      </table>
      <br>
      <table id="BJConfig_table" style=" margin : auto; width:97%;">
        <colgroup>
          <col style="width: 300px"> <!-- Name -->
          <col style="width: 200px"> <!-- value -->
          <col style="width: 200px"> <!-- description -->
        </colgroup>

        <tr>
          <td> <input type="checkbox" v-model="val_sysTimeflag"> System-Timestamp-flag </td>
        </tr>
        <tr>
          <td style="align:right;"> <input type="checkbox" v-model="val_timestamp"> Timestamp </td>
          <td style="text-align:left;"> TidAttribute </td>
          <td>
            <select id="AggWindowType" v-model="val_TIDattr">
              <option v-for="(item, index) in schemaAttrs" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td>  </td>
          <td style="text-align:left;"> Merge Attribute </td>
          <td>
            <select id="AggWindowType" v-model="val_MergeAttr">
              <option v-for="(item, index) in schemaAttrs" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td>  </td>
          <td style="text-align:left;"> Merge Data Seperator </td>
          <td> <input style="width : 50px;" type="text" v-model="val_MergeDataSep"> </td>
        </tr>
      </table>
      <br>
      <table id="BJConfig_table" style=" margin : auto; width:97%; border : 0px;">
        <tr>
          <td> <button style="width : 150px; height:30px;" v-on:click="setTRdata"> APPLY </button> </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  created() {},
  props : ['schemaAttrs', 'taskID', 'BJfromID'],
  data() {
    return{
      sendData : [],
      val_inputQueueSize : 10,
      des_inputQueueSize : '',
      val_duration : 0,
      des_duration : '',
      val_sysTimeflag : 0,
      val_timestamp : 0,
      val_TIDattr : 0,
      val_MergeAttr : 0,
      val_MergeDataSep : ''
    }
  },
  methods : {
    setTRdata() {
      var sendData;
      var showData;
      var type, relOP;
      var distinct_flag;
      var distinct_name;
      var temp;
      var tempArr = [];

      this.sendData = new Object();
      this.sendData = {
        inputQueueSize : this.val_inputQueueSize,
        duration : this.val_duration,
        addTimestamp : this.val_sysTimeflag,
        timestamp : this.val_timestamp,
        tidAttr : this.val_TIDattr,
        mergeAttr : this.val_MergeAttr,
        mergeSep : this.val_MergeDataSep
      }

      this.$emit('TRqueryAdd', this.sendData);
    }
  }
}
</script>
