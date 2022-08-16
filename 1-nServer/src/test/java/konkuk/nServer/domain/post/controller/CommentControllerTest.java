package konkuk.nServer.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.post.domain.Comment;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.dto.requestForm.RegistryComment;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.repository.CommentRepository;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.store.service.StoreService;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignup;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
import konkuk.nServer.domain.storemanager.service.StoremanagerService;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.user.repository.UserRepository;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
class CommentControllerTest {

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

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    @AfterEach
    void clean() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        storeRepository.deleteAll();
        storemanagerRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 달기")
    void saveComment() throws Exception {
        // given
        storemanagerService.signup(getStoremanagerForm());
        Storemanager storemanager = storemanagerRepository.findAll().get(0);

        storeService.registryStoreByStoremanager(storemanager.getId(), getRegistryStore());

        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        Long storeId = storeRepository.findAll().get(0).getId();

        postService.registryPost(user.getId(), RegistryPost.builder()
                .storeId(storeId)
                .closeTime("2022.09.01.18.00") //yyyy.MM.dd.HH.mm
                .limitNumber(5)
                .content("알촌 먹고 싶으신 분, 대환영입니다.")
                .spotId(1L)
                .build());

        Post post = postRepository.findAll().get(0);
        RegistryComment registryComment = RegistryComment.builder().postId(post.getId())
                .content("댓글댓글").build();
        String content = objectMapper.writeValueAsString(registryComment);
        String jwt = jwtTokenProvider.createJwt(user);

        // expected
        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, commentRepository.count());

        Comment comment = commentRepository.findAll().get(0);

        assertEquals("댓글댓글", comment.getContent());
        assertEquals(post.getId(), comment.getPost().getId());
        assertEquals(user.getId(), comment.getUser().getId());
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

    private StoremanagerSignup getStoremanagerForm() {
        return StoremanagerSignup.builder()
                .email("storemanager@google.com")
                .name("홍길동")
                .phone("01087654321")
                .password("pwpw!123")
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
