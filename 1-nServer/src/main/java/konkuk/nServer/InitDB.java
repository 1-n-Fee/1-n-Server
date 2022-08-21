package konkuk.nServer;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.dto.requestForm.RegistryComment;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.post.service.CommentService;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.domain.proposal.dto.requestForm.SaveProposal;
import konkuk.nServer.domain.proposal.service.ProposalService;
import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStudent;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.store.service.StoreService;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignup;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignupForApp;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
import konkuk.nServer.domain.storemanager.service.StoremanagerService;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.user.dto.requestForm.UserSignupForApp;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class InitDB {

    private final UserService userService;
    private final StoremanagerService storemanagerService;
    private final StoreService storeService;
    private final StoremanagerRepository storemanagerRepository;
    private final UserRepository userRepository;
    private final PostService postService;
    private final StoreRepository storeRepository;
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final ProposalService proposalService;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database start");

        initUser();
        initStoremanager();
        initStore();
        initPost();
        initComment();
        initProposal();

        log.info("initialize database end");
    }

    void initUser() {
        userService.signup(UserSignup.builder()
                .email("test1@konkuk.ac.kr")
                .accountType("password")
                .password("test!123")
                .nickname("tester1Nick")
                .name("tester1")
                .role("student")
                .phone("01012345678")
                .major("컴퓨터공학부")
                .sexType("man")
                .build());

        userService.signupForApp(UserSignupForApp.builder()
                .email("test2@konkuk.ac.kr")
                .accountType("password")
                .password("test!123")
                .nickname("tester2Nick")
                .name("tester2")
                .role("student")
                .phone("01045137182")
                .major("경영학과")
                .sexType("woman")
                .build());

        userService.signupForApp(UserSignupForApp.builder()
                .email("test3@konkuk.ac.kr")
                .accountType("kakao")
                .oauthId("kakaoOauthId")
                .nickname("tester3Nick")
                .name("tester3")
                .role("student")
                .phone("01036461831")
                .major("체육교육과")
                .build());
    }

    void initStoremanager() {
        storemanagerService.signup(StoremanagerSignup.builder()
                .email("test4@google.com")
                .name("tester4")
                .phone("0108234251")
                .accountType("password")
                .password("test!123")
                .storeRegistrationNumber("20-70001234")
                .role("storemanager")
                .build());

        storemanagerService.signupForApp(StoremanagerSignupForApp.builder()
                .email("test5@naver.com")
                .name("tester5")
                .phone("01045234143")
                .accountType("password")
                .password("test!123")
                .storeRegistrationNumber("20-70005678")
                .role("storemanager")
                .build());

        storemanagerService.signupForApp(StoremanagerSignupForApp.builder()
                .email("test6@kakao.com")
                .name("tester6")
                .phone("010345134231")
                .accountType("naver")
                .oauthId("naverOauthId")
                .storeRegistrationNumber("20-70091011")
                .role("storemanager")
                .build());
    }

    void initStore() {
        Storemanager storemanager = storemanagerRepository.findAll().get(0);
        RegistryStoreByStoremanager registryStoreByStoremanager1 =
                RegistryStoreByStoremanager.builder()
                        .address("서울 성동구 왕십리로 50 메가박스 스퀘어 2")
                        .phone("070-8633-0405")
                        .breakTime("0000-0000")
                        .businessHours("1100-2000")
                        .deliveryFee(5000)
                        .name("탄탄면공방 서울숲점")
                        .category("chinese")
                        .menus(List.of(new RegistryStoreByStoremanager.MenuDto(10500, "라구탄탄면", "라구탄탄면.png"),
                                new RegistryStoreByStoremanager.MenuDto(9500, "얼큰탄탄국밥", "얼큰탄탄국밥.png"),
                                new RegistryStoreByStoremanager.MenuDto(9800, "오리지널탄탄면", "오리지널탄탄면.png"),
                                new RegistryStoreByStoremanager.MenuDto(10500, "청양탄탄면", "청양탄탄면.png"),
                                new RegistryStoreByStoremanager.MenuDto(4000, "육고명튀김교자(2ea)", "육고명튀김교자.png")))
                        .build();

        storeService.registryStoreByStoremanager(storemanager.getId(), registryStoreByStoremanager1);


        RegistryStoreByStoremanager registryStoreByStoremanager2 =
                RegistryStoreByStoremanager.builder()
                        .address("서울 광진구 능동로13길 19")
                        .phone("02-465-9882")
                        .breakTime("0000-0000")
                        .businessHours("0900-2100")
                        .deliveryFee(4000)
                        .name("더진국수육국밥 건국대점")
                        .category("korean")
                        .menus(List.of(new RegistryStoreByStoremanager.MenuDto(8500, "수육국밥(보통)", "수육국밥(보통).jpeg"),
                                new RegistryStoreByStoremanager.MenuDto(9500, "수육국밥(특)", "수육국밥(특).jpeg"),
                                new RegistryStoreByStoremanager.MenuDto(8500, "순대국밥(보통)", "순대국밥(보통).jpeg"),
                                new RegistryStoreByStoremanager.MenuDto(9500, "순대국밥(특)", "순대국밥(특).jpeg"),
                                new RegistryStoreByStoremanager.MenuDto(23000, "김치찜", "김치찜.jpeg"),
                                new RegistryStoreByStoremanager.MenuDto(23000, "얼큰술국", "얼큰술국.jpeg")))
                        .build();

        storeService.registryStoreByStoremanager(storemanager.getId(), registryStoreByStoremanager2);


        RegistryStoreByStudent registryStoreByStudent = RegistryStoreByStudent.builder()
                .deliveryFee(5000)
                .name("홍콩반점 건대입구역점")
                .category("chinese")
                .menus(List.of(new RegistryStoreByStudent.MenuDto(6000, "짜장면", "짜장면.jpeg"),
                        new RegistryStoreByStudent.MenuDto(7000, "고추짜장", "고추짜장.jpeg"),
                        new RegistryStoreByStudent.MenuDto(7000, "짬뽕", "짬뽕.jpeg"),
                        new RegistryStoreByStudent.MenuDto(20000, "식사1인+탕수육", "식사1인+탕수육.jpeg"),
                        new RegistryStoreByStudent.MenuDto(500, "단무지추가", "default.png")))
                .build();

        storeService.registryStoreByStudent(registryStoreByStudent);
    }

    void initPost() {
        User user = userRepository.findAll().get(0);
        Store store = storeRepository.findAll().get(0);
        postService.registryPost(user.getId(), RegistryPost.builder()
                .storeId(store.getId())
                .closeTime("2022.09.01.18.00") //yyyy.MM.dd.HH.mm
                .limitNumber(5)
                .content(store.getName() + "이(가) 먹고 싶으신 분, 대환영입니다.")
                .spotId(1L)
                .build());
        Post post = postRepository.findAll().get(0);
        List<Menu> menus = store.getMenus();
        SaveProposal saveProposal =
                new SaveProposal(post.getId(), List.of(new SaveProposal.Menus(menus.get(0).getId(), 1),
                        new SaveProposal.Menus(menus.get(1).getId(), 2),
                        new SaveProposal.Menus(menus.get(2).getId(), 3),
                        new SaveProposal.Menus(menus.get(3).getId(), 4)));
        proposalService.saveProposal(user.getId(), saveProposal);


        user = userRepository.findAll().get(1);
        store = storeRepository.findAll().get(1);
        postService.registryPost(user.getId(), RegistryPost.builder()
                .storeId(store.getId())
                .closeTime("2022.09.01.19.00") //yyyy.MM.dd.HH.mm
                .limitNumber(3)
                .content(store.getName() + "이(가) 오늘 끌리는 너. 같이 시켜요.")
                .spotId(1L)
                .build());
        post = postRepository.findAll().get(1);
        menus = store.getMenus();
        saveProposal =
                new SaveProposal(post.getId(), List.of(new SaveProposal.Menus(menus.get(0).getId(), 1),
                        new SaveProposal.Menus(menus.get(1).getId(), 2),
                        new SaveProposal.Menus(menus.get(2).getId(), 3),
                        new SaveProposal.Menus(menus.get(3).getId(), 4)));
        proposalService.saveProposal(user.getId(), saveProposal);


        user = userRepository.findAll().get(2);
        store = storeRepository.findAll().get(2);
        postService.registryPost(user.getId(), RegistryPost.builder()
                .storeId(store.getId())
                .closeTime("2022.09.01.16.30") //yyyy.MM.dd.HH.mm
                .limitNumber(2)
                .spotId(1L)
                .build());
        post = postRepository.findAll().get(2);
        menus = store.getMenus();
        saveProposal =
                new SaveProposal(post.getId(), List.of(new SaveProposal.Menus(menus.get(0).getId(), 1),
                        new SaveProposal.Menus(menus.get(1).getId(), 2),
                        new SaveProposal.Menus(menus.get(2).getId(), 3),
                        new SaveProposal.Menus(menus.get(3).getId(), 4)));
        proposalService.saveProposal(user.getId(), saveProposal);
    }

    void initComment() {
        User user = userRepository.findAll().get(2);
        Post post = postRepository.findAll().get(0);
        commentService.registryComment(user.getId(),
                RegistryComment.builder().content("배달 도착 예상 시간이 언제인가요?")
                        .postId(post.getId()).build());

        user = userRepository.findAll().get(1);
        post = postRepository.findAll().get(0);
        commentService.registryComment(user.getId(),
                RegistryComment.builder().content("올 때 메로나 가능한가요?")
                        .postId(post.getId()).build());
    }

    void initProposal() {
        User user = userRepository.findAll().get(1);
        Post post = postRepository.findAll().get(0);
        Store store = storeRepository.findAll().get(0);

        List<Menu> menus = store.getMenus();
        SaveProposal saveProposal =
                new SaveProposal(post.getId(), List.of(new SaveProposal.Menus(menus.get(0).getId(), 1),
                        new SaveProposal.Menus(menus.get(1).getId(), 2),
                        new SaveProposal.Menus(menus.get(2).getId(), 3),
                        new SaveProposal.Menus(menus.get(3).getId(), 4)));

        proposalService.saveProposal(user.getId(), saveProposal);

        user = userRepository.findAll().get(2);

        saveProposal = new SaveProposal(post.getId(), List.of(new SaveProposal.Menus(menus.get(0).getId(), 1),
                new SaveProposal.Menus(menus.get(1).getId(), 2),
                new SaveProposal.Menus(menus.get(3).getId(), 1)));

        proposalService.saveProposal(user.getId(), saveProposal);
    }


}