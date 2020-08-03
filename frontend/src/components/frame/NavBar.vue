<template>
<div>
  <v-app-bar height="25%" app flat style="color:#00263b;">
  
    <!-- <div style="width:35%" class="text-left"><v-app-bar-nav-icon class="my-0" color="#00263b" @click="openCategory"></v-app-bar-nav-icon></div> -->
    <!-- <v-toolbar-title><router-link to="/"><span><v-icon>mdi-cart</v-icon></span>Page title </router-link></v-toolbar-title> -->
    <div v-if="!loggedIn" style="width:35%;position:absolute;right:2%;" class="text-right d-none d-sm-block">
      <v-btn text small class="my-0" @click="openForm">
        <v-icon small>mdi-account</v-icon>로그인
      </v-btn>
    </div>

    <div v-else style="width:100%;position:absolute;right:2%;" class="text-right d-none d-sm-block" >
      <v-btn text small class="my-0" @click="goMyProfile">
        <v-icon small>mdi-account</v-icon>내 정보
      </v-btn>
      <v-btn text small class="my-0" @click="goPostCreate">
        <v-icon small class="nav-icon">mdi-cart</v-icon>판매하기
      </v-btn>
      <!-- <v-btn text small class="my-0">
        <v-icon small>mdi-chat-processing-outline</v-icon>채팅
      </v-btn> -->
    </div>
    <div style="width:100%;position:absolute;right:2%;" class="text-right d-block d-sm-none" >
      <div v-if="!myProfile">
        <v-btn text small class="my-0">
        <v-icon small>mdi-account</v-icon>로그인
      </v-btn>
      </div>
      <div v-else>
      <v-btn text small class="my-0" @click="goMyProfile">
        <v-icon small>mdi-account</v-icon>내 정보
      </v-btn>
      <!-- <v-btn text small class="my-0">
        <v-icon small class="nav-icon">mdi-cart</v-icon>판매하기
      </v-btn> -->
      <v-btn text small class="my-0">
        <v-icon small>mdi-chat-processing-outline</v-icon>채팅
      </v-btn>
      </div>
    </div>
    <!-- <div class="text-right d-block d-sm-none" style="width:100%;position:absolute;right:2%;">
      <v-menu
      v-model="menu"
      :close-on-content-click="false"
      :max-width="150"
      offset-x
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn icon v-bind="attrs" v-on="on" class="my-0">
          <v-icon color="#00263b">mdi-dots-vertical</v-icon>
        </v-btn>
      </template>
      
      <v-card color="#cbf1f5">
        <v-list dense v-if="isAuthenticated" color="#defcfc">
          <v-list-item @click="dialog=true">
            <v-list-item-icon>
              <v-icon>mdi-account</v-icon>
            </v-list-item-icon>
            <v-list-item-title >로그인/회원가입</v-list-item-title>
          </v-list-item>
          
        </v-list>
        
        <v-list dense v-else color="#defcfc">
          <v-list-item @click="goMyProfile">
            <v-list-item-icon>
              <v-icon>mdi-account</v-icon>
            </v-list-item-icon>
            <v-list-item-title >내 정보</v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-icon @click="goPostCreate">
              <v-icon>mdi-cart</v-icon>
            </v-list-item-icon>
            <v-list-item-title>판매하기</v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-chat-processing-outline</v-icon>
            </v-list-item-icon>
            <v-list-item-title>채팅</v-list-item-title>
          </v-list-item>
        </v-list>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="menu = false" small>Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-menu>
    </div> -->

  </v-app-bar>
  <v-row id="nav">
            <v-col align="center">
                <div style="">
                    <router-link to="/"><v-img width="250px" src="@/assets/logo.png"></v-img></router-link>
                </div>
            </v-col>
            <v-col cols="5">
                <v-flex>
                    <v-combobox multiple v-model="select" color="#a6e3e9" @keydown.enter="searchItem"  chips deletable-chips class="tag-input" append-icon="mdi-magnify" placeholder="상품 및 #해시태그를 입력해 주세요" :search-input.sync="search"  @keyup.space="updateTags" >
                    </v-combobox>
                </v-flex>

            </v-col>
            <v-col align="center">
                <v-btn text @click="goMyProfile">
                    <v-icon outlined>mdi-account-outline</v-icon>내 정보
                </v-btn>
                <v-btn text @click="goPostCreate">
                    <img src="@/assets/gifs/shopping.gif" width="20px">갱생시키기
                </v-btn>
                <v-btn text>
                    <img src="@/assets/gifs/chat-message.gif" width="20px">
                    톡톡
                </v-btn>
            </v-col>
        </v-row>
</div>
</template>

<script>
import { mapState,mapGetters } from 'vuex'
import LoginForm from "@/components/user/LoginForm.vue"

export default {
  props: ['category'],
  components: {
    LoginForm,
  },
  computed: {
      ...mapState(['authorization','myProfile']),
      ...mapGetters(['loggedIn'])
  },
  data() {
    return{
      menu: false,
      dialog: false,
      search:"",
      select:[],
    }
  },
  methods: {
    openCategory() {
      this.category = !this.category
      this.$emit("openCategory", this.category)
    },
    openForm(){
      this.$emit("openForm")
    },
    goPostCreate() {
      this.$router.push({name:'post-create'})
    },
    goMyProfile() {
      this.$router.push({ name: 'MyProfile' })
    },
    searchItem() {
            alert("검색 미구현!")
            this.search=""
            this.select=[]
            console.log(this.search)
        },
        updateTags() {
            this.$nextTick(() => {
                const i = this.search.indexOf("#")
                console.log(i)
                if(i!==-1){
                    const hashtag = this.search.slice(-(this.search.length-i)+1)
                    if(hashtag.length>1){
                        this.select.push(hashtag)
                    }
                }
                // this.select.push(...this.search.split(","));
                
                this.$nextTick(() => {
                    if(i!==-1)
                        this.search = this.search.slice(0,i);
                });
            });
        },
  },
    
}
</script>

<style scoped>
#nav {
  margin-top: 50px;
}
a {
  text-decoration: none;
}
.v-application a {
  color: #00263b;
}
i.v-icon {
  color: #00263b;
}
</style>