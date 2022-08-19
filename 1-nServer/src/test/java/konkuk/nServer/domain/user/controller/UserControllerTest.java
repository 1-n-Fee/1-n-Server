package konkuk.nServer.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.account.domain.AccountType;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.SexType;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.domain.user.service.UserService;
import konkuk.nServer.exception.ExceptionEnum;
import konkuk.nServer.security.jwt.JwtClaim;
import konkuk.nServer.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("/health 확인")
    void test() throws Exception {
        // expected
        mockMvc.perform(get("/health"))
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
        UserSignup userSignup = getUserSignupDto();
        userSignup.setPhone(null);
        String content = objectMapper.writeValueAsString(userSignup);

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
        UserSignup userSignup = getUserSignupDto();
        userSignup.setMajor(null);
        userSignup.setSexType(null);
        String content = objectMapper.writeValueAsString(userSignup);

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
        UserSignup userSignup = getUserSignupDto();
        String content = objectMapper.writeValueAsString(userSignup);

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
        UserSignup userSignup = getUserSignupDto();

        userService.signup(userSignup);

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
        UserSignup userSignup = getUserSignupDto();

        userService.signup(userSignup);

        Map<String, String> map = Map.of("email", "asdf@konkuk.ac.kr", "password", "pwpw!123");
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

        JwtClaim jwtClaim = jwtTokenProvider.validate(jwt.replace("Bearer ", ""));
        User user = userRepository.findById(jwtClaim.getId())
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

    @Test
    @DisplayName("로그인 실패(password)")
    void failLoginByPassword() throws Exception {
        // given
        Map<String, String> map = Map.of("email", "asdf@konkuk.ac.kr", "password", "pwpw!123");
        String content = objectMapper.writeValueAsString(map);

        // expected
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(ExceptionEnum.FAIL_LOGIN.getCode()))
                .andExpect(jsonPath("$.message").value(ExceptionEnum.FAIL_LOGIN.getMessage()))
                .andDo(print());

        assertEquals(0L, userRepository.count());
    }

    @Test
    @DisplayName("비밀번호 변경")
    void changePassword() throws Exception {
        // given
        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user.getId(), user.getRole());

        Map<String, String> map = Map.of("newPassword", "password!123");
        String content = objectMapper.writeValueAsString(map);

        // expected
        mockMvc.perform(patch("/user/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andDo(print()); // http 요청 로그 남기기

        user = userRepository.findAll().get(0);
        assertTrue(passwordEncoder.matches("password!123", user.getPassword().getPassword()));
    }


    @Test
    @DisplayName("임시 비밀번호 발급")
    void getTempPassword() throws Exception {
        // given
        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        Map<String, String> map =
                Map.of("name", "tester",
                        "email", "asdf@konkuk.ac.kr",
                        "phone", "01012345678");
        String content = objectMapper.writeValueAsString(map);

        // expected
        MvcResult result = mockMvc.perform(post("/user/find/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tempPassword").exists())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String tempPassword = (String) objectMapper.readValue(responseBody, Map.class)
                .get("tempPassword");

        User user = userRepository.findAll().get(0);

        assertEquals(1L, userRepository.count());
        assertTrue(passwordEncoder.matches(tempPassword, user.getPassword().getPassword()));


        // 회원 찾기 실패
        map = Map.of("name", "tester1",
                "email", "asdf@konkuk.ac.kr",
                "phone", "01012345678");
        content = objectMapper.writeValueAsString(map);

        mockMvc.perform(post("/user/find/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ExceptionEnum.NO_FOUND_USER.getCode()))
                .andExpect(jsonPath("$.message").value(ExceptionEnum.NO_FOUND_USER.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 찾기")
    void findEmail() throws Exception {
        // given
        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        Map<String, String> map =
                Map.of("name", "tester",
                        "phone", "01012345678");
        String content = objectMapper.writeValueAsString(map);

        // expected
        mockMvc.perform(post("/user/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("asdf@konkuk.ac.kr"))
                .andDo(print());

        // 회원 찾기 실패
        map = Map.of("name", "tester1", "phone", "01012345678");
        content = objectMapper.writeValueAsString(map);

        mockMvc.perform(post("/user/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ExceptionEnum.NO_FOUND_USER.getCode()))
                .andExpect(jsonPath("$.message").value(ExceptionEnum.NO_FOUND_USER.getMessage()))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("닉네임 변경")
    void changeNickname() throws Exception {
        // given
        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);

        Map<String, String> map = Map.of("nickname", "newNick");
        String content = objectMapper.writeValueAsString(map);

        // expected
        mockMvc.perform(patch("/user/change/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andDo(print()); // http 요청 로그 남기기

        user = userRepository.findAll().get(0);
        assertEquals(user.getNickname(), "newNick");
    }

    @Test
    @DisplayName("성별 변경")
    void changeSexType() throws Exception {
        // given
        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);

        Map<String, String> map = Map.of("sexType", "woman");
        String content = objectMapper.writeValueAsString(map);

        // expected
        mockMvc.perform(patch("/user/change/sexType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andDo(print()); // http 요청 로그 남기기

        user = userRepository.findAll().get(0);
        assertEquals(user.getSexType(), SexType.WOMAN);
    }

    @Test
    @DisplayName("사용자 정보 조회")
    void getUserInfo() throws Exception {
        // given
        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);

        // expected
        mockMvc.perform(get("/user/info")
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("asdf@konkuk.ac.kr"))
                .andExpect(jsonPath("$.accountType").value("PASSWORD"))
                .andExpect(jsonPath("$.nickname").value("ithinkso"))
                .andExpect(jsonPath("$.name").value("tester"))
                .andExpect(jsonPath("$.role").value("ROLE_STUDENT"))
                .andExpect(jsonPath("$.phone").value("01012345678"))
                .andExpect(jsonPath("$.major").value("CS"))
                .andExpect(jsonPath("$.sexType").value("MAN"))
                .andDo(print()); // http 요청 로그 남기기
    }

    private UserSignup getUserSignupDto() {
        return UserSignup.builder()
                .email("asdf@konkuk.ac.kr")
                .accountType("password")
                .password("pwpw!123")
                .nickname("ithinkso")
                .name("tester")
                .role("student")
                .phone("01012345678")
                .major("CS")
                .sexType("man")
                .build();
    }


}