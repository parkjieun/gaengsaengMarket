<template>
<v-app id="inspire">
    <v-btn color="rgba(166,227,233,1)" small fixed top left fab @click="goChat">
        <v-icon>mdi-arrow-left</v-icon>
    </v-btn>
    <v-row justify="center">
        <v-card>
            <v-card-title class="text-center headline">
                <div style="margin:auto;">갱생마켓 배송 조회</div>
            </v-card-title>
            <v-divider></v-divider>
            <v-card-subtitle>
                <v-row>
                    <v-col>
                        운송장 번호
                    </v-col>
                    <v-col>
                        {{delivery.invoiceNo}}
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        품목
                    </v-col>
                    <v-col>
                        {{delivery.itemName}}
                    </v-col>
                </v-row>
            </v-card-subtitle>

            <v-divider></v-divider>
            <v-card-text>
                <v-spacer></v-spacer>

                <table class="table table-striped table-condensed">
                    <colgroup>
                        <col :style="{ width: '40%' }" />
                        <col :style="{ width: '30%' }" />
                        <col :style="{ width: '30%' }" />

                    </colgroup>
                    <tr class="posttitle">
                            <th>시간</th>
                            <th>현재 위치</th>
                            <th>배송 상태</th>
                    </tr>
                    <tr class="text-center" v-for="(item,index) in delivery.trackingDetails" :key="index">
                            <td>{{item.timeString}}</td>
                            <td>{{item.where}}</td>
                            <td>{{item.kind}}</td>
                        </tr>
                </table>

                <v-spacer></v-spacer>
            </v-card-text>
        </v-card>
    </v-row>
</v-app>
</template>

<script>
import {
    mapState
} from "vuex"
export default {

    created() {
        console.log(this.delivery)
    },
    computed: {
        ...mapState(['delivery'])
    },
    methods:{
        goChat(){
            history.go(-1)
        }
    },
    watch: {

    }
}
</script>

<style scoped>
.posttitle {
  background: rgb(255, 189, 66);
}

</style>
