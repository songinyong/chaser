
<template>

<div class="container">
    <div class="game">
        <div class="Info_zone">
            <div class="user_location">
                  <h3 align="center">진행유저</h3>
                  <div class="userInfo_zone">
                    <table border="1" width ="400" height="200" align="center" style="table-layout:fixed">
                        
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
                        <tr bgcolor="#EEE6C4" >
                            <td  id="view1" class="viewBtn" style="word-break:break-all" ><button @click="boardCheck(1)">화면전환</button></td>
                            <td  id="view2" class="viewBtn" style="word-break:break-all"><button @click="boardCheck(2)">화면전환</button></td>
                            <td  id="view3" class="viewBtn" style="word-break:break-all"><button  @click="boardCheck(3)">화면전환</button></td>
                            <td  id="view4" class="viewBtn" style="word-break:break-all"><button @click="boardCheck(4)">화면전환</button></td>
                        </tr>       
                    </table>
                </div>
            </div>
            <div class="time_location">
                <h4 id=numOfUser></h4>
                <div class="timer_zone">
                    <div id="timer"></div>
                    <label>라운드:</label><div id="round">1</div>
                </div>

            </div>
        </div>
        <div class="main_zone">
            <div class="board_location">
                <input type="hidden" id="checkedBoard"/>
                <h3 align="center">보드</h3>
                <table border="1" width ="400" height="300" background="https://dx-sprint.s3.ap-northeast-2.amazonaws.com/paper.jpg" style='font-family:"Cookie", cursive; font-size:120%' >
                    
                    <tr bgcolor="transparent">
                        <td style="width:35%;">Chase off</td>
                        <td style="width:15%;" id="Chase off" class="board"></td>
                        <td style="width:35%;">Aces</td>
                        <td style="width:15%;" id="Aces" class="board"></td>            
                    </tr>
                    <tr bgcolor="transparent">
                        <td style="width:35%;">Straight</td>
                        <td style="width:15%;" id="Straight" class="board"></td>
                        <td style="width:35%;">Two Beans </td>
                        <td style="width:15%;" id="Two Beans" class="board"></td>        
                    </tr>     
                    <tr bgcolor="transparent">
                        <td style="width:35%;">Even Straight</td>
                        <td style="width:15%;" id="Even Straight" class="board"></td>
                        <td style="width:35%;">Three Beans</td>
                        <td style="width:15%;" id="Three Beans" class="board"></td>          
                    </tr>
                    <tr bgcolor="transparent">
                        <td style="width:35%;">Four Dice</td>
                        <td style="width:15%;" id="Four Dice" class="board"></td>
                        <td style="width:35%;">Four Beans</td>
                        <td style="width:15%;" id="Four Beans" class="board"></td>          
                    </tr>
                    <tr bgcolor="transparent">
                        <td style="width:35%;" >Full House</td>
                        <td style="width:15%;" id="Full House" class="board"></td>
                        <td style="width:35%;">Five Beans</td>
                        <td style="width:15%;" id="Five Beans" class="board"></td>        
                    </tr>
                    <tr bgcolor="transparent">
                        <td style="width:35%;">Choice</td>
                        <td style="width:15%;" id="Choice" class="board"></td>
                        <td style="width:35%;">Six Beans</td>
                        <td style="width:15%;" id="Six Beans" class="board"></td>
                                
                    </tr>
                </table>
            <button @click="rollDice()">굴리기</button>
            <button @click="insertBoard()">점수 결정</button>
            </div>
            <div class="status_location">
                <h3>상태메시지</h3>
                <textarea id="statMsg" name="statMsg" rows="5"></textarea>

            </div>
            <div class="chat_location">
                <h3>채팅창</h3>
                <textarea id="chatMsg" name="chatMsg" rows="20" style="background-image:url('https://dx-sprint.s3.ap-northeast-2.amazonaws.com/letter.png')"></textarea>
                <input type="text" id="chatInput"  maxlength='50'/>
                <button @click="sendChat()">채팅보내기</button>
            </div>
            <div class="dice_location">
                <h3>주사위</h3>
                <table border="1" width ="70" height="300" align="center" id="dice_list">
                    
                    <tr bgcolor="transparent"  >
                        <td id="one" class="dice"><img id="one dice" src="https://dx-sprint.s3.ap-northeast-2.amazonaws.com/diceone.png" style="width:60px; height:60px;"></td>
                    </tr>
                    <tr bgcolor="transparent"  >
                        <td id="two" class="dice"><img id="two dice" src="@/assets/diceone.png" style="width:60px; height:60px;"></td>
                    </tr>
                    <tr bgcolor="transparent"  >
                        <td id="three" class="dice"><img id="three dice" src="@/assets/diceone.png" style="width:60px; height:60px;"></td>
                    </tr>
                    <tr bgcolor="transparent" >
                        <td id="four" class="dice"><img  id="four dice" src="@/assets/diceone.png" style="width:60px; height:60px;"></td>
                    </tr>
                    <tr bgcolor="transparent" >
                        <td id="five" class="dice"><img id="five dice" src="@/assets/diceone.png" style="width:60px; height:60px;"></td>
                    </tr>
                </table>

            </div>
        </div>
</div>


    <div style="width:70%; float:right;">
    </div>
    <div style="width:30%; float:left;">
    </div>

  </div>
</template>

<script>

const clicked = new Set();

//타이머
var time = 30;

const methods = {
  chgBoardColor: (id) => {
        document.querySelectorAll('.board').forEach((board) => {
            if(board.id != id)
                //document.getElementById(board.id).style.backgroundColor = "#D9E5FF";
                document.getElementById(board.id).style.backgroundColor = "#11ffee00";
                
        });
    },
  chgDiceColor : () => {
        document.querySelectorAll('.dice').forEach((dice) => {
            
            document.getElementById(dice.id).style.backgroundColor = "transparent";
        });
  },


}
export default {
  name: 'gameComponent',

  mounted() {
    
    document.querySelectorAll('.dice').forEach((dice) => {

        dice.addEventListener('click', function(event) {
        
            


        if(clicked.has(dice.id)) {
            clicked.delete(dice.id);
            document.getElementById(dice.id).style.backgroundColor ="transparent"
            
        }
            
        else {
            clicked.add(dice.id);
            document.getElementById(dice.id).style.backgroundColor = "#EEE6C4"
            
        }

    
        });
    });

    document.querySelectorAll('.board').forEach((board) => {
        board.innerHTML = "";
        board.addEventListener('click', function(event) {
        
            document.getElementById('checkedBoard').value = event.target.id ;
            document.getElementById(event.target.id).style.backgroundColor = "#FFFFFF";
            methods.chgBoardColor(event.target.id);

        });


    });

  },
    methods : {
    rollDice() {
      this.$rollDice(clicked);
      clicked.clear();
      methods.chgDiceColor();
    },
    insertBoard() {
        this.$insertBoard();
        clicked.clear();
    },
    boardCheck(userNum) {
        this.$boardCheck(document.getElementById('user'+userNum).innerHTML);
    },

    sendChat() {
        this.$inputMaxChk(document.getElementById('chatInput') , 50)
        if(this.$checkSecurity(document.getElementById('chatInput') ))
            this.$sendChat(document.getElementById('chatInput').value );
 
    },
    screenShare(obj) {
        this.$
    }


  }
}


</script>

<style scoped>
.container {  display: grid;
  grid-template-columns: 0.6fr 1.4fr 1fr;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    "Info_zone Info_zone Info_zone"
    "main_zone main_zone main_zone"
    "main_zone main_zone main_zone";
}

.Info_zone {  display: grid;
  grid-template-columns: 0.6fr 1.4fr 1fr;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    ". . ."
    "time_location user_location ."
    "time_location user_location .";
  grid-area: Info_zone;
}

.user_location { grid-area: user_location; }

.time_location { grid-area: time_location; }

.main_zone {  display: grid;
  grid-template-columns: 0.6fr 1.4fr 1fr;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    "dice_location board_location chat_location"
    "dice_location board_location chat_location"
    "dice_location status_location chat_location";
  grid-area: main_zone;
}

.board_location { grid-area: board_location; }

.status_location { grid-area: status_location; }

.chat_location { grid-area: chat_location; }

.dice_location { grid-area: dice_location; }

@import url('https://fonts.googleapis.com/css2?family=Cookie&display=swap');

#main_board {
    font-family: 'Ms Madi', cursive;
}

#dice_list {
    border-collapse:collapse;
    border:none;
}


</style>