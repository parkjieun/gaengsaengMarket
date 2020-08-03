<template>
<v-app>
    <v-row justify="center">
        <v-dialog v-model="dialog2" max-width="700">
            <v-card>
                <v-card-title class="headline text-center">
                    <div style="margin:auto;">Join us</div>
                </v-card-title>
                <v-divider></v-divider>
                <v-card-text>
                    <v-list>
                        <v-list-item>
                            <v-spacer></v-spacer>
                            <image-input v-model="avatar" @input="input">
                                <div slot="activator">
                                    <v-avatar size="10rem" v-ripple v-if="!avatar" class="grey lighten-3 mb-3">
                                        <v-icon size="5rem">mdi-account</v-icon>
                                    </v-avatar>
                                    <v-avatar size="10rem" v-ripple v-else class="mb-3">
                                        <img :src="avatar.imageURL" alt="avatar">
                                    </v-avatar>
                                </div>
                            </image-input>
                            <!-- <v-slide-x-transition>
                                    <div v-if="avatar && saved == false">
                                        <v-btn class="primary" @click="uploadImage" :loading="saving">Save Avatar</v-btn>
                                    </div>
                                </v-slide-x-transition> -->
                            <v-spacer></v-spacer>
                        </v-list-item>
                        <v-list-item>
                            <v-text-field v-model="nickName" :rules="rules" label="닉네임" required></v-text-field>
                        </v-list-item>
                        <v-list-item>
                            <v-text-field :value="address" label="상세 주소" readonly @click="addressSearch"></v-text-field>
                        </v-list-item>
                        <v-list-item>
                            <v-textarea v-model="introduce" label="상점 소개"></v-textarea>
                        </v-list-item>
                    </v-list>
                </v-card-text>

                <v-divider></v-divider>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="#a6e3e9" outlined="" @click="submit">
                        회원가입
                    </v-btn>

                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-row>
</v-app>
</template>

<script>
import axios from 'axios'
import http from "@/util/http-common"
import ImageInput from '@/components/form/ImageInput.vue'
export default {

    props: ['dialog2', 'socialId'],
    data() {
        return {
            avatar: null,
            saving: false,
            saved: false,
            nickName: "",
            address: "",
            introduce: "",
            img: null,
            rules: [
                value => (value && value.length <= 10) || "10자 이내로 입력해주세요"
            ]
        }
    },
    components: {
        ImageInput: ImageInput
    },
    watch: {
        dialog2(val) {
            val || this.$emit("closeForm")
        }
    },
    methods: {
        uploadImage() {
            this.saving = true
            setTimeout(() => this.savedAvatar(), 1000)
        },
        savedAvatar() {
            this.saving = false
            this.saved = true
        },
        addressSearch() {
            var address2;
            var changeAddress = this.changeAddress
            new daum.Postcode({
                oncomplete: function (data) {
                    address2 = data.roadAddress
                    changeAddress(data.roadAddress)
                }
            }).open();

        },
        changeAddress(value) {
            this.address = value
        },
        input(file) {
            this.img = file.formData.entries().next().value[1]
        },
        submit() {
            const frm = new FormData();
            frm.append("nickName", this.nickName)
            if (this.img !== null) {
                frm.append("img", this.img)
            }
            frm.append("socialId",this.socialId)
            frm.append("introduce", this.introduce)
            frm.append("address", this.address)
            http.post("/api/user", frm, {
                    headers: {
                        Authorization: this.$store.state.authorization,
                        'Content-Type': 'multipart/form-data'
                    }
                })
                .then(res => {
                    console.log(res)
                })
            this.closeForm()
        },
        closeForm() {
            this.$emit("closeForm")
        }
    },

}
</script>

<style scoped>

</style>
