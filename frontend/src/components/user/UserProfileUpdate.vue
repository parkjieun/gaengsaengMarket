<template>
    <v-row>
        <v-col cols="11" md="8" class="mx-auto">
            <!-- 프로필 사진 -->
            <v-row >
            <v-avatar color="#a6e3e9" size="62">
                <img v-if="myProfile.profileImg" :src="'http://i3a504.p.ssafy.io:8003'+myProfile.profileImg" alt="Profile-image">
                <v-icon v-else size="40" dark>mdi-account</v-icon>
            </v-avatar>
            <v-file-input label="프로필 사진 첨부" v-model="myProfile.profileImg" :rules="rules" accept="image/png, image/jpeg, image/jpg" prepend-icon="mdi-camera"></v-file-input>
            </v-row>
            <!-- 닉네임 -->
            <v-text-field v-model="myProfile.nickName" :counter="128" label="닉네임" ></v-text-field>
            <!-- 비밀번호 변경 -->

            <!-- 주소 -->
            <v-text-field v-model="myProfile.address" :counter="255" label="상세 주소" readonly @click="addressSearch"></v-text-field>

            <!-- 자기소개 -->
            <v-textarea v-model="myProfile.introduce" label="상점 소개"></v-textarea>

            

            <v-btn @click="updateProfile(myProfile)" id="clickBtn">수정완료</v-btn>
        </v-col>

    </v-row>
</template>


<script>
import { mapState, mapActions } from 'vuex'
export default {
    data() {
        return {
            rules: [
                value => !value || value.size < 1000000 || '프로필 사진 파일 크기는 1 MB 미만이여야 합니다.',
            ],
        }
    },
    computed: {
        ...mapState(['myProfile']),
    },
    methods: {
        ...mapActions(['getMyProfile', 'updateProfile']),
        

       addressSearch(){
            var address2;
            var changeAddress = this.changeAddress
            new daum.Postcode({
                oncomplete: function(data) {
                    address2 = data.roadAddress
                    changeAddress(data.roadAddress)
                }
            }).open();
            
        },
        changeAddress(value){
            this.myProfile.address=value
        },
        

    },
    created() {
        this.getMyProfile()
    
    },
    watch: {
        myProfile: function() {
            const btn = document.getElementById('clickBtn')
            if (this.myProfile.nick_name.length > 128 || this.myProfile.nick_name.length === 0) {
                btn.classList.add('v-btn--disabled')
            }
            else {
                if (btn.classList.contains('v-btn--disabled')) {
                    btn.classList.remove('v-btn--disabled')
                }
            }
        }
    }

}
</script>

<style scoped>
</style>