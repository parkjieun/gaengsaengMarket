<template>
  <div id="app">
      <h5 id="pageTitle">{{keywords}}에 대한 검색결과</h5>
      <hr>
      <PostList :posts="posts"/>
      <infinite-loading @infinite="infiniteHandler" spinner="waveDots" ref="InfiniteLoading"></infinite-loading>
  </div>
</template>

<script>
import httpPost from "@/util/http-post"
import PostList from '@/components/post/PostList.vue'
import InfiniteLoading from 'vue-infinite-loading'

export default {
    components: {
        PostList,
        InfiniteLoading,
    },
    data() {
        return {
            start : 0,
            posts : [],
            keywords: this.$route.params.keyword
        }

    },
    watch: {
        '$route.params.keyword' : 'fetchPage'
    },
    methods: {
        fetchPage() {
            this.posts = []
            this.start = 0
            this.keywords= this.$route.params.keyword
            this.$refs.InfiniteLoading.stateChanger.reset();
        },
        infiniteHandler($state) {
            httpPost.get(`/api/post?sno=${this.start}&query=${this.keywords}`)
            .then(res => {
                console.log(res)
                if (res.data.length) {
                    this.start += 12;
                    this.posts.push(...res.data);
                    $state.loaded();
                } 
                else {
                    $state.complete();
                }
            });      
        },
    }


}
</script>

<style scoped>
#app {
    width: 80%;
    margin-left: auto;
    margin-right: auto;
}
#pageTitle {
    margin: 10px;
}

</style>