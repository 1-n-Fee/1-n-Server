package konkuk.nServer.domain.post.controller;

import konkuk.nServer.domain.post.dto.requestForm.ChangePostProcess;
import konkuk.nServer.domain.post.dto.requestForm.RegistryPost;
import konkuk.nServer.domain.post.dto.responseForm.FindPost;
import konkuk.nServer.domain.post.dto.responseForm.FindPostDetail;
import konkuk.nServer.domain.post.service.PostService;
import konkuk.nServer.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registryPost(@AuthenticationPrincipal PrincipalDetails userDetail,
                             @RequestBody @Valid RegistryPost registryPost) {
        postService.registryPost(userDetail.getId(), registryPost);
    }

    @GetMapping("/spot/{spotId}")
    public List<FindPost> findPostBySpotId(@AuthenticationPrincipal PrincipalDetails userDetail,
                                           @PathVariable Long spotId) {
        return postService.findPostBySpot(userDetail.getId(), spotId);
    }

    @GetMapping("/{postId}")
    public FindPostDetail findPostDetailById(@PathVariable Long postId) {
        return postService.findPostDetailById(postId);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@AuthenticationPrincipal PrincipalDetails userDetail,
                           @PathVariable Long postId) {
        postService.deletePost(userDetail.getId(), postId);
    }

    @GetMapping("/search")
    public List<FindPost> findPostBySearch(@AuthenticationPrincipal PrincipalDetails userDetail,
                                           String store, String date) {
        if (store != null && date == null)
            return postService.findPostByStoreName(userDetail.getId(), store);

        if (store == null && date != null)
            return postService.findPostByDate(userDetail.getId(), date);

        else throw new IllegalArgumentException();
    }

    @PostMapping("/state/change")
    public void changePostState(@AuthenticationPrincipal PrincipalDetails userDetail,
                                @RequestBody ChangePostProcess form) {
        postService.changePostState(userDetail.getId(), form);
    }


}
