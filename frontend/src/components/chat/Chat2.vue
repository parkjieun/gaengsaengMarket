<template>
    <div class="container" id="app" v-cloak>
        <div>
            <h2>{{room.name}}</h2>
        </div>
        <div class="input-group">
            <div class="input-group-prepend">
                <label class="input-group-text">내용</label>
            </div>
            <input type="text" class="form-control" v-model="text" v-on:keypress.enter="sendMessage">
            <div class="input-group-append">
                <button class="btn btn-primary" type="button" @click="sendMessage">보내기</button>
            </div>
        </div>
        <ul class="list-group">
            <li class="list-group-item" v-for="message in contents" :key="message.message_id">
                {{message.user_id}} - {{message.content}}
            </li>
        </ul>
        <div></div>
    </div>
</template>

<script>
import httpChat from "@/util/http-chat"
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
    
    data() {
        return {
            socket:null,
            stomp:null,
            roomId: '',
            room: {},
            userId: '',
            content: '',
            contents: [],
            text:""
        }
    },
    created() {
        this.roomId = this.$route.params.roomId
        this.userId = this.$store.state.myProfile.userId
        this.socket = new SockJS("http://localhost:8001/ws-stomp");
        this.stomp = Stomp.over(this.socket);
        this.findRoom();
        this.connect()
    },

    methods: {
        findRoom: function () {
            httpChat.get('/api/chat/' + this.roomId,{headers:{Authorization: this.$store.state.authorization}}).then(response => {
                this.contents = response.data;
            });
        },
        sendMessage: function () {

            this.stomp.send("/pub/chat/message/"+this.roomId,  JSON.stringify({

                userId: this.userId,
                content: this.text
            }),{});
              
            this.text = '';
        },
        recvMessage: function (recv) {
            console.log(recv)
            this.contents.unshift({
                "userId": recv.userId,
                "content": recv.content
            })
        },
            connect() {
        var roomId = this.roomId
        var userId = this.userId
        var recvMessage = this.recvMessage
        var stomp = this.stomp
        console.log(this.stomp)
        this.stomp.connect({}, function (frame) {
            console.log(frame)
            stomp.subscribe("/sub/chat/room/" + roomId, res=> {
                var recv = JSON.parse(res.body);
                console.log(recv)
                recvMessage(recv);
            });

        })
        console.log(this.stomp)
    },
    }
}
</script>

<style>

</style>
