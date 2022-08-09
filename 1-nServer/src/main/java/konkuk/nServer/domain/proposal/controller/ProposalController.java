package konkuk.nServer.domain.proposal.controller;

import konkuk.nServer.domain.proposal.dto.requestForm.ApproveProposal;
import konkuk.nServer.domain.proposal.dto.requestForm.SaveProposal;
import konkuk.nServer.domain.proposal.dto.responseForm.FindProposal;
import konkuk.nServer.domain.proposal.service.ProposalService;
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
@RequestMapping("/proposal")
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveProposal(@AuthenticationPrincipal PrincipalDetails userDetail,
                             @RequestBody @Valid SaveProposal form) {
        proposalService.saveProposal(userDetail.getId(), form);
    }

    @DeleteMapping("/{proposalId}")
    public void deleteProposal(@AuthenticationPrincipal PrincipalDetails userDetail,
                               @PathVariable Long proposalId) {
        proposalService.deleteProposal(userDetail.getId(), proposalId);
    }

    @GetMapping("/post/{postId}")
    public List<FindProposal> findProposalByPost(@AuthenticationPrincipal PrincipalDetails userDetail,
                                                 @PathVariable Long postId) {
        return proposalService.findProposalByPost(userDetail.getId(), postId);
    }

    @PostMapping("/approve")
    public void approveProposal(@AuthenticationPrincipal PrincipalDetails userDetail,
                                @RequestBody @Valid ApproveProposal approveProposal) {
        proposalService.approveProposal(userDetail.getId(), approveProposal.getProposalId(), approveProposal.getIsApprove());
    }

}
