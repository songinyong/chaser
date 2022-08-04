import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index.js';
import common from './assets/common.js'

const app = createApp(App)

//const webSocket = new WebSocket("ws://localhost:8088/websocket");
//app.config.globalProperties.$webSocket = webSocket;

app.use(router).use(common).mount('#app')


