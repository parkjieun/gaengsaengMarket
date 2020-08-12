<template>
<v-app>
  <login-form :dialog="dialog" @closeForm="dialog=false" />
    <NavBar v-if="!isChat" @openForm="openForm" />
    <router-view></router-view>
</v-app>
</template>

<script>
import NavBar from '@/components/frame/NavBar.vue'
import CategoryMenu from '@/components/frame/CategoryMenu.vue'
import LoginForm from "@/components/user/LoginForm.vue"
export default {
    components: {
        NavBar,
        CategoryMenu,
        LoginForm
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
            return url.includes("chat") || url.includes("point")
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
