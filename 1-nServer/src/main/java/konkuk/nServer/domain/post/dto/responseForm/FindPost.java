package konkuk.nServer.domain.post.dto.responseForm;

import lombok.Builder;
import lombok.Data;

@Data
public class FindPost {
    Long postId;
    Integer limitNumber;
    Integer currentNumber;
    String closeTime;
    String storeName;
    Integer deliveryFee;
    String category;
    String state;

    @Builder
    public FindPost(Long postId, Integer limitNumber, Integer currentNumber, String category,
                    String closeTime, Integer deliveryFee, String state, String storeName) {
        this.postId = postId;
        this.limitNumber = limitNumber;
        this.currentNumber = currentNumber;
        this.closeTime = closeTime;
        this.deliveryFee = deliveryFee;
        this.state = state;
        this.storeName = storeName;
        this.category = category;
    }

    public void setState(String state) {
        this.state = state;
    }
}
