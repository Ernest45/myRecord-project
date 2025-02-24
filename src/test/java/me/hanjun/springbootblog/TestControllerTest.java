package me.hanjun.springbootblog;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 테스트용 애플리케이션컨텍스트 생성
// 메인애플리케이션 클래스를 찾고 클래스에 포함된 빈을 찾은 다음
@AutoConfigureMockMvc // MockMVC 생성
        // 서버를 배포하지 않아도 테스트용 mvc환경을 만들어 요청 및 전송 응답하는 기능 제공
        // 즉 컨트롤러를 테스트할 때 사용되는 클래스
class TestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    //테스트 실행 전 각 각 mockmvc를 설정
    public void mockMvcSetUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();


    }

    @AfterEach
    //각 테스트 마다 멤버 테이블의 데이터 모두 삭제를 위해서
    public void cleanUp() {
        memberRepository.deleteAll();

    }

    @DisplayName("getAllMembers: 아티클조회에 성공한다.")
    @Test
    public void getAllMembers() throws Exception {

        // given

        final String url = "/test";

        Member savedMember = memberRepository.save(new Member(1L, "홍길동"));

        // when

        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then

        result
                .andExpect(status().isOk())
                // 응답의 0번째 db에서 저장한 값과 같은 지 확인
                .andExpect(jsonPath("$[0].id").value(savedMember.getId()))
                .andExpect(jsonPath("$[0].name").value(savedMember.getName()));
    }
}