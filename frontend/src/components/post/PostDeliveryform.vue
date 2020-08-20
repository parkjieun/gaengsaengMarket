<template>
<v-app>
       <v-dialog v-model="deliverydialog" persistent max-width="600px">
          <v-card>
            <v-card-title style="float:right;padding-top:24px">
              <a @click="close"><img calss="txt_btn" :src="require(`@/assets/post/close.png`)" ></a>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col  cols="12" >
                      <span class="headline" style="font-size:1.125rem !important;font-weight:bold;color:#000">바로구매 (택배거래)</span>
                  </v-col>

                  <v-col cols="12" >
                    <div style=" float: left;margin-right:15px">
                      <img v-if="item.files" :src="'http://i3a504.p.ssafy.io/static/image/post/'+item.files.split(',')[0]" style="height:50px;width:50px">  
                    </div>
                    <div  style=" float: left;">
                      <div style="color:#1E1D29;font-weight:550">{{item.title }}  </div>
                      <div>{{item.price | currency}}원</div>
                    </div>
                  </v-col>

                  <v-col cols="12">
                    <v-radio-group v-model="addrselect" row  v-on:change="addrchange">
                      <v-radio
                        label="기존 주소"
                        color="primary"
                        value="defaultAddr"
                      ></v-radio>
                      <v-radio
                        label="새 주소"
                        color="primary"
                        value="newAddr"
                      ></v-radio>
                    </v-radio-group>
                    <!-- <v-text-field label="배송지" ref="addr" v-model="addr" required :readonly="addrIsActive" ></v-text-field> -->
                    <v-text-field label="배송지" ref="addr" v-model="addr" required readonly @click="addressSearch"></v-text-field>
                  </v-col>
                  
                  <v-col cols="12" sm="6"  >
                    <v-btn large style="width:100%;background-color:#fff;height:50px;font-size:17px;color:#2d3753" @click="payIsActive('point')" :class="{pay_selected: pointIsActive}">갱생포인트</v-btn>
                  </v-col>
                  <v-col cols="12" sm="6" >
                    <v-btn large style="width:100%;background-color:#fff;height:50px;font-size:17px;color:#2d3753" @click="payIsActive('bank')" :class="{pay_selected: bankIsActive}">무통장입금</v-btn>
                  </v-col>

                  <v-col cols="12"  sm="9" v-if="pointIsActive">
                      <v-text-field label="포인트 입력"  ref="point" required type="number" @keyup="pointChange" v-model="point" :rules="[rules.pointcnt]" ></v-text-field>
                      <small>사용 가능 포인트  {{myProfile.pointVal | currency}}P</small>  
                  </v-col>

                  <v-col cols="12" sm="3" v-if="pointIsActive" >
                    <v-btn  style="float:left"  @click="pointAllUse(myProfile.pointVal, item.price)" >전액사용</v-btn>
                  </v-col>

                  <v-col cols="12" v-if="bankIsActive"> 
                    <span style="color:#000">무통장입금(가상계좌) 안내</span>
                    <br><br>
                    가상계좌를 발급 받아 결제금액을 입금할 수 있는 서비스입니다.
                  </v-col>
                  
                  <v-divider></v-divider> 

                  <v-col cols="12"> 
                     <v-btn block  style="font-size:14px;font-weight:bold;background:#2d3753 !important" dark @click="paySubmit"> 구매하기 </v-btn>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>
          </v-card>
        </v-dialog>
        {{getN}}
</v-app>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import http_post from "@/util/http-post"

import PostDirectform from "@/components/post/PostDirectform.vue"

export default {
  name:"PostDirectform",
  props: ['deliverydialog'],
  computed: {
    ...mapGetters(['item']),
    ...mapState(['myProfile']),
    getN () { 
       // console.log("PostDirectform getN"+ this.$store.getters.getN)
        this.addr = this.$store.getters.getN.address; 
      }
  },
  methods: {
    close(){
      this.$emit("closeForm1", "cancel")
    },
    paySubmit(){
      //주소 빈값아닌지 검사
      if(this.addr == ""){
         this.$refs.addr.focus();
         return false;
      }
      // 포인트 입력시 빈값 아닌지 검사.
      else if(this.pointIsActive == true){
        
        if(this.point < this.item.price ){
          this.$refs.point.focus();
          return false;
        }
        else if(this.point > this.item.price){
          this.point = this.item.price;
        }
      }
      else if(this.bankIsActive == true){
         this.point = 0;
      }

      //보내기
      this.buyAction();
      //this.deliverydialog = false
      
       //del_flag변경
      
     // console.log("close1");
      this.$emit("closeForm1", "saleCompleted")
    },
    addrchange(){
      if(this.addrselect == "newAddr"){
        //readonly 제거
        this.addrIsActive=false;
        this.addr = "";
      }else{
        //readonly 추가
        this.addrIsActive=true;
        this.addr = this.myProfile.address;
      }
    },
    pointAllUse(myPoint, itemPrice){
      if(myPoint < itemPrice ){
        this.point=myPoint;
      }else{
        this.point=itemPrice;
      }
    },
    payIsActive(flag){
      if(flag=="point"){
        this.pointIsActive=true;
        this.bankIsActive=false;
      }else{
        this.pointIsActive=false;
        this.bankIsActive=true;
        this.point=null;
      }
    },
    buyAction(){
      
      //업데이트
      http_post.put('/api/post/buy?user_id=' +this.myProfile.userId +'&post_id=' + this.item.post_id + '&price=' + this.point + '&gubun=delivery&sellerUser_id=' + this.item.user_id , {}, 
            {
                headers: {
                  Authorization: this.$store.state.authorization,
                  'Content-Type': 'application/json'
              }
            }
      )
      .then(({ data }) => {
        var msg;

        if (data === 'success') {
            msg = '구매가 완료되었습니다.';
            //vue.$store.dispatch("setPoint", this.point)
            this.$store.commit('setPoint',this.point);
          }else{
            msg = '이미 판매된 상품입니다.';
          }
        //alert(msg);
        //this.myProfile.pointVal -= this.item.price;
        this.$dialog.notify.success(msg, {
          position: 'top-right',
          timeout: 2000
        })

      })
      .catch(() => {
        //alert('에러가 발생했습니다?');
           this.$dialog.notify.warning('에러가 발생했습니다', {
            position: 'top-right',
            timeout: 2000
          })
      })
    },
    pointChange(){
      //console.log("변화 "+ this.point +"   "+ this.myProfile.pointVal);
      if(this.point > this.item.price){
        this.point = this.item.price
      }
      if(this.point > this.myProfile.pointVal){
        this.point = this.myProfile.pointVal
      }
    },
    addressSearch(){
      if(this.addrselect == "newAddr"){
        var address2;
        var changeAddress = this.changeAddress
        new daum.Postcode({
            oncomplete: function(data) {
                address2 = data.roadAddress
                changeAddress(data.roadAddress)
            }
        }).open();
      }
    },
    changeAddress(value){
        this.addr=value
    },
  },
  data () {
    return {
        addr:null,
        addrselect:'defaultAddr',
        addrIsActive:true,
        pointIsActive:true,
        bankIsActive:false,
        point:null, 
        rules: {   
          pointcnt: v => (v >= this.item.price) ||`${(this.item.price- v).toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,") }P 부족합니다`,
        },
      }
  },
  filters: {
      currency (value) {
          var num = new Number(value);
          return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,")
      }
  },
  watch: {
    deliverydialog(val) {
        val || this.$emit("closeForm1")
    },
    // pointChange(){
    //   console.log("변화 "+ this.point );
    //   if(this.point > this.item.price){
    //     this.point = this.item.price
    //   }
    // }
  },
  
}
</script>

<style scoped>
.pay_selected{
  border:1.5px solid #2d3753;
  font-weight:550
}
</style>