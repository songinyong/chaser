<template>
  <div class="room">
    <div class="container">
      <div class="roomInfo_zone">
        <div class="room_tile_location">
          <label id="room_title">방이름</label>
          <input type="text" id="titleInput"  maxlength='20'/>
          <button @click="titleChange()">변경</button>
        </div>
        <div class="roomStatInfo_location">
          <h4 id=numOfUser></h4>

        </div>
      </div>
      <div class="user_zone">
          
          <div class="userInfo_location">
            <h3 align="center">접속유저</h3>
            <table border="1" width ="300" height="200" align="center" style="table-layout:fixed">
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
          </div>
        <div class="start_location">
            <button @click="this.$gameStart()">시작</button>
        </div>
        <div class="out_location">
                <button @click="this.$roomOut()">퇴장</button>
        </div>
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
          <h3>상태메시지</h3>
          <textarea id="statMsg" name="statMsg" rows="5"></textarea>
      </div>
    </div>

  </div>
</template>

<script>


export default {
  name: 'roomComponent',
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
.container {  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 0.8fr 1.2fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    "roomInfo_zone user_zone chat_zone"
    "roomInfo_zone user_zone chat_zone"
    ". stat_zone .";
}

.stat_zone { grid-area: stat_zone; }

.user_zone {  display: grid;
  grid-template-columns: 0.2fr 1.4fr 1.4fr;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    "userInfo_location userInfo_location userInfo_location"
    "userInfo_location userInfo_location userInfo_location"
    ". start_location out_location";
  grid-area: user_zone;
}

.userInfo_location { grid-area: userInfo_location; }

.start_location { grid-area: start_location; }

.out_location { grid-area: out_location; }

.chat_zone { grid-area: chat_zone; }

.roomInfo_zone {  display: grid;
  grid-template-columns: 0.9fr 1.3fr 0.8fr;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    ". room_tile_location ."
    ". roomStatInfo_location ."
    ". roomStatInfo_location .";
  grid-area: roomInfo_zone;
}

.room_tile_location { grid-area: room_tile_location; }

.roomStatInfo_location { grid-area: roomStatInfo_location; }


</style>