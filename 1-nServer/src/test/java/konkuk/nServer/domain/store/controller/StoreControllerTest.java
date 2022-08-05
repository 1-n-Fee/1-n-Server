package konkuk.nServer.domain.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.repository.MenuRepository;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.dto.requestForm.StoremanagerSignup;
import konkuk.nServer.domain.user.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.service.StoremanagerService;
import konkuk.nServer.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
class StoreControllerTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoremanagerRepository storemanagerRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private StoremanagerService storemanagerService;

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @AfterEach
    void clean() {
        log.info("delete menu");
        menuRepository.deleteAll();
        log.info("delete store");
        storeRepository.deleteAll();
        log.info("delete storemanager");
        storemanagerRepository.deleteAll();
    }

    @Test
    @DisplayName("가게 등록")
    void studentSignupNotFillRequired() throws Exception {
        // given
        StoremanagerSignup storemanagerSignup = getStoremanagerForm();
        storemanagerService.signup(storemanagerSignup);

        Storemanager storemanager = storemanagerRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(storemanager);


        RegistryStoreByStoremanager registryStoreByStoremanager = RegistryStoreByStoremanager.builder()
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
        String content = objectMapper.writeValueAsString(registryStoreByStoremanager);

        // expected
        mockMvc.perform(post("/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, storeRepository.count());
        Store store = storeRepository.findAll().get(0);

        assertEquals("024991312", store.getPhone());
        assertEquals("서울특별시 성동구 ~", store.getAddress());
        assertEquals("든든한 국BOB", store.getName());
        assertEquals("1500-1630", store.getBreakTime());
        assertEquals("1000-2100", store.getBusinessHours());
        assertEquals(5000, store.getDeliveryFee());

        List<Menu> menus = menuRepository.findByStore(store);
        assertEquals(5L, menus.size());
        assertThat(menus, Matchers.containsInAnyOrder(
                hasProperty("name", is("돼지 국밥")),
                hasProperty("name", is("돼지 국밥(특)")),
                hasProperty("name", is("소머리 국밥")),
                hasProperty("name", is("콜라(500ml)")),
                hasProperty("name", is("사이다(500ml)"))
        ));
    }

    private StoremanagerSignup getStoremanagerForm() {
        return StoremanagerSignup.builder()
                .email("storemanager@google.com")
                .name("홍길동")
                .phone("01087654321")
                .password("pwpw!")
                .accountType("password")
                .storeRegistrationNumber("20-70006368")
                .role("storemanager")
                .build();
    }


}