<template>
    <div id="myProfile">
        <h3>마이페이지</h3>
        <v-divider class="my-5"></v-divider>
        <v-row justify="center" align="center">
        <!-- profile image -->
            <v-avatar color="#a6e3e9" size="62">
                <img v-if="!!myProfile.profileImg" :src="imgURL" alt="Profile-image">
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
            <v-btn color="#defcfc" small @click="updateBtn" style="margin: 10px;" >수정</v-btn>
            
        </v-row>
        
        <!-- menu -->
        <v-tabs centered>
            <v-tab @click="allProduct()">판매 수</v-tab>
            <v-tab @click="onSale()">판매 중</v-tab>
            <v-tab @click="soldOut()">판매 완료</v-tab>
            <v-tab @click="likePost()">찜한 글</v-tab>
        </v-tabs>

        <!-- product list -->
        <div id="postList">
            <PostList :posts="showPosts"/>
        </div>
    </div>
</template>

<script>
import PostList from '@/components/post/PostList.vue'
import axios from 'axios'
import { mapState, mapActions } from 'vuex'
import httpPost from '@/util/http-post'
const baseURL = "http://i3a504.p.ssafy.io"
export default {
    data() {
        return {
            showPosts: null,
            allPosts: [],
            onSalePosts: [],
            soldOutPosts: [],
            likePosts: [],
            user: this.myProfile,
            myPage: true,
        }
    },
    components: {
        PostList
    },
    methods: {
        ...mapActions(['setUserProfile']),
        
         
        setPosts(data) {
            this.allPosts = data
            this.showPosts = this.allPosts
            // 판매 중인 상품
            this.onSalePosts = data.filter(function(post) {
                return post.type === 1
            })
            // 판매 완료 상품
            this.soldOutPosts = data.filter(function(post) {
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
        likePost() {
            this.showPosts = this.likePosts

        },

        updateBtn() {
            this.$router.push({ name: 'UserProfileUpdate' })
        }

    },
    computed : {
        ...mapState(['myProfile', 'isAuthenticated']),
        imgURL: function() { return baseURL + "/static/image/account/" + this.myProfile.profileImg }
    },
    created() {
        this.setUserProfile()
        .then(() => {
            httpPost.get('/api/post?user_id=' + this.myProfile.userId)
            .then((res) => {
                this.setPosts(res.data)
            })
        })
        .then(() => {
            httpPost.get('/api/post?sno=0&like=true&user_id='+this.myProfile.userId)
            .then((res) => {
                this.likePosts = res.data
            })
        })
    },
    mounted() {
        if ( !this.isAuthenticated ) {
            //alert("로그인을 해주세요")
            this.$dialog.notify.error('로그인을 해주세요', {
                position: 'top-right',
                timeout: 2000
            })
            this.$router.push({name: 'MainPage'})
        }
    }
}
</script>

<style scoped>
#myProfile {
    width: 75%;
    margin-left: auto;
    margin-right: auto;
}
h5 {
    margin-bottom: 10px;
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