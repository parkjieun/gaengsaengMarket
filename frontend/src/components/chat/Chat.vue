<template>
    <div class="container" id="app" v-cloak>
        <div class="row">
            <div class="col-md-12">
                <h3>채팅방 리스트</h3>
            </div>
        </div>
        <div class="input-group">
            <div class="input-group-prepend">
                <label class="input-group-text">방제목</label>
            </div>
            <input type="text" class="form-control" v-model="receiverId" v-on:keyup.enter="createRoom">
            <div class="input-group-append">
                <button class="btn btn-primary" type="button" @click="createRoom">채팅방 개설</button>
            </div>
        </div>
        <ul class="list-group">
            <li class="list-group-item list-group-item-action" v-for="item in chatrooms" v-bind:key="item.roomId" v-on:click="enterRoom(item.roomId)">
                {{item.roomId}}
            </li>
        </ul>
    </div>
</template>

<script>
import httpChat from "@/util/http-chat"
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
            httpChat.get('/api/chat/room',{headers:{Authorization: this.$store.state.authorization}}).then(response => {
                this.chatrooms = response.data;
            });
        },
        createRoom: function () {

            var params = new URLSearchParams();
            params.append("receiverId", this.receiverId);
            httpChat.post('/api/chat/room', params,{headers:{Authorization: this.$store.state.authorization}})
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
        enterRoom(roomId){
            
            this.$router.push({name:"Chat2",params:{roomId:roomId}})
        }
    }
}
</script>

<style>

</style>
