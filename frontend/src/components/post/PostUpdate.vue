<style>
#map:focus {
  outline: 0px solid transparent;
}
.map_wrap,
.map_wrap * {
  margin: 0;
  padding: 0;
  font-family: "Malgun Gothic", dotum, "돋움", sans-serif;
  font-size: 12px;
}
.map_wrap a,
.map_wrap a:hover,
.map_wrap a:active {
  color: #000;
  text-decoration: none;
}
.map_wrap {
  position: relative;
  width: 100%;
  height: 500px;
}

#menu_wrap {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 220px;
  margin: 10px 0 30px 10px;
  padding: 5px;
  overflow-y: auto;
  overflow-x: hidden;
  background: rgba(255, 255, 255, 0.85) !important;
  z-index: 1;
  font-size: 12px;
  border-radius: 10px;
}

#menu_wrap hr {
  display: block;
  height: 1px;
  border: 0;
  border-top: 2px solid #5f5f5f;
  margin: 3px 0;
}

#placesList .item {
  border-bottom: 1px solid #888;
  overflow: hidden;
  cursor: pointer;
  min-height: 65px;
}

#placesList .item .markerbg {
  float: left;
  position: absolute;
  width: 36px;
  height: 37px;
  margin: 10px 0 0 0px;
  background: url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png)
    no-repeat;
}
#placesList .item .marker_1 {
  padding: 4px;
  background-position: 0 -10px;
}
#placesList .item .marker_2 {
  background-position: 0 -56px;
}
#placesList .item .marker_3 {
  background-position: 0 -102px;
}
#placesList .item .marker_4 {
  background-position: 0 -148px;
}
#placesList .item .marker_5 {
  background-position: 0 -194px;
}
#placesList .item .marker_6 {
  background-position: 0 -240px;
}
#placesList .item .marker_7 {
  background-position: 0 -286px;
}
#placesList .item .marker_8 {
  background-position: 0 -332px;
}
#placesList .item .marker_9 {
  background-position: 0 -378px;
}
#placesList .item .marker_10 {
  background-position: 0 -423px;
}
#placesList .item .marker_11 {
  background-position: 0 -470px;
}
#placesList .item .marker_12 {
  background-position: 0 -516px;
}
#placesList .item .marker_13 {
  background-position: 0 -562px;
}
#placesList .item .marker_14 {
  background-position: 0 -608px;
}
#placesList .item .marker_15 {
  background-position: 0 -654px;
}
#pagination {
  margin: 10px auto;
  text-align: center;
}
#pagination a {
  display: inline-block;
  margin-right: 10px;
}
#pagination .on {
  font-weight: bold;
  cursor: default;
  color: #777;
}
</style>

<template>
  <v-app>
    <v-container fluid>
      <v-container>
        <v-col>
          <v-layout row justify-center align-center
            ><h1>상품 수정</h1></v-layout
          >
          <v-col> <v-divider></v-divider></v-col>
        </v-col>
      </v-container>
      <br />
      <br />
      <v-container
        ><v-col>
          <v-file-input
            multiple
            v-model="StandbyImgs"
            accept="image/*"
            enctype="multipart/form-data"
            @change="onFileChange()"
            label="상품 사진을 등록해 주세요"
            filled
            prepend-icon="mdi-camera"
          >
          </v-file-input>
        </v-col>
        <v-card
          v-show="
            UploadImages.length != 0 || onServerThumnailImgsUrl.length != 0
          "
        >
          <v-container fluid
            ><v-row>
              <template v-for="i in UploadImages.length">
                <v-col :key="`A-${i}`" cols="12" md="4">
                  <v-hover v-slot:default="{ hover }">
                    <v-card
                      :elevation="hover ? 12 : 2"
                      :class="{ 'on-hover': hover }"
                    >
                      <v-img
                        :src="thumnailImgsUrl[i - 1]"
                        :lazy-src="thumnailImgsUrl[i - 1]"
                        @click="removeImage(i)"
                        height="225px"
                      >
                      </v-img>
                      <v-layout row justify-center align-center>
                        <v-icon color="red darken-2">mdi-close</v-icon>
                      </v-layout>
                    </v-card>
                  </v-hover>
                </v-col>
              </template>
              <template v-for="j in onServerThumnailImgsUrl.length">
                <v-col :key="j" cols="12" md="4">
                  <v-hover v-slot:default="{ hover }">
                    <v-card
                      :elevation="hover ? 12 : 2"
                      :class="{ 'on-hover': hover }"
                    >
                      <v-img
                        :src="onServerThumnailImgsUrl[j - 1]"
                        :lazy-src="onServerThumnailImgsUrl[j - 1]"
                        @click="removeImageToServer(j)"
                        height="225px"
                      >
                      </v-img>
                      <v-layout row justify-center align-center>
                        <v-icon color="red darken-2">mdi-close</v-icon>
                      </v-layout>
                    </v-card>
                  </v-hover>
                </v-col>
              </template>
            </v-row>
          </v-container>
        </v-card>
      </v-container>
      <v-container>
        <v-row>
          <v-col cols="2"><h2>제목</h2></v-col>
          <v-col>
            <v-text-field
              v-model="title"
              :rules="titleRules"
              :counter="125"
              label="띄어쓰기를 꼭 해주세요"
              @blur="analyzeTitle"
              required
            ></v-text-field>
          </v-col>
        </v-row>
      </v-container>
      <v-container>
        <v-row>
          <v-col cols="2"><h2>카테고리</h2> </v-col>
          <v-col cols="3">
            <v-select
              :items="categoryBig"
              item-text="name"
              item-value="cate_big_id"
              v-model="seletedCateBig"
              ref="cateBig"
              :rules="categoryRules"
              @change="getCateMid()"
              placeholder="카테고리를 선택 해주세요"
              outlined
              required
            ></v-select>
          </v-col>
          <v-col>
            <v-select
              :items="categoryMid"
              item-text="name"
              item-value="cate_mid_id"
              v-model="seletedCateMid"
              ref="cateMid"
              :rules="categoryRules"
              :disabled="!seletedCateBig ? true : false"
              placeholder="세부 카테고리를 선택 해주세요"
              outlined
              required
            ></v-select>
          </v-col>
        </v-row>
      </v-container>
      <v-container>
        <v-row>
          <v-col cols="2"><h2>거래타입</h2></v-col>

          <v-col cols="1">
            <v-btn-toggle
              v-model="toggle_exclusive"
              color="#00bcd4"
              multiple
              group
            >
              <v-btn
                style="border: 1px solid #00bcd4; color: #00bcd4;"
                color="#00bcd4"
                >택배거래</v-btn
              >
              <v-btn
                style="border: 1px solid #00bcd4; color: #00bcd4;"
                color="#00bcd4"
                >직거래</v-btn
              >
            </v-btn-toggle>
          </v-col>
        </v-row>
      </v-container>
      <v-container>
        <v-row v-show="toggle_exclusive.includes(1)">
          <v-col cols="2"><h2>직거래 가능 요일</h2></v-col>
          <v-col>
            <v-btn-toggle v-model="toggle_weekend" multiple group>
              <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;"
                >월</v-btn
              >
              <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;"
                >화</v-btn
              >
              <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;"
                >수</v-btn
              >
              <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;"
                >목</v-btn
              >
              <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;"
                >금</v-btn
              >
              <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;"
                >토</v-btn
              >
              <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;"
                >일</v-btn
              >
            </v-btn-toggle>
          </v-col>
        </v-row>
      </v-container>
      <br />
      <v-container>
        <v-row v-show="toggle_exclusive.includes(1)">
          <v-col cols="2"><h2>직거래 장소</h2></v-col>
          <v-col>
            <div class="map_wrap">
              <div
                id="map"
                style="width:100%;height:100%;position:relative;overflow:hidden;"
              ></div>
              <div id="menu_wrap" class="bg_white">
                <v-row rows="1" style="text-align: center; margin-top: 3px; ">
                  <v-col sm="4" style="font-size: 13px;margin-top: 10px; "
                    >키워드 :
                  </v-col>
                  <v-col sm="5" style="font-size: 13px;padding:0px;">
                    <v-text-field
                      hide-details
                      v-model="searchKeyword"
                    ></v-text-field>
                  </v-col>
                  <v-col sm="3" style=" margin-top: 2px;margin-bottom: 0px;">
                    <v-btn icon color="#00bcd4" @click="searchPlaces()">
                      <v-icon>mdi-magnify</v-icon>
                    </v-btn>
                  </v-col>
                </v-row>

                <hr />
                <ul id="placesList" style="padding-left: 4px;"></ul>
                <div id="pagination"></div>
              </div>
            </div>
            <v-row>
              <v-row justify="center">
                <v-chip class="ma-2" label color="#fc9d9d">
                  {{ selectedTitleAddr }}
                </v-chip>
                <v-chip class="ma-2" label color="#ffcac2">
                  {{ selectedAddr }}
                </v-chip>
              </v-row>
            </v-row>
            <v-row
              justify="center"
              style="color:red;"
              v-if="selectedTitleAddr == ''"
            >
              <br />
              * 반드시 거래위치를 선택해주세요
            </v-row>
          </v-col>
        </v-row>
      </v-container>
      <br />
      <v-form fluid>
        <v-container>
          <v-row>
            <v-col cols="2"><h2>가격</h2></v-col>
            <v-col cols="3"
              ><v-text-field
                append-icon="mdi-currency-krw"
                type="number"
                :rules="priceRules"
                v-model="price"
                label="가격"
                placeholder="가격을 입력하세요"
                sufix=""
                outlined
              ></v-text-field>
            </v-col> </v-row
          ><v-col> <v-divider></v-divider></v-col>
        </v-container>
      </v-form>

      <v-form fluid>
        <v-container>
          <h2>내용</h2>
          <br />
          <v-container>
            <v-textarea
              v-model="contents"
              required
              outlined
              placeholder="내용을 입력해 주세요."
            ></v-textarea>
          </v-container>
          <v-col> <v-divider></v-divider></v-col>
        </v-container>
      </v-form>

      <v-container>
        <h2>태그</h2>
        <br />
        <v-col>
          <v-combobox
            append-icon="mdi-pound"
            v-model="model"
            :filter="filter"
            :hide-no-data="!search"
            :items="items"
            :search-input.sync="search"
            hide-selected
            multiple
            small-chips
            solo
          >
            <template v-slot:selection="{ attrs, item, selected }">
              <v-chip
                v-if="item === Object(item)"
                v-bind="attrs"
                :color="`${item.color} `"
                :input-value="selected"
                label
                small
              >
                <span class="pr-2">
                  {{ item.text }}
                </span>
                <v-icon small @click="remoceTags(item)">x</v-icon>
              </v-chip>
            </template>
            <template v-slot:item="{ index, item }">
              <v-text-field
                v-if="editing === item"
                v-model="editing.text"
                autofocus
                flat
                background-color="transparent"
                hide-details
                solo
                @keyup.enter="edit(index, item)"
              ></v-text-field>
            </template>
          </v-combobox>
        </v-col>
        <v-col> <v-divider></v-divider></v-col>
      </v-container>

      <v-form fluid>
        <v-container
          ><v-layout row justify-center align-center>
            <v-btn color="#A6E3E9" @click="createHandler()">
              수정
            </v-btn>
            <v-btn @click="deletePost()" color="#ffcac2">
              삭제
            </v-btn>
          </v-layout>
        </v-container>
      </v-form>
    </v-container>
  </v-app>
</template>

<script>
import axios from "axios";
export default {
  data: () => ({
    selectedTitleAddr: "", //마커클릭하면 설정되는 직거래 위치 장소이름
    selectedAddr: "", //마커클릭하면 설정되는 직거래 위치 지번? 도로명주소?

    searchKeyword: "이태원역",
    addr: "",
    isInOwnAddr: false,
    pageMap: "",
    pageInfowindow: "", //여기까지 합치는 맵
    focusAdrr: "", //디비에서 가져오는 주소, 포커싱할 주소임
    address: "", //맵에서 선택한 주소
    userId: "",
    postId: "",
    price: "",
    onServerThumnailImgsUrl: [], //서버에 올라와있는 이미지들 url
    deleteImags: [], //삭제 할 이미지들
    StandbyImgs: [], //파일 선택시 고르는 이미지들
    thumnailImgsUrl: [], //선택한 이미지들을 미리보기 보여주는 이미지의 url
    UploadImages: [], //서버에 보낼 최종적인 이미지 리스트
    userImage: "",
    toggle_exclusive: [],
    toggle_weekend: [],
    valid: false,
    title: "",
    contents: "",
    titleRules: [
      (v) => !!v || "제목을 입력해 주세요",
      (v) => (v && v.length <= 125) || "제목은 125자 이하여야 합니다",
    ],
    categoryRules: [(v) => !!v || "카테고리를 선택해 주세요"],
    priceRules: [(v) => (v && v >= 0) || "정확한 가격을 입력해 주세요"],
    categoryBig: [],
    categoryMid: [],
    seletedCateBig: "",
    seletedCateMid: "",
    //아래부터 태그관련 데이터
    activator: null,
    attach: null,
    colors: ["#fc9d9d", "#ffcac2", "#ffe6eb", "#A6E3E9", "#CBF1F5", "#DEFCFC"],
    editing: null,
    index: -1,
    items: [],
    nonce: 1,
    menu: false,
    model: [],
    search: null,
    y: 0,
    inputTags: [],
  }),
  created() {
    axios
      .get("http://i3a504.p.ssafy.io:8000/api/post/category/category_big")
      .then(({ data }) => {
        this.categoryBig = data;
      });

    this.postId = this.$route.params.post_id; //임시
    //this.postId = 10062; //임시 
    axios
      .get("http://i3a504.p.ssafy.io:8000/api/post/" + this.postId)
      .then(({ data }) => {
       // console.log("오는 전체데이터");
        //console.log(data);
        this.focusAdrr = data.addr;
        this.addr = this.focusAdrr; //새로 주소 설정 안하면 기존에 설정된 주소로 보내기 위해서
        let addrInfos = this.focusAdrr.split(",");
        this.selectedTitleAddr = addrInfos[0]; //마커클릭하면 설정되는 직거래 위치 장소이름
        this.selectedAddr = addrInfos[3]; //마커클릭하면 설정되는 직거래 위치 지번? 도로명주소?

        this.seletedCateBig = Number(data.cate_big_id);
        this.getCateMid();
        this.seletedCateMid = data.cate_mid_id;
        this.title = data.title;

        let dbContens = data.contents.split("<br>");
        let resultContens = "";
        for (let lines of dbContens) {
          resultContens += lines + "\n";
        }
        this.contents = resultContens;
        if (data.deal_type == 1) {
          this.toggle_exclusive.push(0);
        } else if (data.deal_type == 2) {
          this.toggle_exclusive.push(1);
        } else if (data.deal_type == 3) {
          this.toggle_exclusive.push(0);
          this.toggle_exclusive.push(1);
        }
        let imgs = data.files.split(",");
        for (let img of imgs) {
          this.onServerThumnailImgsUrl.push(
            "http://i3a504.p.ssafy.io/static/image/post/" + img
          );
        }
        this.price = data.price;
        this.userId = data.user_id;
        let splitTags = data.tags.split(",");
        for (let i in splitTags) {
          this.inputTags.push(splitTags[i]);
          this.model.push({ text: splitTags[i], color: this.colors[i] });
        }

        let sum = data.deal_weak;
        for (let i in sum.toString(2)) {
          if (sum.toString(2)[i] == 1) {
            this.toggle_weekend.push(sum.toString(2).length - i - 1);
            //console.log("["+i+"]째 요일은 값이 들어가 있어요");
          }
        }
      });
  },
  mounted() {
    /*
    console.log("mouted 동작중..");
    this.initMap();
    var container = document.getElementById("map");
    console.log("맵 잘 띄웡ㅆ니 : " + container);
    var options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667),
      level: 3,
    };

    this.pageMap = new kakao.maps.Map(container, options);
    this.pageMap.relayout();
    //this.initMap();*/
     
      if (window.kakao && window.kakao.maps) {
        setTimeout(() => { this.initMap() }, 1000)
      } else {
        const script = document.createElement("script");
        // global kakao
        script.onload = () => kakao.maps.load(this.initMap);
        script.src =
          "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=796352c031f116d976328625bdafa6df&libraries=services";
        document.head.appendChild(script);
      }
     
  },

  watch: {
    model(val, prev) {
      if (val.length === prev.length) return;

      this.model = val.map((v) => {
        if (typeof v === "string") {
          v = {
            text: v,
            color: this.colors[this.nonce - 1],
          };

          this.inputTags.push(v.text);
          this.nonce++;
        }

        return v;
      });
    },
  },

  methods: {
    remoceTags(item) {
      let i = 0;
      for (let tt of this.model) {
        if (tt.text == item.text) {
          this.inputTags.splice(i, 1);
          this.model.splice(i, 1);
          this.nonce--;
          break;
        }
        i++;
      }
    },
    analyzeTitle() {
      axios
        .get(
          "http://i3a504.p.ssafy.io:8002/api/opencv/distractinfo?title=" +
            this.title
        )
        .then(({ data }) => {
          for (let tag of data.tags) {
            this.inputTags.push(tag);
            this.model.push({ text: tag, color: this.colors[0] });
          }

          this.seletedCateBig = data.categories.cate_big_id;
          this.getCateMid();
          this.seletedCateMid = data.categories.cate_mid_id;

          this.price = Number(data.price);

          this.contents =
            this.title +
            "\n" +
            data.tags +
            " 이거 정말 좋은 놈으로 갖고왔습니다.";
        });
    },
    sendAddr(title) {
      this.addr = title;
      let addrInfos = title.split(",");
      this.selectedTitleAddr = addrInfos[0]; //마커클릭하면 설정되는 직거래 위치 장소이름
      this.selectedAddr = addrInfos[3]; //마커클릭하면 설정되는 직거래 위치 지번? 도로명주소?
    },
    initMap() {
      //console.log(" focusAdrr 잘 받아오니? " + this.focusAdrr);

     // console.log("setTimeout 전에 this.pageMap: " + this.pageMap);

      var container = document.getElementById("map");
      //console.log("맵 잘 띄웡ㅆ니 : " + container);
      var options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3,
      };

      var map = new kakao.maps.Map(container, options);
      this.pageMap = map;
      // 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
      var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
      this.pageInfowindow = infowindow;
      //[0] : title (검색 이름), [1]:위도 [2]:경도 [3]:지번(3번 아직 미추가)
      var addrInfos;
      //디비에 주소가 아무것도 저장이 안되어있다면? 혹은 불러오지 못해서 값이 아예없다면?
      if (this.focusAdrr == "" || this.focusAdrr == null) {
        //console.log("아무것도 안들어왔대요");
        addrInfos = ["멀티캠퍼스 역삼", 37.5012860931305, 127.03960466386198];
      } else {
        addrInfos = this.focusAdrr.split(","); //디비에 저장된 정보들 받아오는 변수
      }
      this.searchKeyword = addrInfos[0];
      var searchPlaces = this.searchPlaces;

      // this.searchPlaces();

      setTimeout(() => {
        searchPlaces();
      }, 1000);

      //this.isInOwnAddr = true;
    },
    searchPlaces() {
      var markers = [];
      var sendAddr = this.sendAddr;
      var keyword = this.searchKeyword;
      var map = this.pageMap;
      var infowindow = this.pageInfowindow;
      var theFlag = this.isInOwnAddr;
      var addrInfos = this.focusAdrr.split(","); //디비에 저장된 정보들 받아오는 변수
      var iwRemoveable = true; //인포윈도우 지울지말지 확인하는 불린

      if (!keyword.replace(/^\s+|\s+$/g, "")) {
        alert("키워드를 입력해주세요!");
        return false;
      }

      var ps = new kakao.maps.services.Places();
      // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
      ps.keywordSearch(keyword, placesSearchCB);

      // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
      function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
          // 정상적으로 검색이 완료됐으면
          // 검색 목록과 마커를 표출합니다
          displayPlaces(data);

          // 페이지 번호를 표출합니다
          displayPagination(pagination);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
          alert("검색 결과가 존재하지 않습니다.");
          return;
        } else if (status === kakao.maps.services.Status.ERROR) {
          alert("검색 결과 중 오류가 발생했습니다.");
          return;
        }
      }

      //처음에 직거래 장소로 자기 주소가 등록되어있을때
      function setFirstPlace() {
        // 정상적으로 검색이 완료됐으면
        var moveLatLon = new kakao.maps.LatLng(
          Number(addrInfos[1]),
          Number(addrInfos[2])
        );

        // 결과값으로 받은 위치를 마커로 표시합니다
        var marker = new kakao.maps.Marker({
          map: map,
          position: moveLatLon,
        });

        // 인포윈도우로 장소에 대한 설명을 표시합니다
        var infowindow = new kakao.maps.InfoWindow({
          content:
            '<div style="width:150px;text-align:center;padding:6px 0;">직거래위치<br/><b>' +
            addrInfos[0] +
            "</b></div>",
        });
        infowindow.open(map, marker);
        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setLevel(1);
        map.setCenter(moveLatLon);
      }

      // 검색 결과 목록과 마커를 표출하는 함수입니다
      function displayPlaces(places) {
        var listEl = document.getElementById("placesList"),
          menuEl = document.getElementById("menu_wrap"),
          fragment = document.createDocumentFragment(),
          bounds = new kakao.maps.LatLngBounds(),
          listStr = "";

        // 검색 결과 목록에 추가된 항목들을 제거합니다
        removeAllChildNods(listEl);

        // 지도에 표시되고 있는 마커를 제거합니다
        removeMarker();

        for (var i = 0; i < places.length; i++) {
          // 마커를 생성하고 지도에 표시합니다
          var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i),
            placeI = places[i],
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

          // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
          // LatLngBounds 객체에 좌표를 추가합니다
          bounds.extend(placePosition);

          // 마커와 검색결과 항목에 mouseover 했을때
          // 해당 장소에 인포윈도우에 장소명을 표시합니다
          // mouseout 했을 때는 인포윈도우를 닫습니다
          (function(marker, title) {
            kakao.maps.event.addListener(marker, "mouseover", function() {
              displayInfowindow(marker, title);
            });

            kakao.maps.event.addListener(marker, "mouseout", function() {
              infowindow.close();
            });

            kakao.maps.event.addListener(marker, "click", function() {
              // 마커 위에 인포윈도우를 표시합니다
              // 이동할 위도 경도 위치를 생성합니다 
              let sendToserverAddrInfo =
                title +
                "," +
                marker.getPosition().getLat() +
                "," +
                marker.getPosition().getLng() +
                "," +
                placeI.road_address_name +
                "," +
                placeI.address_name;
              sendAddr(sendToserverAddrInfo);
              var moveLatLon = new kakao.maps.LatLng(
                marker.getPosition().getLat(),
                marker.getPosition().getLng()
              );
              // 인포윈도우로 장소에 대한 설명을 표시합니다
              var infowindow1 = new kakao.maps.InfoWindow({
                content:
                  '<div style="width:150px;text-align:center;padding:6px 0;">새로운 직거래위치<br/><b>' +
                  title +
                  "</b></div>",
                removable: iwRemoveable,
              });
              infowindow1.open(map, marker);

              // 지도 중심을 이동 시킵니다
              map.setCenter(moveLatLon);
              // 지도를 1레벨 올립니다 (지도가 축소됩니다)
              map.setLevel(1);
            });
            itemEl.onmouseover = function() {
              displayInfowindow(marker, title);
              var moveLatLon = new kakao.maps.LatLng(
                marker.getPosition().getLat(),
                marker.getPosition().getLng()
              );
              // 지도 중심을 이동 시킵니다
              map.setCenter(moveLatLon);
            };

            itemEl.onmouseout = function() {
              infowindow.close();
            };

            itemEl.onclick = function() {
              let sendToserverAddrInfo =
                title +
                "," +
                marker.getPosition().getLat() +
                "," +
                marker.getPosition().getLng() +
                "," +
                placeI.road_address_name +
                "," +
                placeI.address_name;
              sendAddr(sendToserverAddrInfo);
              var moveLatLon = new kakao.maps.LatLng(
                marker.getPosition().getLat(),
                marker.getPosition().getLng()
              );
              // 지도 중심을 이동 시킵니다
              map.setCenter(moveLatLon);
            };
          })(marker, places[i].place_name);

          fragment.appendChild(itemEl);
        }

        // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
        listEl.appendChild(fragment);
        menuEl.scrollTop = 0;

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
        map.setBounds(bounds);

        if (!theFlag) {
          setFirstPlace();
        }
      }

      // 검색결과 항목을 Element로 반환하는 함수입니다
      function getListItem(index, places) {
        var el = document.createElement("li"),
          itemStr =
            '<span class="markerbg marker_' +
            (index + 1) +
            '"></span>' +
            '<div  style="padding-left: 42px;">' +
            "   <h5>" +
            places.place_name +
            "</h5>";

        if (places.road_address_name) {
          itemStr +=
            '    <span style="padding-left:26px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png) no-repeat;>">' +
            places.road_address_name +
            "</span>" +
            "   <span  " +
            places.address_name +
            "</span>";
        } else {
          itemStr +=
            '    <span style="padding-left:26px;background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png) no-repeat;>">' +
            places.address_name +
            "</span> ";
        }

        itemStr +=
          ' <br/> <span style="color:#009900;">' +
          places.phone +
          "</span>" +
          "</div>";

        el.innerHTML = itemStr;
        el.className = "item";

        return el;
      }

      // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
      function addMarker(position, idx, title) {
        var imageSrc =
            "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png", // 마커 이미지 url, 스프라이트 이미지를 씁니다
          imageSize = new kakao.maps.Size(36, 37), // 마커 이미지의 크기
          imgOptions = {
            spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin: new kakao.maps.Point(0, idx * 46 + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
          },
          markerImage = new kakao.maps.MarkerImage(
            imageSrc,
            imageSize,
            imgOptions
          ),
          marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage,
          });

        marker.setMap(map); // 지도 위에 마커를 표출합니다
        markers.push(marker); // 배열에 생성된 마커를 추가합니다

        return marker;
      }

      // 지도 위에 표시되고 있는 마커를 모두 제거합니다
      function removeMarker() {
        for (var i = 0; i < markers.length; i++) {
          markers[i].setMap(null);
        }
        markers = [];
      }

      // 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
      function displayPagination(pagination) {
        var paginationEl = document.getElementById("pagination"),
          fragment = document.createDocumentFragment(),
          i;

        // 기존에 추가된 페이지번호를 삭제합니다
        while (paginationEl.hasChildNodes()) {
          paginationEl.removeChild(paginationEl.lastChild);
        }

        for (i = 1; i <= pagination.last; i++) {
          var el = document.createElement("a");
          el.href = "#";
          el.innerHTML = i;

          if (i === pagination.current) {
            el.className = "on";
          } else {
            el.onclick = (function(i) {
              return function() {
                pagination.gotoPage(i);
              };
            })(i);
            document.getElementById("map").focus();
          }

          fragment.appendChild(el);
        }
        paginationEl.appendChild(fragment);
      }

      // 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
      // 인포윈도우에 장소명을 표시합니다
      function displayInfowindow(marker, title) {
        var content =
          '<div style="text-align: center;">' + " * " + title + "</div>";

        infowindow.setContent(content);
        infowindow.open(map, marker);
      }

      // 검색결과 목록의 자식 Element를 제거하는 함수입니다
      function removeAllChildNods(el) {
        while (el.hasChildNodes()) {
          el.removeChild(el.lastChild);
        }
      }
      this.isInOwnAddr = true;
    },
    deletePost() {
      axios
        .delete("http://i3a504.p.ssafy.io:8000/api/post/" + this.postId, {
          headers: {
            Authorization: this.$store.state.authorization,
          },
        })

        .then((response) => {
          console.log(response);
          let msg = "삭제 처리시 문제가 발생했습니다.";
          if (response.status == 200) {
            msg = "삭제가 완료되었습니다.";
            alert(msg);

            this.$router.push({ name: "MainPage" });
          } else {
            alert(msg);
          }
        });
    },
    getCateMid() {
      axios
        .get(
          "http://i3a504.p.ssafy.io:8000/api/post/category/category_mid/" +
            this.seletedCateBig
        )
        .then(({ data }) => {
          this.categoryMid = data;
        });
    },
    removeImageToServer(i) {
      let imgNames = this.onServerThumnailImgsUrl[i - 1].split("/");
      this.deleteImags.push(imgNames.pop());
      this.onServerThumnailImgsUrl.splice(i - 1, 1);
    },
    removeImage(i) {
      this.UploadImages.splice(i - 1, 1);
      this.thumnailImgsUrl.splice(i - 1, 1);
      this.StandbyImgs.splice(i - 1, 1);
    },
    onFileChange() {
      if (this.UploadImages.length > 10 || this.StandbyImgs.length > 10) {
        alert("이미지는 10개 이하 까지 올릴 수 있습니다.");
        this.StandbyImgs.pop();
      }

      // files
      for (let Standbyimg of this.StandbyImgs) {
        this.UploadImages.push(Standbyimg);
        this.thumnailImgsUrl.push(URL.createObjectURL(Standbyimg)); //현재 페이지에 임시로 띄우기 위한 url
      }
    },
    edit(index, item) {
      if (!this.editing) {
        this.editing = item;
        this.index = index;
      } else {
        this.editing = null;
        this.index = -1;
      }
    },
    filter(item, queryText, itemText) {
      if (item.header) return false;

      const hasValue = (val) => (val != null ? val : "");
      const text = hasValue(itemText);
      const query = hasValue(queryText);

      return (
        text
          .toString()
          .toLowerCase()
          .indexOf(query.toString().toLowerCase()) > -1
      );
    },
    createHandler() {
      // const token = window.sessionStorage.getItem("jwt-auth-token");
      let deal_type1 = 1;
      if (this.toggle_exclusive.length == 1) {
        deal_type1 = this.toggle_exclusive[0] == 0 ? 1 : 2;
      } else {
        deal_type1 = 3;
      }

      const querystring = require("querystring");

      // files + 그외 Post dto
      let formData = new FormData();

      for (let UploadImage of this.UploadImages) {
        formData.append("file", UploadImage);
      }

      formData.append("title", this.title);
      formData.append("contents", this.contents);
      formData.append("cate_mid_id", this.seletedCateMid);
      formData.append("user_id", this.$store.state.myProfile.userId); //임시
      formData.append("price", this.price);
      formData.append("deal_type", deal_type1);
      formData.append("deleteFiles", this.deleteImags);
      formData.append("addr", this.addr);
      //선택한 요일
      let sum = 0;
      for (let i in this.toggle_weekend) {
        sum += 1 << this.toggle_weekend[i];
      }
      formData.append("deal_weak", sum);

      let allTags = [];
      for (let t in this.model) {
        allTags.push(this.model[t].text);
      }

      axios
        .put(
          "http://i3a504.p.ssafy.io:8000/api/post/" +
            this.postId +
            "?" +
            querystring.stringify({ tag: allTags }),
          formData,
          {
            headers: {
              Authorization: this.$store.state.authorization,
              "Content-Type": "multipart/form-data",
            },
          }
        )
        .then((response) => {
          console.log(response);
          let msg = "수정 처리시 문제가 발생했습니다.";
          if (response.status == 200) {
            msg = "수정이 완료되었습니다.";
            alert(msg);
            this.$router.push(`/post/detail?post_id=${this.post_id}`)
          } else {
            alert(msg);
          }
        })
        .catch((error) => {
          console.log(error.response);
          if (!error.response && !error.status) {
            this.message = "서버가 응답하지 않습니다.";
          } else {
            switch (error.response.status) {
              case "401":
                this.message = "질문을 작성할 권한이 없습니다.";
                break;

              default:
                break;
            }
          }
        });
    },
  },
};
</script>
<style scoped>
.error-color {
  color: red;
}
.v-card.on-hover {
  opacity: 0.6;
  transition: opacity 0.3s ease-in-out;
}
</style>
