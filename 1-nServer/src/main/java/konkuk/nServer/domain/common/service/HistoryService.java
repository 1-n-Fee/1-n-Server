package konkuk.nServer.domain.common.service;

import konkuk.nServer.domain.common.dto.response.FindAllPostList;
import konkuk.nServer.domain.common.dto.response.FindApplyPostList;
import konkuk.nServer.domain.common.dto.response.FindPostList;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.repository.PostRepository;
import konkuk.nServer.domain.proposal.domain.ProposalState;
import konkuk.nServer.domain.proposal.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                            List<FindApplyPostList.MyMenus> myMenus = proposal.getProposalDetails().stream()
                                    .map(FindApplyPostList.MyMenus::of)
                                    .toList();
                            return FindApplyPostList.of(proposal, myMenus);
                        }
                )
                .toList();
    }

    public List<FindAllPostList> findPostByAll(Long userId) {
        return proposalRepository.findByUserId(userId).stream()
                .filter(proposal -> proposal.getProposalState() == ProposalState.ACCEPTED)
                .map(proposal -> {
                            Post post = proposal.getPost();
                            return FindAllPostList.of(post, Objects.equals(post.getUser().getId(), userId));
                        }
                )
                .toList();
    }
}

