<template>
  <div>
    <div id="AggConfig_title"> ORDER BY </div>
    <div style="height:240px;">
      <table id="AggGroupByConfig_top_table">
        <tr>
          <td>
            <select style="width : 120px;" v-model="val_OB_Attr_Index">
              <option v-for="(item, index) in aggOBItems" :value="index"> {{ item }} </option>
            </select>
          </td>
          <td colspan="2">
            <form>
              <input type="radio" name="whichOrder" checked="checked" value="0" v-model="val_OB_order"/> Ascend
              <input type="radio" name="whichOrder" value="1" v-model="val_OB_order"/> Descend
            </form>
          </td>
          <td> <button id="AggButton" v-on:click="addOBdata"> 추가 </button> </td>
        </tr>
        <tr>
          <td rowspan="3" colspan="3">
            <select style="width : 95%; height:120px; overflow-y:hidden;" size=10>
              <option v-for="(item, index) in val_OB_bulletin" :value="index"> {{ item.itemName }} {{ item.orderName }}</option>
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

        <tr>
          <td> Output Top </td>
          <td> <input type="text" style="width:100px;" v-model="val_OB_outputTop"> </td>
          <td> <input type="checkbox" v-model="val_OB_withRowNum"> With RowNum </td>
          <td> </td>
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
  props : ['aggOBItems'],
  data() {
    return{
      sendData : [],
      val_OB_order : 0,
      val_OB_bulletin : [],
      val_OB_outputTop : '',
      val_OB_withRowNum : '',
      val_OB_Attr_Index : 0
    }
  },
  methods : {
    abcd() {
      this.$emit('query-add', this.sendData);
    },
    addOBdata() {
      var sendData;
      var showData;
      var orderName;

      // 데이터 showing
      if(this.val_OB_order == 0) { orderName = "ASC"; }
      if(this.val_OB_order == 1) { orderName = "DESC"; }

      showData = new Object();
      showData = {
        itemID : this.val_OB_Attr_Index,
        itemName : this.aggOBItems[this.val_OB_Attr_Index],
        orderCode : this.val_OB_order,
        orderName : orderName,
        outputTop : this.val_OB_outputTop,
        withRowNum : this.val_OB_withRowNum
      }
      this.val_OB_bulletin.push(showData);
      this.sendData.push(showData);

      console.log(showData);

      // 데이터 전송
      // var tempsendData = new Object();
      // tempsendData = {
      //   div : 0,    // 쿼리
      //   id : this.val_HAV_Attr_Index,
      //   typeCode : this.val_HAV_operator,
      //   typeName : type,
      //   distinctFlag : distinct_flag,
      //   distinctName : distinct_name,
      //   attrs : this.parentAttrs[this.val_HAV_Attr_Index],
      //   relOP : this.val_HAV_relOP,
      //   relOPName : relOP,
      //   constant : this.val_HAV_constant
      // }
      //
      // this.sendData.push(tempsendData);
    }
  }
}
</script>

<style src="./css/total.css"></style>
