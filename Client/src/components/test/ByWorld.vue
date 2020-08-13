<template>
  <div>
    <div v-for="p in array"> {{ p }} </div>
    <!--
    <select>
      <option v-for="(item, index) in array" :value="index"> {{ item }} </option>
    </select>
     -->
  </div>
</template>

<script>
import EventBus from './EventBus';

export default {
  data(){
    return{
      msg: '',
      array : []
    }
  },
  mounted() {
  },
  created(){
    console.log("By world");

    EventBus.$on('push-msg', (payload)=>{
      console.log("HELLO");
      this.array = payload;
      console.log(this.array);
    });
  },
  methods: {
  }
}
</script>
