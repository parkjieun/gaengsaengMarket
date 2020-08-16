<template>
<div style="cursor:pointer;">
  <v-hover v-slot:default="{ hover }">
    
    <v-card tile width="250px;" height="350" style="position:relative; min-width:250px;max-width:250px;">
      <div v-if="post.type===1">
        <v-img
        :src="'http://i3a504.p.ssafy.io/static/image/post/'+post.files.split(',')[0]"
        height="200px"
        ><v-expand-transition>
            <div
              v-if="hover"

              class="d-flex transition-fast-in-fast-out black darken-2 v-card--reveal display-3 white--text"
              style="height: 100%;opacity:0.5;"
            >
            </div>
          </v-expand-transition></v-img>
      </div>
      <div v-else style="position: relateve;">
        
        <v-img
        :src="'http://i3a504.p.ssafy.io/static/image/post/'+post.files.split(',')[0]"
        height="200px" style="filter:brightness(50%);"
        ><v-expand-transition>
            <div
              v-if="hover"

              class="d-flex transition-fast-in-fast-out black darken-2 v-card--reveal display-3 white--text"
              style="height: 100%;opacity:0.5;"
            >
            </div>
          </v-expand-transition></v-img>
        <h4 style="position:absolute; top:90px; color:white; width:250px; background:black; padding:5px; opacity: 0.8;">판매완료</h4>
      </div>
        <v-divider></v-divider>
        <v-card-title class="py-1" style="text-overflow:ellipsis;white-space:nowrap;word-wrap:normal;width:100%;overflow:hidden;">
          <h5 style="width:100%;" class="card-title text-left">
            {{post.title}}
            </h5>
        </v-card-title >

        <v-card-subtitle class="py-2 text-left" style="height:24px;overflow:hidden;padding-bottom:5px; margin-bottum: 5px;">
           <v-icon x-small>mdi-currency-krw</v-icon><span style="color:#3f696e; font-weight:bold;">{{post.price | currency}}</span>
        </v-card-subtitle>
        <v-card-text class="text-left" style="padding-bottom:0px;padding-top:5px;">

         
          <v-chip style="margin-right:3px;" color="#defcfc" text-color="#517373" small v-for="tag in tags" :key="tag">
              {{ tag }}
            </v-chip>
        </v-card-text>
      <v-card-actions style="position:absolute; bottom: 0; width:250px;">
          <div>
          <v-icon color="red" small class="mx-2">mdi-heart</v-icon><span class="caption"> {{post.like_cnt}} </span>
          </div>
          <div style="width: 75px;"></div>
          <div >
            <v-chip class="my-1 mx-0" small label v-if="post.deal_type !== 2" color="#a6e3e9" style="color:#3f696e; font-weight:bold;">택배</v-chip>
            <v-chip class="my-1 mx-0" small label v-else style="color:#3f696e;">택배</v-chip>
            <v-chip class="my-1 mx-0" small label v-if="post.deal_type !== 1" color="#a6e3e9" style="color:#3f696e; font-weight:bold;">직거래</v-chip>
            <v-chip class="my-1 mx-0" small label v-else style="color:#3f696e;">직거래</v-chip>
          </div>
      </v-card-actions>
    </v-card>
    </v-hover>
</div>
</template>

<script>
export default {
    props: ['post'],
    data(){
      return{
        tags:[],
      }
    },
    created(){
      // console.log(`http://i3a504.p.ssafy.io:8000/api/static/image/+post.files.split(',')[0]`)
      
    },
    mounted(){
      this.tags = this.post.tags.split(",").slice(0,3)
    },
    filters: {
      currency: function (value) {
          var num = new Number(value);
          return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,")
      },
      watch:{
        post(val){
          console.log(this.tags)
        }
    },

  }
}
</script>

<style>
.card-title{
  text-overflow:ellipsis;
  white-space:nowrap;
  word-wrap:normal;
  width:100px;
  overflow:hidden;
}
</style>
