package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.account.domain.AccountType;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.SexType;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.account.repository.PasswordRepository;
import konkuk.nServer.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        passwordRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void test1() {
        UserSignup userSignup = UserSignup.builder()
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

        userService.signup(userSignup);


        assertEquals(1L, userRepository.count());
        assertEquals(1L, passwordRepository.count());

        User user = userRepository.findAll().get(0);
        assertEquals(AccountType.PASSWORD, user.getAccountType());
        assertEquals(Role.ROLE_STUDENT, user.getRole());
        assertEquals("asdf@konkuk.ac.kr", user.getEmail());
        assertEquals("ithinkso", user.getNickname());
        assertEquals("tester", user.getName());
        assertEquals("01012345678", user.getPhone());
        assertEquals(SexType.MAN, user.getSexType());
        assertEquals("CS", user.getMajor());
    }
}