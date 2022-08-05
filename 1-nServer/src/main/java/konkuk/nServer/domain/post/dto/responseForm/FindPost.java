package konkuk.nServer.domain.post.dto.responseForm;

import lombok.Data;

@Data
public class FindPost {
    Long postId;
    Integer limitNumber;
    Integer currentNumber;
    String closeTime;
    Integer deliveryFee;
    String state;
}
