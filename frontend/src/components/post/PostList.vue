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
        <template v-if="len==0" >
            <v-col cols="12" style="text-align:center;margin-top:100px;font-weight:bold;font-size:20px">검색결과가 없습니다.</v-col>
            <v-col cols="12"  style="font-size:14px;text-align:center;">
                - 단어의 철자가 정확한지 확인해 보세요<br>
                - 보다 일반적인 검색어로 다시 검색해 보세요<br>
                - 검색어의 띄어쓰기를 다르게 해보세요<br>
                - 유해/금지어가 아닌지 확인해주세요<br>
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
            windowTop: 0,
            
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
    watch: {
        windowTop: function() {
            if (this.windowTop > 400) {
                var btn = document.getElementById('scrollButton')
                btn.style.display = 'block'
            }
            else {
                var btn = document.getElementById('scrollButton')
                btn.style.display = 'none'
            }
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
        onScroll(e) {
            this.windowTop = e.target.documentElement.scrollTop;
        }
    },
    mounted() {
        window.addEventListener("scroll", this.onScroll)
    },
    beforeDestroy() {
        window.removeEventListener("scroll", this.onScroll);
    },
}
</script>

<style scoped>
#scrollButton {
    position: fixed;
    display: none;
    bottom: 20px;
    left: 30px;
}

</style>