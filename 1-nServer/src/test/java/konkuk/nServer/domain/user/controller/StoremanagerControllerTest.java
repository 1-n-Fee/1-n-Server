package konkuk.nServer.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.dto.requestForm.StoremanagerSignup;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
class StoremanagerControllerTest {

    @Autowired
    private StoremanagerRepository storemanagerRepository;

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        storemanagerRepository.deleteAll();
    }

    @Test
    @DisplayName("가게 관리자 회원가입")
    void studentSignupNotFillRequired() throws Exception {
        // given
        StoremanagerSignup storemanagerSignup = getStoremanagerForm();
        String content = objectMapper.writeValueAsString(storemanagerSignup);

        // expected
        mockMvc.perform(post("/manager/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, storemanagerRepository.count());
        Storemanager storemanager = storemanagerRepository.findAll().get(0);

        assertEquals("01087654321", storemanager.getPhone());
        assertEquals("storemanager@google.com", storemanager.getEmail());
        assertEquals("홍길동", storemanager.getName());
        assertEquals("20-70006368", storemanager.getStoreRegistrationNumber());
        assertEquals(Role.ROLE_STOREMANAGER, storemanager.getRole());
        assertTrue(passwordEncoder.matches("pwpw!123", storemanager.getPassword().getPassword()));
    }

    private StoremanagerSignup getStoremanagerForm() {
        return StoremanagerSignup.builder()
                .email("storemanager@google.com")
                .name("홍길동")
                .phone("01087654321")
                .accountType("password")
                .password("pwpw!123")
                .storeRegistrationNumber("20-70006368")
                .role("storemanager")
                .build();
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