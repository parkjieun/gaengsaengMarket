<template>
        <v-navigation-drawer floating temporary v-model="category">
            <v-list nav dense>
                <div v-for="(link, i) in links" :key="i">
                    <v-list-item v-if="!link.subLinks" :to="link.to" avatar class="v-list-item">
                        <v-list-item-icon>
                            <v-icon>{{ link.icon }}</v-icon>
                        </v-list-item-icon>

                        <v-list-item-title v-text="link.text" />
                    </v-list-item>

                    <v-list-group v-else :key="link.text" no-action :prepend-icon="link.icon" :value="false">
                        <template v-slot:activator>
                            <v-list-item-title>{{ link.text }}</v-list-item-title>
                        </template>

                        <v-list-item v-for="sublink in link.subLinks" :to="sublink.to" :key="sublink.text">
                            <v-list-item-icon>
                                <v-icon>{{ sublink.icon }}</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>{{ sublink.text }}</v-list-item-title>

                        </v-list-item>

                    </v-list-group>

                </div>

            </v-list>

        </v-navigation-drawer>

</template>

<script>
export default {
    props: ['category', ],
    watch: {
        category: function () {
            this.$emit("category", this.category)
        }
    },
    data() {
        return {
            links: [{
                    to: '/category1',
                    icon: 'mdi-view-dashboard',
                    text: 'Category1',
                },
                {
                    icon: 'mdi-folder',
                    text: 'Category2',
                    subLinks: [{
                            text: 'Sub Category1',
                            to: '/category2/subcategory1',
                            icon: 'mdi-view-list'
                        },
                        {
                            text: 'Sub Category2',
                            to: '/category2/subcategory2',
                            icon: 'mdi-plus'
                        },
                    ]
                },
                {
                    icon: 'mdi-application',
                    text: 'Category3',
                    subLinks: [{
                            text: 'SubCategory1',
                            to: '/category3/subcategory1',
                            icon: 'mdi-view-list'
                        },
                        {
                            text: 'Sub Category2',
                            to: '/category3/subcategory2',
                            icon: 'mdi-plus'
                        },
                    ]
                },
            ]
        }
    },

}
</script>

<style scoped lang="scss">

</style>
