package konkuk.nServer.domain.common.dto.response;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import konkuk.nServer.domain.proposal.domain.ProposalState;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FindAllPostList {

    private Long postId;
    private String storeName;
    private Long proposalId;
    private Integer deliveryFeePerPerson;
    private Integer totalAmountForUser;
    private Integer limitNumber;
    private Integer currentNumber;
    private String state;
    private String spotName;
    private boolean isOwner;
    private List<Long> members;


    @Builder
    public FindAllPostList(Long postId, String storeName, Integer deliveryFeePerPerson, Integer totalAmountForUser, List<Long> members,
                           Integer limitNumber, Integer currentNumber, String state, String spotName, boolean isOwner, Long proposalId) {
        this.postId = postId;
        this.storeName = storeName;
        this.deliveryFeePerPerson = deliveryFeePerPerson;
        this.totalAmountForUser = totalAmountForUser;
        this.limitNumber = limitNumber;
        this.currentNumber = currentNumber;
        this.state = state;
        this.spotName = spotName;
        this.isOwner = isOwner;
        this.proposalId = proposalId;
        this.members = members;
    }


    public static FindAllPostList of(Proposal proposal, Long userId, List<Long> members) {
        Post post = proposal.getPost();
        String state = null;
        if (proposal.getProposalState() == ProposalState.AWAITING ||
                proposal.getProposalState() == ProposalState.REJECTED) {
            state = proposal.getProposalState().name();
        }
        else {
            state = post.getProcess().name();
        }

        int deliveryFeePerPerson = post.getStore().getDeliveryFee() / post.getCurrentNumber();
        List<ProposalDetail> proposalDetails = proposal.getProposalDetails();
        int menuPriceSum = proposalDetails.stream()
                .mapToInt(proposalDetail -> proposalDetail.getMenu().getPrice() * proposalDetail.getQuantity())
                .sum();

        return FindAllPostList.builder()
                .proposalId(proposal.getId())
                .postId(post.getId())
                .members(members)
                .storeName(post.getStore().getName())
                .deliveryFeePerPerson(deliveryFeePerPerson)
                .totalAmountForUser(menuPriceSum + deliveryFeePerPerson)
                .limitNumber(post.getLimitNumber())
                .currentNumber(post.getCurrentNumber())
                .state(state)
                .spotName(post.getSpot().name())
                .isOwner(post.getUser().getId().equals(userId))
                .build();
    }
}
