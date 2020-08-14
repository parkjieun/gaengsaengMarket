<template>
<v-app>
 <v-dialog v-model="directdialog" persistent max-width="600px">
          <v-card>
            <v-card-title style="float:right;padding-top:24px">
              <img calss="txt_btn" :src="require(`@/assets/post/close.png`)" @click="close">
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
                        label="장소1"
                        color="primary"
                        value="postAddr"
                      ></v-radio>
                      <v-radio
                        label="장소2"
                        color="primary"
                        value="userAddr"
                      ></v-radio>
                    </v-radio-group>
                    <v-text-field label="직거래 장소"  v-model="addr" required readonly ></v-text-field>
                  </v-col>

                  <v-col cols="12" >
                    <div id="map"></div>
                  </v-col>
                  
                  <v-divider></v-divider> 

                  <v-col cols="12"> 
                     <v-btn block  style="font-size:14px;font-weight:bold;background:#2d3753 !important"  dark @click="paySubmit"> 거래하기 </v-btn>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>
          </v-card>
        </v-dialog>
</v-app>
</template>

<script>

import { mapState, mapGetters } from 'vuex';
import http_post from "@/util/http-post"

import PostDirectform from "@/components/post/PostDirectform.vue"

export default {
  name:"PostDirectform",
  props: ['directdialog','addr'],
  computed: {
    ...mapGetters(['item']),
    ...mapState(['myProfile']),
  },
  methods: {
    close(){
      console.log("close1");
      this.$emit("closeForm2", "cancel")
    },
    paySubmit(){
      //보내기
      this.buyAction();
      
      //del_flag변경
      console.log("close1");
      this.$emit("closeForm2", "saleCompleted")
    },
    addrchange(){
      if(this.addrselect == "userAddr"){
        this.addr = this.item.address;
        this.initMap()
      }else{
        let arr = this.item.addr.split(",")
        this.addr = arr[3] +" " +arr[0]
        this.initMap()
        //this.addr = this.item.addr;
      }
    },
    buyAction(){
      this.point=0;
      
      //업데이트
      http_post.put('/api/post/buy?user_id=' +this.myProfile.userId +'&post_id=' + this.item.post_id + '&price=' + this.point + '&gubun=direct', {}, 
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

          }else{
            msg = '이미 판매된 상품입니다.';
          }
        alert(msg);

      })
      .catch(() => {
        alert('에러가 발생했습니다?');
      })
    },
    initMap(){
      console.log("**************addr:"+this.addr);
      var mapContainer = document.getElementById('map'); // 지도를 표시할 div 
      console.log("mapContainer:"+mapContainer);
      var mapOption = {
            center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };  

      // 지도를 생성합니다    
      var map = new kakao.maps.Map(mapContainer, mapOption); 

      // 주소-좌표 변환 객체를 생성합니다
      var geocoder = new kakao.maps.services.Geocoder();

      // var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png', // 마커이미지의 주소입니다    
      // imageSize = new kakao.maps.Size(64, 69), // 마커이미지의 크기입니다
      // imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
      // // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
      // var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
 

      // 주소로 좌표를 검색합니다
      geocoder.addressSearch(this.addr, function(result, status) {

        // 정상적으로 검색이 완료됐으면 
        if (status === kakao.maps.services.Status.OK) {

          var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

          // 결과값으로 받은 위치를 마커로 표시합니다
          var marker = new kakao.maps.Marker({
            map: map,
            position: coords,
              // image: markerImage // 마커이미지 설정 
          });

          // 인포윈도우로 장소에 대한 설명을 표시합니다
          var infowindow = new kakao.maps.InfoWindow({
            content: '<div style="font-weight:bold;width:150px;text-align:center;padding:6px 0;">거래장소</div>'
          });
          infowindow.open(map, marker);

          // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
          map.setCenter(coords);
        } 
      });    
    }
    
  },
  data () {
    return {
        addrselect:'postAddr',
      }
  },
  created() {
    // console.log("크리에이티드다ㅏㅏ"+ this.addr)
  },
  filters: {
      currency (value) {
          var num = new Number(value);
          return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,")
      },
  },
  watch: {
    directdialog(val) {
        val || this.$emit("closeForm2")
    },
  },
  updated() {
    this.$nextTick(function () {
      console.log("########################3update");
      //this.initMap()// 모든 화면이 렌더링된 후 실행합니다.

      if (window.kakao && window.kakao.maps) {
        console.log("**************addr1");
        this.initMap();
      } else {
        console.log("**************addr2");
        const script = document.createElement('script');
        /* global kakao */
        script.onload = () => kakao.maps.load(this.initMap);
        script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=796352c031f116d976328625bdafa6df&libraries=services,clusterer,drawing';
        document.head.appendChild(script);
      }
    })
  }
}
</script>

<style scoped>
#map {
    width: 100%;
    height: 227px;
}

</style>