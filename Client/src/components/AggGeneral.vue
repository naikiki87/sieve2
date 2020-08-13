<template>
  <div>
    <div id="AggConfig_title"> GENERAL </div>
    <div style="height:240px;">
      <table id="AggConfig_table" style=" margin : auto; width:97%;">
      <colgroup>
        <col style="width: 120px"> <!-- Name -->
        <col style="width: 80px"> <!-- value -->
        <col style="width: 250px"> <!-- description -->
      </colgroup>

      <th>Name</th>
      <th>Value</th>
      <th>Description</th>

      <tr>
        <td> Input Queue Size </td>
        <td> <input class="AggGeneralInput" type="text" v-model="val_InputQueueSize"> </td>
        <td> <input style="width : 95%;" type="text" v-model="des_InputQueueSize"> </td>
      </tr>

      <tr>
        <td> Window Type </td>
        <td>
          <select id="AggWindowType" v-model="val_WindowType">
            <option value = "0"> TUPLE </option>
            <option value = "1"> TIME </option>
          </select>
        </td>
        <td> <input style="width : 95%;" type="text" v-model="des_WindowType"> </td>
      </tr>

      <tr>
        <td> Window Size </td>
        <td> <input class="AggGeneralInput" type="text" v-model="val_WindowSize"> </td>
        <td> <input style="width : 95%;" type="text" v-model="des_WindowSize"> </td>
      </tr>

      <tr>
        <td> Number of Pane </td>
        <td> <input class="AggGeneralInput" type="text" v-model="val_NumberofPane"> </td>
        <td> <input style="width : 95%;" type="text" v-model="des_NumberofPane"> </td>
      </tr>

      <tr>
        <td> Sliding Size </td>
        <td> <input class="AggGeneralInput" type="text" v-model="val_SlidingSize"> </td>
        <td> <input style="width : 95%;" type="text" v-model="des_SlidingSize"> </td>
      </tr>


    </table>
    </div>
    <div>
      <!-- <AggQuery v-bind:prop="abcd" v-on:childs-event="parentsMethod"> </AggQuery> -->
    </div>
  </div>
</template>

<script>
import AggQuery from './AggQuery.vue'

export default {

  data() {
    return{
      abcd : 1234,
      receiveData : '',
      val_InputQueueSize : 10,
      val_WindowType : 1,
      val_WindowSize : 60,
      val_NumberofPane : 6,
      val_SlidingSize : 10,

      des_InputQueueSize : '',
      des_WindowType : '',
      des_WindowSize : '',
      des_NumberofPane : '',
      des_SlidingSize : ''
    }
  },
  methods : {
    parentsMethod(message) {
      alert(message);
    }
  },
  components: {
    'AggQuery' : AggQuery
  }
}
</script>

<style src="./css/total.css"></style>
