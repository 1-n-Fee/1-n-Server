package konkuk.nServer.domain.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FindApplyPostList {

    private String storeName;
    private String spotName;
    private Long postId;
    private String proposalState;
    private Integer limitNumber;
    private Integer currentNumber;

    private List<MyMenus> myMenus;

    @Builder
    public FindApplyPostList(String storeName, String spotName, Long postId, List<MyMenus> myMenus,
                             String proposalState, Integer limitNumber, Integer currentNumber) {
        this.storeName = storeName;
        this.spotName = spotName;
        this.postId = postId;
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
    }
}
