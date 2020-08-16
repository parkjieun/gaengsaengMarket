<template>
<v-app>
    <div>
        <v-tabs class="tabs " hide-slider grow v-model="tab" background-color="#fff" style="">
            <!-- <v-tabs-slider></v-tabs-slider> -->

            <v-tab v-for="(i,index) in categoryBig" @click="goHome(i)" :key="index" :href="`#tab-${i.cate_big_id}`" style="width:60px;" @mouseover="inTab(index,i.cate_big_id)" @mouseout="outTab">
                <div style="font-weight:bold;" >
                    {{i.name}}
                </div>
            </v-tab>
            
            <v-tab-item v-if="index!==0" v-for="(i,index) in categoryBig" :key="index" :value="'tab-' + i.cate_big_id">
                <v-card v-if="c" @mouseover="inCard" class="elevation-2" @mouseleave="outCard" flat tile style="position:absolute;z-index: 91;height:300px;background-color: rgba( 255, 255, 255, 1 );width:100%;">
                    <div style="margin-top:35px;">
                        <v-row style="width:100%;" align="center">
                            <v-col v-for="(j,jndex) in categoryBig" :key="jndex" style="padding:0px;margin-left:0px;margin-right:0px;">
                                <div v-if="jndex==index">
                                    <v-card-text v-if="j==categoryBig.length" style="width:100%;text-align:center;padding:0px;border-left:solid rgba(0,0,0,0.2) 1px; ">
                                        <v-list-item v-for="(k,kndex) in categoryMid[j.cate_big_id]" :key="kndex" style="padding:0px;">
                                            <v-btn @click="$router.push({name:'CategoryPage',params:{categoryNum:k.cate_mid_id}}).catch(res=>{})" text style="margin:auto;" width="100%" height="40px">
                                                <div>
                                                    {{k.name}}
                                                </div>
                                            </v-btn>
                                        </v-list-item>
                                    </v-card-text>
                                    <v-card-text v-else style="width:100%;text-align:center;padding:0px;border-left:solid rgba(0,0,0,0.2) 1px;border-right:solid rgba(0,0,0,0.2) 1px; ">
                                        <v-list-item v-for="(k,kndex) in categoryMid[j.cate_big_id]" :key="kndex" style="padding:0px;">
                                            <v-btn @click="$router.push({name:'CategoryPage',params:{categoryNum:k.cate_mid_id}}).catch(res=>{})" text style="margin:auto;" width="100%" height="40px">
                                                <div>
                                                    {{k.name}}
                                                </div>
                                            </v-btn>
                                        </v-list-item>
                                    </v-card-text>
                                </div>
                            </v-col>
                        </v-row>
                    </div>
                </v-card>
            </v-tab-item>
        </v-tabs>
    </div>

</v-app>
</template>

<script>
import httpPost from "@/util/http-post"
export default {
    data() {
        return {
            tab: [],
            tabs: 0,
            a: false,
            b: false,
            c: false,
            categoryBig: null,
            categoryMid: [],
            category: "",
        };
    },
    created() {
        httpPost.get("/api/post/category/category_big").then(res => {
            const data = res.data
            this.tabs = data.length
            this.categoryBig = data
            this.categoryBig.unshift({
                name: "HOME"
            })

            data.forEach(element => {
                var temp = []
                httpPost.get("/api/post/category/category_mid/" + element.cate_big_id).then(res => {
                    this.categoryMid[element.cate_big_id] = res.data
                })
            });
        })

    },
    computed: {
        show() {
            return this.a || this.b
        },

    },
    methods: {
        inTab(value, id) {
            this.c = true
            this.tab = "tab-" + id
            this.a = true
        },
        out() {
            if (this.a == false && this.b == false) {
                this.tab = "tab-" + this.category
                this.c = false
            }
        },
        inCard() {
            this.b = true
        },
        o() {
            this.a = false
        },
        outCard() {
            this.b = false
            this.$nextTick(() => {
                setTimeout(this.out, 150)
            })
        },
        outTab() {
            this.a = false

            this.$nextTick(() => {
                setTimeout(this.out, 150)
            })
        },
        goHome(value) {

            if (value.name == "HOME") {
                this.$router.push("/").catch(res=>{})
            }
            else {
                this.$router.push({name:'BigCategoryPage', params:{bigCategoryNum: value.cate_big_id}})
            }
        },

    },
    watch: {
        '$route.params.categoryNum'(value) {
            if (!value) {
                this.category = 0
            } else {
                const cn = this.$route.params.categoryNum
                this.categoryMid.forEach(element => {
                    element.forEach(e => {
                        if (e.cate_mid_id == cn) {
                            this.category = e.cate_big_id
                        }
                    })
                })
            }
        }
    }
}
</script>

<style scoped>
.v-tab {
    color: rgba(0, 0, 0, 1) !important;
}

.v-tab--active {
    border-bottom: solid 2px #A6e3e9 !important;
    color: rgba(166, 227, 233, 1) !important;
}

.v-application--wrap {
    min-height: 100px !important;
}
</style>
