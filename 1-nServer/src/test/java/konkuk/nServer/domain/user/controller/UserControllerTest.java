package konkuk.nServer.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.user.domain.AccountType;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.SexType;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.dto.requestForm.RequestUserSignup;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.domain.user.service.UserService;
import konkuk.nServer.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("/user/health 확인")
    void test() throws Exception {
        // expected
        mockMvc.perform(get("/user/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Spring server is running..."))
                .andDo(print()); // http 요청 로그 남기기
        /*
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "글 제목입니다.")
                        .param("content", "글 내용입니다.")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print()); // http 요청 로그 남기기
                */

        /*
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", Matchers.is(2))) // json 배열 사이즈 검증
                .andExpect(jsonPath("$[0].id()").value(post.getId()))
                .andDo(print());
         */

    }

    @Test
    @DisplayName("학생 회원가입(필수 항목 안채우기)")
    void studentSignupNotFillRequired() throws Exception {
        // given
        RequestUserSignup requestUserSignup = RequestUserSignup.builder()
                .email("asdf@konkuk.ac.kr")
                .accountType("password")
                .password("testPassword")
                .nickname("ithinkso")
                .name("tester")
                .role("student")
                .build();
        String content = objectMapper.writeValueAsString(requestUserSignup);

        // expected
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("400"))
                .andExpect(jsonPath("$.message").value("검증 실패"))
                .andExpect(jsonPath("$.validation.phone").value("phone은 필수항목입니다."))
                .andDo(print());

        assertEquals(0L, userRepository.count());
    }

    @Test
    @DisplayName("학생 회원가입(선택 항목 안채우기)")
    void studentSignupNotFillOptional() throws Exception {
        // given
        RequestUserSignup requestUserSignup = RequestUserSignup.builder()
                .email("asdf@konkuk.ac.kr")
                .accountType("password")
                .password("testPassword")
                .nickname("ithinkso")
                .name("tester")
                .role("student")
                .phone("01012345678")
                .build();
        String content = objectMapper.writeValueAsString(requestUserSignup);

        // expected
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, userRepository.count());

        User user = userRepository.findAll().get(0);
        assertEquals(AccountType.PASSWORD, user.getAccountType());
        assertEquals(Role.ROLE_STUDENT, user.getRole());
        assertEquals("asdf@konkuk.ac.kr", user.getEmail());
        assertEquals("ithinkso", user.getNickname());
        assertEquals("tester", user.getName());
        assertEquals("01012345678", user.getPhone());
        assertNull(user.getSexType());
        assertNull(user.getMajor());
    }

    @Test
    @DisplayName("학생 회원가입(모든 항목 채우기)")
    void studentSignupAllFill() throws Exception {
        // given
        RequestUserSignup requestUserSignup = RequestUserSignup.builder()
                .email("asdf@konkuk.ac.kr")
                .accountType("password")
                .password("testPassword")
                .nickname("ithinkso")
                .name("tester")
                .role("student")
                .phone("01012345678")
                .major("CS")
                .sexType("man")
                .build();
        String content = objectMapper.writeValueAsString(requestUserSignup);

        // expected
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, userRepository.count());

        User user = userRepository.findAll().get(0);
        assertEquals(AccountType.PASSWORD, user.getAccountType());
        assertEquals(Role.ROLE_STUDENT, user.getRole());
        assertEquals("asdf@konkuk.ac.kr", user.getEmail());
        assertEquals("ithinkso", user.getNickname());
        assertEquals("tester", user.getName());
        assertEquals("01012345678", user.getPhone());
        assertEquals(SexType.MAN, user.getSexType());
        assertEquals("CS", user.getMajor());
    }


    @Test
    @DisplayName("닉네임 중복 검사")
    void nickNameDuplicate() throws Exception {
        // given
        RequestUserSignup requestUserSignup = RequestUserSignup.builder()
                .email("asdf@konkuk.ac.kr")
                .accountType("password")
                .password("testPassword")
                .nickname("ithinkso")
                .name("tester")
                .role("student")
                .phone("01012345678")
                .major("CS")
                .sexType("man")
                .build();

        userService.signup(requestUserSignup);

        // expected
        mockMvc.perform(get("/user/duplication/nickname/{nickname}", "ithinkso")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDuplication").value(true))
                .andDo(print()); // http 요청 로그 남기기

        mockMvc.perform(get("/user/duplication/nickname/{nickname}", "noNickname")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDuplication").value(false))
                .andDo(print()); // http 요청 로그 남기기
    }

    @Test
    @DisplayName("로그인(password)")
    void loginByPassword() throws Exception {
        // given
        RequestUserSignup requestUserSignup = RequestUserSignup.builder()
                .email("asdf@konkuk.ac.kr")
                .accountType("password")
                .password("testPassword")
                .nickname("ithinkso")
                .name("tester")
                .role("student")
                .phone("01012345678")
                .major("CS")
                .sexType("man")
                .build();

        userService.signup(requestUserSignup);

        Map<String, String> map = Map.of("email", "asdf@konkuk.ac.kr", "password", "testPassword");
        String content = objectMapper.writeValueAsString(map);

        // expected
        MvcResult response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andDo(print())
                .andReturn();

        String jwt = response.getResponse().getHeader("Authorization");

        Long userId = jwtTokenProvider.validateAndGetUserId(jwt.replace("Bearer ", ""));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없음"));

        assertEquals(AccountType.PASSWORD, user.getAccountType());
        assertEquals(Role.ROLE_STUDENT, user.getRole());
        assertEquals("asdf@konkuk.ac.kr", user.getEmail());
        assertEquals("ithinkso", user.getNickname());
        assertEquals("tester", user.getName());
        assertEquals("01012345678", user.getPhone());
        assertEquals(SexType.MAN, user.getSexType());
        assertEquals("CS", user.getMajor());
    }


}