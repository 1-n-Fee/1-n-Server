package konkuk.nServer.domain.store.controller;


import konkuk.nServer.domain.store.dto.requestForm.RegistryStoreByStoremanager;
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
    public void registryStoreByStoremanager(@AuthenticationPrincipal PrincipalDetails userDetail,
                                            @RequestBody RegistryStoreByStoremanager registryStore) {
        if (userDetail.getRole() == Role.ROLE_STOREMANAGER)
            storeService.registryStoreByStoremanager(userDetail.getId(), registryStore);
        else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
    }

    /**
     * TODO
     */
    @PostMapping("/temp")
    @ResponseStatus(HttpStatus.CREATED)
    public void registryStoreByStudent(@AuthenticationPrincipal PrincipalDetails userDetail,
                                       @RequestBody RegistryStoreByStoremanager registryStoreByStoremanager) {
        if (userDetail.getRole() == Role.ROLE_STUDENT)
            storeService.registryStoreByStudent(userDetail.getId(), registryStoreByStoremanager);
        else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
    }

    @PostMapping("/menu/image")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registryImage(List<MultipartFile> menuImage) {
        List<String> imageStoreUrl = storeService.registryImage(menuImage);
        return Map.of("imageStoreUrl", imageStoreUrl);
    }

}
