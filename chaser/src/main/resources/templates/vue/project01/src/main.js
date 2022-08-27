import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index.js';
import common from './assets/common.js'
import security from './assets/security.js'
const app = createApp(App)

//const webSocket = new WebSocket("ws://localhost:8088/websocket");
//app.config.globalProperties.$webSocket = webSocket;

//const clicked = new Set();
//app.config.globalProperties.$clicked = clicked;
app.use(router).use(common).use(security).mount('#app')


