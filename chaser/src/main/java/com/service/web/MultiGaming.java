package com.service.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.service.domain.Board;
import com.service.domain.Dice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MultiGaming implements Gaming {
	private String roomId ;
	
	private int round = 1 ;

	//유저의 턴 시작 시간
	LocalDateTime starTurnTime ;	
	///////////////설정 정보 End///////////////
	
	
	//게임 시작 했는지 확인
	boolean startCheck = false ;
	
	//게임 시작 시간
	LocalDateTime startGameTime = LocalDateTime.now();	

	
	//각 유저의 게임 판
	HashMap<String, Board> gameBoards = new HashMap<String, Board>();
	
	//각 유저의 게임 판
	HashMap<String, Integer> score = new HashMap<String, Integer>();    
	
	List<Dice> diceList = new ArrayList<Dice>();
	
	//현재 플레이어가 주사위 몇번 굴렸는지 확인
	int rolDiceCheck = 0;
	
	@Builder()
	public MultiGaming(String roomId  ) {
		this.roomId = roomId;
		
		for(int i=0; i<5; i++)
			diceList.add(new Dice());
	}
}
