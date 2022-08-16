package konkuk.nServer.domain.proposal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import konkuk.nServer.domain.proposal.domain.ProposalState;
import konkuk.nServer.domain.proposal.dto.requestForm.SaveProposal;
import konkuk.nServer.domain.proposal.repository.ProposalRepository;
import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStudent;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.store.service.StoreService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
@Transactional
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

    @Autowired
    private ProposalRepository proposalRepository;

    @BeforeEach
    @AfterEach
    void clean() {
        proposalRepository.deleteAll();
        postRepository.deleteAll();
        storeRepository.deleteAll();
        storemanagerRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("proposal 저장")
    void saveProposal() throws Exception {
        userService.signup(getUserSignupDto());
        User user = userRepository.findAll().get(0);
        storeService.registryStoreByStudent(getRegistryStoreByStudent());
        Store store = storeRepository.findAll().get(0);
        postService.registryPost(user.getId(), RegistryPost.builder()
                .storeId(store.getId())
                .closeTime("2022.09.01.18.00") //yyyy.MM.dd.HH.mm
                .limitNumber(5)
                .content("알촌 먹고 싶으신 분, 대환영입니다.")
                .spotId(1L)
                .build());
        Post post = postRepository.findAll().get(0);

        List<Menu> menus = store.getMenus();
        SaveProposal saveProposal =
                new SaveProposal(post.getId(), List.of(new SaveProposal.Menus(menus.get(0).getId(), 1),
                        new SaveProposal.Menus(menus.get(1).getId(), 2),
                        new SaveProposal.Menus(menus.get(2).getId(), 3),
                        new SaveProposal.Menus(menus.get(3).getId(), 4)));
        String content = objectMapper.writeValueAsString(saveProposal);


        userService.signup(UserSignup.builder()
                .email("asdf2@konkuk.ac.kr")
                .accountType("password")
                .password("pwpw!123")
                .nickname("ithinkso2")
                .name("tester2")
                .role("student")
                .phone("01012345678")
                .major("CS")
                .sexType("man")
                .build());
        user = userRepository.findAll().get(1);
        String jwt = jwtTokenProvider.createJwt(user);

        // expected
        mockMvc.perform(post("/proposal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        Proposal proposal = proposalRepository.findAll().get(0);

        assertEquals(ProposalState.AWAITING, proposal.getProposalState());
        assertEquals(user.getId(), proposal.getUser().getId());
        assertEquals(post.getId(), proposal.getPost().getId());

        List<ProposalDetail> proposalDetails = proposal.getProposalDetails();
        assertEquals(4, proposalDetails.size());
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

    private RegistryStoreByStudent getRegistryStoreByStudent() {
        return RegistryStoreByStudent.builder()
                .deliveryFee(5000)
                .name("든든한 국BOB")
                .category("korean")
                .menus(List.of(new RegistryStoreByStudent.MenuDto(8000, "돼지 국밥", "53173409-a20e-485d-a034-d1f52bfe5fa8.jpeg"),
                        new RegistryStoreByStudent.MenuDto(9000, "돼지 국밥(특)", "53173409-a20e-485d-a034-d1f52bfe5fa8.jpeg"),
                        new RegistryStoreByStudent.MenuDto(10000, "소머리 국밥", "53173409-a20e-485d-a034-d1f52bfe5fa8.jpeg"),
                        new RegistryStoreByStudent.MenuDto(2000, "콜라(500ml)", "default"),
                        new RegistryStoreByStudent.MenuDto(2000, "사이다(500ml)", "default")))
                .build();
    }
}