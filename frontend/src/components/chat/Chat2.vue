<template>
<v-app>

    <section id="myd" class="chatbox">
		<section class="chat-window">
			<article v-for="message in messages" :key="message.messageId" :class="message.userId==myProfile.userId?'msg-self':'msg-remote'" class="msg-container" id="msg-0" >
				<div v-if="message.userId==myProfile.userId" class="msg-box">
					<div class="flr">
						<div class="messages">
							<p class="msg" id="msg-1">
								{{message.content}}
							</p>
						</div>
						<span class="timestamp"><span class="username">{{myProfile.nickName}}</span>&bull;<span class="posttime">Now</span></span>
					</div>
					<img class="user-img" id="user-0" :src="myProfile.profileImg?'http://i3a504.p.ssafy.io/static/image/account/'+myProfile.profileImg:'https://static.thenounproject.com/png/3069450-200.png'" />
				</div>
                <div v-else class="msg-box">
                    <img class="user-img" id="user-0" :src="myProfile.profileImg?'http://i3a504.p.ssafy.io/static/image/account/'+myProfile.profileImg:'https://static.thenounproject.com/png/3069450-200.png'" />
					<div class="flr">

						<div class="messages">
							<p class="msg" id="msg-1">
								{{message.content}}
							</p>
						</div>
						<span class="timestamp"><span class="username">Name</span>&bull;<span class="posttime">Now</span></span>
					</div>
					
				</div>
			</article>
            
		</section>
		<div class="chat-input" >
			<input type="text" v-model="text" @keydown.enter="sendMessage" />
			<button @click="sendMessage">
                <v-icon style="color:black;">
                    mdi-send
                </v-icon>
            </button>
		</div>
        <div style="height:50px;">
        </div>
	</section>
</v-app>
</template>

<script>
import httpChat from "@/util/http-chat"
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import {mapState} from "vuex"
export default {

    data() {
        return {
            socket: null,
            stomp: null,
            userId: '',
            roomId: '',
            room: {},
            userId: '',
            content: '',
            messages: [],
            text: ""
        }
    },
    created() {
        this.roomId = this.$route.params.roomId
        this.userId = this.$store.state.myProfile.userId
        this.socket = new SockJS("http://i3a504.p.ssafy.io:8001/ws-stomp");
        this.stomp = Stomp.over(this.socket);
        this.findRoom();
        this.connect()
    },
    computed:{
        ...mapState(['myProfile'])
    },
    methods: {
        findRoom(){
            httpChat.get('/api/chat/' + this.roomId, {
                headers: {
                    Authorization: this.$store.state.authorization
                }
            }).then(response => {
                console.log(response)
                this.messages = response.data;
            }).catch(res=>{
                console.log(res)
            });
        },
        sendMessage: function () {
            this.stomp.send("/pub/chat/message/" + this.roomId, JSON.stringify({
                userId: this.userId,
                content: this.text
            }), {});

            this.text = '';
            window.scrollTo(0,document.body.scrollHeight)
        },
        recvMessage: function (recv) {
            this.messages.push({
                "userId": recv.userId,
                "content": recv.content,
                "createDate": recv.createDate
            })
            window.scrollTo(0,document.body.scrollHeight)
        },
        connect() {
            var roomId = this.roomId
            var userId = this.userId
            var recvMessage = this.recvMessage
            var stomp = this.stomp

            this.stomp.connect({}, function (frame) {
                stomp.subscribe("/sub/chat/room/" + roomId, res => {
                    var recv = JSON.parse(res.body);
                    recvMessage(recv);
                });

            })

        },

    }
}
</script>

<style scoped>
html,
body {
    height: 100%;
    width: 100%;
    margin: 0;
    padding: 0;
    font-family: sans-serif;
    background: #1c1e22;
}
body {
	display: flex;
	align-items: center;
	justify-content: center;
    flex-direction: column;
}
::-webkit-scrollbar {
	width: 4px;
}
::-webkit-scrollbar-thumb {
	background-color: #fff;
	border-radius: 2px;
}
.chatbox {
    width: 100%;
    height: 100%;
    max-height: 100%;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    box-shadow: 0 0 4px rgba(0,0,0,.14),0 4px 8px rgba(0,0,0,.28);
}
.chat-window {
    padding-top:5px;
    flex: auto;
    max-height: calc(100% - 60px);
    background: rgba(92, 92, 92, 0.377);
    
}
.chat-input {
    flex: 0 0 auto;
    height: 60px;
    background: rgb(230,230,230);
    border-top: 1px solid #2671ff;
    box-shadow: 0 0 4px rgba(0,0,0,.14),0 4px 8px rgba(0,0,0,.28);
    position: fixed;
    bottom:0px;
    width:100%;
}
.chat-input input {
    height: 59px;
    line-height: 60px;
    outline: 0 none;
    border: none;
    width: calc(100% - 60px);
    color: black;
    text-indent: 10px;
    font-size: 12pt;
    padding: 0;
    background: rgb(230,230,230);
}
.chat-input button {
    float: right;
    outline: 0 none;
    border: none;
    height: 40px;
    width: 40px;
    color:blue;
    border-radius: 50%;
    padding: 2px 0 0 0;
    margin: 10px;
    transition: all 0.15s ease-in-out;
}
.chat-input input[good] + button {
    box-shadow: 0 0 2px rgba(0,0,0,.12),0 2px 4px rgba(0,0,0,.24);
    background: #2671ff;
}
.chat-input input[good] + button:hover {
    box-shadow: 0 8px 17px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
}
.chat-input input[good] + button path {
    fill: white;
}
.msg-container {
    position: relative;
    display: inline-block;
    width: 100%;
    margin: 0 0 10px 0;
    padding: 0;
}
.msg-box {
    display: flex;
    background: #5b5e6c;
    padding: 10px 10px 0 10px;
    border-radius: 0 6px 6px 0;
    max-width: 80%;
    width: auto;
    float: left;
    box-shadow: 0 0 2px rgba(0,0,0,.12),0 2px 4px rgba(0,0,0,.24);
}
.user-img {
    display: inline-block;
    border-radius: 50%;
    height: 40px;
    width: 40px;
    background: #2671ff;
    margin: 0 10px 10px 0;
}
.flr {
    flex: 1 0 auto;
    display: flex;
    flex-direction: column;
    width: calc(100% - 50px);
}
.messages {
    flex: 1 0 auto;
}
.msg {
    display: inline-block;
    font-size: 11pt;
    line-height: 13pt;
    color: rgba(255,255,255,.7);
    margin: 0 0 4px 0;
}
.msg:first-of-type {
    margin-top: 8px;
}
.timestamp {
    color: rgba(0,0,0,.38);
    font-size: 8pt;
    margin-bottom: 10px;
}
.username {
    margin-right: 3px;
}
.posttime {
    margin-left: 3px;
}
.msg-self .msg-box {
    border-radius: 6px 0 0 6px;
    background: #2671ff;
    float: right;
}
.msg-self .user-img {
    margin: 0 0 10px 10px;
}
.msg-self .msg {
    text-align: right;
}
.msg-self .timestamp {
    text-align: right;
}
</style>
