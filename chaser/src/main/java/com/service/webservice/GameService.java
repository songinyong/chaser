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
	 
	boolean diceCtrl(Gaming redisGame, String indexs, String userId);

	public HashMap<String, Integer> calcScoreCheck(Gaming redisGame, HashMap<String, Integer> board);
	
	public List<String> boardCheck(String userId, Gaming redisGame);
		
	boolean insertScore(String userId, Gaming redisGame, String insertSpace);
	
	public HashMap<Integer, Integer> getDiceList(Gaming gaming) ;
	 
	public void nextUser(List<String> userList, Gaming redisGame);
	
	public boolean checkUserTurn(String userId, List<String> userList, Gaming redisGame);
	
	public boolean timeOverCheck(Gaming redisGame);
	
	public void autoInsertScore(Gaming redisGame, String userId);

}
