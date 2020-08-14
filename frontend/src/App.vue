<template>
<v-app>
  <login-form :dialog="dialog" @closeForm="dialog=false" />
    <NavBar v-if="!isChat" @openForm="openForm" />
    <ChatList/>
    <router-view></router-view>
</v-app>
</template>

<script>
import NavBar from '@/components/frame/NavBar.vue'
import CategoryMenu from '@/components/frame/CategoryMenu.vue'
import LoginForm from "@/components/user/LoginForm.vue"
import ChatList from '@/components/chat/ChatList.vue'
export default {
    components: {
        NavBar,
        CategoryMenu,
        LoginForm,
        ChatList,
    },

    data() {
        return {
            dialog: false,
            category: false

        }
    },
    created() {
        console.log("qweqwe")
    },
    computed: {
        isChat() {
            var url = window.location.href
            console.log(url)
            return url.includes("chat") || url.includes("point") || url.includes("delivery")
        }
    },
    methods: {
        openCategory(value) {
            console.log(value)
            this.category = value
        },
        openForm() {
            this.dialog = true;
        },
        closeForm() {
            this.dialog = false;
        }
    },

}
</script>

<style scoped>
textarea {
    resize: none;
}
</style>
