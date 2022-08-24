package konkuk.nServer.domain.common.controller;

import konkuk.nServer.domain.common.dto.response.FindAllPostList;
import konkuk.nServer.domain.common.dto.response.FindApplyPostList;
import konkuk.nServer.domain.common.dto.response.FindOrderMenu;
import konkuk.nServer.domain.common.dto.response.FindPostList;
import konkuk.nServer.domain.common.service.HistoryService;
import konkuk.nServer.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/owner")
    public List<FindPostList> findPostByUser(@AuthenticationPrincipal PrincipalDetails userDetail) {
        return historyService.findPostByUser(userDetail.getId());
    }

    @GetMapping("/apply")
    public List<FindApplyPostList> findApplyPostByUser(@AuthenticationPrincipal PrincipalDetails userDetail) {
        return historyService.findApplyPostByUser(userDetail.getId());
    }

    @GetMapping
    public List<FindAllPostList> findPostByAll(@AuthenticationPrincipal PrincipalDetails userDetail) {
        return historyService.findPostByAll(userDetail.getId());
    }

    @GetMapping("/{postId}")
    public FindOrderMenu findOrderMenuByPostId(@AuthenticationPrincipal PrincipalDetails userDetail,
                                               @PathVariable Long postId) {
        return historyService.findOrderMenuByPostId(userDetail.getId(), postId);
    }

}
