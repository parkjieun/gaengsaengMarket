import Vue from 'vue'
import Vuex from 'vuex'
import http_user from '@/util/http-common'
import http_post from '@/util/http-post'
import router from '../router';



Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    authorization:sessionStorage.getItem("authorization"),
    myProfile: sessionStorage.getItem("myProfile"),
    isAuthenticated: sessionStorage.getItem("isAuthenticated"),
    items: [],
    item: {},
  },
  getters: {
    config: (state) => ({headers: { Authorization: state.authorization }}),
    items(state) {
      return state.items;
    },
    item(state) {
      return state.item;
    },
  },
  mutations: {
    SET_USERPROFILE(state, value) {
      sessionStorage.setItem("myProfile",value)
      state.myProfile = value
    },
    SET_AUTH(state,value){
      sessionStorage.setItem("authorization",value)
      sessionStorage.setItem('isAuthenticated', true)
      state.authorization = value
    },
    setPosts(state, payload) {
      state.items = payload;
    },

    setPost(state, payload) {
      state.item = payload;
    },
    
  },

  actions: {
    setUserProfile( { commit, getters } ) {
      return http_user.get('/api/user', getters.config)
      .then((res) => {
        console.log("내 정보", res)
        commit('SET_USERPROFILE', res.data)
      })
      .catch(err => console.log(err))
    },
    updateProfile( { commit, getters }, data) {
      return http_user.put('/api/user', data, getters.config)
      .then(() => {
        commit('SET_USERPROFILE', data)
        router.push({name: 'MyProfile'})
      })
    },
    setAuth({commit},value){
      commit('SET_AUTH',value)
    },
    getPosts(context) {
      http_post
        .get('/api/post') 
        .then(({ data }) => {
          context.commit('setPosts', data);
        })
        .catch(() => {
          alert('에러가 발생했습니다.');
        })
    },
    getPost(context, payload) {
      http_post
        .get(payload)
        .then(({ data }) => {
           context.commit('setPost', data);
        })
        .catch(() => {
          alert('에러가 발생했습니다.');
        })
    },
    deletePost(context, payload ) {
      http_post
      .delete(payload)
      .then(({ data }) => {
        let msg = '삭제 처리시 문제가 발생했습니다.';
        if (data === 'success') {
          msg = '삭제가 완료되었습니다.';
        }
        alert(msg);
      })
      .catch(() => {
        alert('삭제시 에러가 발생했습니다.');
      })
		},
  },
})
