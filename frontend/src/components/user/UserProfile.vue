<template>
    <div id="userProfile">
        <h3 style="margin-top: 30px; color:#3d1860;">회원정보</h3>
        <v-divider class="my-5" color="#bb99cd"></v-divider>
        <!-- 내정보 -->
        <v-row style="position:relative;" v-if="myPage">
        
        <!-- profile image -->
            <div>
                <v-img v-if="!!user.profileImg" :src="imgURL" alt="Profile-image" :aspect-ratio="1" max-width="200" min-width="200" style="margin-left:15px;"> </v-img>
                <v-img v-else :src="require(`@/assets/post/noUserImg.png`)" ></v-img>
            </div>
        <!-- User Info -->
            
            <div id="username" style="margin: 30px; max-width:300px;">
                <div class="d-flex align-center">
                    <div style="font-weight: bolder;">{{myProfile.nickName}}</div>
                    <div class="px-3" v-if="this.phone" style="font-size:0.8rem; color:gray;">{{phone}}</div>
                </div>
                <v-subheader class="px-0">{{myProfile.introduce}}</v-subheader>
            </div>
            
            <div class="d-md-flex d-none" style="margin: 30px; position:absolute; top: 0; right: 0;">
                <h5> GM: {{$store.state.myProfile.pointVal | currency}} 
                    <span><v-btn color="#e7d8eb" small @click="chargePoint" style="margin: 10px; width:100px; color:#3d1860;">갱생머니 충전</v-btn></span> 
                </h5>  
            </div>
           
            <div class="d-md-none" style="position:absolute; bottom: 0; right: 150px;">
                <h5> GM: {{$store.state.myProfile.pointVal | currency}} 
                    <span><v-btn color="#e7d8eb" small @click="chargePoint" style="margin: 10px; width:100px; color:#3d1860;">갱생머니 충전</v-btn></span> 
                </h5>  
            </div>
        <!-- update button -->
            <v-btn color="#e7d8eb" small @click="updateBtn" style="margin: 10px; width:100px; position: absolute; bottom:0; right:30px; color:#3d1860;">수정</v-btn>
            
        </v-row>

        <!-- 다른 유저 프로필 -->
        <v-row style="position:relative;" v-else>
        
        <!-- profile image -->
            <div>
                <v-img v-if="!!user.profileImg" :src="imgURL" alt="Profile-image" :aspect-ratio="1" max-width="200" min-width="200" style="margin-left:15px;"> </v-img>
                <v-img v-else :src="require(`@/assets/post/noUserImg.png`)" ></v-img>
            </div>
        <!-- User Info -->
            
            <div id="username" style="margin: 30px; max-width:300px;">
                <div class="d-flex align-center">
                    <div style="font-weight: bolder;">{{user.nickName}}</div>
                </div>
                <v-subheader class="px-0">{{user.introduce}}</v-subheader>
            </div>
            
            
            <div style="margin: 30px; position:absolute; top: 0; right: 0; cursor:pointer;" @click="createRoom">
                <v-icon x-large style="margin-left: 5px;" color="#a6e3e9">mdi-chat-processing</v-icon>
            </div>
            
        </v-row>
        
        <!-- menu -->
        <v-divider style="margin-top:30px;"></v-divider>
        <v-tabs grow color="#3d1860">
            <v-tab @click="allProduct()">총 판매</v-tab>
            <v-tab @click="onSale()">판매 중</v-tab>
            <v-tab @click="soldOut()">판매 완료</v-tab>
            <v-tab v-if="myPage" @click="likePost()">찜한 글</v-tab>
        </v-tabs>
        <v-divider></v-divider>

        <!-- product list -->
        <PostList :posts="showPosts" id="postList"/>
    </div>
</template>

<script>
import PostList from '@/components/post/PostList.vue'
import httpUser from '@/util/http-common'
import httpPost from '@/util/http-post'
import httpChat from "@/util/http-chat"

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
            likePosts: [],
            user: null,
            myPage: '',
        }
    },
    components: {
        PostList
    },
    methods: {
        ...mapActions(['setUserProfile']),
        isMyPage() {
            if (this.myProfile.userId == this.$route.params.uid) {
                this.myPage = true
            }
            else {
                this.myPage = false
            }
    
        },
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
        getLikePosts(userId) {
            httpPost.get('/api/post?sno=0&like=true&user_id='+userId)
            .then((res) => {
                this.likePosts = res.data
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
        likePost() {
            this.showPosts = this.likePosts

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
        },
        createRoom(){
            if(this.myProfile == null || this.myProfile == ""){
                            this.$swal({
                        position: 'top-end',

                        title: "로그인 해주세요",
                        showConfirmButton: false,
                        timer:1200,
                        backdrop: false,
                        heightAuto: false,
                        
                    });
            }else{
                var params = new URLSearchParams();
                params.append("receiverId", this.user.userId);
                httpChat.post('/api/chat/room', params,{headers:{Authorization: this.$store.state.authorization}})
                    .then(
                        response => {
                            console.log(response)
                            // this.goChat()
                            this.$store.dispatch("findAllRoom")
                            this.$store.dispatch("setPatner", this.user.userId)
                        }
                    )
                this.$emit("showChatRoomById", this.$store.getters.roomId(this.user.nickName).roomId)      
            }
        },
    },
    computed : {
        
        ...mapState(['myProfile']),
        imgURL: function() { return baseURL + "/static/image/account/" + this.user.profileImg },
        phone: function() { return this.myProfile.phone.slice(0,3) + '-' + this.myProfile.phone.slice(3,7) + '-' + this.myProfile.phone.slice(7,this.myProfile.phone.length)}
    },
    created() {
        this.getUserProfile (this.$route.params.uid)
        this.setUserProfile()
        this.getLikePosts(this.$route.params.uid)
    },
    mounted() {
        this.isMyPage()
        
    },
    watch:{
        '$route'(){
            this.getUserProfile(this.$route.params.uid)
            this.isMyPage()
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