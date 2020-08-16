<template>
<v-app>
    <div class="d-none d-lg-block">
  <login-form :dialog="dialog" @closeForm="dialog=false" />
    <NavBar v-if="!isChat" @openForm="openForm" />
    <ChatList/>
    <router-view></router-view>
    </div>
    <div style="width:100%;height:100%;" class="d-block d-lg-none">
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
