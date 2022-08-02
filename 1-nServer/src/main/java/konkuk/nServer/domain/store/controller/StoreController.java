package konkuk.nServer.domain.store.controller;


import konkuk.nServer.domain.store.dto.requestForm.RegistryStore;
import konkuk.nServer.domain.store.service.StoreService;
import konkuk.nServer.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registryPost(@AuthenticationPrincipal PrincipalDetails userDetail,
                             @RequestBody RegistryStore registryStore) {
        storeService.registryStore(userDetail.getId(), registryStore);
    }

    @PostMapping("/menu/image")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registryImage(List<MultipartFile> menuImage) {
        List<String> imageStoreUrl = storeService.registryImage(menuImage);
        return Map.of("imageStoreUrl", imageStoreUrl);
    }

}
