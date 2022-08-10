package konkuk.nServer.domain.common.dto.response;

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
}
