package konkuk.nServer.domain.store.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStudent;
import konkuk.nServer.domain.store.dto.responseForm.StoreList;
import konkuk.nServer.domain.store.repository.MenuRepository;
import konkuk.nServer.domain.store.repository.StoreRepository;
import konkuk.nServer.domain.store.service.StoreService;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignup;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.domain.storemanager.service.StoremanagerService;
import konkuk.nServer.domain.user.service.UserService;
import konkuk.nServer.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
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

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    @AfterEach
    void clean() {
        log.info("delete menu");
        menuRepository.deleteAll();
        log.info("delete store");
        storeRepository.deleteAll();
        log.info("delete storemanager");
        storemanagerRepository.deleteAll();
        log.info("delete user");
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("가게 등록(By storemanager)")
    void registryStoreByStoremanager() throws Exception {
        // given
        StoremanagerSignup storemanagerSignup = getStoremanagerForm();
        storemanagerService.signup(storemanagerSignup);

        Storemanager storemanager = storemanagerRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(storemanager);

        RegistryStoreByStoremanager registryStoreByStoremanager =
                RegistryStoreByStoremanager.builder()
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

    @Test
    @DisplayName("가게 등록(By student)")
    void registryStoreByUser() throws Exception {
        // given
        UserSignup userSignupDto = getUserSignupDto();
        userService.signup(userSignupDto);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);

        RegistryStoreByStudent registryStoreByStudent = RegistryStoreByStudent.builder()
                .deliveryFee(5000)
                .name("든든한 국BOB")
                .category("korean")
                .menus(List.of(new RegistryStoreByStudent.MenuDto(8000, "돼지 국밥", "53173409-a20e-485d-a034-d1f52bfe5fa8.jpeg"),
                        new RegistryStoreByStudent.MenuDto(9000, "돼지 국밥(특)", "53173409-a20e-485d-a034-d1f52bfe5fa8.jpeg"),
                        new RegistryStoreByStudent.MenuDto(10000, "소머리 국밥", "53173409-a20e-485d-a034-d1f52bfe5fa8.jpeg"),
                        new RegistryStoreByStudent.MenuDto(2000, "콜라(500ml)", "default"),
                        new RegistryStoreByStudent.MenuDto(2000, "사이다(500ml)", "default")))
                .build();
        String content = objectMapper.writeValueAsString(registryStoreByStudent);

        // expected
        mockMvc.perform(post("/store/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        assertEquals(1L, storeRepository.count());
        Store store = storeRepository.findAll().get(0);

        assertEquals("temp", store.getPhone());
        assertEquals("temp", store.getAddress());
        assertEquals("든든한 국BOB", store.getName());
        assertEquals("0000-0000", store.getBreakTime());
        assertEquals("0000-0000", store.getBusinessHours());
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


    @Test
    @DisplayName("음식점 조회")
    void findStore() throws Exception {
        // given
        UserSignup userSignupDto = getUserSignupDto();
        userService.signup(userSignupDto);

        User user = userRepository.findAll().get(0);
        String jwt = jwtTokenProvider.createJwt(user);

        storeService.registryStoreByStudent(RegistryStoreByStudent.builder()
                .deliveryFee(5000)
                .name("kukBob")
                .category("korean")
                .menus(List.of(new RegistryStoreByStudent.MenuDto(8000, "돼지 국밥", "53173409-a20e-485d-a034-d1f52bfe5fa8.jpeg")))
                .build());

        storeService.registryStoreByStudent(RegistryStoreByStudent.builder()
                .deliveryFee(1000)
                .name("jjajangmyeon")
                .category("chinese")
                .menus(List.of(new RegistryStoreByStudent.MenuDto(6000, "짜장면(보통)")))
                .build());

        storeService.registryStoreByStudent(RegistryStoreByStudent.builder()
                .deliveryFee(2000)
                .name("hongkongbanjeom")
                .category("chinese")
                .menus(List.of(new RegistryStoreByStudent.MenuDto(7000, "짬뽕")))
                .build());


        // expected
        assertEquals(3L, storeRepository.count());

        MvcResult result = mockMvc.perform(get("/store/all"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<StoreList> storeList = objectMapper.readValue(responseBody, new TypeReference<List<StoreList>>() {
        });

        assertEquals(3L, storeList.size());


        assertThat(storeList, containsInAnyOrder(
                hasProperty("name", is("kukBob")),
                //hasProperty("category", is("KOREAN")),
                //hasProperty("deliveryFee", is(5000)),
                hasProperty("name", is("jjajangmyeon")),
                hasProperty("category", is("CHINESE"))
                //hasProperty("deliveryFee", is(1000)),
                //hasProperty("name", is("hongkongbanjeom")),
                //hasProperty("category", is("CHINESE")),
                //hasProperty("deliveryFee", is(2000))
        ));

        result = mockMvc.perform(get("/store/korean"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        responseBody = result.getResponse().getContentAsString();
        storeList = objectMapper.readValue(responseBody, new TypeReference<List<StoreList>>() {
        });
        assertEquals(1L, storeList.size());

        result = mockMvc.perform(get("/store/chinese"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        responseBody = result.getResponse().getContentAsString();
        storeList = objectMapper.readValue(responseBody, new TypeReference<List<StoreList>>() {
        });
        assertEquals(2L, storeList.size());

        result = mockMvc.perform(get("/store/western"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        responseBody = result.getResponse().getContentAsString();
        storeList = objectMapper.readValue(responseBody, new TypeReference<List<StoreList>>() {
        });
        assertEquals(0L, storeList.size());

        result = mockMvc.perform(get("/store/japanese"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        responseBody = result.getResponse().getContentAsString();
        storeList = objectMapper.readValue(responseBody, new TypeReference<List<StoreList>>() {
        });
        assertEquals(0L, storeList.size());

        result = mockMvc.perform(get("/store/midnight"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        responseBody = result.getResponse().getContentAsString();
        storeList = objectMapper.readValue(responseBody, new TypeReference<List<StoreList>>() {
        });
        assertEquals(0L, storeList.size());

        result = mockMvc.perform(get("/store/etc"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        responseBody = result.getResponse().getContentAsString();
        storeList = objectMapper.readValue(responseBody, new TypeReference<List<StoreList>>() {
        });
        assertEquals(0L, storeList.size());
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


}