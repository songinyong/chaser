/**
 * rest api 호출 컨트롤러
 * 
 * 제작자:송인용
 **/

package com.service.web;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.webservice.GameService;
import com.service.webservice.RoomService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@CrossOrigin(origins="*")
public class GameRestController {

	
	private final GameService gameService ;
	
	private final RoomService roomService;
	
	
	/**
	 * Redis에 저장된 방 정보 목록을 조회하는 api 
	 * */
	@GetMapping("/roomList")
	public ResponseEntity<JSONObject> getRoomList() {
		
		return roomService.getRoomList();
	}
	
}
