<template>
  <v-app>
    <v-container fluid>
      <v-container>
        <v-col>
          <h1>상품 등록</h1>
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
            @change="onFileChange()"
            label="상품 사진을 등록해 주세요"
            filled
            prepend-icon="mdi-camera"
          >
          </v-file-input>
        </v-col>
        <v-card v-show="UploadImages.length != 0">
          <v-container fluid
            ><v-row>
              <template v-for="i in UploadImages.length">
                <v-col :key="i" cols="12" md="4">
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
                      <div class="align-self">
                        <v-icon color="red darken-2">mdi-close</v-icon>
                      </div>
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
              label="제목을 입력해주세요"
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
                style="border: 1px solid #00bcd4 ; color:#00bcd4"
                color="#00bcd4"
                >택배거래</v-btn
              >
              <v-btn
                style="border: 1px solid #00bcd4 ; color:#00bcd4"
                color="#00bcd4"
                >직거래</v-btn
              >
            </v-btn-toggle>
          </v-col>
        </v-row>
      </v-container>
      <br />
      <v-form v-model="valid" fluid>
        <v-container>
          <v-row>
            <v-col cols="2"><h2>가격</h2></v-col>
            <v-col
              ><v-text-field
                v-model="price"
                label="가격"
                placeholder="가격을 입력하세요"
                outlined
              ></v-text-field>
            </v-col> </v-row
          ><v-col> <v-divider></v-divider></v-col>
        </v-container>
      </v-form>

      <v-form v-model="valid" fluid>
        <v-container>
          <h2>내용</h2>
          <br />
          <v-container>
            <v-textarea
              v-model="contents"
              :rules="contentsRules"
              required
              outlined
              placeholder="내용을 입력해 주세요."
              @change="test()"
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
            v-model="model"
            :filter="filter"
            :hide-no-data="!search"
            :items="items"
            :search-input.sync="search"
            hide-selected
            label="Search for an option"
            multiple
            small-chips
            solo
          >
            <template v-slot:selection="{ attrs, item, parent, selected }">
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
                <v-icon small @click="parent.selectItem(item)">x</v-icon>
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
                placeholder="태그를 입력해주세요."
                @keyup.enter="edit(index, item)"
              ></v-text-field>
              <v-chip v-else :color="`${item.color}  `" dark label small>
                {{ item.text }}
              </v-chip>
              <v-spacer></v-spacer>
              <v-list-item-action @click.stop>
                <v-btn icon @click.stop.prevent="edit(index, item)">
                  <v-icon>{{
                    editing !== item ? "mdi-pencil" : "mdi-check"
                  }}</v-icon>
                </v-btn>
              </v-list-item-action>
            </template>
          </v-combobox>
        </v-col>
        <v-col> <v-divider></v-divider></v-col>
      </v-container>

      <v-form v-model="valid" fluid>
        <v-container>
          <v-btn :disabled="!valid" color="cyan" @click="createHandler()">
            등록
          </v-btn>
        </v-container>
      </v-form>
    </v-container>
  </v-app>
</template>

<script>
import axios from "axios";
export default {
  data: () => ({
    price: "",
    StandbyImgs: [], //파일 선택시 고르는 이미지들
    thumnailImgsUrl: [], //선택한 이미지들을 미리보기 보여주는 이미지의 url
    UploadImages: [], //서버에 보낼 최종적인 이미지 리스트
    userImage: "",
    toggle_exclusive: [0, 1],
    valid: false,
    title: "",
    contents: "",
    titleRules: [
      (v) => !!v || "제목을 입력해 주세요",
      (v) => (v && v.length <= 125) || "제목은 125자 이하여야 합니다",
    ],
    contentsRules: [(v) => !!v || "내용을 입력해 주세요"],
    categoryRules: [(v) => !!v || "카테고리를 선택해 주세요"],
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
    items: [{ header: "추가할 태그를 입력하세요" }],
    nonce: 1,
    menu: false,
    model: [],
    x: 0,
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

          this.items.push(v);
          this.inputTags.push(v.text);
          this.nonce++;
        }

        return v;
      });
    },
  },

  methods: {
    test() {
      console.log(this.contents);
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
    removeImage(i) {
      this.UploadImages.splice(i - 1, 1);
      this.thumnailImgsUrl.splice(i - 1, 1);
      this.StandbyImgs.splice(i - 1, 1);
    },
    onFileChange() {
      if (this.UploadImages.length >= 10 || this.StandbyImgs.length > 10) {
        alert("이미지는 10개 이하 까지 올릴 수 있습니다.");
        this.StandbyImgs = [];
      } else if (
        this.UploadImages.length <= 10 &&
        this.StandbyImgs.length <= 10
      ) {
        for (let Standbyimg of this.StandbyImgs) {
          this.UploadImages.push(Standbyimg);
          this.thumnailImgsUrl.push(URL.createObjectURL(Standbyimg)); //현재 페이지에 임시로 띄우기 위한 url
          console.log("서버에 들어갈 이미지:" + Standbyimg);
        }
        console.log(this.UploadImages);
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
      console.log("등록 버튼 클릭후:" + this.UploadImages);
      axios
        .post(
          "http://i3a504.p.ssafy.io:8000/api/post?" +
            querystring.stringify({ tag: this.inputTags }),
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
          let msg = "등록 처리시 문제가 발생했습니다.";
          if (response.status == 200) {
            msg = "등록이 완료되었습니다.";
            alert(msg);

            this.$router.push({ name: "MainPage" });
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
.v-card.on-hover {
  opacity: 0.6;
  transition: opacity 0.3s ease-in-out;
}

.show-btns {
  color: rgb(97, 74, 74) !important;
}
.align-self {
  display: baseline;
}
/* .v-btn.btn_style_dealType{
  border:10px solid red; 
  color: blue;
} */
</style>
