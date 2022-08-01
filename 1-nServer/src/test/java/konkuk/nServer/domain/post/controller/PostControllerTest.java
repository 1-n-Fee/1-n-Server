package konkuk.nServer.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
import konkuk.nServer.domain.post.domain.Spot;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private StoreRepository storeRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("post 등록")
    void registryPost() throws Exception {
        // given
        /**
         * storeService에서 저장하는 로직 완성 후 호출 -> 테스트
         */
        given(storeRepository.findById(any(Long.class))).willReturn(Optional.of(new Store()));

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

        assertEquals(user, post.getUser());
        assertEquals(LocalDateTime.parse("2022.09.01.18.00", DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")), post.getCloseTime());
        assertEquals(5, post.getLimitNumber());
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
}