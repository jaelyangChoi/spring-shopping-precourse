package shopping.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import shopping.repository.MemberRepository;
import shopping.web.MemberController.TokenResponse;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberAcceptanceTest {

    @LocalServerPort
    private int port;
    private String url;

    @Autowired
    private RestClient.Builder builder;
    private RestClient client;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port;
        client = builder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }


    @Test
    void 회원가입_성공시_토큰발급() {
        var request = new CreateMemberRequest("test@naver.com", "123");
        var response = 요청("/api/members/register", request);

        log.info("응답값 : {}", response.getBody());
        assertTrue(response.getBody().toString().contains("token"));
    }

    @Test
    void 로그인_성공시_토큰발급() {
        //회원가입
        var request1 = new CreateMemberRequest("test@naver.com", "123");
        var request2 = new CreateMemberRequest("test2@naver.com", "1234");
        var 회원가입1_응답 = 요청("/api/members/register", request1);
        요청("/api/members/register", request2);

        //로그인 요청 - 성공
        var 로그인1_응답 = 요청("/api/members/login", request1);
        var 로그인2_응답 = 요청("/api/members/login", request2);

        log.info("로그인1_응답 : {}", 로그인1_응답.getBody());
        log.info("response_success2 : {}", 로그인2_응답.getBody());

        //토큰 값이 발행된다.
        assertThat(로그인1_응답.getBody().token()).isNotEqualTo("login fail");

        //회원마다 토큰 값이 다르다.
        assertThat(로그인2_응답.getBody().token()).isNotEqualTo(로그인1_응답.getBody().token());

        //회원가입 시 발행한 토큰과 로그인 시 발행한 토큰이 같다.
        assertThat(회원가입1_응답.getBody().token()).isNotEqualTo(로그인1_응답.getBody().token());


        //로그인 요청 - 실패
        request1 = new CreateMemberRequest("test@naver.com", "long password");
        var response_fail = 요청("/api/members/login", request1);

        log.info("response_fail : {}", response_fail.getBody());
        assertThat(response_fail.getBody().token()).isEqualTo("login fail");
    }

    record CreateMemberRequest(String email, String password) {
    }

    @NotNull
    private ResponseEntity<TokenResponse> 요청(String path, CreateMemberRequest request) {
        return client
                .post()
                .uri(url + path)
                .body(request)
                .retrieve()
                .toEntity(TokenResponse.class);
    }

}
