package konkuk.nServer.domain.common.dto.response;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.proposal.domain.Proposal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FindAllPostList {

    private Long postId;
    private String storeName;
    private Integer deliveryFee;
    private Integer limitNumber;
    private Integer currentNumber;
    private String postState;
    private String spotName;
    private boolean isOwner;


    @Builder
    public FindAllPostList(Long postId, String storeName, Integer deliveryFee, Integer limitNumber,
                           Integer currentNumber, String postState, String spotName, boolean isOwner) {
        this.postId = postId;
        this.storeName = storeName;
        this.deliveryFee = deliveryFee;
        this.limitNumber = limitNumber;
        this.currentNumber = currentNumber;
        this.postState = postState;
        this.spotName = spotName;
        this.isOwner = isOwner;
    }

    public static FindAllPostList of(Post post, boolean isOwner) {
        return FindAllPostList.builder()
                .limitNumber(post.getLimitNumber())
                .currentNumber(post.getCurrentNumber())
                .postId(post.getId())
                .postState(post.getProcess().name())
                .spotName(post.getSpot().name())
                .storeName(post.getStore().getName())
                .isOwner(isOwner)
                .deliveryFee(post.getStore().getDeliveryFee())
                .build();
    }
}
