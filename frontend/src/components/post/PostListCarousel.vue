<template>
  <div class="contain" style="position:relative;">

  
  <v-icon fab text @click="carouselLeft" style="position:absolute; bottom:120px; left: -30px; z-index:99;">mdi-chevron-left</v-icon>
  <v-icon fab text @click="carouselRight" style="position:absolute; bottom:120px; right: -30px; z-index:99;">mdi-chevron-right</v-icon>
  <div class="row" id="carousel">
    <div class="row__inner">
    
      <div class="tile" v-for="i in len" :key="i"  @click="goDetail(i)">
        <div class="tile__media">
          <img class="tile__img" :src="'http://i3a504.p.ssafy.io/static/image/post/'+posts[i-1].files.split(',')[0]" alt="" />
        </div>
        <div class="tile__details">
          <div class="tile__title">
            <div>
              {{posts[i-1].title}}<br>
              <v-icon x-small color="white">mdi-currency-krw</v-icon>{{posts[i-1].price | currency}}
            </div>
            <div>
            <v-icon color="red" small class="mx-2">mdi-heart</v-icon><span> {{posts[i-1].like_cnt}} </span>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
  <v-btn
      id="scrollButton"
      fab
      small
      color="#a6e3e9"
      retain-focus-on-click
      @click="scrollToTop"
  >
          <v-icon color="#3f696e">mdi-chevron-up</v-icon>
  </v-btn>
</div>
</template>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script>

import PostItemCarousel from '@/components/post/PostItemCarousel.vue'

export default {
    props: ['posts', 'subTitle'],
    data(){
        return{
          defaultImg: ''
            
        }
    },
    components: {
        PostItemCarousel,
    },
    computed:{
        len(){
            if(this.posts!==null){
                return this.posts.length
            }
            return 0
        }
    },
    methods:{
        goDetail(i){
            console.log(i)
            this.$router.push(`/post/detail?post_id=${this.posts[i-1].post_id}`)
        },
        scrollToTop() {
            window.scroll({top: 0, left: 0, behavior: 'smooth'})
        },
        carouselRight() {
          $("#carousel.row").animate({scrollLeft: '+=800'}, 400);

          
        },
        carouselLeft() {
          $("#carousel.row").animate({scrollLeft: '-=800'}, 400);
        }

    },
    filters: {
      currency: function (value) {
          var num = new Number(value);
          return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,")
      },
    },
}
</script>

<style scoped>
#scrollButton {
    position: fixed;
    bottom: 20px;
    right: 30px;
    z-index: 99;
}
body,
html {
  padding: 0 10px;
  margin: 0;
  background: #0e0f11;
  color: #ecf0f1;
  font-family: 'Open Sans', sans-serif;
  min-height: 100vh;
  display: -webkit-box;
  display: flex;
  -webkit-box-orient: horizontal;
  -webkit-box-direction: normal;
          flex-direction: row;
  -webkit-box-align: center;
          align-items: center;
  width: 100%;
}
* {
  box-sizing: border-box;
}
h3 {
  margin: 10px 0;
}
a:link,
a:hover,
a:active,
a:visited {
  -webkit-transition: color 150ms;
  transition: color 150ms;
  color: #95a5a6;
  text-decoration: none;
}
a:hover {
  color: #7f8c8d;
  text-decoration: underline;
}
.contain {
  width: 100%;
}
#carousel.row {
  overflow: hidden;
  width: 100%;
}
.row__inner {
  -webkit-transition: 450ms -webkit-transform;
  transition: 450ms -webkit-transform;
  transition: 450ms transform;
  transition: 450ms transform, 450ms -webkit-transform;
  font-size: 0;
  white-space: nowrap;
  margin: 40.3125px 0;
  padding-bottom: 10px;
}
.tile {
  position: relative;
  display: inline-block;
  width: 250px;
  padding-left:30px;
  padding-right: 30px;
  height: 140.625px;
  margin-left: 5px;
  margin-right: 5px;
  font-size: 20px;
  cursor: pointer;
  -webkit-transition: 450ms all;
  transition: 450ms all;
  -webkit-transform-origin: center left;
          transform-origin: center left;
}
.tile__img {
  width: 250px;
  height: 140.625px;
  -o-object-fit: cover;
     object-fit: cover;
}
.tile__details {
  position: absolute;
  width: 250px;
  /* padding-left:30px; */
  /* margin-left: 30px; */
  bottom: 0;
  left: 30px;
  right: 0;
  top: 0;
  font-size: 10px;
  opacity: 0;
  background: -webkit-gradient(linear, left bottom, left top, from(rgba(0,0,0,0.9)), to(rgba(0,0,0,0)));
  background: linear-gradient(to top, rgba(0,0,0,0.9) 0%, rgba(0,0,0,0) 100%);
  -webkit-transition: 450ms opacity;
  transition: 450ms opacity;
}
.tile__details:after,
.tile__details:before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  display: #000;
}


.tile:hover .tile__details {
  /* padding-left: 30px; */
  opacity: 1;
}
.tile__title {
  width:100%;
  color: white;
  position: absolute;
  bottom: 0;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.row__inner:hover {
  -webkit-transform: translate3d(-62.5px, 0, 0);
          transform: translate3d(-62.5px, 0, 0);
}
.row__inner:hover .tile {
  opacity: 0.3;
}
.row__inner:hover .tile:hover {
  -webkit-transform: scale(1.5);
          transform: scale(1.5);
  opacity: 1;
}
.tile:hover ~ .tile {
  -webkit-transform: translate3d(125px, 0, 0);
          transform: translate3d(125px, 0, 0);
}


</style>