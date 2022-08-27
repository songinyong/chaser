<template>
  
  
  <router-view/>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue' <HelloWorld msg="Welcome to Your Vue.js App"/>
//import LobbyComponent from './components/Lobby.vue'
export default {
  name: 'App',
  components: {
    //HelloWorld
    //LobbyComponent
  },

  
  beforeCreate () {
    this.$webSocketInit();
  }
  
}
</script>

<style>


#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
