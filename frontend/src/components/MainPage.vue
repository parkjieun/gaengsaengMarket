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
        <div class="postList" style="width:80%; margin-left:auto; margin-right:auto;">
            <h3>최근 등록 상품</h3>
            <hr>
            <PostListCarousel :posts="recentPosts"/>
        </div>
        <div style="width: 80%; margin-left:auto; margin-right:auto;">
            <h3>카테고리별 상품</h3>
            <v-tabs style="width: 100%;" grow>
                <v-tab v-for="name in categorytab" :key="name" @click="goToCategory">{{name}}</v-tab>
            </v-tabs>
        </div>
        <div class="postList" style="width:80%; margin-left:auto; margin-right:auto;" v-for="post in categoryPosts" :key="post[0]">
            <div class="categoryTitles" id="categoryTitle" style="align-items:center;"><v-icon small >mdi-alpha-c-circle</v-icon><span style="margin: 5px 5px 5px 5px;"> {{ post[0] }} </span><v-btn text small style="margin-left:10px; color: gray;">전체보기</v-btn></div> 
            <PostList :posts="post[1]"/>
        </div>

    </v-container>
</v-app>
</template>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script>
import banner from "@/assets/banners/banner.png"
import CategoryBar from "@/components/frame/CategoryBar.vue"
import PostListCarousel from "@/components/post/PostListCarousel.vue"
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
            recentPosts: [],
            categoryPosts: [],
            categorytab: []
            // #app > div > div > div:nth-child(4) > div.categoryTitles
            // #app > div > div > div:nth-child(5) > div.categoryTitles
        }
    },
    components: {
        PostList,
        PostListCarousel,
        CategoryBar,
        CategoryMenu,
        CategoryTabs,
        InfiniteLoading,
    },
    computed: {
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
        setCategoryId() {
            var list = document.getElementsByClassName("categoryTitles");
            console.log(list)
            for (var i = 0; i < list.length; i++) {
                console.log(list.item(i))
            }
        },
        goToCategory(i) {
            $('html, body').animate({scrollTop: $("#categoryTitle[i]").offset().top}, 100);
            
        },

    },
    created() {
        httpPost.get('/api/post?sno=0')
        .then(res => {
            this.recentPosts = res.data
        })
        httpPost.get('/api/post/category/category_big')
        .then(res => {
            for (var i = 0; i < res.data.length; i ++) {
                const cate_name = res.data[i].name
                httpPost.get(`/api/post/category/big/${res.data[i].cate_big_id}?sno=0`)
                .then(posts => {
                    if (posts.data.length) {
                        this.categoryPosts.push([cate_name, posts.data.slice(0, 8)])
                        this.categorytab.push(cate_name)
                    }         
                })
            }
        })
        .then(
            this.setCategoryId()
        )
        
    },
    mounted() {
        
   
    }
}
</script>

<style scoped>
h3 {
    margin-top: 10px;
    margin-bottom: 10px;
}
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
