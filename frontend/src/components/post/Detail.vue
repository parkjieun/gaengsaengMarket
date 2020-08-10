<template>
<v-app>
    <v-container justify="center" align="center">
      <v-row style="border-bottom:1px solid; padding:30px 0px 20px;height:78px; ">
         <div style="font-size:12px; height:28px; font-weight:550 ">
           <img style="width:15px; height:15px; margin-right:5px" :src="require(`@/assets/post/home.png`)"> 홈
           <img style="width:6px; height:10px; margin:0px 10px" :src="require(`@/assets/post/next.png`)">  {{item.cate_big_name }} 
           <img style="width:6px; height:10px; margin:0px 10px" :src="require(`@/assets/post/next.png`)"> {{item.cate_mid_name }} 
          </div>
      </v-row> 
        <v-row style="padding:30px 0px 50px;">
          <v-col cols="5">
            <div v-if="item.files" style="border: 1px solid #CCCCCC;"> 
                  <v-carousel height="400px"  delimiter-icon="mdi-minus" :show-arrows="false">
                    <v-carousel-item v-for="(file,i) in item.files.split(',')" :key="i"   >
                        <img :src="'http://i3a504.p.ssafy.io/static/image/post/'+file" style="height:400px;width:100%">
                    </v-carousel-item>
                  </v-carousel>
              </div>
          </v-col>

          <v-col cols="7">
            <div>
              <div style="font-size:24px; line-height:1.4; font-weight:600; margin-bottom:25px; display:flex">
                {{item.title }} 
              </div>

              <div style="font-size:40px; line-height:1.4; font-weight:550; margin-bottom:25px; text-align:left ">
                {{item.price | currency}} <span style="font-size:28px; font-weight:400; margin-left:5px; ">원</span>
              </div>

              <v-divider></v-divider>

              <div style="color:rgb(204,204,204); font-size:16px; margin:15px 0 25px 0; text-align:left  ">
                  <img style="width:16px; height:16px; margin-right:5px" :src="require(`@/assets/post/like_click.png`)"> {{item.like_cnt }}
                  <span style="margin:0 10px 0 10px"> | </span> 
                  <img style="width:16px; height:16px; margin-right:5px" :src="require(`@/assets/post/dateTime.png`)">
                  <span v-if="item.update_date != null"> {{$moment(item.update_date).format('YYYY-MM-DD')}}</span>
                  <span v-else> {{$moment(item.create_date).format('YYYY-MM-DD')}} </span>
              </div>

              <div>
                <div style="margin-bottom:25px;display:flex; font-size:14PX; font-weight:550">
                  <span class="kcuhgh">거래유형</span>
                  
                  <span style="color:#6E47EE;" v-if="item.deal_type == 1">택배거래</span>
                  <span style="color:#6E47EE"  v-else-if="item.deal_type == 2">직거래</span>
                  <span style="color:#6E47EE" v-else>택배/직거래</span>
                
                  <span style="color:#6E47EE" v-if="item.deal_weak != 0">
                    ( {{day}} )
                  </span>
                </div>
              </div>

              <div>
                <div style="margin-bottom:25px;display:flex; font-size:14PX; font-weight:550">
                  <span class="kcuhgh">상품태그</span>
                    <span v-if="item.tags">
                      <span v-for="(tag,i) in item.tags.split(',')" :key="i"  >
                       #{{tag}}
                     </span>
                    </span>
                </div>
              </div>

              <div style="font-weight: 600px;font-size:13px; margin-right:10px;height:56px; color:#555555;   display: flex;">
                  <v-col style="border-right: 1.5px solid #f2f3f6; text-align:center"  @click="goPostLike">
                    <div v-if="item.likeFlag==1"><img calss="txt_btn" :src="require(`@/assets/post/fullheart.png`)"></div>
                    <div v-else><img calss="txt_btn" :src="require(`@/assets/post/emptyheart.png`)"></div>
                      찜하기
                  </v-col>
                  <v-col style="border-right: 1.5px solid #f2f3f6; text-align:center">
                    <div @click="createRoom"><img calss="txt_btn" :src="require(`@/assets/post/chat.png`)"></div>
                    연락하기
                  </v-col>
                  <v-col style="text-align:center"  @click="openDialog">
                    <div><img calss="txt_btn" :src="require(`@/assets/post/card.png`)"></div>
                    바로구매
                  </v-col>
              </div>
            </div>
          </v-col>
        </v-row>

       <v-row>
         <v-col cols="10" >
            <v-tabs background-color="#fafafa"  :grow="true">
              <v-tab ripple>
                <span  style="font-weight:600;"> 상품정보 </span>
              </v-tab>
              <v-tab ripple>
                <span  style="font-weight:600; "> 상품문의 </span>
              </v-tab>

              <v-tab-item>
                <v-card flat>
                  <v-card-text style=" text-align:left; color:#212121">
                    <v-row >
                      <div v-html="item.contents"></div>
                    </v-row>
                  </v-card-text>
                </v-card>
              </v-tab-item>

              <v-tab-item>
                <v-card flat>
                  <v-card-text style=" text-align:left; color:#212121">
                      <post-reply/>
                  </v-card-text>
                </v-card>
              </v-tab-item>
            </v-tabs>
           </v-col>
            <v-col cols="2" style=" text-align:center">
              <div style=" border-bottom:2px solid #cfcfcf;font-weight:600;padding-bottom:20px; ">판매자 정보</div>
                <div style="padding-top:20px">
                  <v-avatar color="#a6e3e9" size="85" @click="goUserProfile">
                    <img v-if="!!item.profile_img"  :src="`http://i3a504.p.ssafy.io/static/image/account/${item.profile_img}`" >
                    <v-icon v-else size="85" dark>mdi-account</v-icon>
                </v-avatar>
                </div>
                <div style="padding-top:10px; color:#72787f; font-weight:550" @click="goUserProfile">
                {{item.nick_name}} 
                </div>
            </v-col>
        </v-row>
       
        <v-divider></v-divider>

   
        <v-row v-if="myProfile != undefined" >  
          <v-col cols="12" style="text-align:right"  v-show="item.user_id ==  myProfile" >    
              <v-btn class="ma-2" tile outlined color="success" @click="goPostUpdate">
              <v-icon left>mdi-pencil</v-icon> 수정하기
            </v-btn>
            <!-- <v-btn class="ma-2" tile color="#FFE6EB" dark @click="ConfirmDelete(item.post_id)">삭제</v-btn> -->
          </v-col>
        </v-row>

      <!-- <v-btn color="primary" dark @click.native.stop="dialog = true">Open Dialog</v-btn> -->
      
      <v-dialog v-model="dialog" max-width="300">
      <v-card>
        <v-card-title style="height:50px;font-size:15px !important;font-weight:550;border-bottom:1px solid rgb(220, 219, 228)">거래방법 선택</v-card-title>
        <v-card-text style="padding:0px">
          <div v-if="item.deal_type == 1 || item.deal_type == 3">
            <v-divider></v-divider>
            <v-btn color="green darken-1"  style="width:100%;font-size:20px !important;height:80px;font-weight:550" text @click.native="dialog = false"  @click="goDelivery()">택배거래</v-btn>
          </div>
         
          <div v-if="item.deal_type == 2 || item.deal_type == 3">
            <v-divider></v-divider>
            <v-btn color="green darken-1" style="width:100%;font-size:20px !important;height:80px; font-weight:550" text @click.native="dialog = false" @click="goDirect()">직거래 </v-btn>
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>
    
    </v-container>
</v-app>
</template>

<script> 
import { mapGetters } from 'vuex';

import httpChat from "@/util/http-chat"
import http_post from "@/util/http-post"
import PostReply from '@/components/post/PostReply.vue'

import Vue from 'vue' 
import vueMoment from 'vue-moment' 
Vue.use(vueMoment)

export default {
 
  name: 'post-detail',
  computed: {
    ...mapGetters(['item']),
    day(){
      let weekCnt = this.item.deal_weak;
      let days ="";
      let week= [{ name: "월", count: 1},{ name: "화",count: 2},{name: "수", count: 4},{ name: "목", count: 8}, 
                  { name: "금",count: 16},{name: "토",count: 32},{name: "일", count: 64}]

      for(let i=6; i>=0; i--){

        if(weekCnt >= week[i].count){
            weekCnt -= week[i].count
            days +=week[i].name + " "
        }
      }
      //console.log(days);

      return days;
    }
  },
  
  created() {
   // this.myProfile = this.$store.state.myProfile.userId;
    this.myProfile = "bf4477387476d3ceff52ecbdeb64ab20" //삭제
    console.log(">>>>>>>"+`${this.$route.query.post_id}`);
    console.dir("this.myProfile:" + this.myProfile);
 

    if(this.myProfile != "undefined" && (this.myProfile != undefined) ){
      this.$store.dispatch('getPost', `/api/post/${this.$route.query.post_id}?user_id=${this.myProfile}`);
    }else{
      this.$store.dispatch('getPost', `/api/post/${this.$route.query.post_id}`);
    }
    //console.log("title "+ this.item.title);
  },
  methods: {
    goDelivery(){
      alert("택배거래");
      //this.dialog = false;
    },
    goDirect(){
      alert("직거래");
      //this.dialog = false;
    },
    openDialog(){
      if(this.myProfile == undefined){
        alert('로그인 해주세요');
      }else{
        this.dialog = true;
        //새창 
        //let routeData = this.$router.resolve({name: 'PostBuy', params: { uid : this.item.user_id }});
        //window.open(routeData.href, '_blank');
      }
    },
    ConfirmDelete(post_id)
    {
      var x = confirm("해당 글을 삭제 하시겠습니까?");
      if (x)
        return this.delete(post_id);
      else 
        return false;
    },   
		delete(post_id) {
			//this.deletePost()
       //this.$router.push({name: this.constants.URL_TYPE.POST.MAIN})
       this.$store.dispatch('deletePost', `/api/post/`+post_id, );
       this.$router.push('/post/list');
    },
    goPostUpdate(){ 
      this.$router.push({ name: 'post-update', params: { post_id:  Number(this.$route.query.post_id)} } )
    },
    goUserProfile(){
      //console.dir(">>>>>>>>>>>goUserProfile: "+this.item.user_id)
      this.$router.push({name: 'UserProfile', params: { uid : this.item.user_id }} )
    },
    goPostLike(){
      //로그인 안했으면
      if(this.myProfile == undefined){
          alert('로그인 해주세요');
      }else{
        console.dir("goPostLike post_id>>>>>>>>>>"+this.myProfile+"/"+this.item.post_id);
       
        http_post.post('/api/post/doLike?user_id=' +this.myProfile +'&post_id=' + this.item.post_id, {}, 
            {
                headers: {
                  Authorization: this.$store.state.authorization,
                  'Content-Type': 'application/json'
              }
            }
        )
        .then(({ data }) => {
          console.log("data:"+data);
          var msg;

          if (data === 'insert') {
              msg = '찜하기가 등록 되었습니다.';
              this.item.likeFlag = "1"
              this.item.like_cnt += 1;
            }else{
              msg = '찜하기가 해지 되었습니다.';
              this.item.likeFlag = "0"
              this.item.like_cnt -= 1;
            }
          alert(msg);

        })
        .catch(() => {
          alert('에러가 발생했습니다?');
        })
      }
    },
            goChat() {

            let routeData = this.$router.resolve('/chat');
            window.open(routeData.href,  "a", "width=400, height=600, left=100, top=50");

        },
                createRoom(){

            var params = new URLSearchParams();
            params.append("receiverId", this.item.user_id);
            httpChat.post('/api/chat/room', params,{headers:{Authorization: this.$store.state.authorization}})
                .then(
                    response => {
                        this.goChat()
                    }
                )

        },
  },
 data () {
   return {
      myProfile : null,
      dialog: false

    }
 },
  components: {
    PostReply,
  },
  filters: {
      currency (value) {
          var num = new Number(value);
          return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,")
      }
  },


};

</script>

<style scoped>
.kcuhgh::before {
    content: "";
    position: absolute;
    top: 7px;
    left: 6px;
    width: 3px;
    height: 3px;
    border-radius: 50%;
    background: rgb(204, 204, 204);
}
.kcuhgh{
position: relative;
    width: 90px;
    padding-left: 15px;
    color: rgb(153, 153, 153);
    text-align:left
}
.txt_btn{
  width:30px; height:30px; margin-right:5px;
}
/* .v-application--wrap{
  min-height: 10vh !important;
}  */
</style>