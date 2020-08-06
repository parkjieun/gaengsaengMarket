import Vue from 'vue'
import VueRouter from 'vue-router'


import MyProfile from '@/components/user/MyProfile.vue'
import UserProfile from '@/components/user/UserProfile.vue'
import UserProfileUpdate from '@/components/user/UserProfileUpdate.vue'
import Layout from "@/components/layout/Layout.vue"
import MainPage from "@/components/MainPage.vue"
import PostCreate from '../components/post/PostCreate.vue'
import UpdateCreate from '../components/post/PostUpdate.vue'
import PostDetail from '../components/post/Detail.vue'
import PostList from '../components/post/List.vue'
import CategoryPage from '@/components/page/CategoryPage.vue'
import Chat from '@/components/chat/Chat.vue'
import Chat2 from '@/components/chat/Chat2.vue'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'MainPage',
    component: MainPage
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/user',
    name: 'MyProfile',
    component: MyProfile
  },
  {
    path: '/user/update',
    name: 'UserProfileUpdate',
    component: UserProfileUpdate
  },
  {
    path:'/layout',
    name:'Layout',
    component: Layout
  },
  
  {
    path: '/user/:uid',
    name: 'UserProfile',
    component: UserProfile
  }, 
  {
    path: "/post/create",
    name: "post-create",
    component: PostCreate,
  },
  {
    path: "/post/update",
    name: "post-update",
    component: UpdateCreate,
  }
  , 
  {
    path: "/post/detail",
    name: "post-detail",
    component: PostDetail,
  }
  , 
  {
    path: "/post/list",
    name: "post-list",
    component: PostList,
  },
  {
    path: '/category/:categoryNum',
    name: 'CategoryPage',
    component: CategoryPage
  }, 
  {
    path:"/chat",
    name:"Chat",
    component:Chat
  },
  {
    path:"/inchat/:roomId",
    name:"Chat2",
    component:Chat2
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
