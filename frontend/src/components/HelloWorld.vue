<template>
  <v-app>
    <!-- <v-btn
    @click="dialog=true"
    >
    login
    </v-btn> -->
    <!-- <login-form ref="foo" @onclick="dialog=true;$emit('render')" :dialog="dialog" @closeForm="dialog=false"/> -->
    <login-form  :dialog="dialog" @closeForm="dialog=false"/>
    
  </v-app>
</template>

<script>
import LoginForm from "@/components/user/LoginForm.vue"
  export default {
    name: 'HelloWorld',
    data(){
      return{
        dialog:false,
      }
    },
    components:{
      LoginForm,
    },
    methods:{
      closeForm(){
        this.dialog=false;
      }
    }
  }
</script>
