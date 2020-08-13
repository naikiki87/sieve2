<template>
  <div>
    <hr>
    <table id="AggQuery_table">
      <tr>
        <td style="width : 100px; font-size:15px; font-weight:700; color:blue;"> QUERY </td>
        <td>
          <input style="width : 95%; height:50px;" type="textarea" v-model="prop">
          <input style="width : 95%; height:20px;" type="text" v-model="input_data">
        </td>
        <p style="width : 95%; height:20px;" type="text" v-model="input_data"> {{ input_data }} </p>
        <button v-on:click="valueTrans"> click </button>

      </tr>
    </table>
  </div>
</template>

<script>
export default {
  props : ['prop'],
  data() {
    return{
      input_data : '',
      val_InputQueueSize : '',
      val_WindowType : '',
      val_WindowSize : '',
      val_NumberofPane : '',
      val_SlidingSize : '',

      des_InputQueueSize : '',
      des_WindowType : '',
      des_WindowSize : '',
      des_NumberofPane : '',
      des_SlidingSize : ''
    }
  },
  methods : {
    valueTrans() {
      // this.$emit('childs-event', 'hello!')
      this.$emit('childs-event', this.input_data)
    }
  }

}
</script>

<style src="./css/total.css"></style>
