package konkuk.nServer.domain.store.controller;


import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStudent;
import konkuk.nServer.domain.store.dto.responseForm.FindStore;
import konkuk.nServer.domain.store.dto.responseForm.StoreList;
import konkuk.nServer.domain.store.dto.responseForm.StoreMenu;
import konkuk.nServer.domain.store.service.StoreService;
import konkuk.nServer.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{category}")
    public List<StoreList> getStoreList(@PathVariable String category) {
        return storeService.getStoreListByCategory(category);
    }

    @GetMapping("/detail/{storeId}")
    public List<StoreMenu> getStoreMenu(@PathVariable Long storeId) {
        return storeService.getStoreMenu(storeId);
    }


    @GetMapping("/storemanager")
    public FindStore findStoreByStoremanager(@AuthenticationPrincipal PrincipalDetails userDetail) {
        return storeService.findStoreByStoremanager(userDetail.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registryStoreByStoremanager(@AuthenticationPrincipal PrincipalDetails userDetail,
                                            @RequestBody @Valid RegistryStoreByStoremanager registryStore) {
        registryStore.validate();
        storeService.registryStoreByStoremanager(userDetail.getId(), registryStore);
    }


    @PostMapping("/student")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registryStoreByStudent(@RequestBody RegistryStoreByStudent registryStore) {
        Long storeId = storeService.registryStoreByStudent(registryStore);
        return Map.of("storeId", storeId);
    }

    @PostMapping("/menu/image")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registryImage(List<MultipartFile> menuImage) {
        List<String> imageStoreUrl = storeService.registryImage(menuImage);
        return Map.of("imageStoreUrl", imageStoreUrl);
    }

}
