<template>
<v-app>
    <v-container justify="center" align="center">
      <v-row style="border-bottom:1px solid #bb99cd; padding:30px 0px 20px;height:78px; ">
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
                      <img :src="'http://i3a504.p.ssafy.io/static/image/post/'+file" style="height:400px;width:100%;" >
                       
                       <div v-show="item.type==0" style="position:absolute; top:0%; height:100%; color:white; width:100%; background:black; opacity: 0.5;" class="soldOut">
                        <div style="text-align:center;padding-top:160px;font-size:40px">판매 완료</div>
                       </div>
                  </v-carousel-item>
                </v-carousel>
            </div>
          </v-col>

          <v-col cols="7">
            <div>
              <div style="font-size:40px; line-height:1.4; font-weight:600; margin-bottom:25px; display:flex">
                {{item.title }} 
              </div>

              <div style="font-size:30px; line-height:1.4; font-weight:550; margin-bottom:25px; text-align:left ">
                {{item.price | currency}} <span style="font-size:28px; font-weight:400; margin-left:5px; ">원</span>
              </div>

              <v-divider></v-divider>

              <div style=" font-size:16px; margin:15px 0 25px 0; text-align:left  ">
                  <img style="width:16px; height:16px; margin-right:5px" :src="require(`@/assets/post/like_click.png`)"> {{item.like_cnt }}
                  <span style="margin:0 10px 0 10px;color:rgb(204,204,204);"> | </span> 
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
                
                  <span style="color:#6E47EE" v-if="item.deal_type != 1">
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
                  <v-col class="hover_big" style="border-right: 1.5px solid #f2f3f6; text-align:center" >
                    <div  @click="goPostLike" v-if="item.likeFlag==1"><img calss="txt_btn"   style="width:40px" :src="require(`@/assets/post/fullheart3.png`)"></div>
                    <div  @click="goPostLike" v-else><img calss="txt_btn" style="width:40px" :src="require(`@/assets/post/plusheart.png`)"></div>
                      <span style="color:#2d3753">찜하기</span>
                  </v-col>
                  <v-col class="hover_big" style="border-right: 1.5px solid #f2f3f6; text-align:center">
                    <div @click="createRoom"><img calss="txt_btn" style="width:40px" :src="require(`@/assets/post/chat2.png`)"></div>
                      <span style="color:#2d3753">채팅</span>
                  </v-col>
                  <v-col class="hover_big" style="text-align:center" >
                    <div  @click="openDialog"><img calss="txt_btn" style="width:40px" :src="require(`@/assets/post/credit-card.png`)"></div>
                      <span style="color:#2d3753">바로구매</span>
                  </v-col>
              </div>
            </div>
          </v-col>
        </v-row>

       <v-row>
         <v-col cols="10" >
            <v-tabs background-color="#fafafa"  :grow="true" color="#643579" >
              <v-tab ripple>
                <span  style="font-weight:600;"> 상품정보 </span>
              </v-tab>
              <v-tab ripple>
                <span  style="font-weight:600; "> 상품문의 </span>
              </v-tab>

              <v-tab-item>
                <v-card flat>
                  <v-card-text style=" text-align:left; color:#212121;font-size: 1rem!important">
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
              <div style=" border-bottom:2px solid #2d3753;font-weight:600;padding-bottom:20px; ">판매자 정보</div>
 
                <v-hover v-slot:default="{ hover }">
                  <v-card class="mx-auto"  max-width="344 " >
                    <v-img  v-if="!!item.profile_img"   style="height: 200px;position:relative;background-position:center center;background-size:cover" :src="`http://i3a504.p.ssafy.io/static/image/account/${item.profile_img}`">
                      <v-expand-transition>
                        <div
                          v-if="hover"
                          class="d-flex transition-fast-in-fast-out white darken-2 v-card--reveal display-3 white--text"
                          style="height: 100%;"
                        >
                        VIEW
                          <v-btn outline  style="position:absolute;background:#fff;color:#2d3753;border:1px solid #2d3753;font-weight:bold "  @click="goUserProfile">
                            VIEW
                        </v-btn>
                        </div>
                      </v-expand-transition>
                    </v-img>

                    <v-img v-else :src="require(`@/assets/post/noUserImg.png`)" >
                      <v-expand-transition>
                        <div 
                          v-if="hover"
                          class="d-flex transition-fast-in-fast-out white darken-2 v-card--reveal display-3 white--text"
                          style="height: 100%;"
                        >
                        
                          <v-btn outline  style="position:absolute;background:#fff;color:#2d3753;border:1px solid #2d3753;font-weight:bold "  @click="goUserProfile">
                            VIEW
                        </v-btn>
                        </div>
                      </v-expand-transition>
                    </v-img>

                    <v-card-subtitle style="font-size:17px">
                      {{item.nick_name}}
                    </v-card-subtitle>
                  </v-card>
                </v-hover>
            </v-col>
        </v-row>
       
        <v-divider></v-divider>

        <v-row v-if="myProfile != null" >  
          <v-col cols="12" style="text-align:right"  v-show="item.user_id ==  myProfile.userId" >    
            <v-btn class="ma-2" tile outlined color="#2FA599" @click="goPostUpdate" >
              <v-icon left>mdi-pencil</v-icon> 수정하기
            </v-btn>
          </v-col>
        </v-row>

      <!-- <post-dealform :dealdialog.sync="dealdialog"  @closeForm="dealdialog=false" /> -->
      <post-dealform :dealdialog.sync="dealdialog"  @closeForm="typeUpdate" />
      {{getN}}
    </v-container>
    
</v-app>
</template>

<script> 
import { mapState, mapGetters } from 'vuex';

import httpChat from "@/util/http-chat"
import http_post from "@/util/http-post"
import PostReply from '@/components/post/PostReply.vue'

import Vue from 'vue' 
import vueMoment from 'vue-moment' 
Vue.use(vueMoment)

import PostDealform from "@/components/post/PostDealform.vue"

export default {
 
  name: 'post-detail',
  computed: {
    ...mapGetters(['item']),
    ...mapState(['myProfile']),
    day(){
      let weekCnt = this.item.deal_weak;  //this.item.deal_weak 맨처음 어디서 받아와??
      let days ="";
      let week= [{ name: "월", count: 1},{ name: "화",count: 2},{name: "수", count: 4},{ name: "목", count: 8}, 
                  { name: "금",count: 16},{name: "토",count: 32},{name: "일", count: 64}]

      for(let i=6; i>=0; i--){

        if(weekCnt >= week[i].count){
            weekCnt -= week[i].count
            days +=week[i].name + " "
        }
      }
      return days.split('').reverse().join('');
    },
    getN () { 
        //console.log("******myProfile 변화: "+ this.$store.getters.getN)
        if(this.$store.getters.getN == null){
          this.item.likeFlag=0
        }else{
          //console.log("디테일에서 로그인 성공시");
          //likeflag 받아오기 & 주소 받아오기
           this.$store.dispatch('getPost', `/api/post/${this.$route.query.post_id}?user_id=${this.myProfile.userId}`);
        }
      }
  },
  created() {
    //console.log("디테일 createds");
    if(this.myProfile == null || this.myProfile == ""){
      this.$store.dispatch('getPost', `/api/post/${this.$route.query.post_id}`);
    }else{
      this.$store.dispatch('getPost', `/api/post/${this.$route.query.post_id}?user_id=${this.myProfile.userId}`);
    }
  },
  methods: {
    goDelivery(){
       this.deliverydialog = true;
    },
    goDirect(){
      this.directdialog = true;
    },
    openDialog(){
      if(this.myProfile == null || this.myProfile == ""){
        alert('로그인 해주세요');
      }else if(this.item.type == 0){
        alert('판매 완료된 상품입니다!');
      }else{
        this.dealdialog = true;
      }
    },
    goPostUpdate(){ 
      this.$router.push({ name: 'post-update', params: { post_id:  Number(this.$route.query.post_id)} } )
    },
    goUserProfile(){
      this.$router.push({name: 'UserProfile', params: { uid : this.item.user_id }} )
    },
    goPostLike(){
      //로그인 안했으면
      if(this.myProfile == null || this.myProfile == ""){
          alert('로그인 해주세요');
      }else{
        http_post.post('/api/post/doLike?user_id=' +this.myProfile.userId +'&post_id=' + this.item.post_id, {}, 
            {
                headers: {
                  Authorization: this.$store.state.authorization,
                  'Content-Type': 'application/json'
              }
            }
        )
        .then(({ data }) => {
          var msg;

          if (data === 'insert') {
              //msg = '찜하기가 등록 되었습니다.';
              this.item.likeFlag = "1"
              this.item.like_cnt += 1;
            }else{
              //msg = '찜하기가 해지 되었습니다.';
              this.item.likeFlag = "0"
              this.item.like_cnt -= 1;
            }
          //alert(msg);

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
        params.append("receiverId", this.item.user_id);
        httpChat.post('/api/chat/room', params,{headers:{Authorization: this.$store.state.authorization}})
            .then(
                response => {
                    // console.log(response)
                    this.$store.dispatch("findAllRoom")
                    this.$store.dispatch("setPatner", this.item.user_id)           
                }
            )
            this.$emit("showChatRoomById", this.$store.getters.roomId(this.item.nick_name).roomId)    
      }
    },
    typeUpdate(result) {
      if(result  == "saleCompleted"){
       // console.log("close3");
        //console.log("deal::::::::"+ result);
        this.item.type = 0;
      }
      this.dealdialog = false;
    },

  },
  data () {
    return {
        dealdialog: false,
      }
  },
  components: {
    PostReply,
    PostDealform,
  },
  filters: {
      currency (value) {
          var num = new Number(value);
          return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,")
      }
  },
  watch:{
    getN2 (value) { 
        //console.log("getN2##############"+ value)
      },
  }

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
  width:40px; height:40px; margin-right:5px;
}
.soldOut{
  position:absolute; 
  opacity: 0.8;
}

.hover_big  img {
    -webkit-transform:scale(1);
    -moz-transform:scale(1);
    -ms-transform:scale(1); 
    -o-transform:scale(1);  
    transform:scale(1);
    -webkit-transition:.3s;
    -moz-transition:.3s;
    -ms-transition:.3s;
    -o-transition:.3s;
    transition:.3s;
}
.hover_big:hover img {
    -webkit-transform:scale(1.2);
    -moz-transform:scale(1.2);
    -ms-transform:scale(1.2);   
    -o-transform:scale(1.2);
    transform:scale(1.2);
}

.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: .5;
  position: absolute;
  width: 100%;
}

</style>