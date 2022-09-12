/*
 * Sprint3부터
 * 소켓 테스트 코드 추가
 * 
 * */

package service.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.service.web.GameRestController;



@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebMvcTest(GameRestController.class)
class GameControllerTest {

    private MockMvc mockMvc; // mockMvc 생성
    
    //roomList 호출 테스트
    @Test
	public void roomList_test() throws Exception {

        mockMvc.perform(
                get("/roomList")) //해당 url로 요청을 한다.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(print());// 응답값 print


    }
}
