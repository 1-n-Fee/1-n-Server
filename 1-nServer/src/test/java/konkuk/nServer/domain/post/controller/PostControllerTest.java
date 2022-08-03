package konkuk.nServer.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.service.StoreService;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.dto.requestForm.StoremanagerSignup;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.domain.user.service.StoremanagerService;
import konkuk.nServer.domain.user.service.UserService;
import konkuk.nServer.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
class PostControllerTest {

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

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        storemanagerRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    @DisplayName("post 등록")
    void registryPost() throws Exception {
        // given
        storemanagerService.signup(getStoremanagerForm());
        Storemanager storemanager = storemanagerRepository.findAll().get(0);

        storeService.registryStoreByStoremanager(storemanager.getId(), getRegistryStore());

        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);


        RegistryPost registryPost = getRegistryPost();
        String content = objectMapper.writeValueAsString(registryPost);


        // expected
        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, userRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals(Category.KOREAN, post.getCategory());
        assertEquals("알촌 먹고 싶으신 분, 대환영입니다.", post.getContent());
        assertEquals(PostProcess.RECRUITING, post.getProcess());

        assertEquals(LocalDateTime.parse("2022.09.01.18.00", DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")), post.getCloseTime());
        assertEquals(5, post.getLimitNumber());
    }

    private RegistryStoreByStoremanager getRegistryStore() {
        return RegistryStoreByStoremanager.builder()
                .address("서울특별시 성동구 ~")
                .phone("024991312")
                .breakTime("1500-1630")
                .businessHours("1000-2100")
                .deliveryFee(5000)
                .name("든든한 국BOB")
                .menus(List.of(new RegistryStoreByStoremanager.MenuDto(8000, "돼지 국밥", "asdjfhae14jlskadf"),
                        new RegistryStoreByStoremanager.MenuDto(9000, "돼지 국밥(특)", "fwefjhsdf31fhu"),
                        new RegistryStoreByStoremanager.MenuDto(10000, "소머리 국밥", "ldjfe"),
                        new RegistryStoreByStoremanager.MenuDto(2000, "콜라(500ml)", "default"),
                        new RegistryStoreByStoremanager.MenuDto(2000, "사이다(500ml)", "default")))
                .build();
    }

    private RegistryPost getRegistryPost() {
        return RegistryPost.builder()
                .category("KOREAN")
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
                .storeRegistrationNumber("20-70006368")
                .role("storemanager")
                .build();
    }
}