<template>
<div>
    <ChatRoom id="chatRoom" ref="chatRoom" :roomId="roomId" :showRoom="showRoom" :messages="messages" @hideRoom="hideRoom" />
    <v-card id="chatList" width="200" class="mx-auto">
        <v-toolbar color="#a6e3e9" flat dense @click="expandChatList">
            <v-toolbar-title color="#3f696e" style="font-size:1rem">
                톡톡<v-icon class="mx-3">mdi-chat</v-icon>
            </v-toolbar-title>
        </v-toolbar>

        <v-virtual-scroll :items="chatrooms" :item-height="45" height="350" id="userListBox">
            <template v-slot="{ item }">
                <v-list-item @click="enterRoom(item.roomId)">
                    <v-list-item-avatar size="30">
                        <v-img :src="item.profileImg"></v-img>
                    </v-list-item-avatar>

                    <v-list-item-content>
                        <v-list-item-title v-text="item.roomName" style="font-size:0.75rem"></v-list-item-title>
                    </v-list-item-content>

                    <v-list-item-icon class="my-auto">
                        <v-icon :color="'#a6e3e9'">mdi-chat-processing</v-icon>
                    </v-list-item-icon>
                </v-list-item>
            </template>
        </v-virtual-scroll>
    </v-card>
</div>
</template>

<script>
import {
    mapState
} from 'vuex'
import httpChat from "@/util/http-chat"
import http from "@/util/http-common"
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import ChatRoom from "@/components/chat/ChatRoom.vue"
const baseURL = "http://i3a504.p.ssafy.io"
export default {
    components: {
        ChatRoom,
    },
    data() {
        return {
            receiverId: '',
            showChatList: false,
            roomId: '',
            showRoom: false,
            messages: [],
        }
    },
    props: ['chatroomId'],
    watch: {
        chatroomId: function() {
            if (this.chatroomId) {
                this.enterRoom(this.chatroomId)
            }
        }
    },
    computed: {
        imgURL: function () {
            return baseURL + "/static/image/account/" + this.myProfile.profileImg
        },
        ...mapState(['myProfile', 'chatrooms']),

    },
    methods: {
        enterRoom(roomId) {
            this.$refs.chatRoom.destroyed()
            this.roomId = roomId
            httpChat.get('/api/chat/room/' + roomId, {
                headers: {
                    Authorization: this.$store.state.authorization
                }
            }).then(res => {
                const joinData = res.data
                joinData.forEach(ele => {
                    if (ele.userId !== this.$store.state.myProfile.userId) {
                        http.get('/api/user/' + ele.userId).then(res2 => {
                            this.$store.dispatch("setPatner", res2.data.userId)
                        })
                    }
                })

            })
            httpChat.get('/api/chat/' + roomId, {
                headers: {
                    Authorization: this.$store.state.authorization
                }
            }).then(response => {
                this.messages = response.data;
            }).catch(res => {

            });

            this.showRoom = true
            this.$refs.chatRoom.connect(roomId)
        },
        expandChatList() {
            $("#userListBox").slideToggle();
            this.showChatList = !this.showChatList;
        },
        hideRoom() {
            this.showRoom = false
            this.$refs.chatRoom.destroyed()
            this.messages = []
            this.$emit("deleteChatroomId")
        }
    },
    created() {
        this.$store.dispatch("findAllRoom")
    },
    mounted() {
        $("#userListBox").hide();
        $("#chatRoom").hide();
    }
}
</script>

<style scoped>
#chatList {
    position: fixed;
    bottom: 0;
    right: 15px;
    z-index: 99;
}

.v-toolbar:hover {
    cursor: pointer;
}

#chatRoom {
    position: fixed;
    bottom: 0;

    right: 230px;
    z-index: 99;
}
</style>
