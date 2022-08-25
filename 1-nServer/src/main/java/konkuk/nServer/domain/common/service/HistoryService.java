package konkuk.nServer.domain.common.service;

import konkuk.nServer.domain.common.dto.response.FindAllPostList;
import konkuk.nServer.domain.common.dto.response.FindApplyPostList;
import konkuk.nServer.domain.common.dto.response.FindOrderMenu;
import konkuk.nServer.domain.common.dto.response.FindPostList;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import konkuk.nServer.domain.proposal.domain.ProposalState;
import konkuk.nServer.domain.proposal.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HistoryService {

    private final PostRepository postRepository;
    private final ProposalRepository proposalRepository;

    public List<FindPostList> findPostByUser(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(FindPostList::of)
                .toList();
    }

    public List<FindApplyPostList> findApplyPostByUser(Long userId) {
        return proposalRepository.findByUserId(userId).stream()
                .map(proposal -> {
                            List<FindApplyPostList.MyMenus> myMenus =
                                    proposal.getProposalDetails().stream()
                                            .map(FindApplyPostList.MyMenus::of)
                                            .toList();
                            return FindApplyPostList.of(proposal, myMenus);
                        }
                )
                .toList();
    }

    public List<FindAllPostList> findPostByAll(Long userId) {
        return proposalRepository.findByUserIdOrderByCreateDateTimeAsc(userId).stream()
                .map(proposal -> FindAllPostList.of(proposal, userId))
                .toList();
    }

    public FindOrderMenu findOrderMenuByPostId(Long userId, Long postId) {
        List<Proposal> proposals = proposalRepository.findByPostId(postId);

        List<FindOrderMenu.Menu> myMenus = new ArrayList<>();
        List<FindOrderMenu.OtherMenu> others = new ArrayList<>();

        for (Proposal proposal : proposals) {
            if (Objects.equals(proposal.getUser().getId(), userId)) {
                List<ProposalDetail> proposalDetails = proposal.getProposalDetails();

                for (ProposalDetail proposalDetail : proposalDetails) {
                    myMenus.add(new FindOrderMenu.Menu(proposalDetail.getMenu().getName(),
                            proposalDetail.getQuantity() * proposalDetail.getMenu().getPrice()));
                }
            } else {
                if (proposal.getProposalState() == ProposalState.ACCEPTED) {
                    List<FindOrderMenu.Menu> otherMenus = new ArrayList<>();

                    List<ProposalDetail> proposalDetails = proposal.getProposalDetails();
                    for (ProposalDetail proposalDetail : proposalDetails) {
                        otherMenus.add(new FindOrderMenu.Menu(proposalDetail.getMenu().getName(),
                                proposalDetail.getQuantity() * proposalDetail.getMenu().getPrice()));
                    }

                    others.add(new FindOrderMenu.OtherMenu(proposal.getUser().getNickname(), otherMenus));
                }
            }
        }

        return new FindOrderMenu(myMenus, others);
    }
}

