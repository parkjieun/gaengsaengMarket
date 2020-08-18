<template>
<v-app id="inspire" style="position:absolute;">
    <v-row justify="center">
        <v-dialog v-model="dialog" max-width="500">
            <v-card>
                <div class="form-structor">
                    <div id="loginDiv" class="login">
                        <h2 class="form-title" id="login" @click="slideLUp">Log in</h2>
                        <button class="kakao submit-btn" @click="kakaoLogin">카카오로 로그인</button>
                        <g-signin-button class="google g-signin-button submit-btn" :params="googleSignInParams" @success="onGoogleSignInSuccess" @error="onGoogleSignInError">
                            구글로 로그인</g-signin-button>
                    </div>
                    <div id="signupDiv" class="signup slide-up">
                        <div class="center">
                            <h2 class="form-title" id="signup">Sign up</h2>
                            <v-list>
                                <v-list-item>
                                    <v-spacer></v-spacer>
                                    <image-input v-model="avatar" @input="input">
                                        <div slot="activator">
                                            <v-avatar size="8rem" v-ripple v-if="!avatar" class="grey lighten-3 mb-3">
                                                <v-icon size="5rem">mdi-account</v-icon>
                                            </v-avatar>
                                            <v-avatar size="8rem" v-ripple v-else class="mb-3">
                                                <img :src="avatar.imageURL" alt="avatar">
                                            </v-avatar>
                                        </div>
                                    </image-input>
                                    <v-spacer></v-spacer>
                                </v-list-item>
                                <v-list-item>
                                    <v-text-field color="#bb99cd" v-model="nickName" :rules="rules" label="닉네임" required></v-text-field>
                                </v-list-item>
                                <v-list-item>
                                    <v-text-field color="#bb99cd" v-model="phone" :rules="phoneRules" label="핸드폰번호" required></v-text-field>
                                </v-list-item>
                                <v-list-item>
                                    <v-text-field color="#bb99cd" :value="address" label="상세 주소" readonly @click="addressSearch"></v-text-field>
                                </v-list-item>
                                <v-list-item>
                                    <v-textarea color="#bb99cd" no-resize rows="2" v-model="introduce" label="상점 소개"></v-textarea>
                                </v-list-item>
                            </v-list>

                            <v-btn color="#bb99cd" class="submit-btn" @click="submit">Sign up</v-btn>
                        </div>
                    </div>
                </div>
            </v-card>
        </v-dialog>
    </v-row>
    <join-form :socialId="socialId" :dialog2="dialog2" @closeForm="closeForm" />

</v-app>
</template>

<script>
import axios from "axios"
import '../../assets/css/button.scss'
import http from '@/util/http-common'
import JoinForm from '@/components/user/JoinForm'
import store from "@/store/index"
import ImageInput from '@/components/form/ImageInput.vue'
import {
    mapState
} from "vuex"
export default {
    name: "LoginForm",
    props: ['dialog'],

    components: {
        JoinForm,
        ImageInput
    },
    data() {
        return {
            dialog2: false,
            googleSignInParams: {
                client_id: '97688771694-ndsidcu77ad92a118jb594cvk2hpr45s.apps.googleusercontent.com'
            },
            socialId: "",
            avatar: null,
            saving: false,
            saved: false,
            nickName: "",
            address: "",
            introduce: "",
            phone: "",
            img: null,
            rules: [
                value => (value && value.length <= 10) || "10자 이내로 입력해주세요"
            ],
            phoneRules: [
                value => (value && !value.includes("-") && (/^[0-9]*$/).test(value) && value.length <= 12) || "\'-\'를 제외한 숫자만 입력해주세요"
            ]
        }
    },

    methods: {
        slideLUp() {
            const signupBtn = document.getElementById('signup');
            // let parent = event.target.parentNode; //div login
            const parent = document.getElementById('loginDiv');
            Array.from(parent.classList).find((element) => {
                if (element !== "slide-up") {
                    parent.classList.add('slide-up')
                } else {
                    signupBtn.parentNode.parentNode.classList.add('slide-up')
                    parent.classList.remove('slide-up')
                }
            });
        },
        slideSUp() {
            const loginBtn = document.getElementById('login');
            // let parent = event.target.parentNode.parentNode;
            const parent = document.getElementById('signupDiv');
            Array.from(parent.classList).find((element) => {
                if (element !== "slide-up") {
                    parent.classList.add('slide-up')
                } else {
                    loginBtn.parentNode.classList.add('slide-up')
                    parent.classList.remove('slide-up')
                }
            });
        },
        closeForm() {
            this.dialog2 = false;
            this.$emit("closeForm")
        },
        loginSuc() {
            this.$swal({
                position: 'top-end',
                icon: 'success',
                width: "250px",
                title: "로그인 성공",
                showConfirmButton: false,

                timer: 1200,
                showClass: {
                    popup: 'animate__animated animate__shakeY'
                },
                hideClass: {
                    popup: 'animate__animated animate__fadeOutUp'
                },
                backdrop: false,
                heightAuto: false,
                
            });
            this.$store.dispatch("findAllRoom")
        },
        onGoogleSignInSuccess(googleUser) {
            var vue = this
            const accessToken = googleUser.getAuthResponse(true).access_token

            http.post("/oauth/authorization/google", {}, {
                headers: {
                    accessToken: accessToken
                }
            }).then(res => {
                const data = res.data
                this.setSocialId(data.socialId)

                vue.$store.dispatch("setAuth", "Bearer " + data.accessToken)
                if (data.isOlder == "true") {
                    vue.closeForm()
                    http.get("/api/user", {
                        headers: {
                            Authorization: vue.authorization
                        }
                    }).then(res => {
                        vue.$store.dispatch("setUserProfile", res.data)
                        vue.loginSuc()
                    })
                } else {
                    this.slideSUp()
                }
            })
        },
        onGoogleSignInError(error) {
            console.log('OH NOES', error)
        },
        kakaoLogin() {
            var joinForm = this.slideSUp
            var vue = this
            var setSocialId = this.setSocialId
            Kakao.Auth.login({
                success: function (response) {

                    http.post("/oauth/authorization/kakao", {}, {
                        headers: {
                            accessToken: response.access_token
                        }
                    }).then(res => {
                        const data = res.data
                        vue.$store.dispatch("setAuth", "Bearer " + data.accessToken)
                        setSocialId(data.socialId)
                        if (data.isOlder == "true") {
                            vue.closeForm()
                            http.get("/api/user", {
                                headers: {
                                    Authorization: vue.authorization
                                }
                            }).then(res => {
                                vue.$store.dispatch("setUserProfile", res.data)
                                vue.loginSuc()
                            })
                        } else {
                            joinForm()
                        }
                    })
                },
                fail: function (error) {
                    console.log(error);
                },
            });
        },
        joinForm() {
            this.$emit("closeForm")
            this.dialog2 = true
        },
        setSocialId(value) {
            this.socialId = value
        },
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
            frm.append("socialId", this.socialId)
            frm.append("introduce", this.introduce)
            frm.append("address", this.address)
            frm.append("phone", this.phone)
            http.post("/api/user", frm, {
                    headers: {
                        Authorization: this.$store.state.authorization,
                        'Content-Type': 'multipart/form-data'
                    }
                })
                .then(res => {
                    this.slideLUp()
                })
                .catch(res => {

                })
        },

    },
    computed: {
        ...mapState(['authorization'])
    },
    watch: {
        dialog(val) {
            val || this.$emit("closeForm")
        },
    },
}
</script>

<style lang="scss" scoped>
.g-signin-button {
    width: 35%;

}

@import url("https://fonts.googleapis.com/css?family=Fira+Sans");

html,
body {
    position: relative;
    min-height: 100vh;
    background-color: #E1E8EE;
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: "Fira Sans", Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
}

.form-structor {
    background-color: #222;
    border-radius: 15px;
    height: 650px;
    width: 500px;
    position: relative;
    overflow: hidden;

    &::after {
        content: '';
        opacity: .8;
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        background-repeat: no-repeat;
        background-position: left bottom;
        background-size: cover;
        background-image: url('../../assets/LoginIMG.jpg');
    }

    .login {
        position: absolute;
        top: 50%;
        left: 50%;
        -webkit-transform: translate(-50%, -50%);
        width: 65%;
        z-index: 5;
        -webkit-transition: all .3s ease;

        &.slide-up {
            top: 5%;
            -webkit-transform: translate(-50%, 0%);
            -webkit-transition: all .3s ease;
        }

        &.slide-up .form-holder,
        &.slide-up .submit-btn {
            opacity: 0;
            visibility: hidden;
        }

        &.slide-up .form-title {
            font-size: 1em;
            cursor: pointer;
        }

        &.slide-up .form-title span {
            margin-right: 5px;
            opacity: 1;
            visibility: visible;
            -webkit-transition: all .3s ease;
        }

        .form-title {
            color: #fff;
            font-size: 1.7em;
            text-align: center;

            span {
                color: rgba(0, 0, 0, 0.4);
                opacity: 0;
                visibility: hidden;
                -webkit-transition: all .3s ease;
            }
        }

        .form-holder {
            border-radius: 15px;
            background-color: #fff;
            overflow: hidden;
            margin-top: 50px;
            opacity: 1;
            visibility: visible;
            -webkit-transition: all .3s ease;

            .input {
                border: 0;
                outline: none;
                box-shadow: none;
                display: block;
                height: 30px;
                line-height: 30px;
                padding: 8px 15px;
                border-bottom: 1px solid #eee;
                width: 100%;
                font-size: 12px;

                &:last-child {
                    border-bottom: 0;
                }

                &::-webkit-input-placeholder {
                    color: rgba(0, 0, 0, 0.4);
                }
            }
        }

        .submit-btn {
            background-color: rgba(0, 0, 0, 0.4);
            color: rgba(256, 256, 256, 0.7);
            border: 0;
            border-radius: 15px;
            display: block;
            margin: 15px auto;
            padding: 15px 45px;
            width: 100%;
            font-size: 13px;
            font-weight: bold;
            text-align: center;
            cursor: pointer;
            opacity: 1;
            visibility: visible;
            -webkit-transition: all .3s ease;

            &:hover {
                transition: all .3s ease;
                background-color: rgba(0, 0, 0, 0.8);

                &.kakao {
                    color: yellow;
                }

                &.google {
                    color: rgb(66, 133, 244);
                }
            }
        }

        .kakao {
            background-color: rgba(254, 229, 0, 0.8);
            color: black;
        }

        .google {
            background-color: rgba(66, 133, 244, 0.8);
            color: white;
        }
    }

    .signup {
        position: absolute;
        top: 10%;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #fff;
        z-index: 5;
        -webkit-transition: all .3s ease;

        &::before {
            content: '';
            position: absolute;
            left: 50%;
            top: -10px;
            -webkit-transform: translate(-50%, 0);
            background-color: #fff;
            width: 200%;
            height: 150px;
            border-radius: 50%;
            z-index: 4;
            -webkit-transition: all .3s ease;
        }

        .center {
            position: absolute;
            top: calc(50%);
            left: 50%;
            -webkit-transform: translate(-50%, -50%);
            width: 65%;
            z-index: 5;
            -webkit-transition: all .3s ease;

            .form-title {
                color: #000;
                font-size: 1.7em;
                text-align: center;

                span {
                    color: rgba(0, 0, 0, 0.4);
                    opacity: 0;
                    visibility: hidden;
                    -webkit-transition: all .3s ease;
                }
            }

            .form-holder {
                border-radius: 15px;
                background-color: #fff;
                border: 1px solid #eee;
                overflow: hidden;
                margin-top: 50px;
                opacity: 1;
                visibility: visible;
                -webkit-transition: all .3s ease;

                .input {
                    border: 0;
                    outline: none;
                    box-shadow: none;
                    display: block;
                    height: 30px;
                    line-height: 30px;
                    padding: 8px 15px;
                    border-bottom: 1px solid #eee;
                    width: 100%;
                    font-size: 12px;

                    &:last-child {
                        border-bottom: 0;
                    }

                    &::-webkit-input-placeholder {
                        color: rgba(0, 0, 0, 0.4);
                    }
                }
            }

            .submit-btn {
                background-color: #bb99cd;
                color: rgba(256, 256, 256, 0.7);
                border: 0;
                border-radius: 15px;
                display: block;
                margin: 15px auto;
                padding: 15px 45px;
                width: 100%;
                font-size: 13px;
                font-weight: bold;
                cursor: pointer;
                opacity: 1;
                visibility: visible;
                -webkit-transition: all .3s ease;

                &:hover {
                    transition: all .3s ease;
                    background-color: rgba(0, 0, 0, 0.8);
                }
            }
        }

        &.slide-up {
            top: 90%;
            -webkit-transition: all .3s ease;
        }

        &.slide-up .center {
            top: 40%;
            -webkit-transform: translate(-50%, 0%);
            -webkit-transition: all .3s ease;
        }

        &.slide-up .form-holder,
        &.slide-up .submit-btn {
            opacity: 0;
            visibility: hidden;
            -webkit-transition: all .3s ease;
        }

        &.slide-up .form-title {
            font-size: 1em;
            margin: 0;
            padding: 0;
            cursor: pointer;
            -webkit-transition: all .3s ease;
        }

        &.slide-up .form-title span {
            margin-right: 5px;
            opacity: 1;
            visibility: visible;
            -webkit-transition: all .3s ease;
        }
    }
}

</style>
