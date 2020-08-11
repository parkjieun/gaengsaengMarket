<template>
  <v-app>
    <v-row justify="center">
      <v-dialog
        v-model="dialog"
        max-width="290"
      >
        <v-card>
          <v-card-title class="headline text-center"><div style="margin:auto;">환영합니다.</div></v-card-title>
          <v-divider></v-divider>
          <v-card-actions>
            
            <v-spacer></v-spacer>
            
              <img @click="kakaoLogin"
                src="//k.kakaocdn.net/14/dn/btqCn0WEmI3/nijroPfbpCa4at5EIsjyf0/o.jpg"
                width="75%"
                class="social"
              />
            
            <v-spacer></v-spacer>
          </v-card-actions>
          <v-card-actions>
            <v-spacer></v-spacer>
              <g-signin-button class="g-signin-button"
                :params="googleSignInParams"
                @success="onGoogleSignInSuccess"
                @error="onGoogleSignInError">

              <img class="social" src="../../assets/google.png" width="100%" height="100%"/>
              </g-signin-button>
              <v-spacer></v-spacer>
          </v-card-actions>

          <v-divider></v-divider>
          <v-card-text>
            <v-spacer></v-spacer>
            <div style="padding-top:2%; text-align:center;">
            로그인 문제 신고는? <a click="dialog2=true">신고하기</a>
            </div>
            <v-spacer></v-spacer>
          </v-card-text>
        </v-card>

      </v-dialog>
    </v-row>
    <join-form :socialId="socialId" :dialog2="dialog2" @closeForm="closeForm"/>
  </v-app>
</template>

<script>
import axios from "axios"
import '../../assets/css/button.scss'
import http from '@/util/http-common'
import JoinForm from '@/components/user/JoinForm'
import store from "@/store/index"
import {mapState} from "vuex"
export default {
  name:"LoginForm",
  props:['dialog'],
  components:{JoinForm},
  data(){
    return {
      dialog2:false,
      googleSignInParams: {
        client_id: '97688771694-ndsidcu77ad92a118jb594cvk2hpr45s.apps.googleusercontent.com'
      },
      socialId:"",
    }
  },
  methods:{
    closeForm(){
      this.dialog2=false;
      this.$emit("closeForm")
    },
    onGoogleSignInSuccess (googleUser) {
      var vue = this
      const accessToken = googleUser.getAuthResponse(true).access_token
      
      http.post("/oauth/authorization/google",{},{headers:{accessToken:accessToken}}).then(res=>{
        const data = res.data
        this.setSocialId(data.socialId)
        console.log(data)
        vue.$store.dispatch("setAuth","Bearer "+data.accessToken)
        if(data.isOlder=="true"){
          vue.closeForm()
          http.get("/api/user",{headers: { Authorization: vue.authorization }}).then(res=>{
            vue.$store.dispatch("setUserProfile",res.data)
            console.log(res)
          })
        }else{
          this.joinForm()
        }
      })
    },
    onGoogleSignInError (error) {
      console.log('OH NOES', error)
    },
    kakaoLogin(){
      var joinForm = this.joinForm
      var vue = this
      var setSocialId = this.setSocialId
      Kakao.Auth.login({
        success: function(response) {
          
          http.post("/oauth/authorization/kakao",{},{headers:{accessToken:response.access_token}}).then(res=>{
            const data = res.data
            vue.$store.dispatch("setAuth","Bearer "+data.accessToken)
            setSocialId(data.socialId)
            if(data.isOlder=="true"){
              vue.closeForm()
              http.get("/api/user",{headers: { Authorization: vue.authorization }}).then(res=>{
                vue.$store.dispatch("setUserProfile",res.data)
                console.log(res)
              })
            }else{
              joinForm()
            }
          })
        },
        fail: function(error) {
          console.log(error);
        },
      });
    },
    joinForm(){
      this.$emit("closeForm")
      this.dialog2=true
    },
    setSocialId(value){
      this.socialId=value
    }

  },
  computed:{
    ...mapState(['authorization'])
  },
  watch: {
    dialog(val) {
        val || this.$emit("closeForm")
    },
},
}
</script>

<style scoped>
.g-signin-button {
  width: 75%;
  
}
.social:hover{
  opacity: 0.6;
}

</style>