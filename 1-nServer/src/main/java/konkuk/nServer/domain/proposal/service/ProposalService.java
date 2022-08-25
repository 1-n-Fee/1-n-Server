package konkuk.nServer.domain.proposal.service;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
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
import konkuk.nServer.domain.user.repository.UserFindDao;
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
    private final UserFindDao userFindDao;
    private final MenuRepository menuRepository;
    private final PostRepository postRepository;

    public void saveProposal(Long userId, SaveProposal form) {
        Post post = postRepository.findById(form.getPostId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));
        if (post.getProcess() == PostProcess.DELETE || post.getProcess() == PostProcess.CLOSE) {
            throw new ApiException(ExceptionEnum.NOT_ACCESS_POST);
        }

        User user = userFindDao.findById(userId);

        ProposalState proposalState = Objects.equals(post.getUser().getId(), userId) ? ProposalState.ACCEPTED : ProposalState.AWAITING;
        if (proposalState == ProposalState.ACCEPTED) post.increaseCurrentNumber();

        Proposal proposal = Proposal.builder()
                .proposalState(proposalState)
                .createDateTime(LocalDateTime.now())
                .post(post)
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

    @Transactional(readOnly = true)
    public List<FindProposal> findProposalByPost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_POST));

        if (post.getProcess() == PostProcess.DELETE || post.getProcess() == PostProcess.CLOSE) {
            throw new ApiException(ExceptionEnum.NOT_ACCESS_POST);
        }

        if (!Objects.equals(post.getUser().getId(), userId))
            throw new ApiException(ExceptionEnum.NOT_OWNER_POST);

        return proposalRepository.findByPostIdAndProposalState(postId, ProposalState.AWAITING).stream()
                .map(proposal -> {
                    List<FindProposal.Menus> menus = proposalDetailRepository.findByProposal(proposal).stream()
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
            throw new ApiException(ExceptionEnum.NOT_OWNER_POST);

        if (proposal.getProposalState() == ProposalState.AWAITING) {
            proposal.changeState(isApprove);
            if (isApprove) proposal.getPost().increaseCurrentNumber();
        } else {
            throw new ApiException(ExceptionEnum.NO_CHANGE_PROPOSAL);
        }
    }

    public void deleteProposal(Long userId, Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_PROPOSAL));
        if (proposal.getPost().getProcess() == PostProcess.DELETE || proposal.getPost().getProcess() == PostProcess.CLOSE) {
            throw new ApiException(ExceptionEnum.NOT_ACCESS_POST);
        }

        if (!Objects.equals(proposal.getUser().getId(), userId))
            throw new ApiException(ExceptionEnum.NOT_OWNER_PROPOSAL);

        if (proposal.getProposalState() == ProposalState.AWAITING) {
            proposal.getPost().decreaseCurrentNumber();
            proposalRepository.delete(proposal);
        } else {
            throw new ApiException(ExceptionEnum.NO_CHANGE_PROPOSAL);
        }
    }
}
