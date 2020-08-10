<template>
  <v-container ustify="center" align="center">
    <v-row>
      <!-- <v-col cols="12" md="12">  -->
        <v-textarea v-model="message"
          filled
          auto-grow
          name="message"
          ref="message"
          label="상품문의 내용을 입력하세요."
          value=""
          rows="2"
          row-height="30"
          :rules="[rules.length(10)]"
          counter="10"
           required
        ></v-textarea>
        
        <v-btn class="ma-2" outlined fab color="indigo" @click="submitReply">
          <v-icon>mdi-pencil</v-icon>
        </v-btn>
        
       <!-- </v-col> -->
    </v-row>
    <v-row>
      <v-col cols="12" md="12" v-for="(reply, i) in replys" :key="i" style="border-bottom: 1px solid #ededed" >
        <v-row>
          <v-col cols="1" >
            <v-avatar color="#a6e3e9" size="48" @click="goUserProfile(reply.user_id)">
              <img v-if="!!reply.profile_img" :src="`http://i3a504.p.ssafy.io/static/image/account/${reply.profile_img}`" alt="Profile-image">
              <v-icon v-else size="48" dark>mdi-account</v-icon>
            </v-avatar>
          </v-col>
          <v-col cols="11">
            <div style="font-weight:550;color:#0f5272">
              {{reply.nick_name}} <span style="float:right;color:#000;font-weight:400">{{$moment(reply.create_date).format('YY.MM.DD')}}</span>
            </div>
            <div>
              {{reply.contents}}  
              <v-btn text small v-show="reply.user_id == userId " @click="ReplyConfirmDelete(reply.reply_id)"  style="float:right;color:#888888;font-weight:550;">
                <img style="margin-right:4px;width:15px" :src="require(`@/assets/post/del.png`)">삭제
              </v-btn>
            </div>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>


<script> 
import { mapGetters } from 'vuex';
import http_post from "@/util/http-post"

import Vue from 'vue' 
import vueMoment from 'vue-moment' 
Vue.use(vueMoment)

export default {
  data () {
   return {
      message : "",
      userId : null,
      postId: `${this.$route.query.post_id}`,
      rules: {   
        length: len => v => (v || '').length <= len || `${len}자 이내로 작성해주세요.`,
      },
    }
  },
  created() {
    //this.userId= this.$store.state.myProfile.userId 
    this.userId= "bf4477387476d3ceff52ecbdeb64ab20" //삭제
    this.$store.dispatch('getReplys', `/api/post/reply?post_id=${this.$route.query.post_id}`)
  },
  computed: {
   ...mapGetters(['replys']),
  },
  methods:{
    submitReply(){
      //alert();
      if(this.userId == "undefined" || this.userId == undefined){
        alert("로그인해주세요");
      }else{
        if(this.message.length == 0 || this.message.length > 10){
            this.$refs.message.focus();
        }else{

          http_post.post('/api/post/reply',{contents : this.message, user_id:this.userId,post_id:this.postId }, 
              {
                  headers: {
                    //Authorization: this.$store.state.authorization, 
                    //삭제
                    Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI0NzFhZGJhOTQxNjhjNjU3OWRkNjdiNjJlN2ZiZTlkOSJ9.e9LyXH2t0SZGhKhhFSYroyHXup6A_7mi3SNI0ew6b28", 
                }
              }
          )
          .then(({ data }) => {
            console.log("data:"+data.create_date);
            if(data != null){
              this.message = "";
              //표에 추가, 이미지, 닉네임, 내용, 생성일, user_id, reply_id
                this.replys.unshift({
                  reply_id: data.reply_id,
                  nick_name: data.nick_name,
                  profile_img: data.profile_img,
                  contents: data.contents,
                  user_id: data.user_id,
                  create_date:data.create_date,
                });
            }
          })
          .catch(() => {
            alert('에러가 발생했습니다?');
          })

        }//else
      }
    },
    goUserProfile(user_id){
      this.$router.push({name: 'UserProfile', params: { uid : user_id }} )
    },
    ReplyConfirmDelete(reply_id)
    {
      var x = confirm("삭제 하시겠습니까?");
      if (x)
        return this.goReplyDel(reply_id);
      else 
        return false;
    },
    goReplyDel(reply_id){

      console.log(">> "+reply_id);

      http_post.delete('/api/post/reply?reply_id='+reply_id, 
            {
                headers: {
                  //Authorization: this.$store.state.authorization,
                   //삭제
                  Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI0NzFhZGJhOTQxNjhjNjU3OWRkNjdiNjJlN2ZiZTlkOSJ9.e9LyXH2t0SZGhKhhFSYroyHXup6A_7mi3SNI0ew6b28", 
              }
            }
      )
      .then(({ data }) => {
          console.log("data:"+data);
            if(data == "success"){
              this.message = "";
              //console.log(this.replys);
              //console.dir(this.replys.findIndex(i => i.reply_id == reply_id));
              this.replys.splice(this.replys.findIndex(i => i.reply_id == reply_id),1);
            }

      })
      .catch(() => {
          alert('에러가 발생했습니다?');
      })
    }, //goReplyDel
  },

}

</script>

<style scoped>
</style>