<template>
  <div>
    <div id="AggConfig_title"> HAVING </div>
    <div style="height:240px;">
      <table id="AggGroupByConfig_top_table">
        <tr>
          <td>
            <select id="AggWindowType" v-model="val_HAV_operator">
              <option value = "0"> Group </option>
              <option value = "1"> MAX </option>
              <option value = "2"> MIN </option>
              <option value = "3"> SUM </option>
              <option value = "4"> AVG </option>
              <option value = "5"> Count </option>
              <option value = "6"> Renew </option>
            </select>
          </td>
          <td> <input type="checkbox" v-model="val_HAV_distinct_flag"> distinct </td>
          <td>
            <select v-model="val_HAV_Attr_Index">
              <option v-for="(item, index) in parentAttrs" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
          <td>
            <select id="AggWindowType" v-model="val_HAV_relOP">
              <option value = "0"> = </option>
              <option value = "1"> 〈 </option>
              <option value = "2"> 〈= </option>
              <option value = "3"> 〉 </option>
              <option value = "4"> 〉= </option>
            </select>
          </td>
          <td> <input style="width : 50px;" type="text" v-model="val_HAV_constant"> </td>
          <td> <button id="AggButton" v-on:click="abcd2"> 수정 </button> </td>
          <td> <button id="AggButton" v-on:click="addHAVINGdata"> 추가 </button> </td>
        </tr>
        <tr>
          <td rowspan="5" colspan="6">
            <select style="width : 95%; height:120px; overflow-y:hidden;" size=10>
              <option v-for="(item, index) in val_HAV_bulletin" :value="index"> {{ item.type }}({{ item.distinct }}{{ item.attrs.name }}) {{ item.relOP }} {{ item.constant }} </option>
            </select>
          </td>
          <td> <button id="AggButton" v-on:click="abcd2"> 상향 </button> </td>
        </tr>
        <tr>
          <td> <button id="AggButton" v-on:click="abcd2"> 하향 </button> </td>
        </tr>
        <tr>
          <td> <button id="AggButton" v-on:click="abcd2"> 삭제 </button> </td>
        </tr>
        <tr>
          <td> <button id="AggButton" v-on:click="addAND"> + AND </button> </td>
        </tr>
        <tr>
          <td> <button id="AggButton" v-on:click="addOR"> + OR </button> </td>
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
      val_HAV_operator : 0,
      val_HAV_distinct_flag : '',
      val_HAV_Attr_Index : '',
      val_HAV_relOP : '',
      val_HAV_constant : '',
      val_HAV_constant : '',
      val_HAV_bulletin : []
    }
  },
  methods : {
    abcd() {
      this.$emit('query-add', this.sendData);
    },
    abcd2() {
      console.log("Abcd2");

    },
    addAND() {
      var type = 0;
      var typeName = "AND";
      var showData = new Object();
      showData = {
        div : 1,    // AND / OR
        id : '',
        type : typeName,
        distinct : '',
        attrs : '',
        relOP : '',
        constant : ''
      }
      this.val_HAV_bulletin.push(showData);

      var tempsendData = new Object();
      tempsendData = {
        div : 1,    // 쿼리
        id : '',
        typeCode : type,
        typeName : typeName,
        distinctFlag : '',
        distinctName : '',
        attrs : '',
        relOP : '',
        relOPName : '',
        constant : ''
      }
      this.sendData.push(tempsendData);
    },
    addOR() {
      var type = 1;
      var typeName = "OR";
      var showData = new Object();
      showData = {
        div : 1,    // AND / OR
        id : '',
        type : typeName,
        distinct : '',
        attrs : '',
        relOP : '',
        constant : ''
      }
      this.val_HAV_bulletin.push(showData);

      var tempsendData = new Object();
      tempsendData = {
        div : 1,    // 쿼리
        id : '',
        typeCode : type,
        typeName : typeName,
        distinctFlag : '',
        distinctName : '',
        attrs : '',
        relOP : '',
        relOPName : '',
        constant : ''
      }
      this.sendData.push(tempsendData);
    },
    addHAVINGdata() {
      var sendData;
      var showData;
      var type, relOP;
      var distinct_flag;
      var distinct_name;

      // 데이터 showing
      if(this.val_HAV_operator == 0) { type = "Group"; }
      if(this.val_HAV_operator == 1) { type = "MAX"; }
      if(this.val_HAV_operator == 2) { type = "MIN"; }
      if(this.val_HAV_operator == 3) { type = "SUM"; }
      if(this.val_HAV_operator == 4) { type = "AVG"; }
      if(this.val_HAV_operator == 5) { type = "Count"; }
      if(this.val_HAV_operator == 6) { type = "Renew"; }
      if(this.val_HAV_distinct_flag == "1") {
        distinct_name = "DISTINCT ";
        distinct_flag = 1;
      }
      else {
        distinct_name = "";
        distinct_flag = 0;
      }

      if(this.val_HAV_relOP == 0) { relOP = "="; }
      if(this.val_HAV_relOP == 1) { relOP = "〈"; }
      if(this.val_HAV_relOP == 2) { relOP = "〈="; }
      if(this.val_HAV_relOP == 3) { relOP = "〉"; }
      if(this.val_HAV_relOP == 4) { relOP = "〉="; }

      showData = new Object();
      showData = {
        div : 0,
        id : this.val_HAV_Attr_Index,
        type : type,
        distinct : distinct_name,
        attrs : this.parentAttrs[this.val_HAV_Attr_Index],
        relOP : relOP,
        constant : this.val_HAV_constant
      }

      this.val_HAV_bulletin.push(showData);

      // 데이터 전송
      var tempsendData = new Object();
      tempsendData = {
        div : 0,    // 쿼리
        id : this.val_HAV_Attr_Index,
        typeCode : this.val_HAV_operator,
        typeName : type,
        distinctFlag : distinct_flag,
        distinctName : distinct_name,
        attrs : this.parentAttrs[this.val_HAV_Attr_Index],
        relOP : this.val_HAV_relOP,
        relOPName : relOP,
        constant : this.val_HAV_constant
      }

      this.sendData.push(tempsendData);
    }
  }
}
</script>

<style src="./css/total.css"></style>
