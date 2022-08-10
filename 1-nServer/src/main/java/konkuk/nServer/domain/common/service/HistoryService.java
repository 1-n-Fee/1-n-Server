package konkuk.nServer.domain.common.service;

import konkuk.nServer.domain.common.dto.response.FindApplyPostList;
import konkuk.nServer.domain.common.dto.response.FindPostList;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.proposal.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final PostRepository postRepository;
    private final ProposalRepository proposalRepository;

    public List<FindPostList> findPostByUser(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(post ->
                        FindPostList.builder()
                                .limitNumber(post.getLimitNumber())
                                .currentNumber(post.getCurrentNumber())
                                .postId(post.getId())
                                .postState(post.getProcess().name())
                                .spotName(post.getSpot().name())
                                .storeName(post.getStore().getName())
                                .build()
                )
                .toList();
    }

    public List<FindApplyPostList> findApplyPostByUser(Long userId) {
        return proposalRepository.findByUserId(userId).stream()
                .map(proposal -> {
                            Post post = proposal.getPost();
                            List<FindApplyPostList.MyMenus> myMenus = proposal.getProposalDetails().stream()
                                    .map(proposalDetail ->
                                            FindApplyPostList.MyMenus.builder()
                                                    .name(proposalDetail.getMenu().getName())
                                                    .quantity(proposalDetail.getQuantity())
                                                    .unitPrice(proposalDetail.getMenu().getPrice())
                                                    .build())
                                    .toList();

                            return FindApplyPostList.builder()
                                    .limitNumber(post.getLimitNumber())
                                    .currentNumber(post.getCurrentNumber())
                                    .postId(post.getId())
                                    .proposalState(proposal.getProposalState().name())
                                    .spotName(post.getSpot().name())
                                    .storeName(post.getStore().getName())
                                    .myMenus(myMenus)
                                    .build();
                        }
                )
                .toList();
    }
}

