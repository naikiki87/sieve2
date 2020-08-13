<template>
  <div>
    <div id="HJConfig_title" v-model="taskID"> HALF JOIN : {{ taskID }} </div>
    <!-- <div> <button v-on:click="abcd2"> TEST </button> </div> -->

    <div>
      <table id="HJConfig_table" style=" margin : auto; width:97%;">
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
          <td> <input class="AggGeneralInput" type="text" v-model="val_InputQueueSize"> </td>
          <td> <input style="width : 95%;" type="text" v-model="des_InputQueueSize"> </td>
        </tr>
      </table>
      <table id="HJConfig_table" style=" margin : auto; width:97%;">
        <colgroup>
          <col style="width: 100px"> <!-- Name -->
          <col style="width: 100px"> <!-- value -->
          <col style="width: 50px"> <!-- description -->
          <col style="width: 350px"> <!-- description -->
        </colgroup>
        <tr>
          <td rowspan="2"> <h3 v-model="HJfromID"> ID : {{ HJfromID[0] }} </h3> </td>
          <td>
            <select v-model="val_Type1">
              <option value = "0"> STREAM </option>
              <option value = "1"> TABLE </option>
            </select>
          </td>
          <td> <input style="width:95%; text-align:center;" type="text" v-model="val_Input1"> </td>
          <td>
            <select style="width:95%;" v-model="val_HJAttrIndex1">
              <option v-for="(item, index) in parentAttrs[0]" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td colspan="2">4 </td>
          <td>
            <select style="width:95%;" v-model="val_HJAttrIndex1" disabled>
              <option v-for="(item, index) in parentAttrs[0]" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td> 6</td>
          <td colspan="2" style="text-align:right;"> Fixed Attribute : </td>
          <td>
            <select style="width:95%;" v-model="val_HJAttrIndex1">
              <option v-for="(item, index) in parentAttrs[0]" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td colspan="3">9 </td>
          <td>
            <input type="checkbox" v-model="val_onRfrsTime1"> On Refresh Time :
            <select v-model="val_refreshTimeHour1" class="HJrefreshTime">
              <option v-for="(item, index) in timeHours" :value="index"> {{ item }} </option>
            </select>
            시
            <select v-model="val_refreshTimeMin1" class="HJrefreshTime">
              <option v-for="(item, index) in timeMins" :value="index"> {{ item }} </option>
            </select>
            분
          </td>
        </tr>
      </table>
      <table id="HJConfig_table" style=" margin : auto; width:97%; border : 0px;">
        <tr>
          <td> </td>
          <td>
            <select v-model="val_isEqual" style="width:50px;" align="center">
              <option value = "0"> = </option>
              <option value = "1"> != </option>
            </select>
          </td>
          <td> <input type="checkbox" v-model="val_2ColJoin"> 2 Column Join </td>
          <td> </td>
        </tr>
      </table>
      <table id="HJConfig_table" style=" margin : auto; width:97%;">
        <colgroup>
          <col style="width: 100px"> <!-- Name -->
          <col style="width: 100px"> <!-- value -->
          <col style="width: 50px"> <!-- description -->
          <col style="width: 350px"> <!-- description -->
        </colgroup>
        <tr>
          <td rowspan="2"> <h3 v-model="HJfromID"> ID : {{ HJfromID[1] }} </h3> </td>
          <td>
            <select v-model="val_Type2">
              <option value = "0"> STREAM </option>
              <option value = "1"> TABLE </option>
            </select>
          </td>
          <td> <input style="width:95%; text-align:center;" type="text" v-model="val_Input1"> </td>
          <td>
            <select style="width:95%;" v-model="val_HJAttrIndex2">
              <option v-for="(item, index) in parentAttrs[1]" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td colspan="2">4 </td>
          <td>
            <select style="width:95%;" v-model="val_HJAttrIndex2" disabled>
              <option v-for="(item, index) in parentAttrs[1]" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td> 6</td>
          <td colspan="2" style="text-align:right;"> Fixed Attribute : </td>
          <td>
            <select style="width:95%;" v-model="val_HJAttrIndex2Fixed">
              <option v-for="(item, index) in parentAttrs[1]" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
        </tr>
        <tr>
          <td colspan="3">9 </td>
          <td>
            <input type="checkbox" v-model="val_onRfrsTime2"> On Refresh Time :
            <select v-model="val_refreshTimeHour2" class="HJrefreshTime">
              <option v-for="(item, index) in timeHours" :value="index"> {{ item }} </option>
            </select>
            시
            <select v-model="val_refreshTimeMin2" class="HJrefreshTime">
              <option v-for="(item, index) in timeMins" :value="index"> {{ item }} </option>
            </select>
            분
          </td>
        </tr>
      </table>
      <table id="HJConfig_table" style=" margin : auto; width:97%; border : 0px;">
        <tr>
          <td style="text-align:left;"> <input type="checkbox" v-model="val_BooleanRemove"> Removing a tuple in table using Boolean value </td>
        </tr>
      </table>
      <table id="HJConfig_table" style=" margin : auto; width:97%; border : 0px;">
        <tr>
          <td style="text-align:left;">
            <select v-model="val_MatchAnd">
              <option value = "0"> Match And Replace </option>
              <option value = "1"> Match And Update </option>
            </select>
          </td>
        </tr>
      </table>
      <table id="HJConfig_table" style=" margin : auto; width:97%; border : 0px;">
        <colgroup>
          <col style="width: 200px">
          <col style="width: 200px">
          <col style="width: 200px">
        </colgroup>

        <tr>
          <td> Decay Base : <input style="width:70px; text-align:center;" type="text" v-model="val_DecayBase"> </td>
          <td> Half Life : <input style="width:70px; text-align:center;" type="text" v-model="val_HalfLife"> </td>
          <td> Thresh Hold : <input style="width:70px; text-align:center;" type="text" v-model="val_ThreshHold"> </td>
        </tr>
      </table>
      <table id="HJConfig_table" style=" margin : auto; width:97%; border : 0px;">
        <tr>
          <td>
            <select style="width : 100%; height:30px; overflow-y:hidden;" size=10>
              <option v-for="(item, index) in val_HJTextArea" :value="index"> {{ item }} </option>
            </select>
          </td>
        </tr>
      </table>
      <table id="HJConfig_table" style=" margin : auto; width:97%; border : 0px;">
        <tr>
          <!-- <td> <button v-on:click="addCondition"> ADD Condition </button> </td> -->
          <td> <button v-on:click="sendHJdata"> ADD Condition </button> </td>
          <td> <button v-on:click="delCondition"> DEL Condition </button> </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  created() {},
  props : ['parent', 'parentAttrs', 'taskID', 'HJfromID'],
  data() {
    return{
      sendData : [],
      timeHours : ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"],
      timeMins : ["00", "10", "20", "30", "40", "50"],
      val_Type1 : 0,
      val_Type2 : 0,
      val_isEqual : 0,
      val_Input1 : 0,
      val_HJAttrIndex1 : 0,
      val_HJAttrIndex2 : 0,
      val_2ColJoin : '',
      val_refreshTimeHour1 : '',
      val_refreshTimeHour2 : '',
      val_refreshTimeMin1 : '',
      val_refreshTimeMin2 : '',
      val_onRfrsTime1 : '',
      val_onRfrsTime2 : '',
      val_BooleanRemove : 0,
      val_MatchAnd : 0,
      val_DecayBase : 1,
      val_HalfLife : 1,
      val_ThreshHold : 0.001,
      val_InputQueueSize : 10,
      des_InputQueueSize : '',
      val_HJTextArea : [],
      val_HJAttrIndex2Fixed : 0
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
    sendHJdata() {
      var sendData;
      var showData;
      var type, relOP;
      var distinct_flag;
      var distinct_name;
      this.sendData = new Object();
      this.sendData = {
        parentAttrs : this.parentAttrs,
        parentIDs : this.HJfromID,
        AttrIndex1 : this.val_HJAttrIndex1,
        AttrIndex2 : this.val_HJAttrIndex2,
        type1 : this.val_Type1,
        type2 : this.val_Type2,
        InputQueueSize : this.val_InputQueueSize,
        isEqual : this.val_isEqual,
        BooleanRemove : this.val_BooleanRemove,
        DecayBase : this.val_DecayBase,
        HalfLife : this.val_HalfLife,
        ThreshHold : this.val_ThreshHold,
        MatchAnd : this.val_MatchAnd,
        AttrIndex2Fixed : this.val_HJAttrIndex2Fixed
      }
      this.$emit('HJqueryAdd', this.sendData);

      var str = "completed";
      this.val_HJTextArea.push(str);
    }
  },
  components: {
  }
}
</script>
