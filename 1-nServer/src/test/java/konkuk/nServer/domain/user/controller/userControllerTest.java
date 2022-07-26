package konkuk.nServer.domain.user.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("디스플레이 이름")
    void test() {
        // expected
        mockMvc.perform()

    }

}