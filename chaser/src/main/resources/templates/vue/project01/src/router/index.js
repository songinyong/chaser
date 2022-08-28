
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
        component: LobbyComponent
      },
      {
        path: '/room',
        component: roomComponent

      },
      {
        path: '/game',
        component: gameComponent

      }
   ]
  });

  export default router;