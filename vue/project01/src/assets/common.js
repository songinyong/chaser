/** 
 * 뷰 컴포넌트중 게임 진행에 필요한 동작 함수 정의
 * 
*/

import { getCurrentInstance} from "@vue/runtime-core";
import router from '@/router/index.js';
//import logger from 'js/logger.js'
//const app = getCurrentInstance();
//const $webSocket = app.appContext.config.globalProperties.$webSocket;
//const $webSocket = new WebSocket("ws://34.64.92.123:8088/websocket")
const $webSocket = new WebSocket("ws://localhost:8088/websocket")
var timer ;
//게임 시간 주기
var time=30;

//유저의 아이디
var userId;

//유저가 서버에 보내는 명령어 호출 횟수를 제한
var limitTimeCheck = new Date();

//유저의 이름
var userNm ;
//현재 보고 있는 유저의 보드
var viewNm ;
const methods = {

  webSocketInit : () => {
    $webSocket.onopen = function() { console.log("연결 완료"); };
    $webSocket.onclose = function() { methods.socketClose();};
    $webSocket.onmessage = function(event) { methods.socketMessage(event);};
    $webSocket.onerror = function(event) { methods.socketError(event);};
  },
  //웹소켓 닫힘
  socketClose : () => {
    console.log("웹소켓이 닫혔습니다.");
    // 웹소켓이 닫혀있으면 재연결을 시도합니다.
    // webSocket이 close되면 오브젝트의 속성, 메서드가 사라지기 때문에,
    // 해당 함수를 호출하여 초기화하여 줍니다.
    methods.webSocketInit(); 
  },
  
  //웹소켓 수신
  socketMessage : (event) => {
    
    var receiveData = event.data; // 수신 data 
    var obj2 = JSON.parse(receiveData);
    const url = window.location.href;
    //console.log(obj2);
    var i = 0;
    var userList = [];
    //window.location.href = "http://localhost:8080/room" ;

    if(obj2.status =='init') {
      userNm = obj2.userNm;
      userId = obj2.userId;
      document.getElementById('userName').innerHTML = userNm;
    }
    /*
    else if(obj2.status =='numofUser') {

      console.log(document.getElementById('numOfUsser'))
      userNm = obj2.numOfUser;
      userId = obj2.userId;
      document.getElementById('numOfUser').innerHTML = "접속자수: " + obj2.numOfUser +"명";
    }*/
    else if(obj2.status == 0) {

      document.querySelectorAll('.user').forEach((user) => {
        user.innerHTML = "";
      })

      userList = Object.keys(obj2.userList);
      if(obj2.turn == undefined) {
        
        for(i=0; i< userList.length; i++) {
          document.getElementById('user'+(i+1)).innerHTML = userList[i];  
        //ready와 owner 표시
          document.getElementById('stat'+(i+1)).innerHTML = obj2.userList[userList[i]];
        }
      }
      else {
        for(i=4; i> userList.length ; i--) {
          document.getElementById('view'+(i)).innerHTML = "";
        }

        for(i=0; i< userList.length; i++) {
          document.getElementById('user'+(i+1)).innerHTML = userList[i];  
          if(userList[i] == obj2.currentUser)
              document.getElementById('stat'+(i+1)).innerHTML = "turn";

          //document.getElementById('view'+(i+1)).innerHTML = "<button onclick=\"methods.boardCheck(" +(i+1)+ ")\">화면전환</button>";
          

      }

    }
      //메시지가 서버로부터 올때
      if(obj2.mesg != undefined) 
        methods.setStat(obj2.mesg);

      //방정보가 바뀔때
      if(obj2.roomTitle != undefined) 
          document.getElementById('room_title').innerHTML = obj2.roomTitle ;
  }
    else if(obj2.status == 2) {
      router.push(url + '/');

    }
    else if( obj2.status == 3 ) {
      router.push(url + '/room');

    }
    else if( obj2.status == 4 ) {
      router.push(url + '/game');
      methods.setTimer(time);

      
      
    }
    // 다이스 점수 계산
    else if( obj2.status == 'dice') {
      methods.diceResult(obj2.diceList);
      if(userNm == obj2.currentUser || viewNm == obj2.currentUser) {
        methods.showBoard(obj2.board.board);  
        methods.preScore(obj2.chkBoard);
        
        if(3-obj2.diceRollCount > 0)
            methods.setStat('기회가'+ (3-obj2.diceRollCount) +'번 남았습니다');
        else
            methods.setStat('더이상 굴릴 수 없습니다');
        }
    }
    //스코어 점수 계산
    else if(obj2.status == 'score') {

      if(userNm == obj2.currentUser || viewNm == obj2.currentUser)
          methods.showBoard(obj2.board.board);

      methods.stopTimer(timer);
      //타이머 설정
      if(obj2.gaming == true) 
        methods.setTimer(time);
      
      userList = Object.keys(obj2.userList);

      document.querySelectorAll('.user').forEach((user) => {
        user.innerHTML = "";
      })

      for(i=0; i< userList.length; i++) {
        document.getElementById('user'+(i+1)).innerHTML = userList[i];
        //turn 표시
        if(userList[i] == obj2.nextUser)
            document.getElementById('stat'+(i+1)).innerHTML = "turn";
      }
      document.getElementById('round').innerHTML  = obj2.round

    }
    else if(obj2.status == 'chat') {
      document.getElementById("chatMsg").append(obj2.userNm + ": " + obj2.chatMsg+"\r\n");
      document.getElementById("chatMsg").scrollTop = document.getElementById("chatMsg").scrollHeight;
    }
    else if(obj2.status == 'timeOut') {
      methods.diceResult(obj2.diceList);
      if(userNm == obj2.currentUser || viewNm == obj2.currentUser)
          methods.showBoard(obj2.board.board);

      methods.stopTimer(timer);
      //타이머 설정
      if(obj2.gaming == true) {
        methods.setTimer(time);
      }

      //시간 아웃으로 턴 강제로 넘김

      userList = Object.keys(obj2.userList);

      document.querySelectorAll('.user').forEach((user) => {
        user.innerHTML = "";
      })

      for(i=0; i< userList.length; i++) {
        document.getElementById('user'+(i+1)).innerHTML = userList[i];
        //다음턴임을 보임
        if(userList[i] == obj2.nextUser)
            document.getElementById('stat'+(i+1)).innerHTML = "turn";

        
      }
      methods.setStat("시간 아웃으로 턴이 넘어갔습니다");
      document.getElementById('round').innerHTML  = obj2.round
    }
    else if(obj2.status == 'boardChk') {
  
      methods.showBoard(obj2.board.board);
      if(obj2.chkBoard != undefined)
          methods.preScore(obj2.chkBoard);
    }
    else if(obj2.status == 'nameChg') {
  
      document.getElementById('userName').innerHTML = obj2.userNm ;
      userNm = obj2.userNm ;
    }
    else if(obj2.status == 'end') {
  
      var endMesg = "";
      var userKey = Object.keys(obj2.score);

      methods.stopTimer(timer);

      for(i=0; i<userKey.length; i++) {
        endMesg = endMesg + userKey[i] +"점수는:" +obj2.score[userKey[i]] + "\n"
      }
      
      if (window.confirm(endMesg + "\n 방으로 돌아가시겠습니까?"))
      {
          // They clicked Yes
          router.push(url + '/room');
      }
      else
      {
          methods.roomOut()
          // They clicked no
          router.push(url + '/');
      }
    }
    // 세션이 끊겼을때
    else if(obj2.status == 'close') {

      userList = Object.keys(obj2.userList);
      for(i=0; i<4; i++) {
        document.getElementById('user'+(i+1)).innerHTML = "";  
        document.getElementById('stat'+(i+1)).innerHTML = "";
      }
      methods.setStat(obj2.mesg);
      if(obj2.currentUser == undefined) {
        
        for(i=0; i< userList.length; i++) {
          document.getElementById('user'+(i+1)).innerHTML = userList[i];  
        //ready와 owner 표시
          document.getElementById('stat'+(i+1)).innerHTML = obj2.userList[userList[i]];
        }
      }
      else {
        for(i=4; i> userList.length ; i--) {
          document.getElementById('view'+(i)).innerHTML = "";
        }

        for(i=0; i< userList.length; i++) {
          document.getElementById('user'+(i+1)).innerHTML = userList[i];  
          if(userList[i] == obj2.currentUser)
              document.getElementById('stat'+(i+1)).innerHTML = "turn";

          //document.getElementById('view'+(i+1)).innerHTML = "<button onclick=\"methods.boardCheck(" +(i+1)+ ")\">화면전환</button>";
      }

    }
  }
    
    else ;

  },

  //웹소켓 에러
  socketError : () => {
    alert("에러가 발생하였습니다.");
  },
  //입장시 이름 올림
  nameSet : () => {
    document.getElementById('userNm').innerHTML = "turn";
    
  },

  //2. 방 생성
  roomCreate : () => {

    var msg = {
      "roomStatus" : "create"
      }
      var jsonData = JSON.stringify(msg);
      // 세션리스트에 메시지를 송신한다.

      if(methods.setLimitTimer(limitTimeCheck))
          $webSocket.send(jsonData);  
  },
  //2. 방 입장
  roomEnter : () => {
    var msg = {
      "roomStatus" : "enter",
      "roomId" : document.getElementById('roomId').value
      }
      var jsonData = JSON.stringify(msg);
      // 세션리스트에 메시지를 송신한다.
      if(methods.setLimitTimer(limitTimeCheck))
          $webSocket.send(jsonData);   
  },
  //2. 방 검색
  roomSearch : () => {
    var msg = {
      "roomStatus" : "enter",
      "roomId" : document.getElementById('roomName').value
  
      }
      var jsonData = JSON.stringify(msg);
      // 세션리스트에 메시지를 송신한다.
      if(methods.setLimitTimer(limitTimeCheck))
          $webSocket.send(jsonData);       
  },
  //2. 이름 변경
  nameChange : () => {

    var msg = {
      "roomStatus" : "nameChg",
      "userNm" : document.getElementById('userNm').value
      }
      var jsonData = JSON.stringify(msg);
      // 세션리스트에 메시지를 송신한다.

      if(methods.setLimitTimer(limitTimeCheck))
          $webSocket.send(jsonData);   

  },

  titleChange : () => {
    userNm = document.getElementById('titleInput').value;
    var msg = {
      "roomStatus" : "titleChg",
      "title" : document.getElementById('titleInput').value
      }
      var jsonData = JSON.stringify(msg);
      // 세션리스트에 메시지를 송신한다.
      if(methods.setLimitTimer(limitTimeCheck))
          $webSocket.send(jsonData);  
  },

  //3. 게임 시작
  gameStart : () => {
    var msg = {
      "roomStatus" : "start",
      //"roomId" : document.getElementById('roomId').value
      }
      var jsonData = JSON.stringify(msg);
      // 세션리스트에 메시지를 송신한다.
      if(methods.setLimitTimer(limitTimeCheck))
          $webSocket.send(jsonData);   
  
  },

    //3. 퇴장
    roomOut : () => {
      var msg = {
        "roomStatus" : "out"
        }
        var jsonData = JSON.stringify(msg);
        // 세션리스트에 메시지를 송신한다.
        if(methods.setLimitTimer(limitTimeCheck))
            $webSocket.send(jsonData);   
    
    },

    //4. 화면 로딩 되었을때
    gameInit : () => {
      var msg = {
        "roomStatus" : "gaming",
        "gameAction" : "gameInit"
        }
        var jsonData = JSON.stringify(msg);
        // 세션리스트에 메시지를 송신한다.
        $webSocket.send(jsonData);   
    },

    //4. 주사위 굴리기
    rollDice : (clicked) => {

      var index = "";
      if(clicked.has("one"))
          index += "1";
      if(clicked.has("two"))
          index += "2";
      if(clicked.has("three"))
          index += "3";     
      if(clicked.has("four"))
          index += "4";
      if(clicked.has("five"))
          index += "5";  
      var msg = {
        "roomStatus" : "gaming",
        "gameAction" : "dice",
        "index" : index
        }

        var jsonData = JSON.stringify(msg);
        // 세션리스트에 메시지를 송신한다.
        if(methods.setLimitTimer(limitTimeCheck))
            $webSocket.send(jsonData);  
    },

    //4. 보드판 선택하기
    insertBoard : () => {

      if(document.getElementById('checkedBoard').style.color =="000000" );

      else {
      var msg = {
        "roomStatus" : "gaming",
        "gameAction" : "score",
        "insertSpace" : document.getElementById('checkedBoard').value
        }

        var jsonData = JSON.stringify(msg);

        // 세션리스트에 메시지를 송신한다.
        if(methods.setLimitTimer(limitTimeCheck))
            $webSocket.send(jsonData);  

      }
    },

    //4. 굴려진 다이스 표시
    diceResult : (diceList) => {
      
      document.getElementById('one').innerHTML = diceList["1"];
      document.getElementById('two').innerHTML = diceList["2"];
      document.getElementById('three').innerHTML = diceList["3"];
      document.getElementById('four').innerHTML = diceList["4"];
      document.getElementById('five').innerHTML = diceList["5"];
    },

    //4. 예상 점수를 보드판에 보여줌
    preScore : (board) => {

      const keys = Object.keys(board)
      
      for(var i=0; i<keys.length; i++) {
        
        if(document.getElementById(keys[i]).style.color !="#000000") {
          document.getElementById(keys[i]).innerHTML = board[keys[i]];
          document.getElementById(keys[i]).style.color = "#0078FF" ;
        }


      }
    },

      //4. 점수를 보드판에 올림
    showBoard : (board) => {

      const keys = Object.keys(board)
          
      for(var i=0; i<keys.length; i++) {
          
        if(board[keys[i]] == -1) 
            document.getElementById(keys[i]).innerHTML = "";
        else {
          document.getElementById(keys[i]).innerHTML = board[keys[i]];
          document.getElementById(keys[i]).style.color = "#000000" ;
        }

      }
    },

    //4. 점수판 체크
    boardCheck : (userName) => {
      viewNm = userName;
      
      var msg = {
        "roomStatus" : "gaming",
        "gameAction" : "boardChk",
        "userNm" : userName
        }
        var jsonData = JSON.stringify(msg);
        // 세션리스트에 메시지를 송신한다.
         $webSocket.send(jsonData);  
        
    },


      //timer 설정
      setTimer : (tim) => {
        var time = tim;
        timer = setInterval(()=> {
    
        document.getElementById("timer").innerHTML = time ;
        time--;
    
            if(time < 0) {
                time = 30;
                methods.stopTimer(timer)
                //next 넘김
                methods.insertBoard();
            }
        }, 1000)
      },

      //서버 호출 횟수 제한 타이머
      setLimitTimer : (preTime) => {
        var time = new Date();

        if( time - preTime > 800) {
          limitTimeCheck = time;
          return true;
        }
        else {
          
          return false;
        }
      },


      stopTimer : (timer) => {
        clearInterval(timer);
      },

      //스탯창 컨트롤 함수 scroll
      setStat : (msg) => {
        document.getElementById("statMsg").append(msg+"\r\n");
        document.getElementById("statMsg").scrollTop = document.getElementById("statMsg").scrollHeight;
        //document.getElementById("statMsg").scrollTop(document.getElementById(("statMsg").prop('scrollHeight')));
      },

      //채팅 보내기
      sendChat : (chat) => {

        if(chat != "") {
        var msg = {
          "roomStatus" : "chat",
          "chatMsg" : chat
          }
          var jsonData = JSON.stringify(msg);
          // 세션리스트에 메시지를 송신한다.
          if(methods.setLimitTimer(limitTimeCheck))
              $webSocket.send(jsonData); 
        }
      }
      
}

export default {

 
  install(Vue) {
    Vue.config.globalProperties.$webSocketInit = methods.webSocketInit ;
  
    Vue.config.globalProperties.$roomCreate = methods.roomCreate;

    Vue.config.globalProperties.$roomEnter = methods.roomEnter ;

    Vue.config.globalProperties.$roomSearch = methods.roomSearch ;

    Vue.config.globalProperties.$nameChange = methods.nameChange;

    Vue.config.globalProperties.$gameStart = methods.gameStart;

    Vue.config.globalProperties.$roomOut = methods.roomOut;

    Vue.config.globalProperties.$rollDice = methods.rollDice;

    Vue.config.globalProperties.$insertBoard = methods.insertBoard;

    Vue.config.globalProperties.$setTimer = methods.setTimer;

    Vue.config.globalProperties.$boardCheck = methods.boardCheck;
    
    Vue.config.globalProperties.$sendChat = methods.sendChat;
    Vue.config.globalProperties.$titleChange = methods.titleChange;
    
  }



}