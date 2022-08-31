package konkuk.nServer.domain.proposal.dto.responseForm;

import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FindProposal {

    private Long proposalId;
    private String userNickname;
    private List<Menus> menus;

    @Data
    @AllArgsConstructor
    public static class Menus {
        private Long menuId;
        private Integer quantity;
        private String name;
        private Integer price;

        public static FindProposal.Menus of(ProposalDetail proposalDetail) {
            return new FindProposal.Menus(proposalDetail.getMenu().getId(),
                    proposalDetail.getQuantity(), proposalDetail.getMenu().getName(),
                    proposalDetail.getMenu().getPrice());
        }
    }
}
