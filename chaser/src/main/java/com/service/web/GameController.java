package com.service.web;
/*로컬에서 테스트용*/

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@CrossOrigin(origins="*")
public class GameController {

		
	@GetMapping("/")
	public String startIndex() {
		return "view2";
	}
		
}
