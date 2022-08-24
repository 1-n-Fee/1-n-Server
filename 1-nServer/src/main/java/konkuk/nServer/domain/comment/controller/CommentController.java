package konkuk.nServer.domain.comment.controller;

import konkuk.nServer.domain.comment.dto.requestForm.RegistryComment;
import konkuk.nServer.domain.comment.service.CommentService;
import konkuk.nServer.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registryComment(@AuthenticationPrincipal PrincipalDetails userDetail,
                                @RequestBody @Valid RegistryComment registryComment) {
        commentService.registryComment(userDetail.getId(), registryComment);
    }

}
