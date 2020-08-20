<template>
    <div id="userProfile">
        <h3 style="margin-top: 30px; color:#3d1860;">회원정보</h3>
        <v-divider class="my-5" color="#bb99cd"></v-divider>
        <!-- 내정보 -->
        <v-row style="position:relative;" v-if="myPage">
        
        <!-- profile image -->
            <div>
                <v-img v-if="!!user.profileImg" :src="imgURL" alt="Profile-image" :aspect-ratio="1" max-width="200" min-width="200" style="margin-left:15px;"> </v-img>
                <!-- <v-img v-else :src="require(`@/assets/post/noUserImg.png`)" ></v-img> -->
                <div v-else style="width: 200px; height: 200px; display:flex; align-items:center; justify-content:center;">
                    <v-avatar color="#bb99cd" size="150"><v-icon dark size="120">mdi-account</v-icon></v-avatar>
                </div>
            </div>
        <!-- User Info -->
            
            <div id="username" style="margin: 30px; max-width:400px; width:400px;">
                <div class="d-flex align-center">
                    <div style="font-weight: bolder;">{{myProfile.nickName}}</div>
                    <div class="px-3" v-if="this.phone" style="font-size:0.8rem; color:gray;">{{phone}}</div>
                </div>
                <div class="px-0 body-2 py-2" height="100" style="white-space: pre-wrap;">{{myProfile.introduce}}</div>
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
                 <div v-else style="width: 200px; height: 200px; display:flex; align-items:center; justify-content:center;">
                    <v-avatar color="#bb99cd" size="150"><v-icon dark size="120">mdi-account</v-icon></v-avatar>
                </div>
            </div>
        <!-- User Info -->
            
            <div id="username" style="margin: 30px; max-width:400px; width:400px;">
                <div class="d-flex align-center">
                    <div style="font-weight: bolder;">{{user.nickName}}</div>
                </div>
                <div class="px-0 body-2 py-2" height="100" style="white-space: pre-wrap;">{{user.introduce}}</div>
            </div>
            
            
            <div style="margin: 30px; position:absolute; top: 0; right: 0; cursor:pointer;" @click="createRoom">
                <v-icon x-large style="margin-left: 5px;" color="#bb99cd">mdi-chat-processing</v-icon>
            </div>
            
        </v-row>
        
        <!-- menu -->
        <v-divider style="margin-top:30px;"></v-divider>
        <v-tabs grow color="#3d1860">
            <v-tab>총 판매</v-tab>
            <v-tab>판매 중</v-tab>
            <v-tab>판매 완료</v-tab>
            <v-tab v-if="myPage">찜한 글</v-tab>
            
            <v-tab-item>
                <v-divider></v-divider>
                <PostList :posts="allPosts" id="postList"/>
                <infinite-loading @infinite="allInfiniteHandler" spinner="waveDots" ref="InfiniteLoading"></infinite-loading>
            </v-tab-item>
            <v-tab-item>
                <v-divider></v-divider>
                <PostList :posts="onSalePosts" id="postList"/>
                <infinite-loading @infinite="saleInfiniteHandler" spinner="waveDots" ref="InfiniteLoading"></infinite-loading>
            </v-tab-item>
            <v-tab-item>
                <v-divider></v-divider>
                <PostList :posts="soldOutPosts" id="postList"/>
                <infinite-loading @infinite="soldoutInfiniteHandler" spinner="waveDots" ref="InfiniteLoading"></infinite-loading>
            </v-tab-item>
            <v-tab-item>
                <v-divider></v-divider>
                <PostList :posts="likePosts" id="postList"/>
                <infinite-loading @infinite="likeInfiniteHandler" spinner="waveDots" ref="InfiniteLoading"></infinite-loading>
            </v-tab-item>
        </v-tabs>
        
    </div>
</template>

<script>
import PostList from '@/components/post/PostList.vue'
import httpUser from '@/util/http-common'
import httpPost from '@/util/http-post'
import httpChat from "@/util/http-chat"
import InfiniteLoading from 'vue-infinite-loading'


import axios from 'axios'
import { mapState, mapActions,mapGetters } from 'vuex'
const baseURL = "http://i3a504.p.ssafy.io"
export default {
    data() {
        return {
            allPosts: [],
            onSalePosts: [],
            soldOutPosts: [],
            likePosts: [],
            user: null,
            myPage: '',
            allStart: 0,
            onsaleStart: 0,
            soldoutStart: 0,
            likeStart: 0,
        }
    },
    components: {
        PostList,
        InfiniteLoading
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
            })
        },
        allInfiniteHandler($state) {
            httpPost.get(`/api/post?sno=${this.allStart}&user_id=${this.$route.params.uid}`)
            .then(res => {
                if (res.data.length) {
                    this.allStart += 12;
                    this.allPosts.push(...res.data)
                    $state.loaded();
                } 
                else {
                    $state.complete();
                }
            })
        },
        saleInfiniteHandler($state) {
            httpPost.get(`/api/post?sno=${this.onsaleStart}&user_id=${this.$route.params.uid}&type=1`)
            .then(res => {
                if (res.data.length) {
                    this.onsaleStart += 12;
                    this.onSalePosts.push(...res.data)
                    $state.loaded();
                } 
                else {
                    $state.complete();
                }
            })
        },
        soldoutInfiniteHandler($state) {
            httpPost.get(`/api/post?sno=${this.soldoutStart}&user_id=${this.$route.params.uid}&type=0`)
            .then(res => {
                if (res.data.length) {
                    console.log(res.data)
                    this.soldoutStart += 12;
                    this.soldOutPosts.push(...res.data)
                    $state.loaded();
                } 
                else {
                    $state.complete();
                }
            })
        },
        likeInfiniteHandler($state) {
            httpPost.get(`/api/post?sno=${this.likeStart}&like=true&user_id=`+this.$route.params.uid)
            .then((res) => {
                if (res.data.length) {
                    this.likeStart += 12;
                    this.likePosts.push(...res.data) 
                    $state.loaded();
                } 
                else {
                    $state.complete();
                }
            })
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
    },
    mounted() {
        this.isMyPage()
        
    },
    watch:{
        '$route'(){
            this.getUserProfile(this.$route.params.uid)
            this.isMyPage()
            this.allPosts = []
            this.onSalePosts = []
            this.soldOutPosts = []
            this.likePosts = []
            this.allStart = 0
            this.onsaleStart = 0
            this.soldoutStart = 0
            this.likeStart = 0
            this.$refs.InfiniteLoading.stateChanger.reset();
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
    width: 80%;
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