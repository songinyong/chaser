/*
 * Gaming 인터페이스
 * 멀티게임용 게이밍 클래스와 함께 머신러닝 기반 봇용 게이밍 클래스가 생성될 수 있음 
 * */

package com.service.web;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import com.service.domain.Board;
import com.service.domain.Dice;

public interface Gaming {

	public String getRoomId();
	
	public int getRound();
	
	public void setRound(int round);
	
	public LocalDateTime getStarTurnTime() ;
	
	public void setStarTurnTime(LocalDateTime turnTime);
	
	public HashMap<String, Board> getGameBoards();
	
	public HashMap<String, Integer> getScore() ;
	
	public void setScore(int score, String userId);
	
	public List<Dice> getDiceList();

	public int getRolDiceCheck() ;
	
	public void setRolDiceCheck(int i) ;
	
	public boolean isStartCheck();
	
	public void setStartCheck(boolean check) ;
	
    public int getCurrentTurn();
    
    public void setCurrentTurn(int i);

}
