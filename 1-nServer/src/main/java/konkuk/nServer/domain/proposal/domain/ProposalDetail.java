package konkuk.nServer.domain.proposal.domain;

import konkuk.nServer.domain.store.domain.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class ProposalDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public ProposalDetail(Integer quantity, Menu menu) {
        this.quantity = quantity;
        this.menu = menu;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }
}
