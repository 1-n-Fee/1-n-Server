package konkuk.nServer.domain.store.controller;


import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.domain.store.dto.requestForm.RegistryStore;
import konkuk.nServer.domain.store.service.StoreService;
import konkuk.nServer.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        //storeService.registryStore(userDetail.getUserId(), registryStore);
    }


}
