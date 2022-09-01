package konkuk.nServer.domain.common.dto.response;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.proposal.domain.Proposal;
import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FindApplyPostList {

    private String storeName;
    private String spotName;
    private Long postId;
    private Long proposalId;
    private String proposalState;
    private Integer limitNumber;
    private Integer currentNumber;

    private List<MyMenus> myMenus;

    @Builder
    public FindApplyPostList(String storeName, String spotName, Long postId, List<MyMenus> myMenus,
                             String proposalState, Integer limitNumber, Integer currentNumber, Long proposalId) {
        this.storeName = storeName;
        this.spotName = spotName;
        this.postId = postId;
        this.proposalId = proposalId;
        this.proposalState = proposalState;
        this.limitNumber = limitNumber;
        this.currentNumber = currentNumber;
        this.myMenus = myMenus;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class MyMenus {
        String name;
        Integer unitPrice;
        Integer quantity;

        public static MyMenus of(ProposalDetail proposalDetail) {
            return FindApplyPostList.MyMenus.builder()
                    .name(proposalDetail.getMenu().getName())
                    .quantity(proposalDetail.getQuantity())
                    .unitPrice(proposalDetail.getMenu().getPrice())
                    .build();
        }
    }

    public static FindApplyPostList of(Proposal proposal) {
        Post post = proposal.getPost();
        List<FindApplyPostList.MyMenus> myMenus =
                proposal.getProposalDetails().stream()
                        .map(FindApplyPostList.MyMenus::of)
                        .toList();
        return FindApplyPostList.builder()
                .limitNumber(post.getLimitNumber())
                .currentNumber(post.getCurrentNumber())
                .postId(post.getId())
                .proposalId(proposal.getId())
                .proposalState(proposal.getProposalState().name())
                .spotName(post.getSpot().name())
                .storeName(post.getStore().getName())
                .myMenus(myMenus)
                .build();
    }
}
