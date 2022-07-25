package com.service.domain;

import java.util.HashMap;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {

	String userId ;
	
	HashMap<String, Integer> board = new HashMap<String, Integer>();
	
	@Builder()
	private Board(String userId) {
		this.userId = userId ;
		
		board.put("Aces" , -1);
		board.put("Two Beans" , -1);
		board.put("Three Beans" , -1);
		board.put("Four Beans" , -1);
		board.put("Five Beans" , -1);
		board.put("Six Beans" , -1);
		board.put("Choice" , -1);
		board.put("Full House" , -1);
		board.put("Four Dice" , -1);
		board.put("Even Straight" , -1);
		board.put("Straight" , -1);
		board.put("Chase off" , -1);
	}
}
