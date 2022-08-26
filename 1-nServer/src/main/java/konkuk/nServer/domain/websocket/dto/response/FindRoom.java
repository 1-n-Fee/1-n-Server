package konkuk.nServer.domain.websocket.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FindRoom {

    private Long postId;
    private String storeName;
    private boolean isOwner;
    private String postState;
    private Integer userTotalPrice;
    private Integer deliveryFeePerPerson;
    private int spotId;
    private Integer limitNumber;
    private Integer currentNumber;
    private List<String> members;


    @Builder
    public FindRoom(Long postId, String storeName, boolean isOwner, String postState, Integer userTotalPrice,
                    Integer deliveryFeePerPerson, int spotId, Integer limitNumber, Integer currentNumber, List<String> members) {
        this.postId = postId;
        this.storeName = storeName;
        this.isOwner = isOwner;
        this.postState = postState;
        this.userTotalPrice = userTotalPrice;
        this.deliveryFeePerPerson = deliveryFeePerPerson;
        this.spotId = spotId;
        this.limitNumber = limitNumber;
        this.currentNumber = currentNumber;
        this.members = members;
    }

}
