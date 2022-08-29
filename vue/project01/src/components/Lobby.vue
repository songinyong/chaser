<template>

  <div class="lobby">
    <div class="container">
      <div class="picture_zone"><img alt="Vue logo" src="@/assets/logo.png"/></div>
      <div class="room_zone">
        <div class="roomList_location">
          <button @click="getRoomList()">새로고침</button>
          <h3>방목록</h3>
          <ul id="roomList">
          </ul>
        </div>
        <div class="roomButton_location">
          <label>방 이름</label>
          <input type="text" id="roomName" name="name" maxlength='15'/>
          <button @click="roomSearch()">방 검색</button>
          <br/>
          <label>방 아이디</label>
          <input type="text" id="roomId" name="name" maxlength='5'/>
          <button @click="roomEnter()">입장</button>
          <button onclick="test()">빠른입장</button>
          <button @click="this.$roomCreate()">방생성</button>
        </div>
      </div>
      <div class="userInfo_zone">

        <div class="userInfo_location">
          <h3 id="userName"></h3>
          <input type="text" id="userNm" name="name" maxlength='10'/>
          <button @click="nameChange()">이름변경</button>
        </div>
      </div>
  <div class="gameInfo_zone"></div>
 
     </div>
  </div>

</template>

<script>

export default {
  name: 'LobbyComponent',
  mounted() {
    this.getRoomList()

  },
  methods : {
    nameChange() {

      if(document.getElementById('userNm').value.length >0) {
        this.$inputMaxChk(document.getElementById('userNm'), 10)
        if(this.$checkSecurity(document.getElementById('userNm'))) {
            this.$nameChange()
            alert('이름이 변경되었습니다')
        }
      }
          
      
    },
    roomSearch() {
      this.$inputMaxChk(document.getElementById('roomId'), 15)
      if(this.$checkSecurity(document.getElementById('roomId')))
          this.$roomSearch()
    },
    roomEnter() {
      this.$inputMaxChk(document.getElementById('roomId'), 5)
      if(this.$checkSecurity(document.getElementById('roomId')))
          this.$roomEnter()
    },
    getRoomList() {
      //console.log(window.location.hostname)
      fetch('http://34.64.92.123:8088/roomList')
      .then(response =>{
          return response.json();
      }).then(data =>{
        this.getRoomListcallBack(data);
      })
      },

      getRoomListcallBack(data) {
        console.log(data.result)
       if(data.result) {
        
        const ul = document.getElementById('roomList');
        if(ul) {
          while(ul.firstChild) {
            ul.removeChild(ul.firstChild);
          }
        }

        for(var i=0; i< data.roomList.length; i++) {
          
          const userNum = data.roomList[i].userList.length;
          var checkStart = "대기중"
          if(data.roomList[i].startCheck)
              checkStart = "게임진행중"
          const addValue 
            = data.roomList[i].roomTitle + "#" + data.roomList[i].roomId +":" + checkStart +" " + userNum +"/ 4";
          
          // 2. 추가할 li element 생성
          // 2-1. 추가할 li element 생성
          const li = document.createElement("li");
          /* 2-2. li에 id 속성 추가 
          li.setAttribute('id',addValue);*/
          // 2-3. li에 text node 추가 
          const textNode = document.createTextNode(addValue);
          li.appendChild(textNode);
          
          // 3. 생성된 li를 ul에 추가
          document
            .getElementById('roomList')
            .appendChild(li);

       }

      }
    
  }
}
}

</script>

<style scoped>
.container {  display: grid;
  grid-template-columns: 1.1fr 1fr 0.9fr;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    "room_zone picture_zone gameInfo_zone"
    "room_zone userInfo_zone gameInfo_zone"
    "room_zone userInfo_zone gameInfo_zone";
}

.picture_zone { grid-area: picture_zone; }

.room_zone {  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    "roomList_location roomList_location roomList_location"
    "roomList_location roomList_location roomList_location"
    ". roomButton_location .";
  grid-area: room_zone;
}

.roomList_location { grid-area: roomList_location; }

.roomButton_location { grid-area: roomButton_location; }

.userInfo_zone {  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr 0.8fr 1.2fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    ". . ."
    ". userInfo_location ."
    ". . .";
  grid-area: userInfo_zone;
}

.userInfo_location { grid-area: userInfo_location; }

.gameInfo_zone { grid-area: gameInfo_zone; }




</style>