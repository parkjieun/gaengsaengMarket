<template>
<v-app>
    <v-container style="height:100%" >
        <v-row style="height:15%">
            <v-col class="text-center"><h1>갱생머니 충전</h1></v-col>
        </v-row>
        <v-row style="height:80%" align="center">
            <v-col align="center" style="height:100%">
                <v-card tile height="100%">
                    <v-row class="pa-1" >
                        <v-col cols="5">
                            현재 보유 머니 
                        </v-col>
                        <v-col cols=5 class="text-right">
                            {{$store.state.myProfile.pointVal | currency}}
                        </v-col>
                        <v-col>
                            GM
                        </v-col>
                    </v-row>
                    <v-row class="pa-1">
                        <v-col cols="5">
                            충전 금액
                        </v-col>
                        <v-col cols="5">
                            <input type="text" @blur="endTyp" v-model="chargePoint" placeholder="0" style="width:100%;border-bottom:solid 1px black;">
                        </v-col>
                        <v-col>
                            GM
                        </v-col>
                    </v-row>
                    <v-row class="pa-1">
                        <v-col cols="5">
                            결제 금액
                        </v-col >
                        <v-col cols="5" class="text-right">
                            {{payMoney | currency}}
                        </v-col>
                        <v-col>
                            원
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col align="center">
                            <v-btn @click="charge" dark color="rgb(61, 24, 96)">
                                충전하기
                            </v-btn>
                        </v-col>
                    </v-row>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
    <v-snackbar
        v-model="snackbar"
       color="#bb99cd"
        timeout="5000"
        top
        vertical

      >
        충전이 성공적으로 완료되었습니다.
        <template v-slot:action="{ attrs }">
          <v-btn
            dark
            text
            v-bind="attrs"
            @click="closeWin"
          >
            확인
          </v-btn>
        </template>
      </v-snackbar>
</v-app>
</template>

<script>
import http from "@/util/http-common"
import { integer } from 'vee-validate/dist/rules'
export default {
    data(){
        return {
            chargePoint:"",
            payMoney:0,
            snackbar:false,
        }
    },
    methods:{
        endTyp(){
            this.payMoney = this.chargePoint
        },
        charge(){
            http.patch("/api/user/point/payment",{pointVal:parseInt(this.chargePoint)},{headers:{Authorization: this.$store.state.authorization}}).then(res=>{
                this.$store.dispatch("getMyProfile")
                // alert("포인트 충전이 완료되었습니다.")
                // self.close()
                setInterval(res=>{
                    self.close()
                },5000)
                this.snackbar=true;
                
            })
            
        },
        closeWin(){
            self.close()
        }
    },
    filters:{
              currency: function (value) {
          var num = new Number(value);
          return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,")
      },
    }
}
</script>

<style scoped>
.container{
    height:100%;
}
input{
    text-align: right;
}
</style>