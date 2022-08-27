package com.service.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.service.domain.jpa.User;
import com.service.domain.redis.Room;
import com.service.domain.redis.RoomRepository;
import com.service.web.MultiGaming;
import com.service.web.dto.RoomDto;



@Service
public class RoomServiceImpl implements RoomService {
	

	@Autowired
	public RoomRepository roomRepo;
	
	//방생성
	public Room roomCreate(String roomId) {
		//방에 아이디 부여
		Room room = new Room();
		room.setRoomId(roomId);
		room.setGaming(new MultiGaming());
		room.setRoomTitle(roomId);
		roomRepo.save(room);
		
		return room;
	}
	
	
	//start나 ready 클릭할때
	//게임 시작 조건이 충족될시 true, 아닐시 false 
	public boolean userclickStart(Room room, String sessionId) {
		if(room.getUserList().contains(sessionId) && !room.getRoomOwner().equals(sessionId) ) {
			if(!room.getUserReady().contains(sessionId)) 
				room.getUserReady().add(sessionId);
			else
				room.getUserReady().remove(sessionId);

			return false;
		}
		else if(room.getRoomOwner().equals(sessionId) ) {
			if(room.getUserReady().size()== room.getUserList().size()-1) {
			
				return true;
			}	
			else
				return false;

		}
		
		return false;
	}
	
	
	//user 들어올때
	public boolean userJoin(Room room, String sessionId) {
		
		if(!room.getUserList().contains(sessionId) ) {
			room.getUserList().add(sessionId);
			roomRepo.save(room);
			
			return true;
		}	
		else 
			return false;
		
	}

	//user 나갈때
	public int userOut(Room room, String sessionId) {
		
		if(room.getUserList().contains(sessionId) ) {
			room.getUserList().remove(sessionId);
			room.getUserReady().remove(sessionId);
			roomRepo.save(room);			
			
			if(room.getRoomOwner().equals(sessionId)) {
				if(!room.getUserList().isEmpty())
					ownerSet(room, room.getUserList().get(0));
				return 0;	
			}

			return 1;
		}	
		else 
			return -1;
		
	}
	
	//방장 설정
	public boolean ownerSet(Room room, String sessionId) {

			room.setRoomOwner(sessionId);
			return true;
	}
	
	
	//방제목 설정
	public boolean titleSet(Room room, String title) {

		room.setRoomTitle(title);
		return true;
    }
	
		

	//현재 생성된 방 목록들의 상태 보여줌
	public ResponseEntity<JSONObject> getRoomList() {
    	JSONObject resultObj = new JSONObject();  
    	
    	Iterator<Room> itr =  roomRepo.findAll().iterator();
    	List<RoomDto> roomList = new ArrayList<RoomDto>();
    	
    	
    	while(itr.hasNext()) {
    		Room room = itr.next() ;

    		if(room !=null && room.getUserList().size() > 0)
    			roomList.add(RoomDto.builder()
    				.roomId(room.getRoomId())
    				.startCheck(room.isProgressCheck())
    				.roomTitle(room.getRoomTitle())
    				.userList(room.getUserList())
    				.build()) ;
    		
    	}

    	
    	try {
    		resultObj.put("result","true");
    		resultObj.put("roomList", roomList);
    		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.ACCEPTED);
    	}
    	catch (Exception e) {
    		resultObj.put("result","false");
    		resultObj.put("reason",e);
    		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.ACCEPTED);
    	}
		
	}
	
	//이름 추가
	public HashMap<String, String> getNameAndReady(Room room, HashMap<String, User> name) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		for(String userId : room.getUserList()) {
			
			if(room.getUserReady().contains(userId))
			    result.put(name.get(userId).getUserNm(), "Ready");
			else if(room.getRoomOwner().equals(userId))
				result.put(name.get(userId).getUserNm(), "owner");
			else
				result.put(name.get(userId).getUserNm(), "");
		}
		
		return result;
				
	}

	
}
