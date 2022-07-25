package com.service.web;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import com.service.domain.Board;
import com.service.domain.Dice;

public interface Gaming {

	public void setStarTurnTime(LocalDateTime turnTime);
	
	public int getRound();
	
	public void setRound(int round);
	
	public LocalDateTime getStarTurnTime() ;
	
	public List<Dice> getDiceList();

	public int getRolDiceCheck() ;
	
	public void setRolDiceCheck(int i) ;
	
	public HashMap<String, Integer> getScore() ;
	
	public void setStartCheck(boolean check) ;
	
	public HashMap<String, Board> getGameBoards();
	

}
