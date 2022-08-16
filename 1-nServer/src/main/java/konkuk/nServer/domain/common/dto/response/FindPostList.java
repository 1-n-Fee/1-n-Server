package konkuk.nServer.domain.common.dto.response;

import konkuk.nServer.domain.post.domain.Post;
import lombok.Builder;
import lombok.Data;

@Data
public class FindPostList {

    private String storeName;
    private String spotName;
    private Long postId;
    private String postState;
    private Integer limitNumber;
    private Integer currentNumber;

    @Builder
    public FindPostList(String storeName, String spotName, Long postId,
                        String postState, Integer limitNumber, Integer currentNumber) {
        this.storeName = storeName;
        this.spotName = spotName;
        this.postId = postId;
        this.postState = postState;
        this.limitNumber = limitNumber;
        this.currentNumber = currentNumber;
    }

    public static FindPostList of(Post post) {
        return FindPostList.builder()
                .limitNumber(post.getLimitNumber())
                .currentNumber(post.getCurrentNumber())
                .postId(post.getId())
                .postState(post.getProcess().name())
                .spotName(post.getSpot().name())
                .storeName(post.getStore().getName())
                .build();
    }
}
