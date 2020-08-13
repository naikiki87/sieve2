<template>
  <div>
    <div id="AggConfig_title"> SELECT </div>
    <div style="height:240px;">
      <table id="AggGroupByConfig_top_table">
        <tr>
          <td>
            <select id="AggWindowType" v-model="val_SEL_operator">
              <option value = "0"> Group </option>
              <option value = "1"> MAX </option>
              <option value = "2"> MIN </option>
              <option value = "3"> SUM </option>
              <option value = "4"> AVG </option>
              <option value = "5"> Count </option>
              <option value = "6"> Renew </option>
            </select>
          </td>
          <td> <input type="checkbox" v-model="val_SEL_Distinct_flag"> distinct_flag </td>
          <td>
            <select v-model="val_SEL_Attr_Index">
              <option v-for="(item, index) in parentAttrs" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
          <td> <button id="AggButton" v-on:click="abcd"> 수정 </button> </td>
          <td> <button id="AggButton" v-on:click="addSelectInputText"> 추가 </button> </td>
        </tr>
        <tr>
          <td rowspan="3" colspan="4">
            <select style="width : 95%; height:120px; overflow-y:hidden;" size=10>
              <option v-for="(item, index) in val_SEL_InputText" :value="index"> {{ item[1] }}({{ item[2] }}{{ item[3].name }}) </option>
            </select>
          </td>
          <td> <button id="AggButton" v-on:click="abcd"> 상향 </button> </td>
        </tr>
        <tr>
          <td> <button id="AggButton" v-on:click="abcd"> 하향 </button> </td>
        </tr>
        <tr>
          <td> <button id="AggButton" v-on:click="abcd"> 삭제 </button> </td>
        </tr>
      </table>
      <div style="padding-top : 5px;">
        <button id="AggButton2Query" v-on:click="abcd"> to Query </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props : ['parentAttrs', 'taskID'],
  data() {
    return{
      sendData : [],
      val_SEL_InputText : [],
      val_SEL_operator : 0,
      val_SEL_Distinct_flag : 0,
      val_SEL_Attr_Index : '',

      OBItems : []
    }
  },
  methods : {
    abcd() {
      this.$emit('query-add', this.sendData);
    },
    addSelectInputText() {
      var sendData;
      var showData;
      var type;
      var distinct_flag;
      var distinct_name;

      // 데이터 showing
      if(this.val_SEL_operator == 0) { type = "Group"; }
      if(this.val_SEL_operator == 1) { type = "MAX"; }
      if(this.val_SEL_operator == 2) { type = "MIN"; }
      if(this.val_SEL_operator == 3) { type = "SUM"; }
      if(this.val_SEL_operator == 4) { type = "AVG"; }
      if(this.val_SEL_operator == 5) { type = "Count"; }
      if(this.val_SEL_operator == 6) { type = "Renew"; }
      if(this.val_SEL_Distinct_flag == "1") {
        distinct_name = "DISTINCT ";
        distinct_flag = 1;
      }
      else {
        distinct_name = "";
        distinct_flag = 0;
      }

      var tempOBItem;

      tempOBItem = type + '(' + distinct_name + this.parentAttrs[this.val_SEL_Attr_Index].name + ')';
      console.log("Here : " + tempOBItem);

      this.OBItems.push(tempOBItem);
      this.$emit('obItem-add', this.OBItems);

      showData = [this.val_SEL_Attr_Index, type, distinct_name, this.parentAttrs[this.val_SEL_Attr_Index]];
      this.val_SEL_InputText.push(showData);

      // 데이터 전송
      // if(this.val_SEL_Distinct_flag == "1") { distinct_flag = 1;} else { distinct_flag = 0;}

      var tempSELECT = new Object();
      tempSELECT.id = this.val_SEL_Attr_Index;
      tempSELECT.opCode = this.val_SEL_operator;
      tempSELECT.opName = type;
      tempSELECT.distinctCode = distinct_flag;
      tempSELECT.distinctName = distinct_name;
      tempSELECT.AttrName = this.parentAttrs[this.val_SEL_Attr_Index].name;

      this.sendData.push(tempSELECT);
    }
  }
}
</script>

<style src="./css/total.css"></style>
