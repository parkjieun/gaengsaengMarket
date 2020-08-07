<template>
<div class="container" id="app" v-cloak>
<div class="nochat" v-if="chatrooms.length==0">
    진행중인 채팅이 없습니다.
</div>
    <div v-else>
    <v-list subheader>
        <v-list-item v-for="item in chatrooms" :key="item.roomId" @click="enterRoom(item.roomId)">

            <v-list-item-avatar>
                <!-- <v-img :src="item.avatar"></v-img> -->
                <v-icon>mdi-account</v-icon>
            </v-list-item-avatar>
            <v-list-item-content>
                <v-list-item-title v-text="item.roomName"></v-list-item-title>
            </v-list-item-content>
            <v-list-item-icon>
                <v-icon :color="'deep-purple accent-4'">mdi-chat</v-icon>
            </v-list-item-icon>

        </v-list-item>
    </v-list>
    </div>
</div>
</template>

<script>
import httpChat from "@/util/http-chat"
import http from "@/util/http-common"
export default {

    data() {
        return {
            receiverId: '',
            chatrooms: []
        }
    },
    created() {
        this.findAllRoom();
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
                                
                                this.chatrooms.push(element)
                            })
                            console.log(element)
                            
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
        }
    }
}
</script>

<style>
.nochat{
    margin-top:50%;
    margin-bottom:auto;
    font-weight: bold;
    font-size:30px;
}
.container{
    width:100%;
    height: 100%;
}
</style>
