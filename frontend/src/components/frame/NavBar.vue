<template>
<div>

    <ChatNavigation :chatFlag="chatFlag" @flag="getChatFlag"/>
    <v-app-bar height="25%" app flat style="color:#00263b;">
        <div v-if="!loggedIn" style="width:35%;position:absolute;right:2%;" class="text-right d-none d-sm-block">
            <v-btn text small class="my-0" @click="openForm">
                <v-icon small>mdi-account</v-icon>로그인
            </v-btn>
        </div>

        <div v-else style="width:100%;position:absolute;right:2%;" class="text-right d-none d-sm-block">
            <v-btn text small class="my-0" @click="goPostCreate">
                <v-icon small class="nav-icon">mdi-cart</v-icon>판매하기
            </v-btn>
            <v-btn text small class="my-0" @click="goMyProfile">
                <v-icon small>mdi-account</v-icon>내 정보
            </v-btn>

            <v-btn text small class="my-0" @click="logout">
                <v-icon small class="nav-icon">mdi-account-off</v-icon>로그아웃
            </v-btn>

        </div>
        <div style="width:100%;position:absolute;right:2%;" class="text-right d-block d-sm-none">
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

    <v-row id="nav" align="center">
        <v-col align="center">
            <div style="">
                <router-link to="/">
                    <v-img width="200px" src="@/assets/logo3.png"></v-img>
                </router-link>
            </div>
        </v-col>
        <!-- search -->
        <v-col cols="5">
            <v-flex>
                <v-combobox multiple v-model="select" color="#643579" @click:append="searchItem" chips deletable-chips class="tag-input" append-icon="mdi-magnify" placeholder="제목 및 #해시태그를 검색해주세요" :search-input.sync="search" @keyup.space="updateTags" @keydown.enter="searchItem">
                </v-combobox>
            </v-flex>
        </v-col>
        <v-col>
            <v-btn text @click="goMyProfile" style="margin-left:20px;">
                <v-icon outlined>mdi-account-outline</v-icon>내 정보
            </v-btn>
            <v-btn text @click="goPostCreate">
                <img src="@/assets/gifs/shopping.gif" width="20px">갱생시키기
            </v-btn>
            <!-- <v-btn text @click="goChat">
                <img src="@/assets/gifs/chat-message.gif" width="20px">
                톡톡
            </v-btn> -->
        </v-col>
    </v-row>
    <v-row>
        <v-col align="center" style="margin:auto;padding:0px;">
            <div style="border-top:solid 1px rgba(0,0,0,0.1);border-bottom:solid 1px rgba(0,0,0,0.1);">
                <category-tabs style="width:80%;height:48px;z-index: 1;" />
            </div>
        </v-col>
    </v-row>

</div>
</template>

<script>
import {
    mapState,
    mapGetters
} from 'vuex'
import LoginForm from "@/components/user/LoginForm.vue"
import CategoryTabs from "@/components/frame/CategoryTabs.vue"
import ChatNavigation from '@/components/chat/ChatNavigation.vue'

export default {
    name: "NavBar",
    components: {
        LoginForm,
        CategoryTabs,
        ChatNavigation,
    },
    computed: {
        ...mapState(['authorization', 'myProfile', "isAuthenticated"]),
        ...mapGetters(['loggedIn'])
    },
    data() {
        return {
            menu: false,
            dialog: false,
            search: "",
            select: [],
            chatFlag: false,
        }
    },
    created() {
    },
    methods: {
        openForm() {
            this.$emit("openForm")
            // this.dialog=true
        },

        goPostCreate() {
            if (this.isAuthenticated) {
                this.$router.push({
                    name: 'post-create'
                })
            } else {
                alert("회원만 판매글을 등록할 수 있습니다. 로그인을 해주세요.")
            }

        },
        goMyProfile() {
            if (this.isAuthenticated) {
                this.$router.push({
                    name: 'UserProfile',
                    params: {
                        uid: this.$store.state.myProfile.userId
                    }
                })
            } else {
                alert("로그인을 해주세요")
            }

        },
        searchItem() {
            const title = this.search
            const tags = this.select.join(',')
            const keyword = `tags=${tags}&title=${title}`
            this.search = ""
            this.select = []
            this.$router.push({ name: 'SearchPage', params: { tags: tags, title: title, keyword: keyword} })
            
        },
        updateTags() {
            this.$nextTick(() => {
                const i = this.search.indexOf("#")
                console.log(i)
                if (i !== -1) {
                    const hashtag = this.search.slice(-(this.search.length - i) + 1)
                    if (hashtag.length > 1) {
                        this.select.push(hashtag)
                    }
                }
                // this.select.push(...this.search.split(","));
                this.$nextTick(() => {
                    if (i !== -1)
                        this.search = this.search.slice(0, i);
                });
            });
        },
        goChat() {
            if (this.isAuthenticated) {
                this.chatFlag = !this.chatFlag
                // let routeData = this.$router.resolve('/chat');
                // window.open(routeData.href, "a", "width=400, height=600, left=100, top=50");
            } else {
                alert("로그인을 해주세요")
            }
            

        },
        logout() {
            this.$store.dispatch("logout")
        },
        getChatFlag(i) {
            this.chatFlag = i
        }
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
