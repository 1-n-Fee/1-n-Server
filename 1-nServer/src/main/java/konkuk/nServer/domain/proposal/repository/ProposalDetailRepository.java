package konkuk.nServer.domain.proposal.repository;

import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalDetailRepository extends JpaRepository<ProposalDetail, Long> {
    List<ProposalDetail> findByProposal(Proposal proposal);
}
