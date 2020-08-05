<template>
<div>
  <v-hover v-slot:default="{ hover }">
    <v-card tile width="250" >
        <v-img
        :src="'http://i3a504.p.ssafy.io:8000/api/static/image/'+post.files.split(',')[0]"
        height="200px"
        ><v-expand-transition>
            <div
              v-if="hover"

              class="d-flex transition-fast-in-fast-out black darken-2 v-card--reveal display-3 white--text"
              style="height: 100%;opacity:0.5;"
            >
            </div>
          </v-expand-transition></v-img>
        <v-divider></v-divider>
        <v-card-title style="text-overflow:ellipsis;white-space:nowrap;word-wrap:normal;width:100%;overflow:hidden;">
          <div style="width:100%;" class="card-title">
            {{post.title}}
            </div>
        </v-card-title >

        <v-card-subtitle class="text-left" style="height:24px;overflow:hidden;padding-bottom:0px;">

            <v-chip style="margin-right:3px;" small v-for="tag in tags" :key="tag">
              {{ tag }}
            </v-chip>
        </v-card-subtitle>
        <v-card-text class="text-right" style="padding-bottom:0px;padding-top:5px;">

          {{post.price | currency}}
        </v-card-text>
        
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn  icon>
          <v-icon>mdi-heart</v-icon>
        </v-btn>
        <v-btn icon>
          <v-icon>mdi-share-variant</v-icon>
        </v-btn>
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
      console.log(`http://i3a504.p.ssafy.io:8000/api/static/image/+post.files.split(',')[0]`)
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
      }
  },
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
