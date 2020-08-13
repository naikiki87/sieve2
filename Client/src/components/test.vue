<template>
  <div> TEST HELLO() 2
    <button v-on:click="testM"> test </button>
  </div>
</template>

<script>
import userList from "../assets/data/users.json";

export default {
  data(){
    return{
      user : userList,
      msg: '',
      array : [],
      count: 0
    }
  },
  created(){
    console.log("Hello json");
  },
  methods: {
    testM() {
      console.log("Test MMM");
      console.log(this.user.hostserver);
      console.log(this.user.users[0].firstname);
      console.log(this.user.users[0].lastname);
    }
  }
}
</script>
