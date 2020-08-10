<template>
<div id="app">
    <v-row style="border-bottom:1px solid; padding:30px 0px 20px;height:78px; " id="subMenu">
        <div style="font-size:12px; height:28px; ">
            <img style="width:15px; height:15px; margin-right:5px" :src="require(`@/assets/post/home.png`)"> 홈
            <img style="width:6px; height:10px; margin:0px 10px" :src="require(`@/assets/post/next.png`)">  {{ categoryBig }} 
            <img style="width:6px; height:10px; margin:0px 10px" :src="require(`@/assets/post/next.png`)"> {{ categoryMid }} 
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
            categoryMid: '',
            categoryBigList: [],
            categoryBigId: null,
        }
    },
    watch: {
        '$route.params.categoryNum': 'fetchData'
    },
    methods: {
        fetchData() {
            this.posts = [],
            this.start = 0,
            this.categoryMid = '',
            this.categoryBig = '',
            this.categoryBigList = [],
            this.categoryBigId = null,
            this.$refs.InfiniteLoading.stateChanger.reset();
            this.getCategory(this.$route.params.categoryNum)
        },
        infiniteHandler($state) {
            console.log("스크롤")
            httpPost.get(`/api/post/category/${this.$route.params.categoryNum}?sno=` + this.start)
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
        getCategory(categoryId) {
            // 카테고리 저장
            httpPost.get('/api/post/category/category_big')
            .then(res => {
                this.categoryBigList = res.data
                for (var i = 0; i < res.data.length; i++) {
                    if (this.categoryMid) {
                        break;
                    }
                    httpPost.get('/api/post/category/category_mid/' + res.data[i].cate_big_id)
                    .then(mid => {
                        for (var j = 0; j < mid.data.length; j++) {    
                            if (mid.data[j].cate_mid_id == categoryId) {
                                this.categoryMid = mid.data[j].name
                                this.categoryBigId = mid.data[j].cate_big_id
                                break;
                            }
                        }      
                    })   
                }    
            })  
        }

    },
    created() {
        this.getCategory(this.$route.params.categoryNum)
             
    },
    mounted() {

    }
}
</script>

<style scoped>
#app {
    width: 80%;
    margin: 0 auto 0 auto;
}

</style>
