<template>
  <div>
        <h2>post 목록</h2>
        <table>
            <colgroup>
                <col :style="{width: '10%'}" />
            </colgroup>
            <tr><td>title</td></tr>
            <tr v-for="(post, index) in items" :key="index + '_items'">
                <td><router-link :to="'/post/detail?post_id=' + post.post_id">{{post.title}}</router-link></td>
            </tr>
        </table>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
    name:'post-list',
    created() {
        this.$store.dispatch('getPosts');
    },
     computed: {
     ...mapGetters(['items']),
    },
    methods:{
    },
    data(){
        return{
        }
    }, 

}
</script>

<style>

</style>