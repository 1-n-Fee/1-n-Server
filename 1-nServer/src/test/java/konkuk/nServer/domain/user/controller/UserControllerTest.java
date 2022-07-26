package konkuk.nServer.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.user.domain.AccountType;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.SexType;
import konkuk.nServer.domain.user.domain.Student;
import konkuk.nServer.domain.user.dto.requestForm.RequestSignupForm;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.StudentRepository;
import konkuk.nServer.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private StudentRepository studentRepository;

    @Autowired
    private StoremanagerRepository storemanagerRepository;

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        // expected
        mockMvc.perform(get("/user/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
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

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "글 제목입니다.")
                        .param("content", "글 내용입니다.")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("json의 title 검증"))
                .andDo(print()); // http 요청 로그 남기기
         */
    }

    @Test
    @DisplayName("학생 회원가입")
    void studentSignup() throws Exception {

        JSONObject jsonContent = new JSONObject();
        jsonContent.put("email", "asdf@konkuk.ac.kr");
        jsonContent.put("accountType", "password");
        jsonContent.put("password", "testPassword");
        jsonContent.put("nickname", "ithinkso");
        jsonContent.put("name", "tester");
        jsonContent.put("role", "student");

        // 1. 필수 항목 안채우기
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent.toString())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("400"))
                .andExpect(jsonPath("$.message").value("검증 실패"))
                .andExpect(jsonPath("$.validation.phone").value("phone은 필수항목입니다."))
                .andDo(print());

        // 2. 선택항목 안채우기
        jsonContent.put("phone", "01012345678");
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent.toString())
                )
                .andExpect(status().isCreated())
                .andDo(print());

        jsonContent.put("major", "CS");
        jsonContent.put("sexType", "man");

        // 3. 선택항목까지 다 채우기
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent.toString())
                )
                .andExpect(status().isCreated())
                .andDo(print());


        Student student = studentRepository.findAll().get(0);

        assertEquals(AccountType.PASSWORD, student.getAccountType());
        assertEquals(Role.ROLE_STUDENT, student.getRole());
        assertEquals("asdf@konkuk.ac.kr", student.getEmail());
        assertEquals("ithinkso", student.getNickname());
        assertEquals("tester", student.getName());
        assertEquals("01012345678", student.getPhone());
        assertEquals(SexType.MAN, student.getSexType());
        assertEquals("CS", student.getMajor());
    }

    @Test
    @DisplayName("닉네임 중복 검사")
    void nickNameDuplicate() throws Exception {
        // given
        RequestSignupForm requestSignupForm = RequestSignupForm.builder()
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

        userService.signup(requestSignupForm);

        // expected
        mockMvc.perform(get("/user/duplication/nickname/{nickname}", requestSignupForm.getNickname())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDuplication").value(true))
                .andDo(print()); // http 요청 로그 남기기
    }


}