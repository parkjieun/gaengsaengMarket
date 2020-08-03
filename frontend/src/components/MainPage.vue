<template>
<v-app>
    <v-container fluid>
       
        <v-row align="center">
            <v-col cols="10" align="center" style="margin:auto;">
                <category-tabs style="height:48px;z-index: 1;" />
                <v-carousel style="z-index:5;" delimiter-icon="mdi-minus" height="200px" cycle hide-delimiter-background :show-arrows="false">
                    <v-carousel-item src="../assets/banners/banner.png">
                    </v-carousel-item>
                </v-carousel>
            </v-col>
        </v-row>
        <v-row id="postList">
            <post-list :posts="posts"/>
        </v-row>
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

export default {
    data() {
        return {
            search: "",
            select:[],
            items: [],
            posts: null,
        }
    },
    components: {
        PostList,
        CategoryBar,
        CategoryMenu,
        CategoryTabs,
    },
    modules: {
        banner
    },
    methods: {
        searchItem() {
            alert("검색 미구현!")
            this.search=""
            console.log(this.search)
        },
        updateTags() {
            this.$nextTick(() => {
                const i = this.search.indexOf("#")
                console.log(i)
                if(i!==-1){
                    const hashtag = this.search.slice(-(this.search.length-i)+1)
                    if(hashtag.length>1){
                        this.select.push(hashtag)
                    }
                }
                // this.select.push(...this.search.split(","));
                
                this.$nextTick(() => {
                    if(i!==-1)
                        this.search = this.search.slice(0,i);
                });
            });
        },

        goMyProfile() {
            this.$router.push({ name: 'MyProfile' })
        },
        goPostCreate() {
            this.$router.push( { name: 'post-create' })
        },

    },
    mounted() {
        httpPost.get("/api/post").then(res => {
            console.log(res.data)
            this.posts=res.data
        })
    }
}
</script>

<style scoped>
#postList {
    width: 80%;
    margin-left: auto;
    margin-right: auto;
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
