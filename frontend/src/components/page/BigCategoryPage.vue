<template>
<div id="app">
    <v-row style="border-bottom:1px solid; padding:30px 0px 20px;height:78px; " id="subMenu">
        <div style="font-size:12px; height:28px; ">
            <img style="width:15px; height:15px; margin-right:5px" :src="require(`@/assets/post/home.png`)"> 홈
            <img style="width:6px; height:10px; margin:0px 10px" :src="require(`@/assets/post/next.png`)">  {{ categoryBig }} 
        </div>
    </v-row> 
    <div id="postList" style="margin-top: 30px;"> 
            <PostList :posts="posts"/>
    </div>
    <infinite-loading @infinite="infiniteHandler" spinner="waveDots" ref="InfiniteLoading"></infinite-loading>
</div>
</template>

<script>
import httpPost from "@/util/http-post"
import PostList from "@/components/post/PostList.vue"
import InfiniteLoading from 'vue-infinite-loading'

export default {
    components:{
        PostList,
        InfiniteLoading,
    },
    data(){
        return{
            posts:[],
            start: 0,
            categoryBig: '',
        }
    },
    watch: {
        '$route.params.bigCategoryNum': 'fetchData'
    },
    methods: {
        fetchData() {
            this.posts = []
            this.start = 0
            this.categoryBig = this.$route.params.bigCategoryName
            this.$refs.InfiniteLoading.stateChanger.reset();
        },
        infiniteHandler($state) {
            httpPost.get(`/api/post/category/big/${this.$route.params.bigCategoryNum}?sno=` + this.start)
            .then(res => {
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

    },
    mounted() {
        this.categoryBig = this.$route.params.bigCategoryName
             
    },
}
</script>

<style scoped>
#app {
    width: 80%;
    margin: 0 auto 0 auto;
}

</style>
