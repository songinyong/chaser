package com.service.web.handler;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.domain.Board;
import com.service.domain.jpa.User;
import com.service.domain.redis.Room;
import com.service.domain.redis.RoomRepository;
import com.service.web.Gaming;
import com.service.web.MultiGaming;
import com.service.webservice.GameService;
import com.service.webservice.RoomService;
import com.service.webservice.UserService;



@Component
public class MultiHandler extends TextWebSocketHandler  {

	//서버에 접속한 유저 리스트 
	private static HashMap<String, User> userMap = new HashMap<String, User>();
	private static HashMap<String,MultiGaming> gameMap = new HashMap<String,MultiGaming>();
	private static HashMap<String, Room> roomMap = new HashMap<String, Room>();
	
	private static List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
	
    /**
     * 서버에 접속한 웹소캣별 게이밍 진행상태 저장
     */
    public static UserService userService ;
    public static GameService gameService;
    public static RoomService roomService;
    
    
	@Autowired
	public RoomRepository roomRepo;
	
	/*client가 서버에게 메시지 보냄*/
    
    //멀티노드로 만든다면 redis에서 room 정보 리스트 수 를 저장하도록 해야함
    private static int nextRoomId = 0;
  

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	try {
    	
    	String payload = message.getPayload();
       
        JSONObject obj = jsonToObjectParser(payload);
        //System.out.println(obj);
        HashMap<String, Object> result = new HashMap();

        //정상적으로 연결되어 있을때만 메시지 처리
        if (session != null) {
        	
        	if(obj.get("roomStatus") != null) {

        		//방생성
        		if(obj.get("roomStatus").equals("create")) {
        			 
        	            String roomId = Integer.toHexString(nextRoomId);
        	            
        	            if(obj.get("userNm") != null)
        	            	userMap.get(session.getId()).setUserNm(session.getId().split("-")[0]);
        	            
        	            
        	            userMap.get(session.getId()).setRoomId(roomId);
        	            
        	            nextRoomId++;
        	            roomMap.put(roomId, roomService.roomCreate(roomId));
        	            //방에 들어감
        	            roomService.userJoin(roomMap.get(roomId), session.getId());
        	            //생성한 방의 방장이 됨
        	            roomService.ownerSet(roomMap.get(roomId), session.getId());
        	            
        	            //초기 방 이름 설정
        	            roomService.titleSet(roomMap.get(roomId), roomId);
        				
        				result.put("status", 3);

                		sendMessage(session, makeJson(result));
                		
                		result.put("status", 0);
                		result.put("result", true);
                		result.put("roomId", roomId);
                		result.put("userId", session.getId());
                		result.put("userNm", userMap.get(session.getId()).getUserNm());
                		result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
                		result.put("roomTitle", roomMap.get(roomId).getRoomTitle() + "#" + roomId);
                		sendMessage(session, makeJson(result));
                		
        		}
        		//채팅
        		else if(obj.get("roomStatus").equals("chat")) {
        			String roomId ;

        			if(userMap.get(session.getId()).getRoomId() != null) {
        			    roomId = userMap.get(session.getId()).getRoomId();

        			    String chatMsg = (String) obj.get("chatMsg");
        			    result.put("status", "chat");
        			    result.put("chatMsg", chatMsg);
        			    result.put("userNm", userMap.get(session.getId()).getUserNm());
                		//방에 있는 사람들에게 메시지 보냄
                		for (String u : roomMap.get(roomId).getUserList()) 
                			sendMessage(userMap.get(u).getSession(), makeJson(result));
                		
        			
        			}
        		}
        		
        		//방입장
        		else if(obj.get("roomStatus").equals("enter")) {
        			String roomId =  (String) obj.get("roomId");
        			
        			if(roomMap.get(roomId)==null) {
        				//없는 방이라는 메시지 전달
                   		result.put("result", false);
                		result.put("msg", "존재하지 않는 방입니다");
        			}
        			else if(roomMap.get(roomId).getUserList().size()>3) {
        				//인원이 가득찬 방이라는 메시지 전달 
                   		result.put("result", false);
                		result.put("msg", "인원이 가득찬 방입니다");
        			}
        			else if(roomMap.get(roomId).isProgressCheck()) {
        				//게임이 진행중이라는 메시지 전달
                   		result.put("result", false);
                		result.put("msg", "게임이 시작한 방입니다");
        			}
        			
        			else {
   				
        				//방에 들어감
        	            if(roomService.userJoin(roomMap.get(roomId), session.getId())) {
        	            	userMap.get(session.getId()).setRoomId(roomId);
        	            	
            				HashMap soloMessage = new HashMap();
            				soloMessage.put("status", 3);
            				sendMessage(session, makeJson(soloMessage));
        	            	
        	            	//이름 설정
            	            if(obj.get("userNm") != null)
            	            	userMap.get(session.getId()).setUserNm(session.getId().split("-")[0]);
        	            	
                    		result.put("result", true);
                    		result.put("userId", session.getId());
                    		result.put("userNm", userMap.get(session.getId()).getUserNm());
                    		result.put("status", 0);
                    		result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
                    		result.put("mesg", userMap.get(session.getId()).getUserNm()+" 님이 들어오셨습니다");
                    		result.put("roomTitle", roomMap.get(roomId).getRoomTitle() + "#" + roomId);
                    		
            				for (String u : roomMap.get(roomId).getUserList()) 
            					sendMessage(userMap.get(u).getSession(), makeJson(result));


            				
        	            }
        	            else {
                    		result.put("result", false);
            				for (String u : roomMap.get(roomId).getUserList()) 
            					sendMessage(userMap.get(u).getSession(), makeJson(result));
        	            }
        				
        			}
        			
        		}
        		//방퇴장
        		else if(obj.get("roomStatus").equals("out")) {
        			String roomId = userMap.get(session.getId()).getRoomId();
        			
        			if(!roomId.isEmpty()) {
        			if(roomMap.get(roomId).getUserList().contains(session.getId())) {
        				
        				int resultCheck = roomService.userOut(roomMap.get(roomId), session.getId());
        				userMap.get(session.getId()).setRoomId(null);
        				
        				if(roomMap.get(roomId).getUserList().size()>0) {
        				
	                		result.put("result", true);
	                		result.put("userId", session.getId());  
	                		result.put("mesg", userMap.get(session.getId()).getUserNm()+" 님이 퇴장하셨습니다");
	                		result.put("status", 0);
	                		result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
	                		
	        				for (String u : roomMap.get(roomId).getUserList()) 
	        					sendMessage(userMap.get(u).getSession(), makeJson(result));   
        				
            				HashMap soloMessage = new HashMap();
            				soloMessage.put("status", 2);
            				sendMessage(session, makeJson(soloMessage));
	        				
        				if(resultCheck == 0) {
        					//roomService.ownerSet(roomMap.get(roomId), roomMap.get(roomId).getUserList().get(0));
                    		
                    		result.put("userId", roomMap.get(roomId).getUserList().get(0));  
                    		result.put("mesg", roomMap.get(roomId).getUserList().get(0)+"님이 새로운 방장이 되셨습니다");
        					
            				for (String u : roomMap.get(roomId).getUserList()) 
            					sendMessage(userMap.get(u).getSession(), makeJson(result)); 
        				}
        				} // -> 방에 한명이라도 남아 있을경우 END
        				else {
            				HashMap soloMessage = new HashMap();
            				soloMessage.put("status", 2);
            				sendMessage(session, makeJson(soloMessage));
        					
        					roomRepo.delete(roomMap.get(roomId));
            				roomMap.get(roomId).getUserList().clear();
            				roomMap.replace(roomId, null);      					
            				
        				} // -> 모든 인원이 사라진 방은 삭제함 
        				

        			}
        			}
        			
        		}
        		//방삭제
        		else if(obj.get("roomStatus").equals("delete")) {
        			String roomId = (String) obj.get("roomId");

        			if(roomMap.get(roomId).getRoomOwner().equals(session.getId())) {
        				
        				result.put("delete", true);
        				for (String u : roomMap.get(roomId).getUserList()) 
        					sendMessage(userMap.get(u).getSession(), makeJson(result));
        				
        				//userMap에 세팅된 룸 Id 초기화
        				for(String u :roomMap.get(roomId).getUserList()) {
        					userMap.get(u).setRoomId(null);
        				}
        				roomMap.get(roomId).getUserList().clear();
        				roomMap.replace(roomId, null);
        				roomRepo.delete(roomMap.get(roomId));
        				
      					
        			}
        		}   
        		//게임 시작이나 ready start 될시 result에 보내줘야함
        		else if(obj.get("roomStatus").equals("start")) {
        			
        			String roomId = userMap.get(session.getId()).getRoomId();
        			
        			if(!roomId.isEmpty()) {
	        			boolean gameStart = roomService.userclickStart(roomMap.get(roomId), session.getId());
	
	            		
	            		// -> 방장일때 start
	            		if(roomMap.get(roomId).getRoomOwner().equals(session.getId()) && gameStart) {
	                		
	        				roomMap.get(roomId).setGaming(gameService.gameStart(roomId, obj));
	        				
	        				result.put("status", 4);
	                    	//게임 score 정보 와 board 초기화
	                    	for (String u : roomMap.get(roomId).getUserList()) {
	                    		roomMap.get(roomId).getGaming().getGameBoards().put(u, Board.builder().userId(u).build());
	                    		sendMessage(userMap.get(u).getSession(), makeJson(result));
	                    	}
	                    		
	                    	roomMap.get(roomId).setProgressCheck(true);
	                    	roomMap.get(roomId).getGaming().setStartCheck(true);
	                    	

	                    	//턴 시작
	                    	Gaming redisGame = roomMap.get(roomId).getGaming();
	                    	redisGame.setStarTurnTime(LocalDateTime.now());

	                    	
	                    	String userId = roomMap.get(roomId).getUserList().get(redisGame.getCurrentTurn());
	                    	
	                    	
                    		result.put("gaming", true);
                    		result.put("time", 30);
                    		result.put("status", 0);
                			result.put("currentUser", userMap.get(userId).getUserNm());
                			result.put("round", redisGame.getRound());
                			result.put("turn", redisGame.getCurrentTurn());
                			result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
	                    		
	                    	for (String u : roomMap.get(roomId).getUserList()) 
	                    		sendMessage(userMap.get(u).getSession(), makeJson(result));
	                    		
	                    	
	                    	roomRepo.save(roomMap.get(roomId));  
	            		} 
	            		else if(roomMap.get(roomId).getRoomOwner().equals(session.getId()) && !gameStart) {
	
	            			result.put("stat", false);
	                		result.put("msg", "모든 유저가 Ready가 아님");
	                		
	                		for (String u : roomMap.get(roomId).getUserList()) 
	                			sendMessage(userMap.get(u).getSession(), makeJson(result));                		
	            			
	            		}
	            		//일반 유저일떄 ready나 ready 해제는 룸서비스 userclickStart가 처리
	            		else {
	            			
            				result.put("userId", session.getId());
            				result.put("userNm", userMap.get(session.getId()).getUserNm());
            				result.put("status", 0);
	            			result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
	            			
	            			if(roomMap.get(roomId).getUserReady().contains(session.getId())) {
	            				result.put("ready", true);

	                    		for (String u : roomMap.get(roomId).getUserList()) 
	                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
	            			}
	            			else {
	            				result.put("ready", false);

	                    		for (String u : roomMap.get(roomId).getUserList()) 
	                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
	            			}

	            		}
            		
        			}
            		       			
        		} //유저 이름 변경
        		else if(obj.get("roomStatus").equals("nameChg")) {
        			String userNm = (String) obj.get("userNm");
        			
        			userMap.get(session.getId()).setUserNm(userNm);
    				result.put("status", "nameChg");
    				result.put("userNm", userNm);        			
        			sendMessage(session, makeJson(result));
        				
        		} //방 이름 변경
        		else if(obj.get("roomStatus").equals("titleChg")) {
                    String roomId = userMap.get(session.getId()).getRoomId();
        			
        			if(!roomId.isEmpty()) {
            			//방장일때만 세팅 가능함
            			if(roomMap.get(roomId).getRoomOwner().equals(session.getId())) {
            				roomService.titleSet(roomMap.get(roomId), (String) obj.get("title"));
            				roomRepo.save(roomMap.get(roomId));
            				
            				result.put("status", 0);
            				result.put("roomTitle", roomMap.get(roomId).getRoomTitle() + "#" + roomId);
            				result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
                    		for (String u : roomMap.get(roomId).getUserList()) 
                    			sendMessage(userMap.get(u).getSession(), makeJson(result));

            			}
        			}
	
        		}        		
        		
            	//게임진행중일때
        		//한 유저가 다이스 전부 굴림
        		//2번의 기회동안 주사위를 더 던질지, 그만 굴릴지 선택
        		//주사위 굴리기가 완료되면 현재 보드판에서 삽입할 수 있는 판들 목록 출력
        		//삽입 완료되면 다른 유저에게 넘기면서 턴이 마무리됨
        		//모든 유저의 턴이 끝나면 라운드가 끝남 남은 라운드가 있을시 게임 계속 진행
        		//게임 진행 완료되면 계산 공유후 게임 종료
        		/*
        		 * !제약사항 한턴에 30초 이상씩 시간을 쓸 수 없음
        		 * 시간 오버될경우 현재 주사위 기준 가장 높은 점수를 주는말에 점수를 매김
        		 * 
        		 * 라운드가 남아있을시 true 없으면 false -> 게임 end
        		 * */
            	else if(obj.get("roomStatus").equals("gaming")) {
            		String roomId = userMap.get(session.getId()).getRoomId();

            		Gaming redisGame = roomMap.get(roomId).getGaming();
            		
            		if(redisGame.getRound() <13  ) {
            		
            			
            			if(gameService.checkUserTurn(session.getId(), roomMap.get(roomId).getUserList(), redisGame )) {
            				
            				
		            		if(obj.get("gameAction").equals("dice")) {
		            			//시간 지났는지 확인, 시간 지났을시 임의로 점수 배정후 턴 넘김
		            			if(gameService.timeOverCheck(redisGame)) {
		            				gameService.autoInsertScore(redisGame, session.getId());
		            				gameService.nextUser(roomMap.get(roomId).getUserList(), redisGame);
		            				
		            				if(redisGame.getRound() > 12) 
		            					result.put("gaming", false);
		            						
		            				else {
		            					result.put("gaming", true);
			                    		//턴 시작
			                    		roomMap.get(roomId).getGaming().setStarTurnTime(LocalDateTime.now());
			                    		roomMap.get(roomId).getGaming().setRolDiceCheck(0);
		            					
		            				}
		            				String userId = roomMap.get(roomId).getUserList().get(redisGame.getCurrentTurn());
		                			result.put("currentUser", userMap.get(session.getId()).getUserNm());
		                			result.put("nextUser", userMap.get(userId).getUserNm());
		                			result.put("status", "timeOut");
		                			result.put("round", redisGame.getRound());
		                			result.put("turn", redisGame.getCurrentTurn());
		                			result.put("diceList", gameService.getDiceList(redisGame));	            				
		                			result.put("board", roomMap.get(roomId).getGaming().getGameBoards().get(session.getId()));
		                			result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
		                			
		                			//방 전체 메시지에 보냄
		                    		for (String u : roomMap.get(roomId).getUserList()) 
		                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
	
		            				
		            			}else {
		            				
		            				//주사위 굴릴 수 있는지 체크후 처리함
			            			boolean ctrlResult = gameService.diceCtrl(redisGame, (String) obj.get("index"), session.getId());
			            			
			            			if(ctrlResult) {
			            				
			                			result.put("gaming", true);
			                			result.put("result", true);
			                			result.put("status", "dice");
			                			result.put("diceList", gameService.getDiceList(redisGame));
			                			result.put("chkBoard", gameService.calcScoreCheck(redisGame, redisGame.getGameBoards().get(session.getId()).getBoard()));
			                			result.put("currentUser", userMap.get(session.getId()).getUserNm());
			                			result.put("board", roomMap.get(roomId).getGaming().getGameBoards().get(session.getId()));
			                			result.put("diceRollCount", roomMap.get(roomId).getGaming().getRolDiceCheck());
			                			//방 전체 메시지에 보냄
			                    		for (String u : roomMap.get(roomId).getUserList()) 
			                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
			            				
			            			}
			            			else {
			                			result.put("gaming", true);
			                			result.put("result", false);
			                			result.put("msg", "주사위를 굴릴 수 없습니다");
			            				
			                			sendMessage(session,  makeJson(result));
			            				
			            			}
		            			}
		            			
		            		} // 
		            	
		            		else if(obj.get("gameAction").equals("score")) {
		            			
		            			//시간 지났는지 확인, 시간 지났을시 임의로 점수 배정후 턴 넘김
		            			if(gameService.timeOverCheck(redisGame)) {
		            				
		            				gameService.autoInsertScore(redisGame, session.getId());
		            				gameService.nextUser(roomMap.get(roomId).getUserList(), redisGame);
		            				
		            				if(redisGame.getRound() > 12)
		            					result.put("gaming", false);
		            				else {
		            					result.put("gaming", true);
			                    		//턴 시작
			                    		roomMap.get(roomId).getGaming().setStarTurnTime(LocalDateTime.now());
			                    		roomMap.get(roomId).getGaming().setRolDiceCheck(0);
		            					
		            				}
		            				String userId = roomMap.get(roomId).getUserList().get(redisGame.getCurrentTurn());
		                			result.put("currentUser", userMap.get(session.getId()).getUserNm());
		                			result.put("nextUser", userMap.get(userId).getUserNm());
		                			result.put("status", "timeOut");
		                			result.put("round", redisGame.getRound());
		                			result.put("turn", redisGame.getCurrentTurn());
		                			result.put("diceList", gameService.getDiceList(redisGame));	            				
		                			result.put("board", roomMap.get(roomId).getGaming().getGameBoards().get(session.getId()));
		                			result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
		                			
		                			//방 전체 메시지에 보냄
		                    		for (String u : roomMap.get(roomId).getUserList()) 
		                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
	
		            				
		            			} else {
		            			
			            			boolean ctrlResult = gameService.insertScore(session.getId(), redisGame, (String) obj.get("insertSpace")) ;
			            			
			            			if(ctrlResult) {
			            				
			            				//다음 유저에게 넘김
			                			gameService.nextUser(roomMap.get(roomId).getUserList(), redisGame);
			            				
			            				if(redisGame.getRound() > 12) {
			            					result.put("gaming", false);
			            					gameService.calcEndscore(redisGame, roomMap.get(roomId).getUserList(), userMap);
			            					result.put("score", redisGame.getScore());
			            					result.put("status", "end");
				                			//방 전체 메시지에 보냄
				                    		for (String u : roomMap.get(roomId).getUserList()) 
				                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
			            					
			            				}
			            					
			            				else {
			            					result.put("gaming", true);
				                    		//턴 시작
				                    		roomMap.get(roomId).getGaming().setStarTurnTime(LocalDateTime.now());
				                    		roomMap.get(roomId).getGaming().setRolDiceCheck(0);
			            					
			            				
			                			
			                			result.put("result", true);
			            				String userId = roomMap.get(roomId).getUserList().get(redisGame.getCurrentTurn());
			                			result.put("currentUser", userMap.get(session.getId()).getUserNm());
			                			result.put("nextUser", userMap.get(userId).getUserNm());
			                			result.put("status", "score");
			                			result.put("round", redisGame.getRound());
			                			result.put("turn", redisGame.getCurrentTurn());
			                			result.put("board", roomMap.get(roomId).getGaming().getGameBoards().get(session.getId()));
			                			result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
			                			           			
			                			//방 전체 메시지에 보냄
			                    		for (String u : roomMap.get(roomId).getUserList()) 
			                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
			                    		
			            				}
			                    					
			            			}
			            			else {
			                			result.put("gaming", true);
			                			result.put("result", false);
			                    		sendMessage(session, makeJson(result));
			            				
			            			}	  
		            			}
		            			
		            		}
		            		else if(obj.get("gameAction").equals("boardChk")) {
		            			
		            			
		            			for(String user : roomMap.get(roomId).getUserList()) {
		            			
		            				
                                    if(userMap.get(user).getUserNm().equals(obj.get("userNm"))) {
                                    	
                                    	
                                    	String userId = userMap.get(user).getId();
        	                			result.put("gaming", true);
        	                			result.put("status", "boardChk");
        	                			
        	                			result.put("board", roomMap.get(roomId).getGaming().getGameBoards().get(user));	  
        	                			
        	                			if(roomMap.get(roomId).getUserList().get(redisGame.getCurrentTurn()).equals(user) && redisGame.getRolDiceCheck()>0)
        	                			    result.put("chkBoard", gameService.calcScoreCheck(redisGame, redisGame.getGameBoards().get(user).getBoard()));

        	                			
        	                			sendMessage(session, makeJson(result));
        	                			
        	                			break;
		            				}
                                    
		            			}
		            			
		            		}

		                	//클라이언트가 현재 진행상황 요청할때
		                	else if(obj.get("roomStatus").equals("progress")) {
	
		            			result.put("score", redisGame.getScore());
	
		                    	for (String u : roomMap.get(roomId).getUserList()) 
		                    		sendMessage(userMap.get(u).getSession(), makeJson(result));
		            			
		                	}
		            		
		            		//! 강제 종료 후에 구현해야함
		                	else if(obj.get("roomStatus").equals("quit")) {
	
		            			result.put("score", redisGame.getScore());
	
		                    	for (String u : roomMap.get(roomId).getUserList()) 
		                    		sendMessage(userMap.get(u).getSession(), makeJson(result));
		            			
		                	}
	            		
            			} //정상적인 유저 인지 확인
	            		else if(obj.get("gameAction").equals("boardChk")) {
	            			
	            			
	            			for(String user : roomMap.get(roomId).getUserList()) {
	            			
	            				
                                if(userMap.get(user).getUserNm().equals(obj.get("userNm"))) {
                                	
                                	String userId = userMap.get(user).getId();
    	                			result.put("gaming", true);
    	                			result.put("status", "boardChk");
    	                			
    	                			result.put("board", roomMap.get(roomId).getGaming().getGameBoards().get(user));	  
    	                			
    	                			if(roomMap.get(roomId).getUserList().get(redisGame.getCurrentTurn()).equals(user) && redisGame.getRolDiceCheck()>0)
    	                			    result.put("chkBoard", gameService.calcScoreCheck(redisGame, redisGame.getGameBoards().get(user).getBoard()));

    	                			
    	                			sendMessage(session, makeJson(result));
    	                			
    	                			break;
	            				}
                                
	            			}
	            				
	            			
	            		}
	            		
            		} //게임이 끝나있을때
            		else {
            			result.put("gaming", false);
            			result.put("score", redisGame.getScore());
            			//result.put("runningTime", "");
        				
            			//방 전체 메시지에 보냄
                		for (String u : roomMap.get(roomId).getUserList()) 
                			sendMessage(userMap.get(u).getSession(), makeJson(result));
            			
            			
                		roomMap.get(roomId).setGaming(null);
                		roomMap.get(roomId).setProgressCheck(false);
            		}

            	}    
            	


        	}
        		
        	
        }

        //sendMessage(session, makeJson(result));
        
        }
        catch (NullPointerException e) {
        	
        }   
        
     }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    	//접속시 로그 등록
    	userService.addConnLog(session);
    	//전체 세션 리스트에 등록, 방은 아직 배정되지 않음
    	sessionList.add(session);

    	userMap.put(session.getId(), User.builder().session(session)
    												.userid(session.getId())
    														.build());
    	
    	
    	HashMap<String, Object> result = new HashMap();
    	/*
    	
    	result.put("status", "numofUser");
    	result.put("numOfuser", sessionList.size());
    	
    	for(WebSocketSession s: sessionList) 
    		sendMessage(s, makeJson(result));
    		
    		*/
    		
    		
    	
    	result.put("status", "init");
    	result.put("userNm", userMap.get(session.getId()).getUserNm());
    	result.put("userId", session.getId());
    	sendMessage(session, makeJson(result));
    	
    	userService.addConnLog(session);
    	
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	//접속 해제 로그 등록
    	userService.addDisLog(session);
    	//세션 리스트에서 제외
    	sessionList.remove(session);
    	HashMap result = new HashMap();
    	    	
    	String roomId = userMap.get(session.getId()).getRoomId() ;
    	
    	if(roomId != null) {
    		
    		int resultCheck =roomService.userOut(roomMap.get(userMap.get(session.getId()).getRoomId()), session.getId());
    		
    		if(roomMap.get(roomId).getUserList().size() > 0) {
    			
    			
	    		result.put("status", "close");
	    		result.put("userId", session.getId());  
	    		result.put("mesg", userMap.get(session.getId()).getUserNm()+"님의 세션 연결이 끊겼습니다");
	    		result.put("userList", roomService.getNameAndReady(roomMap.get(roomId), userMap));
	    		
	    		if(roomMap.get(roomId).isProgressCheck()) {

	    			closeGame(roomMap.get(roomId).getUserList(), roomMap.get(roomId).getGaming());
	    			String userId = roomMap.get(roomId).getUserList().get(roomMap.get(roomId).getGaming().getCurrentTurn());
	    			
	    			result.put("currentUser", userMap.get(userId).getUserNm());
	    		}

	    		for (String u : roomMap.get(roomId).getUserList()) 
	    			sendMessage(userMap.get(u).getSession(), makeJson(result)); 
	    		
	    		roomRepo.save(roomMap.get(roomId));
    		
    		}
    		else {
				roomRepo.delete(roomMap.get(roomId));
				//roomMap.get(roomId).getUserList().clear();
				roomMap.replace(roomId, null);     			
    		}

    	}
    	
		userMap.remove(session.getId());

    }
   
    //게임중 유저가 나갔을때 턴 처리해서 게임 이어가는 함수
	private void closeGame(List<String> userList, Gaming redisGame) {
		
		
		if(userList.size() <= redisGame.getCurrentTurn()) {
			redisGame.setCurrentTurn(0);
			redisGame.setRound(redisGame.getRound()+1);
			
		}
		else 
			redisGame.setCurrentTurn(redisGame.getCurrentTurn()+1);

	}
	
	//현재 게임 상태 객체로 만들어서 반환
	private TextMessage makeJson(HashMap data) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(data);
			return new TextMessage(json) ;
		} catch (JsonProcessingException e) {
			
			return null;
		}
	}
	
	//session 연결 상태 학인후 메시지 보냄
	private void sendMessage(WebSocketSession session, TextMessage mesg) {
		if (session != null) {
			if(session.isOpen() && mesg !=null)
				try {
					session.sendMessage(mesg);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		}
	}
	//방 아이디 확인후 방에 들어가 있는 유저들에게 메시지 보냄 !안정성을 위해 session 연결 상태를 확인후 보내기로함
	/*
	private void sendMessage(String roomId, TextMessage mesg) {
		if (roomMap.get(roomId) != null && mesg !=null) {
			
			for(String userId : roomMap.get(roomId).getUserList()) {
				try {
					userMap.get(userId).getSession().sendMessage(mesg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}*/
	
	//jsoon 파싱 함수
	private static JSONObject jsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	//턴 넘기는 부분 공통 묶음
	private void nextTurn() {
		
	}
}
