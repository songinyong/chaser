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
	
	public List<Dice> getDiceList();

	public int getRolDiceCheck() ;
	
	public void setRolDiceCheck(int i) ;
	
	public boolean isStartCheck();
	
	public void setStartCheck(boolean check) ;
	
    public int getCurrentTurn();
    
    public void setCurrentTurn(int i);

}
