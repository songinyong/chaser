<template>
  <div class="room">



    <div class="container">
      <div class="info_zone"></div>
      <div class="main_zone">
        <div class="room_zone">
          
          <h3 id="room_title"></h3>
          <input type="text" id="titleInput"  maxlength='20' placeholder="방제목은 방장만 변경 가능합니다"/>
          <button @click="titleChange()">변경</button>
        </div>

      <div class="user_zone">    
            <h3 align="center">접속유저</h3>
            <table border="1" width ="500" height="200" align="center" style="table-layout:fixed">
                <tr bgcolor="#EEE6C4" >
                    <td id="user1" class="user" style="word-break:break-all"></td>
                    <td id="user2" class="user" style="word-break:break-all"></td>
                    <td id="user3" class="user" style="word-break:break-all"></td>
                    <td id="user4" class="user" style="word-break:break-all"></td>
                </tr>
                <tr bgcolor="#EEE6C4" >
                    <td id="stat1" class="user" style="word-break:break-all"></td>
                    <td id="stat2" class="user" style="word-break:break-all"></td>
                    <td id="stat3" class="user" style="word-break:break-all"></td>
                    <td id="stat4" class="user" style="word-break:break-all"></td>
                </tr>
            </table>
        
            <button @click="this.$gameStart()">시작</button>
            <button @click="this.$roomOut()">퇴장</button>
        
      </div>
      <div class="chat_zone">
          <h3>채팅창</h3>
          <textarea id="chatMsg" name="chatMsg" rows="20" style="background-image:url('https://dx-sprint.s3.ap-northeast-2.amazonaws.com/letter.png')"></textarea>
          <br/>
          <input type="text" id="chatInput"  maxlength='50'/>
          <br/>
          <button @click="sendChat()">채팅보내기</button>
      </div>
      <div class="stat_zone">
        <div class="stat_location">
          <h3>상태메시지</h3>
          <textarea id="statMsg" name="statMsg" rows="5"></textarea>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>
<script setup>
  import { useHead } from "@vueuse/head"
  
  </script>
<script>


export default {
  name: 'roomComponent',
  created() {
      document.title = 'chaser';
    },
 
  setup() {
    useHead({
      meta: [
        { name: 'viewport', content: 'width=device-width,initial-scale=1.0,user-scalable=no' },
        { charset: 'utf-8' },
    ]

  })
  },
  methods : {
      sendChat() {
        this.$inputMaxChk(document.getElementById('chatInput') , 50)
        if(this.$checkSecurity(document.getElementById('chatInput') ))
            this.$sendChat(document.getElementById('chatInput').value );
 
      },
      titleChange() {
        this.$inputMaxChk(document.getElementById('titleInput') , 20)
        if(this.$checkSecurity(document.getElementById('titleInput') ))
            this.$titleChange();
      }

  }
}
</script>

<style scoped>
#titleInput {
  width: 200px; 
  height: 30px;
  border-top : 3px solid black;
  border-left : 3px solid black;
  border-right : 3px solid black;
  border-bottom : 3px solid black;
}

.container {  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 0.3fr 1.7fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    "info_zone info_zone info_zone"
    "main_zone main_zone main_zone"
    "main_zone main_zone main_zone";
}

.info_zone { grid-area: info_zone; }

.main_zone {  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    "room_zone user_zone chat_zone"
    "room_zone user_zone chat_zone"
    ". stat_zone .";
  grid-area: main_zone;
}

.chat_zone { grid-area: chat_zone; }

.user_zone { grid-area: user_zone; }

.room_zone { grid-area: room_zone; }

.stat_zone { grid-area: stat_zone; }







</style>