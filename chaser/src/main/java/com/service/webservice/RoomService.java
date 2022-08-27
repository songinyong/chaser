package com.service.webservice;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import com.service.domain.jpa.User;
import com.service.domain.redis.Room;

public interface RoomService {
	public boolean userclickStart(Room room, String session);
	public boolean userJoin(Room room, String session);
	public int userOut(Room room, String session);
	public Room roomCreate(String roomId);
	public boolean ownerSet(Room room, String sessionId);
	public boolean titleSet(Room room, String title);
	public ResponseEntity<JSONObject> getRoomList();
	public HashMap<String, String> getNameAndReady(Room room, HashMap<String, User> name);
}
