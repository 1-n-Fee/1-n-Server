package konkuk.nServer.domain.storemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.account.domain.AccountType;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignup;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignupForApp;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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
    @DisplayName("가게 관리자 회원가입(웹)")
    void signupStoremanager() throws Exception {
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
        assertEquals(AccountType.PASSWORD, storemanager.getAccountType());
        assertEquals("storemanager@google.com", storemanager.getEmail());
        assertEquals("홍길동", storemanager.getName());
        assertEquals("20-70006368", storemanager.getStoreRegistrationNumber());
        assertEquals(Role.ROLE_STOREMANAGER, storemanager.getRole());
        assertTrue(passwordEncoder.matches("pwpw!123", storemanager.getPassword().getPassword()));
    }

    @Test
    @DisplayName("가게 관리자 회원가입(앱)")
    void signupStoremanagerForApp() throws Exception {
        // given
        StoremanagerSignupForApp storemanagerSignupForApp = getStoremanagerFormForApp();
        String content = objectMapper.writeValueAsString(storemanagerSignupForApp);

        // expected
        mockMvc.perform(post("/manager/signup/app")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, storemanagerRepository.count());
        Storemanager storemanager = storemanagerRepository.findAll().get(0);

        assertEquals("01087654321", storemanager.getPhone());
        assertEquals(AccountType.PASSWORD, storemanager.getAccountType());
        assertEquals("storemanager@google.com", storemanager.getEmail());
        assertEquals("홍길동", storemanager.getName());
        assertEquals("20-70006368", storemanager.getStoreRegistrationNumber());
        assertEquals(Role.ROLE_STOREMANAGER, storemanager.getRole());
        assertTrue(passwordEncoder.matches("pwpw!123", storemanager.getPassword().getPassword()));
    }

    @Test
    @DisplayName("가게 관리자 회원가입(앱, oauth)")
    void signupStoremanagerForAppOauth() throws Exception {
        // given
        StoremanagerSignupForApp storemanagerSignupForApp = getStoremanagerFormForAppOauth();
        String content = objectMapper.writeValueAsString(storemanagerSignupForApp);

        // expected
        mockMvc.perform(post("/manager/signup/app")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, storemanagerRepository.count());
        Storemanager storemanager = storemanagerRepository.findAll().get(0);

        assertEquals("01087654321", storemanager.getPhone());
        assertEquals(AccountType.KAKAO, storemanager.getAccountType());
        assertEquals("storemanager@google.com", storemanager.getEmail());
        assertEquals("홍길동", storemanager.getName());
        assertEquals("20-70006368", storemanager.getStoreRegistrationNumber());
        assertEquals(Role.ROLE_STOREMANAGER, storemanager.getRole());
        assertEquals("kakaoOauthId", storemanager.getKakao().getKakaoId());
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

    private StoremanagerSignupForApp getStoremanagerFormForApp() {
        return StoremanagerSignupForApp.builder()
                .email("storemanager@google.com")
                .name("홍길동")
                .phone("01087654321")
                .accountType("password")
                .password("pwpw!123")
                .storeRegistrationNumber("20-70006368")
                .role("storemanager")
                .build();
    }

    private StoremanagerSignupForApp getStoremanagerFormForAppOauth() {
        return StoremanagerSignupForApp.builder()
                .email("storemanager@google.com")
                .name("홍길동")
                .phone("01087654321")
                .accountType("kakao")
                .oauthId("kakaoOauthId")
                .storeRegistrationNumber("20-70006368")
                .role("storemanager")
                .build();
    }
}
