<template>

  <div class="lobby">
    <div class="container">
      <div class="picture_zone"><img alt="Vue logo" src="@/assets/dice.jpg" width="200" height="200" style="width:250px; height:250px"/></div>
      <div class="room_zone">
        <div class="roomList_location">
          <h4 id=numOfUser></h4>
          
          <button @click="getRoomList()" class="custom-btn main_btn"><span>새로고침</span></button>
          <h3>방목록</h3>
          <ul id="roomList">
          </ul>
        </div>
        <div class="roomButton_location">
          <!--<label>방 이름</label>
          <input type="text" id="roomName" name="name" maxlength='15'/>
          <button @click="roomSearch()">방 검색</button>
          <button onclick="test()">빠른입장</button>
          <br/>-->
          <label>방 아이디</label>
          <input type="text" id="roomId" name="name" maxlength='5'/>
          <button @click="roomEnter()" class="custom-btn main_btn"><span>입장</span></button>
          <br/>
        
          <button @click="this.$roomCreate()" class="custom-btn main_btn"><span>방생성</span></button>
        </div>
      </div>
      <div class="userInfo_zone">

        <div class="userInfo_location">
          <h3 id="userName"></h3>
          <input type="text" id="userNm" name="name" maxlength='10'/>
          <button @click="nameChange()" class="custom-btn main_btn"><span>이름설정</span></button>
        </div>
        
        <div class="update_list">
          <h3>
            CSS, 레이아웃 작업중
          </h3>
        </div>
      </div>
  <div class="gameInfo_zone"></div>
 
     </div>
  </div>

</template>
<script setup>
import { useHead } from "@vueuse/head"

</script>
<script>

export default {
  name: 'LobbyComponent',
  created() {
      document.title = 'chaser';
    },
  mounted() {
    this.getRoomList()

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
          
            var roomId =  data.roomList[i].roomId;
          // 2. 추가할 li element 생성
          // 2-1. 추가할 li element 생성
          const li = document.createElement("li");
          /* 2-2. li에 id 속성 추가 
          li.setAttribute('id',addValue);*/
          // 2-3. li에 text node 추가 
          const textNode = document.createTextNode(addValue);
          li.appendChild(textNode);
          li.addEventListener("click",function(){
            
            document.getElementById('roomId').value = roomId ;
        });
          
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
.container {  
  display: grid;
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
    ". update_list .";
  grid-area: userInfo_zone;
}

.userInfo_location { grid-area: userInfo_location }
.update_list { grid-area: update_list }

.gameInfo_zone { grid-area: gameInfo_zone; }



.custom-btn {
  width: 110px;
  height: 40px;
  padding: 10px 25px;
  border: 2px solid #000;
  font-family: 'Lato', sans-serif;
  font-weight: 150;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
}


.main_btn {
  transition: all 0.3s ease;

}

.main_btn:hover {
   box-shadow:
   -7px -7px 20px 0px #fff9,
   -4px -4px 5px 0px #fff9,
   7px 7px 20px 0px #0002,
   4px 4px 5px 0px #0001;
}




</style>