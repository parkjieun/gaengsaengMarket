<template>
    <v-container justify="center" align="center">
      <v-row style="border-bottom:1px solid; padding:30px 0px 20px;height:78px; ">
         <div style="font-size:12px; height:28px; ">
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
                        <img :src="'http://i3a504.p.ssafy.io:8000/api/static/image/'+file" style="height:400px;width:100%">
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
                  <span v-if="item.update_date != null"> {{item.update_date }}</span>
                  <span v-else> {{item.create_date }}</span>
              </div>

              <div>
                <div style="margin-bottom:25px;display:flex; font-size:14PX; font-weight:550">
                  <span class="kcuhgh">거래유형</span>
                  
                  <span style="color:#6E47EE;" v-if="item.deal_type == 1">택배거래</span>
                  <span style="color:#6E47EE"  v-else-if="item.deal_type == 2">직거래</span>
                  <span style="color:#6E47EE" v-else>택배/직거래</span>
                
                  <span style="color:#6E47EE" v-if="item.deal_weak != 0">
                    (
                      <span v-for="(day,i) in week" :key="i"  v-show="item.deal_weak/day.count == 1">
                        <span>{{day.name}} </span>
                      </span>
                    )
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
                  <v-col style="border-right: 1.5px solid #f2f3f6; text-align:center">
                    <div><img calss="txt_btn" :src="require(`@/assets/post/heart.png`)"></div>
                      찜하기
                  </v-col>
                  <v-col style="border-right: 1.5px solid #f2f3f6; text-align:center">
                    <div><img calss="txt_btn" :src="require(`@/assets/post/chat.png`)"></div>
                    연락하기
                  </v-col>
                  <v-col style="text-align:center">
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
                  <v-card-text style=" text-align:left; color:#212121">서비스 준비 중 입니다. </v-card-text>
                </v-card>
              </v-tab-item>
            </v-tabs>
           </v-col>
            <v-col cols="2" style=" text-align:center">
              <div style=" border-bottom:2px solid #cfcfcf;font-weight:600;padding-bottom:20px; ">판매자 정보</div>
                <div style="padding-top:20px">
                  <v-avatar color="#a6e3e9" size="85" @click="goUserProfile(item.user_id)">
                  <!-- <img :src="'http://i3a504.p.ssafy.io:8003'+item.profile_img" > -->
                    <img src="http://i3a504.p.ssafy.io:8000/api/static/image/1596138378575_분홍.PNG" >
                </v-avatar>
                </div>
                <div style="padding-top:10px; color:#72787f" @click="goUserProfile(item.user_id)">
                {{item.user_id}} 
                 <!-- {{item.nick_name}} -->
                </div>
            </v-col>
        </v-row>
       
        <v-divider></v-divider>

        <!-- v-show="item.user_id ==  myProfile.userId" -->
        <v-row v-if="myProfile != null" >  
          <v-col cols="12" style="text-align:right">   
              <v-btn class="ma-2" tile outlined color="success" @click="goPostUpdate">
              <v-icon left>mdi-pencil</v-icon> 수정하기 {{myProfile.value}}
            </v-btn>
            <!-- <v-btn class="ma-2" tile color="#FFE6EB" dark @click="ConfirmDelete(item.post_id)">삭제</v-btn> -->
          </v-col>
        </v-row>

    </v-container>
</template>

<script> 
import { mapGetters } from 'vuex';


export default {
 
  name: 'post-detail',
  computed: {
    ...mapGetters(['item']),
    //totoal(){ return this.$store.state.count;}
  },
  
  created() {
    console.log(">>>>>>>"+`${this.$route.query.post_id}`);
    this.$store.dispatch('getPost', `/api/post/${this.$route.query.post_id}`);
    this.myProfile = this.$store.state.myProfile;
    console.dir(this.myProfile);
  },

  methods: {
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
       this.$store.dispatch('deletePost', `/api/post/`+post_id);
       this.$router.push('/post/list');
    },
    goPostUpdate(){ 
      this.$router.push({ name: 'post-update', params: { post_id:  Number(this.$route.query.post_id)} } )
    },
    goUserProfile(user_id){
      console.dir(">>>>>>>>>>>goUserProfile: "+user_id)
      this.$router.push({name: 'UserProfile', params: { uid : user_id }} )
    },

  },
 data () {
   return {
      myProfile : null,
      week: [
          {
              name: "일",
              count: 1
          },
          {
              name: "월",
              count: 10
          },
          {
              name: "화",
              count: 100
          },
          {
              name: "수",
              count: 1000
          },
          {
              name: "목",
              count: 10000
          },
          {
              name: "금",
              count: 100000
          },
          {
              name: "토",
              count: 1000000
          }
      ],
    }
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
</style>