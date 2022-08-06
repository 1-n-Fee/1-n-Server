package konkuk.nServer.domain.proposal.repository;

import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalDetailRepository extends JpaRepository<ProposalDetail, Long> {
}
