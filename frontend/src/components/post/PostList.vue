<template>
    <v-row>
        <template v-for="i in len">
            <v-col :key="i" cols="12" sm="6" md="4" lg="3" xl="2" align="center" >
                <!-- <router-link :to="'/post/detail?post_id=' + posts[i-1].post_id"> -->
                <div @click="goDetail(i)">
                <post-item :post="posts[i-1]" />
                <!-- </router-link>   -->
                </div>  
            </v-col>
        </template>
        <v-btn
            id="scrollButton"
            fab
            small
            color="#a6e3e9"
            retain-focus-on-click
            @click="scrollToTop"
        >
            <v-icon color="#3f696e">mdi-chevron-up</v-icon>
        </v-btn>
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
        },
        scrollToTop() {
            window.scroll({top: 0, left: 0, behavior: 'smooth'})
        },
    },
}
</script>

<style scoped>
#scrollButton {
    position: fixed;
    bottom: 20px;
    right: 30px;
    z-index: 99;
}

</style>