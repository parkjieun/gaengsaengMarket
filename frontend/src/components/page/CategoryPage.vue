<template>
<v-app>
    <v-container fluid>
        <v-row id="postList">
            <post-list :posts="posts" />
        </v-row>
    </v-container>
</v-app>
</template>

<script>
import httpPost from "@/util/http-post"
import PostList from "@/components/post/PostList.vue"
export default {
    compoennts:{
        PostList,
    },
    data(){
        return{
            posts:[],
        }
    },
    created(){
        httpPost.get(`/api/post/category/${this.$route.params.categoryNum}?sno=0`).then(res=>{
            this.posts=res.data
        })
    }
}
</script>

<style>

</style>
