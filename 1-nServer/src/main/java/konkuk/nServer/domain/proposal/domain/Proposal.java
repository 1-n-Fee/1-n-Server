package konkuk.nServer.domain.proposal.domain;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProposalState proposalState;

    private LocalDateTime createDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User user;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProposalDetail> proposalDetails = new ArrayList<>();


    public void addProposalDetail(ProposalDetail proposalDetail) {
        this.proposalDetails.add(proposalDetail);
        proposalDetail.setProposal(this);
    }

    @Builder
    public Proposal(ProposalState proposalState, LocalDateTime createDateTime, Post post, User user) {
        this.proposalState = proposalState;
        this.createDateTime = createDateTime;
        this.post = post;
        this.user = user;
    }
}
