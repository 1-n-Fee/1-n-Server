package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.user.domain.AccountType;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.SexType;
import konkuk.nServer.domain.user.domain.Student;
import konkuk.nServer.domain.user.dto.requestForm.RequestSignupForm;
import konkuk.nServer.domain.user.repository.PasswordRepository;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.StudentRepository;
import konkuk.nServer.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private StudentRepository studentRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private StoremanagerRepository storemanagerRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        studentRepository.deleteAll();
        passwordRepository.deleteAll();
        storemanagerRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void test1() {
        RequestSignupForm requestSignupForm = RequestSignupForm.builder()
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

        userService.signup(requestSignupForm);


        assertEquals(1L, studentRepository.count());
        assertEquals(1L, passwordRepository.count());

        Student student = studentRepository.findAll().get(0);
        assertEquals(AccountType.PASSWORD, student.getAccountType());
        assertEquals(Role.ROLE_STUDENT, student.getRole());
        assertEquals("asdf@konkuk.ac.kr", student.getEmail());
        assertEquals("ithinkso", student.getNickname());
        assertEquals("tester", student.getName());
        assertEquals("01012345678", student.getPhone());
        assertEquals(SexType.MAN, student.getSexType());
        assertEquals("CS", student.getMajor());
    }
}