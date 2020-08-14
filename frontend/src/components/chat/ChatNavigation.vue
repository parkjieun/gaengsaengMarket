<template>
  <v-navigation-drawer absolute right temporary v-model="chatFlag" style="z-index: 99;">
       <template v-slot:prepend  >
        <v-list-item two-line>
          <v-list-item-avatar>
            <img :src="imgURL">
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>{{myProfile.nickName}}</v-list-item-title>
            <v-list-item-subtitle>{{myProfile.introduce}}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </template>

      <v-divider></v-divider>

      <v-list
        dense
        nav
      >
         <v-list-item v-for="item in chatrooms" :key="item.roomId" @click="enterRoom(item.roomId)" style="height: 25px;">

            <v-list-item-avatar>
                <v-img :src="item.profileImg"></v-img>
            </v-list-item-avatar>
            <v-list-item-content>
                <v-list-item-title v-text="item.roomName"></v-list-item-title>
            </v-list-item-content>
            <v-list-item-icon style="margin: auto;">
                <v-icon :color="'#a6e3e9'">mdi-chat</v-icon>
            </v-list-item-icon>

        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    

</template>

<script>
import { mapState } from 'vuex'
import httpChat from "@/util/http-chat"
import http from "@/util/http-common"
const baseURL = "http://i3a504.p.ssafy.io"
export default {
    props: ['chatFlag'],
    data() {
        return {
            receiverId: '',
            chatrooms: [],
            showChatList: false,
        }
    },
    watch: {
        chatFlag: function() {
            this.$emit('flag', this.flag)
        }
    },
    computed: {
       flag() {
           return this.chatFlag
       },
       imgURL: function() { return baseURL + "/static/image/account/" + this.myProfile.profileImg },
       ...mapState(['myProfile'])
    },
    methods: {
        findAllRoom: function () {
            httpChat.get('/api/chat/room', {
                headers: {
                    Authorization: this.$store.state.authorization
                }
            }).then(response => {
                this.getJoinMember(response.data)
            });
        },
        getJoinMember(data) {
            data.forEach(element => {
                httpChat.get('/api/chat/room/' + element.roomId, {
                    headers: {
                        Authorization: this.$store.state.authorization
                    }
                }).then(res => {
                    const joinData = res.data
                    joinData.forEach(ele => {
                        if (ele.userId !== this.$store.state.myProfile.userId) {
                            
                            http.get('/api/user/' + ele.userId).then(res2 => {
                      
                                element['roomName'] = res2.data.nickName
                                element['profileImg'] = baseURL + "/static/image/account/" + res2.data.profileImg
                                this.chatrooms.push(element)
                            })
                            
                        }
                    })

                })
            });

        },

        createRoom() {

            var params = new URLSearchParams();
            params.append("receiverId", this.receiverId);
            httpChat.post('/api/chat/room', params, {
                    headers: {
                        Authorization: this.$store.state.authorization
                    }
                })
                .then(
                    response => {
                        alert(response.data.name + "방 개설에 성공하였습니다.")
                        this.receiverId = '';
                        this.findAllRoom();
                    }
                )
                .catch(response => {
                    alert("채팅방 개설에 실패하였습니다.");
                });

        },
        enterRoom(roomId) {
            httpChat.get('/api/chat/room/' + roomId, {
                    headers: {
                        Authorization: this.$store.state.authorization
                    }
                }).then(res => {
                    const joinData = res.data
                    joinData.forEach(ele => {
                        if (ele.userId !== this.$store.state.myProfile.userId) {
                            http.get('/api/user/' + ele.userId).then(res2 => {
                                this.$store.dispatch("setPatner",res2.data.userId)
                            })
                        }
                    })

                })
            this.$router.push({
                name: "Chat2",
                params: {
                    roomId: roomId
                }
            })
        },
        expandChatList() {$("#userListBox").slideToggle();
            this.showChatList = !this.showChatList;
        },
    },
    created() {
        this.findAllRoom();
    }

}
</script>

<style scoped>
.allUsersList{
  width: 300px;
  margin: 20px;
}
.allUsersList .card-header{
  background: #683db8;
  color: #FFF;
  padding: 10px;
}
.allUsersList .image img{
  border-radius: 16px;
}
.usersChatList{
  position: absolute;
  width: 250px;
  bottom: 0;
  margin-bottom: 0;
  right: 360px;
}
.show{
  display: block;
}
.thumb-user-list{
  display: none;
}
.thumb-user-list .image img{
  border-radius: 30px;
}
.usersChatList .card-header{
  background: #683db8;
  font-size: 13px;
}
.chatBox{
  position: absolute;
  bottom: 0;
  right: 0;
  width: 300px;
  margin: 40px;
  margin-bottom: 0;
  font-size: 13px;
}
.chat-content{
  overflow: auto;
  height: 300px;
}
.chatBox .card{
  border-radius: 4px;
}
.chatBox .card-header{
  background: #683db8;
}
.header-title{
  height: 50px;
}
.card-header-title i{
  font-size: 10px;
  color: #32e4cd;
  margin-right: 6px;
}
.card-header .card-header-title{
  color: #FFF;
}
.chat-content small{
  margin: 0;
}
.chat-content p{
  background: #ecf1f8;
  padding: 10px;
  border-radius: 8px;
  margin-bottom: 0;
}
.my-content .media-content{
  width: 80%;
}
.my-content .message{
  float: right;
  background: #683db8;
  color: #FFF;
  text-align: right;
  padding: 10px;
  margin-bottom: 4px;
  font-size: 13px;
}
.my-content .chat-content small{
  float: right;
}
.my-content small{
  display: block;
  float: right;
  width: 100%;
  text-align: right;
}
.chat-textarea{
  font-size: 14px;
  padding: 8px;
  height: 40px;
  width: 100%;
  border: none;
  overflow: auto;
  outline: none;

  -webkit-box-shadow: none;
  -moz-box-shadow: none;
  box-shadow: none;
  resize: none;
}
.chat-message-group{
  
}
.chat-message-group .chat-thumb{
  float: left;
  width: 20%;
}
.chat-message-group .chat-messages{
  float: left;
  width: 80%;
  margin-bottom: 20px;
}
.chat-message-group .message{
  float: left;
  padding: 10px;
  background: #ecf1f8;
  font-size: 13px;
  border-radius: 5px;
  margin-bottom: 3px;
}
.chat-messages .from{
  float: left;
  display: block;
  width: 100%;
  text-align: left;
  font-size: 11px;
}
.chat-thumb img{
  border-radius: 40px;
}
.writer-user .chat-messages{
  float: right;
  width: 100%;
}
.writer-user .chat-messages .message{
  float: right;
  background: #683db8;
  color: #FFF;
}
.writer-user .chat-messages .from{
  float: left;
  display: block;
  width: 100%;
  text-align: right;
}
.chat-message-group .typing{
  float: left;
}
.chatBox .card-header-icon i{
  color: #FFF;
  font-size: 13px;
}
/* CSSS */
.outside-box{
  height: 100px;
  background: #F8C;
  width: 200px;
  margin: 20px;
  overflow: auto;
}
.outside-box .content-insider{
  height: 300px;
  background: #C9C;
}
/* CSS Spinner */
.spinner {
  margin: 0 30px;
  width: 70px;
  text-align: center;
}

.spinner > div {
  width: 4px;
  height: 4px;
  background-color: #888;

  border-radius: 100%;
  display: inline-block;
  -webkit-animation: sk-bouncedelay 1.4s infinite ease-in-out both;
  animation: sk-bouncedelay 1.4s infinite ease-in-out both;
}

.spinner .bounce1 {
  -webkit-animation-delay: -0.32s;
  animation-delay: -0.32s;
}

.spinner .bounce2 {
  -webkit-animation-delay: -0.16s;
  animation-delay: -0.16s;
}

@-webkit-keyframes sk-bouncedelay {
  0%, 80%, 100% { -webkit-transform: scale(0) }
  40% { -webkit-transform: scale(1.0) }
}

@keyframes sk-bouncedelay {
  0%, 80%, 100% { 
    -webkit-transform: scale(0);
    transform: scale(0);
  } 40% { 
    -webkit-transform: scale(1.0);
    transform: scale(1.0);
  }
}
/* EmojiBox */
.emojiBox{
  width: 150px;
  margin: 30px;
}
.emojiBox .box{
  height: 100px;
  padding: 4px;
}
/* */
.card-header-title img{
  border-radius: 40px;
}
</style>