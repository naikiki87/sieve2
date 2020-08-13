<template>
  <div>
    <div id="AggConfig_title" v-model="taskID"> GROUP BY {{ taskID }} </div>

    <div style="height:240px;">
      <table id="AggGroupByConfig_top_table">
        <tr>
          <td> 속성명 : </td>
          <td>
            <select style="width:200px;" v-model="val_GBAttrIndex">
              <option v-for="(item, index) in parentAttrs" :value="index"> {{ item.id }} - {{ item.name }} </option>
            </select>
          </td>
          <td>
            <button id="AggButton" v-on:click="addGBInputText"> 추가 </button>
          </td>
        </tr>
        <tr>
          <td rowspan="3" colspan="2">
            <select style="width : 95%; height:120px; overflow-y:hidden;" size=10>
              <option v-for="(item, index) in val_GBInputText" :value="index"> {{ item[1].name }} </option>
            </select>
          </td>
          <td> <button id="AggButton" v-on:click="addGBInputText"> 상향 </button> </td>
        </tr>
        <tr>
          <td> <button id="AggButton" v-on:click="abcd"> 하향 </button> </td>
        </tr>
        <tr>
          <td> <button id="AggButton" v-on:click="abcd2"> 삭제 </button> </td>
        </tr>
        <!-- <tr>
          <td v-model="val_TEST"> abcd {{ val_TEST }} </td>
        </tr> -->
      </table>
      <div style="padding-top : 5px;">
        <button id="AggButton2Query" v-on:click="abcd"> to Query </button>
      </div>
    </div>
  </div>
</template>

<script>
import AggQuery from './AggQuery.vue'
export default {
  props : ['parentAttrs', 'taskID', 'configData'],
  data() {
    return{
      sendData : 23456,
      // val_GBAttrIndex : '',
      val_GBAttrIndex : '',
      val_GBInputText : [],
      val_TEST : ''
    }
  },
  updated() {
    this.val_TEST = this.configData;
    console.log(this.val_TEST);
  },
  methods : {
    abcd2() {
      // console.log("abcd2");
      // console.log(this.configData);
    },
    abcd() {
      var sendData2 = ["1234", this.sendData];
      this.$emit('query-add', this.val_GBInputText);
    },
    addGBInputText() {
      this.val_GBInputText.push([this.val_GBAttrIndex, this.parentAttrs[this.val_GBAttrIndex]]);
    }
  },
  components: {
    'AggQuery' : AggQuery
  }
}
</script>

<style src="./css/total.css"></style>
