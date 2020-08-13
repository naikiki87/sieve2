<template>
  <div class="hello">
    <button v-on:click="clickBtn">HelloWorld</button>
    <div v-for="p in array"> {{ p }} </div>
    <select>
      <option v-for="(item, index) in array" :value="index"> {{ item }} </option>
    </select>
    <ByWorld></ByWorld>
  </div>
</template>

<script>
import EventBus from './EventBus';
import ByWorld from './ByWorld'

export default {
  name: 'HelloWorld',
  components:{
    ByWorld
  },
  data(){
    return{
      data : [1,2,3,4,5,6],
      array : []
    }
  },
  created() {
    this.array = this.data;
    this.clickBtn();
  },
  methods:{
    clickBtn(){
      EventBus.$emit('push-msg', this.data);
      console.log("click BTN clicked");
    }
  }
}
</script>
