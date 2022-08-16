package konkuk.nServer;

import konkuk.nServer.domain.storemanager.service.StoremanagerService;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignup;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.user.dto.requestForm.UserSignupForApp;
import konkuk.nServer.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDB {

    private final UserService userService;
    private final StoremanagerService storemanagerService;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database");
        initUser();
        initStoremanager();


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
                .phone("01012345678")
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
                .phone("01012345678")
                .major("체육교육과")
                .build());
    }

    void initStoremanager() {
        storemanagerService.signup(StoremanagerSignup.builder()
                .email("test4@google.com")
                .name("tester4")
                .phone("01087654321")
                .accountType("password")
                .password("test!123")
                .storeRegistrationNumber("20-70006368")
                .role("storemanager")
                .build());
    }

}