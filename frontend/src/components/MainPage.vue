<template>
<v-app>
    <v-container fluid style="padding-top:0px;">
        <v-row align="center">
            <v-col align="center" style="margin:auto;padding:0px;">
                <v-carousel style="z-index:5;" delimiter-icon="mdi-minus" height="250px" cycle hide-delimiter-background :show-arrows="false">
                    
                    <v-carousel-item src="https://media.bunjang.co.kr/images/nocrop/494869579.jpg">
                    </v-carousel-item>
                    <v-carousel-item src="https://media.bunjang.co.kr/images/nocrop/485696213.jpg">
                    </v-carousel-item>
                </v-carousel>
            </v-col>
        </v-row>
        <div id="postList" style="width:80%; margin-left:auto; margin-right:auto;"> 
            <post-list :posts="posts"/>
        </div>
        <infinite-loading @infinite="infiniteHandler" spinner="waveDots"></infinite-loading>
    </v-container>
</v-app>
</template>

<script>
import banner from "@/assets/banners/banner.png"
import CategoryBar from "@/components/frame/CategoryBar.vue"
import PostList from "@/components/post/PostList.vue"
import CategoryMenu from "@/components/frame/CategoryMenu.vue"
import CategoryTabs from "@/components/frame/CategoryTabs.vue"
import httpPost from "@/util/http-post"
import InfiniteLoading from 'vue-infinite-loading'
export default {
    data() {
        return {
            search: "",
            select:[],
            items: [],
            posts: [],
            start: 0,
         
        }
    },
    components: {
        PostList,
        CategoryBar,
        CategoryMenu,
        CategoryTabs,
        InfiniteLoading,
    },
    modules: {
        banner
    },
    methods: {


        goMyProfile() {
            this.$router.push({ name: 'MyProfile' })
        },
        goPostCreate() {
            this.$router.push( { name: 'post-create' })
        },
        infiniteHandler($state) {
            console.log("스크롤")

            httpPost.get("/api/post?sno=" + this.start)
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
        } 

    },
    mounted() {
   
    }
}
</script>

<style scoped>
.tag-input span.chip {
  background-color: #1976d2;
  color: #fff;
  font-size: 1em;
}

.tag-input span.v-chip {
  background-color: #1976d2;
  color: #fff;
  font-size:1em;
  padding-left:7px;
}

.tag-input span.v-chip::before {
    content: "label";
    font-family: 'Material Icons';
    font-weight: normal;
    font-style: normal;
    font-size: 20px;
    line-height: 1;
    letter-spacing: normal;
    text-transform: none;
    display: inline-block;
    white-space: nowrap;
    word-wrap: normal;
    direction: ltr;
    -webkit-font-feature-settings: 'liga';
    -webkit-font-smoothing: antialiased;
}
</style>
