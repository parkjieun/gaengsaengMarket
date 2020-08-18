<template>
  <div id="app">
      <v-row style="border-bottom:1px solid; padding:30px 0px 20px;height:78px; " id="subtitle">
        <div style="font-size:12px; height:28px; ">
            <v-icon >mdi-magnify</v-icon> <span v-html="msg" style="font-size: 16px"></span>
        </div>
        </v-row> 
        <template v-if="posts.length == 0" >
            <v-col cols="12" style="text-align:center;margin-top:100px;font-weight:bold;font-size:20px">검색결과가 없습니다.</v-col>
            <v-col cols="12"  style="font-size:14px;text-align:center;">
                - 단어의 철자가 정확한지 확인해 보세요<br>
                - 보다 일반적인 검색어로 다시 검색해 보세요<br>
                - 검색어의 띄어쓰기를 다르게 해보세요<br>
                - 유해/금지어가 아닌지 확인해주세요<br>
            </v-col>
        </template>
        <div v-else>
            <PostList :posts="posts"/>
        </div>
  </div>
</template>

<script>
import httpPost from "@/util/http-post"
import PostList from '@/components/post/PostList.vue'

export default {
    components: {
        PostList,
    },
    data() {
        return {
            posts : [],
            tags: this.$route.params.tags,
            title: this.$route.params.title,
            msg: '',
        }
    },
    watch: {
        '$route.params.keyword' : 'fetchPage'
    },
    methods: {
        fetchPage() {
            this.posts = []
            this.tags = this.$route.params.tags
            this.title = this.$route.params.title
            if (this.tags && this.title) {
                this.msg = `해시태그 검색:  <span style="color:red;">${this. tags}</span> & 제목 검색: <span  style="color:red;">${this.title}</span>`
                httpPost.get(`/api/post?query=${this.title}&tags=${this.tags}`)
                .then(res => {
                    this.posts = res.data
                })
            }
            else if (this.tags || this.title) {
                if (this.tags) {
                    this.msg = `해시태그 검색: <span style="color:red;">${this. tags}</span>`
                    httpPost.get(`/api/post?tags=${this.tags}`)
                    .then(res => {
                        this.posts = res.data
                    })
                }
                else {
                    this.msg = `제목 검색: <span style="color:red;">${this.title}</span>`
                    httpPost.get(`/api/post?query=${this.title}`)
                    .then(res => {
                        this.posts = res.data
                    })
                }
            }
            else {
                console.log(this.tags)
                console.log(this.title)
                this.msg = "검색어를 정확히 입력해주세요"
            }
        },

    },
    created() {
        this.fetchPage()
    }


}
</script>

<style scoped>
#app {
    width: 80%;
    margin-left: auto;
    margin-right: auto;
}

</style>