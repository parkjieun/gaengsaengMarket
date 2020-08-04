<template>
<div>
  <v-app-bar height="25%" app flat style="color:#00263b;">
  
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

      <v-btn text small class="my-0">
        <v-icon small>mdi-chat-processing-outline</v-icon>채팅
      </v-btn>
      </div>
    </div>
 
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
        <v-row>
          <v-col align="center" style="margin:auto;padding:0px;">
          <div style="border-top:solid 1px rgba(0,0,0,0.1);border-bottom:solid 1px rgba(0,0,0,0.1);">
                <category-tabs style="width:75%;height:48px;z-index: 1;" />
          </div>
          </v-col>
        </v-row>
</div>
</template>

<script>
import { mapState,mapGetters } from 'vuex'
import LoginForm from "@/components/user/LoginForm.vue"
import CategoryTabs from "@/components/frame/CategoryTabs.vue"
export default {
  props: ['category'],
  components: {
    LoginForm,
    CategoryTabs,
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