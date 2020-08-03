<template>
  <v-row  id="search">
    <v-col cols="11" sm="6" lg="4">
      <v-row >
        <v-text-field label="Search" color="#a6e3e9" single-line placeholder="상품을 검색해주세요." v-model="searchText"></v-text-field>
        <v-btn icon><v-icon color="#a6e3e9">mdi-magnify</v-icon></v-btn>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
export default {
  data() {
    return {
      searchText: '',

    }
  },

}
</script>

<style>
#search {
  justify-content: center;
  align-items: center;
}
.v-btn {
  margin-top: 10px;
}
</style>