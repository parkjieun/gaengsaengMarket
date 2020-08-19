<template>
<div id="app">
    <v-row style="border-bottom:1px solid; padding:30px 0px 20px;height:78px; " id="subMenu">
        <div style="font-size:14px; height:28px; font-weight:550;z-index:5; ">
            <a @click="gohome()" class="top_a"><img style="width:16px; height:16px; margin-right:5px" :src="require(`@/assets/post/home.png`)">홈</a>
            <img style="width:6px; height:10px; margin:0px 10px" :src="require(`@/assets/post/next.png`)">
            <a class="top_a">  {{categoryBig[0].name }} </a>
            <!-- <img style="width:15px; height:15px; margin-right:5px" :src="require(`@/assets/post/home.png`)"> 홈
            <img style="width:6px; height:10px; margin:0px 10px" :src="require(`@/assets/post/next.png`)">  {{ categoryBig[0].name }}  -->
        </div>
    </v-row> 
    <template v-if="noData" >
            <v-col cols="12" style="text-align:center;margin-top:100px;font-weight:bold;font-size:20px">해당 카테고리에 등록된 상품이 없습니다.</v-col>
    </template>
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
            noData: false
        }
    },
    watch: {
        '$route.params.bigCategoryNum': 'fetchData'
    },
    methods: {
        gohome(){
          this.$router.push("/")
        },
        goBigCategory(){
           this.$router.push({name:'BigCategoryPage', params:{bigCategoryNum: this.categoryBig[0].cate_big_id}})
        },
        fetchData() {
            this.posts = []
            this.start = 0
            this.noData = false;
            this.$refs.InfiniteLoading.stateChanger.reset();
            this.getCategoryName()
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
                    if (this.posts.length == 0) {
                        this.noData = true
                    }
                }
            });      
        },
        getCategoryName() {
            httpPost.get('/api/post/category/category_big')
            .then(res => {
                this.categoryBig = res.data.filter(cate => {
                    return (cate.cate_big_id == this.$route.params.bigCategoryNum)
                })
            })
        }

    },
    mounted() {
        this.getCategoryName()

    },
}
</script>

<style scoped>
#app {
    width: 80%;
    margin: 0 auto 0 auto;
}
.top_a{
  color:#3d1860;
  font-weight:bold;
}
</style>
