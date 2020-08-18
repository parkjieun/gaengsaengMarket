<template>
<v-app>
    <div :class="computeClass">
  <login-form :dialog="dialog" @closeForm="dialog=false" />
    <NavBar v-if="!isChat" @openForm="openForm" />
    <ChatList v-if="!isChat" :chatroomId="chatroomId" @deleteChatroomId="deleteChatroomId"/>
    <router-view @showChatRoomById="showChatRoomById"></router-view>
    </div>
    <div v-if="!isChat" style="width:100%;height:100%;" class="d-block d-md-none">
        <small-page/>
    </div>
</v-app>
</template>

<script>
import NavBar from '@/components/frame/NavBar.vue'
import CategoryMenu from '@/components/frame/CategoryMenu.vue'
import LoginForm from "@/components/user/LoginForm.vue"
import ChatList from '@/components/chat/ChatList.vue'
import SmallPage from '@/components/page/SmallPage.vue'
export default {
    components: {
        NavBar,
        CategoryMenu,
        LoginForm,
        ChatList,
        SmallPage,
    },

    data() {
        return {
            dialog: false,
            category: false,
            chatroomId: '',

        }
    },
    created() {

    },
    computed: {
        isChat() {
            var url = window.location.href
            // console.log(url)
            return url.includes("point")
        },
        computeClass(){
            var url = window.location.href
            if(url.includes("point")){
                return []
            }
            else return ['d-none','d-md-block']
        }
    },
    methods: {
        openCategory(value) {
            // console.log(value)
            this.category = value
        },
        openForm() {
            this.dialog = true;
        },
        closeForm() {
            this.dialog = false;
        },
        showChatRoomById(roomId) {
            this.chatroomId = roomId
        },
        deleteChatroomId() {
            this.chatroomId = ''
        }
    },

}
</script>

<style scoped>
textarea {
    resize: none;
}
</style>
