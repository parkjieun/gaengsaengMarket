<template>
  <div class="home">
    <SearchBar/>
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import SearchBar from '@/components/frame/SearchBar.vue'

export default {
  name: 'Home',
  components: {
    SearchBar,
    HelloWorld
  }
}
</script>
