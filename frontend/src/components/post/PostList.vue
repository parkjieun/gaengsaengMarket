<template>
    <v-row>
        <template v-for="i in len">
            <v-col :key="i" cols="12" md="4" lg="3" xl="2" align="center" >
                <!-- <router-link :to="'/post/detail?post_id=' + posts[i-1].post_id"> -->
                <div @click="goDetail(i)">
                <post-item :post="posts[i-1]" />
                <!-- </router-link>   -->
                </div>
                
            </v-col>
        </template>
    </v-row>  
</template>

<script>
import PostItem from '@/components/post/PostItem.vue'

export default {
    props: ['posts'],
    data(){
        return{
            
        }
    },
    components: {
        PostItem,
    },
    computed:{
        len(){
            if(this.posts!==null){
                return this.posts.length
            }
            return 0
        }
    },
    methods:{
        goDetail(i){
            console.log(i)
            this.$router.push(`/post/detail?post_id=${this.posts[i-1].post_id}`)
        }
    },
}
</script>

<style>


</style>