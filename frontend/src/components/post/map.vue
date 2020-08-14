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
  <v-container fluid>
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
            <v-text-field hide-details v-model="searchKeyword"></v-text-field>
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
  </v-container>
</template>
<script>
export default {
  props: ["propsdata"],
  data: () => ({
    searchKeyword: "이태원역",
    addr: "",
    isInOwnAddr: false,
    pageMap: "",
    pageInfowindow: "",
  }),
  mounted() {
   

    /* this.initMap();
    var container = document.getElementById("map");
    console.log("맵 잘 띄웡ㅆ니 : " + container);
    var options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667),
      level: 3,
    };

    this.pageMap = new kakao.maps.Map(container, options); 
    this.pageMap.relayout() 
    //this.initMap();*/
  },
  methods: {
    sendAddr(title) {
      this.$emit("addr", title);
    },
    initMap() {
      console.log("부모에서 propsdata 잘 받아오니? " + this.propsdata);

      var container = document.getElementById("map");
      console.log("맵 잘 띄웡ㅆ니 : " + container);
      var options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3,
      };

      this.pageMap = new kakao.maps.Map(container, options);
      this.pageMap.relayout();
      //this.initMap();

      // 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
      var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
      this.pageInfowindow = infowindow;
      //[0] : title (검색 이름), [1]:위도 [2]:경도 [3]:지번(3번 아직 미추가)
      var addrInfos;
      //디비에 주소가 아무것도 저장이 안되어있다면? 혹은 불러오지 못해서 값이 아예없다면?
      if (this.propsdata == "" || this.propsdata == null) {
        console.log("아무것도 안들어왔대요");
        addrInfos = ["멀티캠퍼스 역삼", 37.5012860931305, 127.03960466386198];
      } else {
        addrInfos = this.propsdata.split(","); //디비에 저장된 정보들 받아오는 변수
      }
      this.searchKeyword = addrInfos[0];
      this.searchPlaces();
      this.isInOwnAddr = true;
      this.pageMap.relayout();
    },

    // 키워드 검색을 요청하는 함수입니다
    searchPlaces() {
      var markers = [];
      var sendAddr = this.sendAddr;
      var keyword = this.searchKeyword;
      var map = this.pageMap;
      var infowindow = this.pageInfowindow;
      var theFlag = this.isInOwnAddr;
      var addrInfos = this.propsdata.split(","); //디비에 저장된 정보들 받아오는 변수
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
        map.setCenter(moveLatLon);
        map.setLevel(1);
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
              console.log(
                "click..:" + placeI.road_address_name + placeI.address_name
              );
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
    },
  },
};
</script>
