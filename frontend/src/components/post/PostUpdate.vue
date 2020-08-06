<template>
  <v-app>
    <v-container fluid>
      <ValidationObserver   v-slot="{ invalid }"  >
        <v-container>
          <v-col> 
            <v-layout row justify-center align-center><h1>상품 수정</h1></v-layout>
            <v-col> <v-divider></v-divider></v-col>
          </v-col>
        </v-container>
        <br />
        <br />
        <v-container
          ><v-col>
            <v-file-input multiple v-model="StandbyImgs" accept="image/*" enctype="multipart/form-data" @change="onFileChange()" label="상품 사진을 등록해 주세요" filled prepend-icon="mdi-camera"> </v-file-input>
          </v-col>
          <v-card v-show="UploadImages.length != 0 || onServerThumnailImgsUrl.length != 0">
            <v-container fluid
              ><v-row>
                <template v-for="i in UploadImages.length">
                  <v-col :key="`A-${i}`" cols="12" md="4">
                    <v-hover v-slot:default="{ hover }">
                      <v-card :elevation="hover ? 12 : 2" :class="{ 'on-hover': hover }">
                        <v-img :src="thumnailImgsUrl[i - 1]" :lazy-src="thumnailImgsUrl[i - 1]" @click="removeImage(i)" height="225px"> </v-img>
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
                      <v-card :elevation="hover ? 12 : 2" :class="{ 'on-hover': hover }">
                        <v-img :src="onServerThumnailImgsUrl[j - 1]" :lazy-src="onServerThumnailImgsUrl[j - 1]" @click="removeImageToServer(j)" height="225px"> </v-img>
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
              <ValidationProvider v-validate.persist ="'required|max:125'"> <v-text-field v-model="title" :rules="titleRules" :counter="125" required></v-text-field> </ValidationProvider
            ></v-col>
          </v-row>
        </v-container>
        <v-container>
          <v-row>
            <v-col cols="2"><h2>카테고리</h2> </v-col>
            <v-col cols="3">
              <ValidationProvider v-validate.persist ="'required'"> <v-select :items="categoryBig" item-text="name" item-value="cate_big_id" v-model="seletedCateBig" ref="cateBig" :rules="categoryRules" @change="getCateMid()" placeholder="카테고리를 선택 해주세요" outlined required></v-select> </ValidationProvider
            ></v-col>
            <v-col>
              <ValidationProvider v-validate.persist ="'required'"><v-select :items="categoryMid" item-text="name" item-value="cate_mid_id" v-model="seletedCateMid" ref="cateMid" :rules="categoryRules" :disabled="!seletedCateBig ? true : false" placeholder="세부 카테고리를 선택 해주세요" outlined required></v-select> </ValidationProvider>
            </v-col>
          </v-row>
        </v-container>
        <v-container>
          <v-row>
            <v-col cols="2"><h2>거래타입</h2></v-col>

            <ValidationProvider v-validate.persist ="'required'" v-slot="{ errors }"
              ><v-col cols="1">
                <v-btn-toggle v-model="toggle_exclusive" color="#00bcd4" multiple group>
                  <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;" color="#00bcd4">택배거래</v-btn>
                  <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;" color="#00bcd4">직거래</v-btn>
                </v-btn-toggle>
              </v-col>
              <v-col
                ><span class="error-color">{{ errors[0] }}</span></v-col
              >
            </ValidationProvider>
          </v-row>
        </v-container>
        <v-container>
          <v-row v-show="toggle_exclusive.includes(1)">
            <v-col cols="2"><h2>거래 가능 요일</h2></v-col>
            <ValidationProvider v-validate.persist ="'required'" v-slot="{ errors }">
              <v-col cols="1">
                <v-btn-toggle v-model="toggle_weekend" multiple group>
                  <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;">월</v-btn>
                  <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;">화</v-btn>
                  <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;">수</v-btn>
                  <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;">목</v-btn>
                  <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;">금</v-btn>
                  <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;">토</v-btn>
                  <v-btn style="border: 1px solid #00bcd4; color: #00bcd4;">일</v-btn>
                </v-btn-toggle>
              </v-col>
              <v-col
                ><span class="error-color">{{ errors[0] }}</span></v-col
              >
            </ValidationProvider>
          </v-row>
        </v-container>
        <br />
        <v-form fluid>
          <v-container>
            <v-row>
              <v-col cols="2"><h2>가격</h2></v-col>
              <ValidationProvider v-validate.persist ="'required|min:0'" v-slot="{ errors }">
                <v-col><v-text-field type="number" :rules="priceRules" v-model="price" label="가격" placeholder="가격을 입력하세요" sufix="" outlined></v-text-field> </v-col>
                <v-col
                  ><span class="error-color">{{ errors[0] }}</span></v-col
                >
              </ValidationProvider> </v-row
            ><v-col> <v-divider></v-divider></v-col>
          </v-container>
        </v-form>

        <v-form fluid>
          <v-container>
            <h2>내용</h2>
            <br />
            <v-container>
              <ValidationProvider v-validate.persist ="'required|min:0'" v-slot="{ errors }">
                <v-textarea v-model="contents" required outlined placeholder="내용을 입력해 주세요."></v-textarea>
                <v-col
                  ><span class="error-color">{{ errors[0] }}</span></v-col
                >
              </ValidationProvider>
            </v-container>
            <v-col> <v-divider></v-divider></v-col>
          </v-container>
        </v-form>

        <v-container>
          <h2>태그</h2>
          <br />
          <v-col>
            <v-combobox v-model="model" :filter="filter" :hide-no-data="!search" :items="items" :search-input.sync="search" hide-selected label="Search for an option" multiple small-chips solo>
              <template v-slot:selection="{ attrs, item, parent, selected }">
                <v-chip v-if="item === Object(item)" v-bind="attrs" :color="`${item.color} `" :input-value="selected" label small>
                  <span class="pr-2">
                    {{ item.text }}
                  </span>
                  <v-icon small @click="parent.selectItem(item)">x</v-icon>
                </v-chip>
              </template>
              <template v-slot:item="{ index, item }">
                <v-text-field v-if="editing === item" v-model="editing.text" autofocus flat background-color="transparent" hide-details solo @keyup.enter="edit(index, item)"></v-text-field>
                <v-chip v-else :color="`${item.color}  `" dark label small>
                  {{ item.text }}
                </v-chip>
                <v-spacer></v-spacer>
                <v-list-item-action @click.stop>
                  <v-btn icon @click.stop.prevent="edit(index, item)">
                    <v-icon>{{ editing !== item ? "mdi-pencil" : "mdi-check" }}</v-icon>
                  </v-btn>
                </v-list-item-action>
              </template>
            </v-combobox>
          </v-col>
          <v-col> <v-divider></v-divider></v-col>
        </v-container>

        <v-form fluid>
          <v-container
            ><v-layout row justify-center align-center>
              <v-btn :disabled="invalid" color="cyan" @click="createHandler()">
                수정
              </v-btn>
              <v-btn @click="deletePost()" color="#f96d80">
                삭제
              </v-btn>
            </v-layout>
          </v-container>
        </v-form>
      </ValidationObserver>
    </v-container>
  </v-app>
</template>

<script>
import axios from "axios";
import { ValidationObserver, ValidationProvider, extend } from "vee-validate";
import { min, max, required } from "vee-validate/dist/rules";

extend("required", {
  ...required,
  message: " * 반드시 입력해야하는 항목입니다.",
});
extend("min", {
  ...min,
  message: "{length}글자 이상으로 입력해주세요.",
});
extend("max", {
  ...max,
  message: "{length}글자 이하로 입력해주세요.",
});
export default {
  components: {
    ValidationProvider,
    ValidationObserver,
  },
  data: () => ({
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
    titleRules: [(v) => !!v || "제목을 입력해 주세요", (v) => (v && v.length <= 125) || "제목은 125자 이하여야 합니다"],
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
    items: [{ header: "추가할 태그를 입력하세요" }],
    nonce: 1,
    menu: false,
    model: [],
    search: null,
    y: 0,
    inputTags: [],
  }),
  created() {
    axios.get("http://i3a504.p.ssafy.io:8000/api/post/category/category_big").then(({ data }) => {
      this.categoryBig = data;
    });

    this.postId = this.$route.params.post_id; //임시
  //  this.postId = 10036; //임시
    console.log("현재 수정페이지...포스트아이디 넘어오나" + this.postId + " / " + `${this.$route.query.post_id}`);
    axios.get("http://i3a504.p.ssafy.io:8000/api/post/" + this.postId).then(({ data }) => {
      console.log("오는 전체데이터");
      console.log(data);

      this.seletedCateBig = Number(data.cate_big_id);
      this.getCateMid();
      this.seletedCateMid = data.cate_mid_id;
      this.title = data.title;
      this.contents = data.contents;
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
        this.onServerThumnailImgsUrl.push("http://i3a504.p.ssafy.io:8000/api/static/image/" + img);
      }
      this.price = data.price;
      this.userId = data.user_id;
      let splitTags = data.tags.split(",");
      for (let i in splitTags) {
        this.inputTags.push(splitTags[i]);
        this.model.push({ text: splitTags[i], color: this.colors[i] });
        this.items.push({ text: splitTags[i], color: this.colors[i] });
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
          } else {
            alert(msg);
          }
        });
    },
    getCateMid() {
      axios.get("http://i3a504.p.ssafy.io:8000/api/post/category/category_mid/" + this.seletedCateBig).then(({ data }) => {
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
        .put("http://i3a504.p.ssafy.io:8000/api/post/" + this.postId + "?" + querystring.stringify({ tag: allTags }), formData, {
          headers: {
            Authorization: this.$store.state.authorization,
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          console.log(response);
          let msg = "수정 처리시 문제가 발생했습니다.";
          if (response.status == 200) {
            msg = "수정이 완료되었습니다.";
            alert(msg);
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
