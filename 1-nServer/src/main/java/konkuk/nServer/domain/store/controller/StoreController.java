package konkuk.nServer.domain.store.controller;


import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStudent;
import konkuk.nServer.domain.store.dto.responseForm.StoreList;
import konkuk.nServer.domain.store.service.StoreService;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
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


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registryStoreByStoremanager(@AuthenticationPrincipal PrincipalDetails userDetail,
                                            @RequestBody @Valid RegistryStoreByStoremanager registryStore) {
        registryStore.validate();
        if (userDetail.getRole() == Role.ROLE_STOREMANAGER)
            storeService.registryStoreByStoremanager(userDetail.getId(), registryStore);
        else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
    }


    @PostMapping("/student")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registryStoreByStudent(@AuthenticationPrincipal PrincipalDetails userDetail,
                                                      @RequestBody RegistryStoreByStudent registryStore) {
        if (userDetail.getRole() == Role.ROLE_STUDENT) {
            Long storeId = storeService.registryStoreByStudent(registryStore);
            return Map.of("storeId", storeId);
        } else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);

    }

    @PostMapping("/menu/image")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registryImage(List<MultipartFile> menuImage) {
        List<String> imageStoreUrl = storeService.registryImage(menuImage);
        return Map.of("imageStoreUrl", imageStoreUrl);
    }

}
