/*
 * 멀티게임 버전 게임 서비스 구현
 * */

package com.service.webservice;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.domain.Board;
import com.service.domain.Dice;
import com.service.domain.redis.RoomRepository;
import com.service.web.Gaming;
import com.service.web.MultiGaming;



@Service
public class MultiGameServiceImpl implements GameService{

	
	@Autowired
	public RoomRepository roomRepo;
	
	Random random = new Random();
	
	//게임 한판당 시간
	private final int TURNTIME = 30;
	
	
	//!dto 추가 작업 해야함
	public Gaming gameStart(String roomId, JSONObject gameSet) {
	
		Gaming redisGame = MultiGaming.builder()
				.roomId(roomId)
				.build();
		
				return redisGame;
			

	}
		
	public boolean diceCtrl(Gaming redisGame, String indexs) {
		
		//라운드가 남았는지 테스트
		if(redisGame.getRound() < 13) {
			
			//턴 시간 오버되지 않았을시
			if(!timeOverCheck(redisGame)) {
				
				if(redisGame.getRolDiceCheck() == 0) {
					rolAllDice(redisGame);
					redisGame.setRolDiceCheck(1);
				}
					
				else if(redisGame.getRolDiceCheck() > 0 && redisGame.getRolDiceCheck() <3) {
					
					for(String s : indexs.split("")) 
						rolDice(redisGame, Integer.parseInt(s)-1);

					redisGame.setRolDiceCheck(redisGame.getRolDiceCheck()+1);
				}

			}
			else {
				
				if(redisGame.getRolDiceCheck() == 0) 
					rolAllDice(redisGame);
				//시간 오버일경우 임의적으로 점수 계산후 다음 차례에게 넘김
				return false ;
			}
			
			return true ;
		}
		else {
			//게임 엔드
			return false ;
		}
				

	}
	

	// 주사위 전부 굴림
	private void rolAllDice(Gaming gaming) {
		for( Dice d : gaming.getDiceList()) 
			d.setNum(random.nextInt(6)+1);
			
	}
	
	// 특정 인덱스 주사위만 굴림
	private void rolDice(Gaming gaming, int index) {
		gaming.getDiceList().get(index).setNum(random.nextInt(6)+1);
		
	}
	
	//현재 주사위 상태를 hashMap에 담아서 보여줌
	public HashMap<Integer, Integer> getDiceList(Gaming gaming) {
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		int count = 1;
		for(Dice d : gaming.getDiceList()) {
			result.put(count, d.getNum());
			count++ ;
		}
		
		return result ;
		
		
	}
	
	
	
	
	// 게임 남은 시간초 비교 true면 시간초 over되어 round 줄임, false면 아직 라운드 진행중
	private boolean timeOverCheck(Gaming redisGame) {
		LocalDateTime startTime =redisGame.getStarTurnTime();
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		Duration duration = Duration.between(startTime, currentTime);

				
		if(duration.getSeconds() > 30) {
			return true;
		}
		else {
			return false ;
		}
	
	}
	
	public HashMap<String, Integer> calcScoreCheck(Gaming redisGame) {
		
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		
		//중복된 길이 체크
		Set<Integer> chkNum = new HashSet<Integer>();
		
		//각 숫자마다 몇번 나왔는지 체크
		int[] countNum = {0,0,0,0,0,0};
		
		for(Dice d : redisGame.getDiceList() ) {
			chkNum.add(d.getNum());
			if(d.getNum() == 1)
				countNum[0]++ ;
			else if(d.getNum() == 2)
				countNum[1]++ ;
			else if(d.getNum() == 3)
				countNum[2]++ ;
			else if(d.getNum() == 4)
				countNum[3]++ ;
			else if(d.getNum() == 5)
				countNum[4]++ ;
			else if(d.getNum() == 6)
				countNum[5]++ ;
		}
		
		if( chkNum.size()==1)
			result.put("Chase off", 50);
		else
			result.put("Chase off", 0);
		
		
		if(chkNum.size()==5 && chkNum.contains(1)) {
			result.put("Straight", 50);
			result.put("Even Straight", 0);
			
		}
		else if(chkNum.size()==5 && chkNum.contains(6)) {
			result.put("Straight", 0);
			result.put("Even Straight", 30);
			
		}
		else {
			result.put("Straight", 0);
			result.put("Even Straight", 0);			
			
		}
		

		if(chkNum.size()==2 ) {
			int fourDiceCheck = 0 ;
			for(int i =1; i<5; i++)
				if(redisGame.getDiceList().get(0).equals(redisGame.getDiceList().get(i)))
					fourDiceCheck++;
			
			//fourDice
			if(fourDiceCheck == 0 || fourDiceCheck ==3) {
				result.put("Four Dice", redisGame.getDiceList().stream().collect(Collectors.summingInt(d -> d.getNum())));
				result.put("Full House", 0);
				result.put("Choice", 0);
			}
			else {
				result.put("Four Dice", 0);
				result.put("Full House", redisGame.getDiceList().stream().collect(Collectors.summingInt(d -> d.getNum())));
				result.put("Choice", redisGame.getDiceList().stream().collect(Collectors.summingInt(d -> d.getNum())));				
			}
				
		}
		
		if(chkNum.size()==3 ) {
			result.put("Choice", 0);
		}
		else {
			if(!result.containsKey("Choice"))
				result.put("Choice", 0);
		}
		
		result.put("Aces", countNum[0]*1);
		result.put("Two Beans", countNum[1]*2);
		result.put("Three Beans", countNum[2]*3);
		result.put("Four Beans", countNum[3]*4);
		result.put("Five Beans", countNum[4]*5);
		result.put("Six Beans", countNum[5]*6);
		
		return result ;
			
	}
	
	//점수를 넣을 수 있는 보드판 제공
	public List<String> boardCheck(String userId, Gaming redisGame) {
		
		List<String> result = new ArrayList<String>();
		
		Board board = redisGame.getGameBoards().get(userId);
		
		for (Entry<String, Integer> entry : board.getBoard().entrySet() ) {
			if(entry.getValue() == -1)
				result.add(entry.getKey());

		}
		
		return result ;
		
	}
	
	public boolean insertScore(String userId, Gaming redisGame, String insertSpace) {
		
		Board board = redisGame.getGameBoards().get(userId);
		
		List<String> result = boardCheck(userId, redisGame) ;
		
		//점수 넣을 수 있는 판이면, 그자리에 점수 계산해서 넣는다
		if(result.contains(insertSpace)) {
			
			redisGame.getGameBoards().get(userId).getBoard().replace(insertSpace, calcScoreCheck(redisGame).get(insertSpace));
			return true ;
		}
		else
			return false ;

	}
	


	/*Round 끝날때마다 클리어 타임이 얼마나 걸렸나 확인
	private void saveClearTime(MultiGaming redisGame) {
		
		LocalDateTime startTime =redisGame.getStarRoundTime();
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		Duration duration = Duration.between(startTime, currentTime);
		
		
		//나노초를 밀리초로 변환
		int saveTime = duration.getNano()/1000000 ;
		int saveSecondTime = Long.valueOf(duration.getSeconds()).intValue() * 1000 ;
		
		redisGame.setClearTime(redisGame.getClearTime() + saveTime + saveSecondTime);
		//System.out.println("savedTime"+redisGame.getClearTime());
		
	} */
	

}
