import { createRouter, createWebHistory } from "vue-router"

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      name: 'home',
      path: '/',
      component: () => import('../components/TheHome.vue')
    },
    {
      name: 'login',
      path: '/login',
      component: () => import('../components/LoginForm.vue')
    },
    {
      name: 'register',
      path: '/reg',
      component: () => import('../components/RegisterForm.vue')
    },
    {
      name: 'profile',
      path: '/profile',
      component: () => import('../components/UserProfile.vue')
    },
    {
      name: 'new-doc',
      path: '/create-doc',
      component: () => import('../components/NewDocument.vue')
    }
  ]
})
export default router
