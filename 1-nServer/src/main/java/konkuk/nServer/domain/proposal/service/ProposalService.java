package konkuk.nServer.domain.proposal.service;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import konkuk.nServer.domain.proposal.domain.ProposalState;
import konkuk.nServer.domain.proposal.dto.requestForm.SaveProposal;
import konkuk.nServer.domain.proposal.dto.responseForm.FindProposal;
import konkuk.nServer.domain.proposal.repository.ProposalDetailRepository;
import konkuk.nServer.domain.proposal.repository.ProposalRepository;
import konkuk.nServer.domain.store.domain.Menu;
import konkuk.nServer.domain.store.repository.MenuRepository;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final ProposalDetailRepository proposalDetailRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final PostRepository postRepository;

    public void saveProposal(Long userId, SaveProposal form) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        Post post = postRepository.findById(form.getPostId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));

        Proposal proposal = Proposal.builder()
                .proposalState(ProposalState.AWAITING)
                .post(post)
                .createDateTime(LocalDateTime.now())
                .user(user)
                .build();

        form.getMenus().forEach(menuDto -> {
            Menu findMenu = menuRepository.findById(menuDto.getMenuId())
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_MENU));

            ProposalDetail proposalDetail = proposalDetailRepository.save(new ProposalDetail(menuDto.getQuantity(), findMenu));
            proposal.addProposalDetail(proposalDetail);
        });

        proposalRepository.save(proposal);
    }

    public List<FindProposal> findProposalByPost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));

        if (!Objects.equals(post.getUser().getId(), userId))
            throw new ApiException(ExceptionEnum.NOT_MATCH_OWNER);

        return proposalRepository.findByPostId(postId).stream()
                .filter(proposal -> proposal.getProposalState()==ProposalState.AWAITING)
                .map(proposal -> {
                    List<FindProposal.Menus> menus = proposalDetailRepository.findByProposal(proposal)
                            .stream()
                            .map(proposalDetail -> new FindProposal.Menus(proposalDetail.getMenu().getId(), proposalDetail.getQuantity()))
                            .toList();
                    return new FindProposal(proposal.getId(), proposal.getUser().getNickname(), menus);
                })
                .collect(Collectors.toList());
    }

    public void approveProposal(Long userId, Long proposalId, Boolean isApprove) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_PROPOSAL_DETAIL));

        if (!Objects.equals(proposal.getPost().getUser().getId(), userId))
            throw new ApiException(ExceptionEnum.NOT_MATCH_OWNER);

        if (proposal.getProposalState() == ProposalState.AWAITING) {
            proposal.changeState(isApprove);
        } else {
            throw new ApiException(ExceptionEnum.NO_CHANGE_PROPOSAL);
        }
    }
}
