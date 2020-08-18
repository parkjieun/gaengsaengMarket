import Vue from 'vue'
import Vuex from 'vuex'
import http_user from '@/util/http-common'
import http_post from '@/util/http-post'
import router from '../router';
import httpChat from '@/util/http-chat'



Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    authorization:sessionStorage.getItem("authorization"),
    isAuthenticated: sessionStorage.getItem("isAuthenticated"),
    myProfile: sessionStorage.getItem("myProfile")?JSON.parse(sessionStorage.getItem("myProfile")):[],
    patner:{},
    items: [],
    item: {},
    replys: [],
    delivery:null,
    chatrooms:[],
  },
  getters: {
    config: (state) => ({headers: { Authorization: state.authorization }}),
    roomId: (state) => (nickName) => {
      return state.chatrooms.find(room => room.roomName == nickName)
    },
    items(state) {
      return state.items;
    },
    item(state) {
      return state.item;
    },
    replys(state) {
      return state.replys;
    },
    loggedIn(state){
      if(state.myProfile!=null && state.myProfile && state.myProfile!="" && state.myProfile!="null"){
        return true
      }
      return false
    }, 
    getN(state){
      // console.log(">>>>GETTERS")
      return state.myProfile
    },
  },
  mutations: {
    SET_USERPROFILE(state, value) {
      sessionStorage.setItem("myProfile",JSON.stringify(value))
      sessionStorage.setItem("isAuthenticated", true)
      state.isAuthenticated = true
      state.myProfile = value
    },
    SET_AUTH(state,value){
      sessionStorage.setItem("authorization",value)
      
      state.authorization = value
    },
    setPosts(state, payload) {
      state.items = payload;
    },

    setPost(state, payload) {
      state.item = payload;
    },
    setReplys(state, payload) {
      state.replys = payload;
    },
    setPatner(state,value){
      state.patner = value
    },
    LOGOUT(state){
      state.myProfile=""
      state.isAuthenticated=false
      state.authorization=""
      state.chatrooms=[]
      sessionStorage.removeItem("myProfile")
      sessionStorage.removeItem("isAuthenticated")
      sessionStorage.removeItem("authorization")
    },
    setPoint(state, value){
      // console.log("포인트 변환***"+ value);
      state.myProfile.pointVal -=  value;
      // console.log("포인트 변환222***"+ state.myProfile.pointVal);
},
    SET_DELIVERY(state,value){
      state.delivery=value
    },
    ADD_CHATROOM(state,value){
      state.chatrooms.push(value)
    }
  },

  actions: {
    setUserProfile( { commit, getters } ) {
      return http_user.get('/api/user', getters.config)
      .then((res) => {
        // console.log("내 정보", res)
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
    setPoint({commit},value){
      commit('setPoint',value)
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
    getReplys(context, payload) {
      http_post
        .get(payload)
        .then(({ data }) => {
           context.commit('setReplys', data);
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
    setPatner({commit}, userId){
      http_user.get("/api/user/"+userId).then(res=>{
        commit("setPatner",res.data)
      })
    },
    getMyProfile({commit , getters}){
      http_user.get("/api/user",getters.config).then(res=>{
        commit("SET_USERPROFILE",res.data)
      })
    },
    logout({commit}){
      commit("LOGOUT")
    },
    setDelivery({commit},value){
      commit("SET_DELIVERY",value)
    },
  findAllRoom(context) {
    context.state.chatrooms=[]
    httpChat.get('/api/chat/room', context.getters.config).then(response => {
        const data = response.data
        data.forEach(element => {
          httpChat.get('/api/chat/room/' + element.roomId, context.getters.config).then(res => {
              const joinData = res.data
              joinData.forEach(ele => {
                  if (ele.userId !== context.state.myProfile.userId) {
                      http_user.get('/api/user/' + ele.userId).then(res2 => {
                          element['roomName'] = res2.data.nickName
                          if ( res2.data.profileImg != null ) {
                            element['profileImg'] = "http://i3a504.p.ssafy.io/static/image/account/" + res2.data.profileImg
                          }
                          else {
                            element['profileImg'] = res2.data.profileImg
                          }
                          context.commit("ADD_CHATROOM",element)
                      })
                  }
              })

          })
      });
    });
},
  },
})
