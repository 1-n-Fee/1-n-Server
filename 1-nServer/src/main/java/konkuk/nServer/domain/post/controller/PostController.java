package konkuk.nServer.domain.post.controller;


import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registryPost(@AuthenticationPrincipal PrincipalDetails userDetail,
                             @RequestBody RegistryPost registryPost) {
        postService.registryPost(userDetail.getId(), registryPost);
    }


}