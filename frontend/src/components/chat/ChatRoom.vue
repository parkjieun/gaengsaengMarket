<template>
<v-card width="300" class="mx-auto" id="chatRoom">
    <v-toolbar color="#a6e3e9" flat dense>
        <v-toolbar-title color="#3f696e" style="font-size:0.9rem; cursor:pointer;" id="chatTitle" @click="goUserProfile(patner.userId)">
            <div>
                <v-avatar size="30">
                    <v-img :src="patnerImg" />
                </v-avatar><span class="mx-3">{{patner.nickName}}</span>
            </div>
            <v-icon small @click="closeRoom">mdi-close</v-icon>

        </v-toolbar-title>
    </v-toolbar>

    <v-list dense id="chatBody" v-if="messages.length">

        <v-list-item two-line v-for="item in messages" :key="item.messageId">

            <div v-if="item.userId != userId" class="row">
                <v-avatar size="30" style="margin-right: 5px;" class="my-auto">
                    <v-img :src="patnerImg" />
                </v-avatar>
                <v-list-item-content class="py-1">
                    <v-list-item-title class="receiveMsg my-auto caption delivery" v-if="isJsonString(item.content)">
                        <a data-tooltip-text="배송조회" @click="godeli(item.content)" v-text="deliveryLink(item.content)">

                        </a>
                    </v-list-item-title>
                    <v-list-item-title class="receiveMsg my-auto caption" v-else>{{item.content}}</v-list-item-title>

                    <v-list-item-subtitle style="width:150px; font-size:0.1rem; margin-left:10px;">{{item.createDate | processingDate}}</v-list-item-subtitle>
                </v-list-item-content>
            </div>
            <div v-else class="row justify-end">
                <v-list-item-content class="py-1" style="width:auto; max-width:180px;">
                    <v-list-item-title class="sendMsg my-auto caption delivery" v-if="isJsonString(item.content)">
                        <a data-tooltip-text="배송조회" @click="godeli(item.content)" v-text="deliveryLink(item.content)">

                        </a>
                        </v-list-item-title>
                    <v-list-item-title class="sendMsg my-auto caption" v-else>{{item.content}}</v-list-item-title>
                    <v-list-item-subtitle style="font-size:0.1rem; text-align:right; margin-right:10px">{{item.createDate | processingDate}}</v-list-item-subtitle>
                </v-list-item-content>
                <v-avatar size="30" style="margin-right: 5px;" class="my-auto">
                    <v-img :src="myProfileImg" />
                </v-avatar>
            </div>
        </v-list-item>
    </v-list>
    <v-list dense id="chatBody" v-else>
        <v-list-item style="background: #ffe6eb; font-size:0.7rem">
            <v-list-item-title style="color: #805d64;">
                채팅 기록이 없습니다. <br>
                메세지를 보내 채팅을 시작하세요 :)
            </v-list-item-title>
        </v-list-item>
    </v-list>

    <v-divider></v-divider>
    <v-row class="chat-input" style="width:300px; margin:0; height:50px;">
        <v-col cols="11">
            <input type="text" v-model="text" @keydown.enter="sendMessage" style="padding:0; width:100%; height:100%; font-size: 0.9rem;" placeholder="채팅내용을 입력해주세요" />
        </v-col>
        <v-col cols="1" style="padding: 10px 0;">
            <button @click="sendMessage" style="width:100% height: 100%;">
                <v-icon small style="color:#3f696e; margin: 0;">
                    mdi-send
                </v-icon>
            </button>
        </v-col>
    </v-row>
    <delivery-detail :deliveryModal="deliveryModal" @closeModal="closeModal" />
</v-card>
</template>

<script>
import httpChat from "@/util/http-chat"
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import DeliveryDetail from '@/components/chat/DeliveryDetail.vue'
import axios from 'axios'
import {
    mapState
} from "vuex"
const baseURL = "http://i3a504.p.ssafy.io"
var t_key = "DlCHvPTEJjHmTu3U9IlZhA"
export default {
    props: ['roomId', 'showRoom', 'messages'],
    components: {
        DeliveryDetail,
    },
    data() {
        return {
            socket: null,
            stomp: null,
            userId: '',
            room: {},
            myProfileImg: null,
            userId: '',
            content: '',
            text: "",
            scroll: $("#chatBody").prop('scrollHeight'),
            deliveryModal: false,
        }
    },
    created() {
        this.userId = this.$store.state.myProfile.userId
    },
    updated: function () {
        $("#chatBody").scrollTop($("#chatBody").prop('scrollHeight'))

    },
    watch: {
        showRoom() {
            if (this.showRoom) {
                $('#chatRoom').show();
            } else {
                $('#chatRoom').hide();
            }
        },

    },
    computed: {
        ...mapState(['myProfile', 'patner']),
        patnerImg() {
            return baseURL + "/static/image/account/" + this.patner.profileImg
        }
    },
    methods: {
        sendMessage: function () {
            if (this.text !== "") {
                this.stomp.send("/pub/chat/message/" + this.roomId, JSON.stringify({
                    userId: this.userId,
                    content: this.text
                }), {});
                this.text = '';
            }
        },
        recvMessage: function (recv) {
            this.messages.push({
                "userId": recv.userId,
                "content": recv.content,
                "createDate": recv.createDate
            })
        },
        connect(roomId) {
            this.socket = new SockJS("http://i3a504.p.ssafy.io:8001/ws-stomp");
            // this.socket = new SockJS("http://localhost:8001/ws-stomp");
            this.stomp = Stomp.over(this.socket);
            var recvMessage = this.recvMessage
            var stomp = this.stomp
            stomp.debug = () => {};
            this.stomp.connect({}, function (frame) {
                stomp.subscribe("/sub/chat/room/" + roomId, res => {
                    var recv = JSON.parse(res.body);
                    recvMessage(recv);
                });
            })
        },
        destroyed() {
            if (this.socket)
                this.socket.close()
        },
        itemHeight(item) {
            var i = document.getSelection(item)
            return i.style.height
        },
        closeRoom() {
            $('#chatRoom').hide();
            this.showRoom = false
            this.$emit('hideRoom', false)
        },
        deliveryLink(content) {
            const object = JSON.parse(content)
            return object.content
        },
        godeli(content) {
            const object = JSON.parse(content)
            axios.get(`http://info.sweettracker.co.kr/api/v1/trackingInfo?t_invoice=${object.content}&t_code=${object.code.replace("\"","").replace("\"","")}&t_key=${t_key}`)
                .then(res => {
                    this.$store.dispatch("setDelivery", res.data)
                    this.deliveryModal = true
                })
        },
        isJsonString(str) {
            try {
                var json = JSON.parse(str);
                return (typeof json === 'object');
            } catch (e) {
                return false;
            }
        },
        closeModal() {
            this.deliveryModal = false
        },
        goUserProfile(id) {
            this.$router.push({name: 'UserProfile', params: {uid: id}})
        },
    },
    filters: {
        processingDate(value) {
            const date = new Date(value)
            return date.getMonth() + "월 " + date.getDate() + "일  " + date.getHours() + "시 " + date.getMinutes() + "분";
        }
    },

}
</script>

<style scoped>
#chatTitle {
    width: 300px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

#chatBody {
    overflow-y: scroll;
    height: 300px;
}

.receiveMsg {
    background: #ffe6eb;
    padding: 5px 5px 5px 10px;
    border-radius: 20px;
    color: #805d64;
    white-space: pre-line;
    width: auto;
    max-width: 180px;
}

.sendMsg {
    float: right;
    background: #defcfc;
    padding: 5px 5px 5px 10px;
    border-radius: 20px;
    color: #517373;
    white-space: pre-line;
    width: auto;
    max-width: 180px;

}

.delivery a{
    color: rebeccapurple;
}
</style>
