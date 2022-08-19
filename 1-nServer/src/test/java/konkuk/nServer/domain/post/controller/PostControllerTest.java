package konkuk.nServer.domain.post.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.dto.responseForm.FindPost;
import konkuk.nServer.domain.post.dto.responseForm.FindPostDetail;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.domain.store.domain.Store;
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
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private StoreRepository storeRepository;

    @BeforeEach
    @AfterEach
    void clean() {
        postRepository.deleteAll();
        storeRepository.deleteAll();
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

        Long storeId = storeRepository.findAll().get(0).getId();

        RegistryPost registryPost = getRegistryPost();
        registryPost.setStoreId(storeId);
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
        assertEquals("알촌 먹고 싶으신 분, 대환영입니다.", post.getContent());
        assertEquals(PostProcess.RECRUITING, post.getProcess());

        assertEquals(LocalDateTime.parse("2022.09.01.18.00", DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")), post.getCloseTime());
        assertEquals(5, post.getLimitNumber());
    }

    @Test
    @DisplayName("post 찾기(By Spot)")
    void findPostBySpot() throws Exception {
        // given
        storemanagerService.signup(getStoremanagerForm());
        Storemanager storemanager = storemanagerRepository.findAll().get(0);

        storeService.registryStoreByStoremanager(storemanager.getId(), getRegistryStore());

        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);
        Store store = storeRepository.findAll().get(0);

        RegistryPost registryPost = getRegistryPost();
        registryPost.setStoreId(store.getId());
        String content = objectMapper.writeValueAsString(registryPost);

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        MvcResult result = mockMvc.perform(get("/post/spot/1")
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // expected
        String responseBody = result.getResponse().getContentAsString();
        List<FindPost> response =
                objectMapper.readValue(responseBody, new TypeReference<List<FindPost>>() {
                });

        assertEquals(1L, response.size());
        FindPost findPost = response.get(0);

        assertEquals(store.getCategory().name(), findPost.getCategory());
        assertEquals("OWNER", findPost.getState());
        assertEquals("2022.09.01.18.00", findPost.getCloseTime());
        //assertEquals("든든한 국BOB", findPost.getStoreName()); // 한글 깨짐
        assertEquals(store.getDeliveryFee(), findPost.getDeliveryFee());
        assertEquals(5, findPost.getLimitNumber());
        assertEquals(0, findPost.getCurrentNumber());

        Post post = postRepository.findAll().get(0);
        assertEquals(post.getId(), findPost.getPostId());
    }

    @Test
    @DisplayName("postDetail 조회(By id)")
    void findPostDetailById() throws Exception {
        // given
        storemanagerService.signup(getStoremanagerForm());
        Storemanager storemanager = storemanagerRepository.findAll().get(0);

        storeService.registryStoreByStoremanager(storemanager.getId(), getRegistryStore());

        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);
        Store store = storeRepository.findAll().get(0);

        RegistryPost registryPost = getRegistryPost();
        registryPost.setStoreId(store.getId());
        String content = objectMapper.writeValueAsString(registryPost);

        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        Post post = postRepository.findAll().get(0);

        MvcResult result = mockMvc.perform(get("/post/{postId}", post.getId())
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // expected
        String responseBody = result.getResponse().getContentAsString();
        FindPostDetail findPostDetail = objectMapper.readValue(responseBody, FindPostDetail.class);

        assertEquals(store.getCategory().name(), findPostDetail.getCategory());
        assertEquals("2022.09.01.18.00", findPostDetail.getCloseTime());
        //assertEquals("든든한 국BOB", findPostDetail.getStoreName()); // 한글 깨짐
        //assertEquals("알촌 먹고 싶으신 분, 대환영입니다.", findPostDetail.getContent()); // 한글 깨짐
        assertEquals(store.getDeliveryFee(), findPostDetail.getDeliveryFee());
        assertEquals(5, findPostDetail.getLimitNumber());
        assertEquals(0, findPostDetail.getCurrentNumber());
        assertEquals(1, findPostDetail.getSpotId());
    }

    @Test
    @DisplayName("post 찾기(By StoreName)")
    void findPostByStoreName() throws Exception {
        // given
        storemanagerService.signup(getStoremanagerForm());
        Storemanager storemanager = storemanagerRepository.findAll().get(0);

        storeService.registryStoreByStoremanager(storemanager.getId(), getRegistryStore());

        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);
        Store store = storeRepository.findAll().get(0);

        RegistryPost registryPost = getRegistryPost();
        registryPost.setStoreId(store.getId());
        postService.registryPost(user.getId(), registryPost);

        // expected
        MvcResult result = mockMvc.perform(get("/post/search")
                        .param("store", "든든한 국BOB")
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<FindPost> response =
                objectMapper.readValue(responseBody, new TypeReference<List<FindPost>>() {
                });

        assertEquals(1L, response.size());
        FindPost findPost = response.get(0);

        assertEquals(store.getCategory().name(), findPost.getCategory());
        assertEquals("OWNER", findPost.getState());
        assertEquals("2022.09.01.18.00", findPost.getCloseTime());
        //assertEquals("든든한 국BOB", findPost.getStoreName()); // 한글 깨짐
        assertEquals(store.getDeliveryFee(), findPost.getDeliveryFee());
        assertEquals(5, findPost.getLimitNumber());
        assertEquals(0, findPost.getCurrentNumber());

        Post post = postRepository.findAll().get(0);
        assertEquals(post.getId(), findPost.getPostId());
    }

    @Test
    @DisplayName("post 찾기(By date)")
    void findPostByDate() throws Exception {
        // given
        storemanagerService.signup(getStoremanagerForm());
        Storemanager storemanager = storemanagerRepository.findAll().get(0);

        storeService.registryStoreByStoremanager(storemanager.getId(), getRegistryStore());

        UserSignup userSignup = getUserSignupDto();
        userService.signup(userSignup);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);
        Store store = storeRepository.findAll().get(0);

        RegistryPost registryPost = getRegistryPost();
        registryPost.setStoreId(store.getId());
        postService.registryPost(user.getId(), registryPost);

        // expected
        MvcResult result = mockMvc.perform(get("/post/search")
                        .param("date", "20220901")
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<FindPost> response =
                objectMapper.readValue(responseBody, new TypeReference<List<FindPost>>() {
                });

        assertEquals(1L, response.size());
        FindPost findPost = response.get(0);

        assertEquals(store.getCategory().name(), findPost.getCategory());
        assertEquals("OWNER", findPost.getState());
        assertEquals("2022.09.01.18.00", findPost.getCloseTime());
        //assertEquals("든든한 국BOB", findPost.getStoreName()); // 한글 깨짐
        assertEquals(store.getDeliveryFee(), findPost.getDeliveryFee());
        assertEquals(5, findPost.getLimitNumber());
        assertEquals(0, findPost.getCurrentNumber());

        Post post = postRepository.findAll().get(0);
        assertEquals(post.getId(), findPost.getPostId());
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