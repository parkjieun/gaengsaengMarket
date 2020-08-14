<template>
<v-app>
    <v-dialog v-model="dealdialog" max-width="20rem" >
      <v-card>
        <!-- <v-card-title style="padding:1rem;font-size:0.7rem;">
          <h2>거래방법 선택</h2>
        </v-card-title> -->
        <v-card-text style="padding:0px">
          <div v-if="item.deal_type == 1 || item.deal_type == 3">
            <v-divider></v-divider>
            <v-btn color="#2d3753"  style="width:100%;font-size:20px !important;height:5.5rem;font-weight:550" text @click="goDelivery()">
              택배거래 <img :src="require(`@/assets/post/delivery.png`)" style="width:35px">
            </v-btn>
            <!-- <v-btn color="green darken-1"  style="width:100%;font-size:20px !important;height:80px;font-weight:550" text @click.native="dealdialog = false"  @click="goDelivery()">택배거래</v-btn> -->
          </div>
        
          <div v-if="item.deal_type == 2 || item.deal_type == 3">
            <v-divider></v-divider>
            <v-btn color="#2d3753" style="width:100%;font-size:20px !important;height:5.5rem; font-weight:550" text @click="goDirect()">
              직거래 <img :src="require(`@/assets/post/handshake.png`)" style="width:35px">
            </v-btn>
          </div>

          <!-- <button @click="$emit('update:dealdialog', !dealdialog)">Close</button> -->
        </v-card-text>
      </v-card>
    </v-dialog>

    <!-- <post-deliveryform :deliverydialog="deliverydialog"  @closeForm1="deliverydialog=false"/>  -->
    <post-deliveryform :deliverydialog="deliverydialog"  @closeForm1="typeUpdate"/>
    <post-directform :directdialog="directdialog" :addr="item.addr | arrange" @closeForm2="typeUpdate"/>
</v-app>
</template>

<script>
import { mapState, mapGetters } from 'vuex';

import PostDirectform from "@/components/post/PostDirectform.vue"
import PostDeliveryform from "@/components/post/PostDeliveryform.vue"

export default {
  name:"PostDealForm",
  props: ['dealdialog'],
    computed: {
    ...mapGetters(['item']),
    ...mapState(['myProfile']),
  },
  methods: {
    goDelivery(){
       this.$emit('closeForm')
       this.deliverydialog = true;
    },
    goDirect(){
      console.log("###직거래 열기###");
      //this.$emit('closeForm')
      this.directdialog = true;
    },
    // closeForm1() {
    //   this.deliverydialog = false;
    // },
    typeUpdate(result) {
      if(result != undefined){
        console.log("close2");
        console.log("direct::::::::"+ result);
        this.directdialog = false;
        this.deliverydialog = false;
        this.$emit('closeForm',result);
      }
    },
  },
  data () {
    return {
        directdialog: false,
        deliverydialog: false,
      }
 },
  components: {
    PostDirectform,
    PostDeliveryform,
  },
  watch: {
      dealdialog(val) {
          val || this.$emit("closeForm")
      },
  },
  filters: {
    arrange (val) {
      if(val != null){
        let arr = val.split(",")
        console.log("filterssssssss"+arr[3] +" " +arr[0]);
        return arr[3] +" " +arr[0]
      }
    }
  },
}
</script>

<style scoped>
</style>