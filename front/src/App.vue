<template>
  <el-container>
    <el-header class="page-header">
      <el-row justify="space-between" align="middle">
        <el-col :span="10" class="logo-menu">
          <div class="main-logo">
            <span class="material-icons-round" style="font-size: 3em">source</span>&nbsp;
            <span class="logo-text">BrainDocs</span>
          </div>
          <el-menu
              :default-active="activeMenu"
              background-color="transparent"
              text-color="white"
              active-text-color="#DBB046"
              class="el-menu-demo"
              mode="horizontal"
              style="border-bottom: none"
              :ellipsis="true"
              router
              @select="menuClicked"
          >
            <el-menu-item :route="{ name: 'new-doc' }" index="1" class="menu-item">New document</el-menu-item>
            <el-menu-item index="2" class="menu-item">Search</el-menu-item>
            <el-menu-item index="3" class="menu-item">Directories</el-menu-item>
          </el-menu>
        </el-col>
        <el-col :span="3" style="text-align: end">
          <el-button type="primary" @click="signIn">
            <span class="material-icons-round">account_circle</span>&nbsp;Sign
          </el-button>
        </el-col>
      </el-row>
    </el-header>
    <el-main class="page-main">
      <router-view></router-view>
    </el-main>
    <el-footer class="page-footer">
      <span>BrainDocs &copy;</span>
    </el-footer>
  </el-container>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router"
import { computed, onMounted, onUnmounted, ref } from "vue"
import { useStore } from "vuex";

const router = useRouter()
const store = useStore();
const activeMenu = ref('')

const signIn = () => {
  activeMenu.value = ''
  router.push({ name: 'login' })
}
const menuClicked = (index: string) => {
  activeMenu.value = index
}

const onWidthChange = () => store.commit('windowResize', window.innerWidth)
onMounted(() => window.addEventListener('resize', onWidthChange))
onUnmounted(() => window.removeEventListener('resize', onWidthChange))
onWidthChange()
</script>

<style>
@import url('https://fonts.googleapis.com/icon?family=Material+Icons+Round');
@import url('https://fonts.googleapis.com/css2?family=Ubuntu:wght@400&display=swap');

body {
  margin: 0;
}

* {
  font-family: 'Ubuntu', sans-serif;
}

.log-form-box {
  border-radius: 4px;
  border: 1px solid var(--el-border-color-base);
  padding: 16px;
  width: 70%;
  margin: 10% auto;
}

@media only screen and (max-width: 768px) {
  /* For mobile phones: */
  .log-form-box {
    width: 95%;
  }
}
</style>

<style scoped>
.logo-menu {
  display: flex;
}

.page-header {
  height: auto;
  background: #3670FF;
}

.main-logo {
  display: inline-flex;
  align-items: center;
  color: white;
}

.page-footer {
  height: 150px;
  padding: 32px;
  text-align: center;
  background: rgba(206, 204, 199, 0.42);
}

.page-main {
  min-height: 100vh;
}

.menu-item {
  font-size: large;
}

.logo-text {
  font-size: x-large;
  margin-right: 1em;
}
</style>
