package konkuk.nServer.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.user.domain.*;
import konkuk.nServer.domain.user.dto.requestForm.RequestSignupForm;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.StudentRepository;
import konkuk.nServer.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
    private StudentRepository studentRepository;

    @Autowired
    private StoremanagerRepository storemanagerRepository;

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean() {
        studentRepository.deleteAll();
        storemanagerRepository.deleteAll();
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
    }

    @Test
    @DisplayName("학생 회원가입(필수 항목 안채우기)")
    void studentSignupNotFillRequired() throws Exception {
        // given
        RequestSignupForm requestSignupForm = RequestSignupForm.builder()
                .email("asdf@konkuk.ac.kr")
                .accountType("password")
                .password("testPassword")
                .nickname("ithinkso")
                .name("tester")
                .role("student")
                .build();
        String content = objectMapper.writeValueAsString(requestSignupForm);

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

        assertEquals(0L, studentRepository.count());
    }

    @Test
    @DisplayName("학생 회원가입(선택 항목 안채우기)")
    void studentSignupNotFillOptional() throws Exception {
        // given
        RequestSignupForm requestSignupForm = RequestSignupForm.builder()
                .email("asdf@konkuk.ac.kr")
                .accountType("password")
                .password("testPassword")
                .nickname("ithinkso")
                .name("tester")
                .role("student")
                .phone("01012345678")
                .build();
        String content = objectMapper.writeValueAsString(requestSignupForm);

        // expected
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, studentRepository.count());

        Student student = studentRepository.findAll().get(0);
        assertEquals(AccountType.PASSWORD, student.getAccountType());
        assertEquals(Role.ROLE_STUDENT, student.getRole());
        assertEquals("asdf@konkuk.ac.kr", student.getEmail());
        assertEquals("ithinkso", student.getNickname());
        assertEquals("tester", student.getName());
        assertEquals("01012345678", student.getPhone());
        assertNull(student.getSexType());
        assertNull(student.getMajor());
    }

    @Test
    @DisplayName("학생 회원가입(모든 항목 채우기)")
    void studentSignupAllFill() throws Exception {
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
        String content = objectMapper.writeValueAsString(requestSignupForm);

        // expected
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, studentRepository.count());

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
    @DisplayName("가게 매니저 회원가입(필수 항목 안채우기)")
    void storeManagerSignupNotFillRequired() throws Exception {
        // given
        RequestSignupForm requestSignupForm = RequestSignupForm.builder()
                .accountType("password")
                .password("testPassword")
                .name("tester")
                .storeAddress("서울특별시 광진구")
                .storePhone("021234567")
                .storeName("건국치킨")
                .storeRegistrationNumber("041-206-29-84031")
                .build();
        String content = objectMapper.writeValueAsString(requestSignupForm);

        // expected
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("400"))
                .andExpect(jsonPath("$.message").value("검증 실패"))
                .andExpect(jsonPath("$.validation.phone").value("phone은 필수항목입니다."))
                .andExpect(jsonPath("$.validation.role").value("role은 필수항목입니다."))
                .andDo(print());

        assertEquals(0L, storemanagerRepository.count());
    }

    @Test
    @DisplayName("가게 매니저 회원가입(정상 회원가입)")
    void storeManagerSignupFillRequired() throws Exception {
        // given
        RequestSignupForm requestSignupForm = RequestSignupForm.builder()
                .accountType("password")
                .password("testPassword")
                .name("tester")
                .role("storemanager")
                .phone("01012345678")
                .storeAddress("서울특별시 광진구")
                .storePhone("021234567")
                .storeName("건국치킨")
                .storeRegistrationNumber("041-206-29-84031")
                .build();
        String content = objectMapper.writeValueAsString(requestSignupForm);

        // expected
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, storemanagerRepository.count());

        Storemanager storemanager = storemanagerRepository.findAll().get(0);
        assertEquals(AccountType.PASSWORD, storemanager.getAccountType());
        assertEquals(Role.ROLE_STOREMANAGER, storemanager.getRole());
        assertEquals("tester", storemanager.getName());
        assertEquals("01012345678", storemanager.getPhone());
        assertEquals("서울특별시 광진구", storemanager.getStoreAddress());
        assertEquals("021234567", storemanager.getStorePhone());
        assertEquals("건국치킨", storemanager.getStoreName());
        assertEquals("041-206-29-84031", storemanager.getStoreRegistrationNumber());

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


}