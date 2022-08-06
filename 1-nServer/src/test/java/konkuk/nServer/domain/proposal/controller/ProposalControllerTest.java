package konkuk.nServer.domain.proposal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.store.service.StoreService;
import konkuk.nServer.domain.user.dto.requestForm.StoremanagerSignup;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.domain.user.service.StoremanagerService;
import konkuk.nServer.domain.user.service.UserService;
import konkuk.nServer.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@Slf4j
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
class ProposalControllerTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoremanagerService storemanagerService;

    @Autowired
    private StoremanagerRepository storemanagerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        storeRepository.deleteAll();
        storemanagerRepository.deleteAll();
        userRepository.deleteAll();
    }


    private RegistryPost getRegistryPost() {
        return RegistryPost.builder()
                .storeId(1L)
                .closeTime("2022.09.01.18.00") //yyyy.MM.dd.HH.mm
                .limitNumber(5)
                .content("알촌 먹고 싶으신 분, 대환영입니다.")
                .spotId(1L)
                .build();
    }

    private UserSignup getUserSignupDto() {
        return UserSignup.builder()
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
    }

    private StoremanagerSignup getStoremanagerForm() {
        return StoremanagerSignup.builder()
                .email("storemanager@google.com")
                .name("홍길동")
                .phone("01087654321")
                .password("pwpw!")
                .accountType("password")
                .storeRegistrationNumber("20-70006368")
                .role("storemanager")
                .build();
    }

    private RegistryStoreByStoremanager getRegistryStore() {
        return RegistryStoreByStoremanager.builder()
                .address("서울특별시 성동구 ~")
                .phone("024991312")
                .breakTime("1500-1630")
                .businessHours("1000-2100")
                .deliveryFee(5000)
                .name("든든한 국BOB")
                .category("korean")
                .menus(List.of(new RegistryStoreByStoremanager.MenuDto(8000, "돼지 국밥", "asdjfhae14jlskadf"),
                        new RegistryStoreByStoremanager.MenuDto(9000, "돼지 국밥(특)", "fwefjhsdf31fhu"),
                        new RegistryStoreByStoremanager.MenuDto(10000, "소머리 국밥", "ldjfe"),
                        new RegistryStoreByStoremanager.MenuDto(2000, "콜라(500ml)", "default"),
                        new RegistryStoreByStoremanager.MenuDto(2000, "사이다(500ml)", "default")))
                .build();
    }
}