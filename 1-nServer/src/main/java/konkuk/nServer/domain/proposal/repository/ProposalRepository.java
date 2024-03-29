package konkuk.nServer.domain.proposal.repository;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.domain.ProposalState;
import konkuk.nServer.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    Optional<Proposal> findByUserAndPost(User user, Post post);

    List<Proposal> findByPostId(Long postId);

    List<Proposal> findByPostIdAndProposalState(Long postId, ProposalState proposalState);

    List<Proposal> findByUserId(Long userId);

    List<Proposal> findByUserIdOrderByCreateDateTimeAsc(Long userId);

    Optional<Proposal>  findByUserIdAndPostIdAndProposalState(Long userId, Long postId, ProposalState proposalState);
}
