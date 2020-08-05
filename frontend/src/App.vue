<template>
  <v-app id="app">
    <!-- <div>
      <CategoryMenu :category="category" @category="openCategory"/>
    </div>
    <div id="navbar"> -->
      <NavBar :category="category" @openForm="openForm" @openCategory="openCategory"/>
    <!-- </div> -->
    
    <!-- Sizes your content based upon application components -->
    <!-- <v-main>

      <v-container fluid> -->

        <!-- If using vue-router -->
        <router-view></router-view>
      <!-- </v-container>
    </v-main> -->

    <!-- <v-footer app>

    </v-footer> -->
    <login-form  :dialog="dialog" @closeForm="dialog=false"/>
  </v-app>
</template>

<script>
import NavBar from '@/components/frame/NavBar.vue'
import CategoryMenu from '@/components/frame/CategoryMenu.vue'
import LoginForm from "@/components/user/LoginForm.vue"
export default {
  components: {
    NavBar,
    CategoryMenu,
    LoginForm
  },
  
  data() {
    return {
      dialog:false,
      category: false
      
    }
  },
  methods: {
    openCategory(value) {
      console.log(value)
      this.category = value
    },
    openForm(){
      this.dialog=true;
    },
    closeForm(){
      this.dialog=false;
    }
  },
  
}
</script>

<style>
textarea{
  resize:none;
}
</style>