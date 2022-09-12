
import { createWebHistory, createRouter } from "vue-router";
import LobbyComponent from '@/components/Lobby.vue'
import roomComponent from '@/components/Room.vue'
import gameComponent from '@/components/Game.vue'
import chatComponent from '@/components/chat.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
      {
        path: '/',
        component: LobbyComponent,
        meta: {
          title: "chaser",
        },
      },
      {
        path: '/room',
        component: roomComponent,
        meta: {
          title: "chaser",
        },

      },
      {
        path: '/game',
        component: gameComponent,
        meta: {
          title: "chaser",
        },

      }
   ]
  });


  export default router;