<template>
    <div id="userProfile">
        <h3>회원정보</h3>
        <v-divider class="my-5"></v-divider>
        <v-row justify="center" align="center">
        <!-- profile image -->
            <v-avatar color="#a6e3e9" size="62">
                <img v-if="!!user.profileImg" :src="imgURL" alt="Profile-image">
                <v-icon v-else size="40" dark>mdi-account</v-icon>
            </v-avatar>
        <!-- User Info -->
            
            <div id="username">
                <h4>{{user.nickName}}</h4>
                <v-subheader>{{user.introduce}}</v-subheader>
            </div>
            
            <div v-if="myPage">
                <h5> GM: {{$store.state.myProfile.pointVal | currency}} </h5>
            </div>
        <!-- update button -->
        <!-- user == login.user만 보여줄 것!!! -->
            <v-btn v-if="myPage" color="#defcfc" small @click="updateBtn" style="margin: 10px;">수정</v-btn>
            <v-btn v-if="myPage" color="#defcfc" small @click="chargePoint" style="margin: 10px;">갱생머니 충전</v-btn>
        </v-row>
        
        <!-- menu -->
        <v-tabs centered>
            <v-tab @click="allProduct()">판매 수</v-tab>
            <v-tab @click="onSale()">판매 중</v-tab>
            <v-tab @click="soldOut()">판매 완료</v-tab>
            <v-tab v-if="myPage">찜한 글</v-tab>
        </v-tabs>

        <!-- product list -->
        <PostList :posts="showPosts" id="postList"/>
    </div>
</template>

<script>
import PostList from '@/components/post/PostList.vue'
import httpUser from '@/util/http-common'
import httpPost from '@/util/http-post'
import axios from 'axios'
import { mapState, mapActions,mapGetters } from 'vuex'
const baseURL = "http://i3a504.p.ssafy.io"
export default {
    data() {
        return {
            showPosts: null,
            allPosts: [],
            onSalePosts: [],
            soldOutPosts: [],
            user: null,
        }
    },
    components: {
        PostList
    },
    methods: {
        ...mapActions(['setUserProfile']),
        getUserProfile(userID) {
            httpUser.get('/api/user/' + userID,{headers:{ Authorization: this.$store.state.authorization }})
            .then((res) => {
                this.user = res.data
                httpPost.get('/api/post?user_id=' + res.data.userId)
                .then((res) => {
                    this.setPosts(res.data)
                })
            })
        },
        setPosts(posts) {
          this.allPosts = posts
          this.showPosts = this.allPosts
          // 판매 중인 상품
          this.onSalePosts = posts.filter(function(post) {
              return post.type === 1
          })
          // 판매 완료 상품
          this.soldOutPosts = posts.filter(function(post) {
              return post.type === 0
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

        updateBtn() {
            this.$router.push({ name: 'UserProfileUpdate' })
        },
        chargePoint(){
            var store = this.$store
            let routeData = this.$router.resolve('/point');
            var win = window.open(routeData.href,  "a", "width=400, height=400, left=100, top=50")
            var timer = setInterval(function() { 
                if(win.closed) {
                    clearInterval(timer)
                    store.dispatch("getMyProfile")
                }
            }, 1000);
        }
    },
    computed : {
        
        ...mapState(['myProfile']),
        
        myPage: function() { return this.myProfile.userId == this.user.userId },
        imgURL: function() { return baseURL + "/static/image/account/" + this.user.profileImg }
    },
    created() {
        
        this.getUserProfile (this.$route.params.uid)
        this.setUserProfile()
    },
    watch:{
        '$route'(){
            this.getUserProfile(this.$route.params.uid)
        },

    },
    filters:{
        currency: function (value) {
            var num = new Number(value);
            return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,")
        },
    }
}
</script>

<style scoped>
#userProfile {
    width: 75%;
    margin-left: auto;
    margin-right: auto;
}
#postList {
    margin-left: auto;
    margin-right: auto;
}
#username {
    width: 30%;
    margin-left: 20px;
    margin-right: 10px;
}
</style>