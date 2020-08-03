<template>
<v-app>
<v-menu min-width="25%" v-model="menu" :position-x="x" :position-y="y" rounded="0" offset-y>
    <v-list>
        <v-list-item v-for="(item, index) in items" :key="index">
            <v-menu offset-x open-on-hover :position-x="x" :position-y="y">
                <template v-slot:activator="{ on, attrs }">
                <v-btn
                    text
                    v-bind="attrs"
                    v-on="on"
                    width="100%"
                >
                    {{item.title}}
                </v-btn>
                </template>
        
                <v-list>
                <v-list-item
                    v-for="(item, index) in item.items"
                    :key="index"
                    @click="menu=false"
                >
                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                </v-list-item>
                </v-list>
            </v-menu>
            <!-- <v-list-item-title>{{ item.title }}</v-list-item-title> -->
        </v-list-item>
    </v-list>
</v-menu>
</v-app>
</template>

<script>
export default {
    props: ['menu', 'x', 'y'],
    data() {
        return {
            bigCategory: [],
            midCategory: [],

            items: [      
                { title: 'Click Me' ,items:[{title:"sub2"},{title:"sub2"},{title:"sub2"},{title:"sub2"}]},
      { title: 'Click Me' ,items:[{title:"sub2"},{title:"sub2"},{title:"sub2"},{title:"sub2"}]},
      { title: 'Click Me' ,items:[{title:"sub2"},{title:"sub2"},{title:"sub2"},{title:"sub2"}]},
      { title: 'Click Me 2' ,items:[{title:"sub2"},{title:"sub2"},{title:"sub2"},{title:"sub2"}]},
      ],
        }
    },
    watch: {
        menu(val) {
            val || this.$emit("closeMenu")
        }
    }
}
</script>

<style>

</style>
