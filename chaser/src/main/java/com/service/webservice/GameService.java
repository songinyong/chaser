/*
 * 솔로모드 인터페이스 
 * */

package com.service.webservice;



import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import com.service.domain.Dice;
import com.service.web.Gaming;



public interface GameService {

	//룸 Id가 될 수도 있음
	Gaming gameStart(String sessionId,JSONObject gameSet);
	 
	boolean diceCtrl(Gaming redisGame, String indexs);

	HashMap<String, Integer> calcScoreCheck(Gaming redisGame) ;
	
	public List<String> boardCheck(String userId, Gaming redisGame);
		
	boolean insertScore(String userId, Gaming redisGame, String insertSpace);
	
	public HashMap<Integer, Integer> getDiceList(Gaming gaming) ;
	 
	

}
