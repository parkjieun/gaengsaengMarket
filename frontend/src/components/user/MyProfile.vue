<template>
    <div id="myProfile">
        <v-row justify="center" align="center">
        <!-- profile image -->
            <v-avatar color="#a6e3e9" size="62">
                <img v-if="!!myProfile.profileImg" :src="'http://i3a504.p.ssafy.io:8003'+this.myProfile.profileImg" alt="Profile-image">
                <v-icon v-else size="40" dark>mdi-account</v-icon>
            </v-avatar>
        <!-- User Info -->
            
            <div id="username">
                <h4 style="margin-left:15px;">{{myProfile.nickName}}</h4>
                <v-subheader>{{myProfile.introduce}}</v-subheader>
            </div>
            
            <div v-if="myPage">
                <h5> Point: {{myProfile.pointVal}} </h5>
            </div>
        <!-- update button -->
        <!-- user == login.user만 보여줄 것!!! -->
            <v-btn prepend-icon="mdi-" color="#defcfc" small @click="updateBtn" style="margin: 10px;" >수정</v-btn>
            
        </v-row>
        <v-divider></v-divider>
        
        <!-- menu -->
        <v-tabs centered>
            <v-tab @click="allProduct()">판매 수</v-tab>
            <v-tab @click="onSale()">판매 중</v-tab>
            <v-tab @click="soldOut()">판매 완료</v-tab>
            <v-tab @click="likePost()">찜한 글</v-tab>
        </v-tabs>

        <!-- product list -->
        <div id="postList">
            <PostList :posts="allPosts"/>
        </div>
    </div>
</template>

<script>
import PostList from '@/components/post/PostList.vue'
import axios from 'axios'
import { mapState, mapActions } from 'vuex'
import httpPost from "@/util/http-post"

export default {
    data() {
        return {
            showPosts: null,
            allPosts: [],
            onSalePosts: [],
            soldOutPosts: [],
            user: this.myProfile,
            myPage: true,
            img,
        }
    },
    components: {
        PostList
    },
    methods: {
        ...mapActions(['getMyProfile', 'getPostsByUID']),
        
         
        setPosts(posts) {
            console.log("판매중?")
            console.log(posts)
          this.allPosts = posts
          this.showPosts = this.allPosts
          // 판매 중인 상품
          this.onSalePosts = posts.filter(function(post) {
              return post.type === 1
          })
          // 판매 완료 상품
          this.soldOutPosts = posts.filter(function(post) {
              return post.type === 2
          }) 
        },
        
        allProduct() {
            this.showPosts = this.allPosts
        },
        onSale() {
            this.showPosts = this.onSalePosts
        },
        soldOut() {
            this.showPosts = this.soldOutPosts
        },
        likePost() {

        },

        updateBtn() {
            this.$router.push({ name: 'UserProfileUpdate' })
        }

    },
    computed : {
        ...mapState(['myProfile', 'postsByUID']),
    },
    created() {
        this.getMyProfile()
        this.getPostsByUID(this.myProfile.userId)
        this.setPosts(this.postsByUID)
        
    },
    mounted() {
        httpPost.get("/api/post?user_id="+ this.myProfile.userId).then(res => {
            console.log("회원정보 작성글", res.data)
            this.allPosts=res.data
            this.setPosts(res.data)
        })
    }
}
</script>

<style scoped>
#postList {
    width: 65%;
    margin-left: auto;
    margin-right: auto;
}
#username {
    width: 30%;
    margin-left: 20px;
    margin-right: 10px;
}
</style>