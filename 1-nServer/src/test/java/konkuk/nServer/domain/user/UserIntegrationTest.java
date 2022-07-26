package konkuk.nServer.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.user.domain.AccountType;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.SexType;
import konkuk.nServer.domain.user.domain.Student;
import konkuk.nServer.domain.user.dto.requestForm.RequestSignupForm;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
class UserIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StoremanagerRepository storemanagerRepository;

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean() {
        studentRepository.deleteAll();
        storemanagerRepository.deleteAll();
    }


    @Test
    @DisplayName("학생 회원가입")
    void studentSignup() throws Exception {

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

        String content = objectMapper.writeValueAsString(requestSignupForm);

        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());


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
