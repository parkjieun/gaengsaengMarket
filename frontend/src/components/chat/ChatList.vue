<template>
<div>
    <ChatRoom id="chatRoom" ref="chatRoom" :roomId="roomId" :showRoom="showRoom" :messages="messages" @hideRoom="hideRoom" />
    <v-card id="chatList" width="200" class="mx-auto">
        <v-toolbar color="#bb99cd" flat dense >
            <v-toolbar-title color="#bb99cd" style="font-size:1rem; width:100%; display:flex; justify-content: space-between;">
                <div @click="expandChatList" style="cursor: pointer;">톡톡<v-icon class="mx-3">mdi-chat</v-icon></div> <v-icon @click="showHelpMsg" style="cursor: pointer;" title='제품페이지의 "채팅" 혹은 회원정보의 "채팅아이콘"을 클릭하면 채팅방을 개설할 수 있습니다.' id="chatHelp">mdi-help-circle-outline</v-icon>
            </v-toolbar-title>
        </v-toolbar>
        <div id="userListBox" style="height:350px;">
        <v-card-item><v-alert color="#f5edf7" dismissible close-icon="mdi-close-circle-outline" v-model="showHelp" id="helpMsg" style="font-size:10px; z-index:99;">제품페이지의 <strong>채팅</strong> 혹은 회원정보의 <strong>채팅아이콘</strong>을 클릭하면 채팅방을 개설할 수 있습니다.</v-alert></v-card-item>
    
        <v-virtual-scroll :items="chatrooms" :item-height="45"  height="350">
            
            <template v-slot="{ item }">
                <v-list-item @click="enterRoom(item.roomId)">
                    <v-list-item-avatar size="30" color="#bb99cd">
                        <v-img v-if="item.profileImg" :src="item.profileImg"></v-img>
                        <v-icon dark v-else>mdi-account</v-icon>
                    </v-list-item-avatar>

                    <v-list-item-content>
                        <v-list-item-title v-text="item.roomName" style="font-size:0.75rem"></v-list-item-title>
                    </v-list-item-content>

                    <v-list-item-icon class="my-auto">
                        <v-icon :color="'#bb99cd'">mdi-chat-processing</v-icon>
                    </v-list-item-icon>
                </v-list-item>
            </template>
            
        </v-virtual-scroll>
        </div>
        
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
            showHelp: false,
        }
    },
    props: ['chatroomId'],
    watch: {
        chatroomId: function() {
            if (this.chatroomId) {
                this.enterRoom(this.chatroomId)
            }
        },
        isAuthenticated: function() {
            if (!this.isAuthenticated) {
                $("#userListBox").hide();
            }
        }

    },
    computed: {
        imgURL: function () {
            return baseURL + "/static/image/account/" + this.myProfile.profileImg
        },
        ...mapState(['myProfile', 'chatrooms', 'isAuthenticated']),

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
            if (this.$store.state.isAuthenticated) {
                $("#userListBox").slideToggle();
                this.showChatList = !this.showChatList;
            }
            else {
                alert("로그인한 사용자만 채팅기능을 사용할 수 있습니다.")
            }
            
        },
        showHelpMsg() {
            if (this.$store.state.isAuthenticated) {
                $('#userListBox').show();
                this.showHelp = !this.showHelp
            }
            else {
                alert("로그인한 사용자만 채팅기능을 사용할 수 있습니다.")
            }
            
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

#chatRoom {
    position: fixed;
    bottom: 0;

    right: 230px;
    z-index: 99;
}
</style>
