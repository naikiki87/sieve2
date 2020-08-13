<template>
  <div>
    <div id="HJConfig_title" v-model="taskID"> BINARY JOIN : {{ taskID }} </div>
    <!-- <div> <button v-on:click="abcd2"> TEST </button> </div> -->
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
          <td> <input style="width : 95%;" type="text" v-model="des_InputQueueSize"> </td>
        </tr>
      </table>
      <table id="BJConfig_table" style=" margin : auto; width:97%;">
        <colgroup>
          <col style="width: 200px"> <!-- Name -->
          <col style="width: 200px"> <!-- value -->
          <col style="width: 200px"> <!-- description -->
        </colgroup>

        <th>STREAM</th>
        <th>Win Type(Time / Tuple)</th>
        <th>Win Size</th>

        <tr>
          <td v-model="BJfromID"> Task : {{ BJfromID[0] }} </td>
          <td>
            <select class="BJvalWinType" v-model="val_winType1">
              <option value = "0"> TIME </option>
              <option value = "1"> TUPLE </option>
            </select>
          </td>
          <td> <input style="width : 95%; text-align:center;" type="text" v-model="val_winSize1"> </td>
        </tr>
        <tr>
          <td v-model="BJfromID"> Task : {{ BJfromID[1] }} </td>
          <td>
            <select class="BJvalWinType" v-model="val_winType2">
              <option value = "0"> TIME </option>
              <option value = "1"> TUPLE </option>
            </select>
          </td>
          <td> <input style="width : 95%; text-align:center;" type="text" v-model="val_winSize2"> </td>
        </tr>

      </table>
      <br>
      <table id="BJConfig_table" style=" margin : auto; width:97%;">
        <colgroup>
          <col style="width: 80px"> <!-- Name -->
          <col style="width: 200px"> <!-- value -->
          <col style="width: 80px"> <!-- description -->
        </colgroup>

        <tr>
          <td v-model="BJfromID"> Task : {{ BJfromID[0] }} </td>
          <td>
            <select style="width:91%;" v-model="val_AttrIndex1">
              <option v-for="(item, index) in schemaAttrs[0]" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
          <td></td>
        </tr>
        <tr>
          <td v-model="BJfromID"> Task : {{ BJfromID[1] }} </td>
          <td>
            <select style="width:91%;" v-model="val_AttrIndex2">
              <option v-for="(item, index) in schemaAttrs[1]" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
          <td> <button class="BJbutton" v-on:click="setBJdata"> 추가 </button> </td>
        </tr>
        <tr>
          <td rowspan="3" colspan="2">
            <select style="width : 93%; height:110px; overflow-y:hidden;" size=10>
              <option v-for="(item, index) in val_BJTextArea" :value="index"> {{ item }} </option>
            </select>
          </td>
          <td> <button class="BJbutton" v-on:click="setBJdata"> 상향 </button> </td>
        </tr>

        <tr>
          <td> <button class="BJbutton" v-on:click="setBJdata"> 하향 </button> </td>
        </tr>
        <tr>
          <td> <button class="BJbutton" v-on:click="setBJdata"> 삭제 </button> </td>
        </tr>
      </table>
      <br>
      <table id="BJConfig_table" style=" margin : auto; width:97%; border : 0px;">
        <tr>
          <td> <button v-on:click="applyBJdata"> Apply Condition </button> </td>
          <td> <button v-on:click="delCondition"> Delete Condition </button> </td>
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
      des_InputQueueSize : '',
      val_winType1 : 0,
      val_winSize1 : 10,
      val_winType2 : 0,
      val_winSize2 : 10,
      val_AttrIndex1 : 0,
      val_AttrIndex2 : 0,
      val_BJTextArea : []
    }
  },
  methods : {
    parentsMethod(message) {
      alert(message);
    },
    addCondition() {
      console.log("add condition");
    },
    delCondition() {
      console.log("del condition");
    },
    setBJdata() {
      var sendData;
      var showData;
      var type, relOP;
      var distinct_flag;
      var distinct_name;
      var temp;
      // var info1, infor2;
      var tempArr = [];

      // if(this.BJfromID.length != 2) {
      //   alert("error");
      // }
      // else {
        // for(var i=0; i<this.BJfromID.length; i++) {
        // info1 = new Object();
        // info1 = {
        //   from_id : this.BJfromID[0],
        //   schema : this.schemaAttrs[0],
        //   winType : this.val_winType1,
        //   winSize : this.val_winSize1
        // }

        for(var i=0; i<2; i++) {
          temp = new Object();
          temp = {
            from_id : this.BJfromID[i],
            schema : this.schemaAttrs[i]
          }
          tempArr.push(temp);
        }

        this.sendData = new Object();

        // error control

        this.sendData = {
          schemaInfo : tempArr,
          attrIndex1 : this.val_AttrIndex1,
          attrIndex2 : this.val_AttrIndex2,
          inputQueueSize : this.val_inputQueueSize,
          winType1 : this.val_winType1,
          winType2 : this.val_winType2,
          winSize1 : this.val_winSize1,
          winSize2 : this.val_winSize2
        }

        var from1 = this.sendData.schemaInfo[0].from_id;
        var from2 = this.sendData.schemaInfo[1].from_id;
        var item1 = this.sendData.schemaInfo[0].schema[this.val_AttrIndex1].name;
        var item2 = this.sendData.schemaInfo[1].schema[this.val_AttrIndex2].name;
        var str = from1 + '.' + item1 + " = " + from2 + '.' + item2;

        this.val_BJTextArea = [];   // initialize
        this.val_BJTextArea.push(str);
      // }
    },
    applyBJdata() {
      this.$emit('BJqueryAdd', this.sendData);
    }
  },
  components: {
  }
}
</script>
