package konkuk.nServer.domain.post.dto.responseForm;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.store.domain.Store;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;

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

    public static FindPost of(Post post) {
        return FindPost.builder()
                .postId(post.getId())
                .deliveryFee(post.getStore().getDeliveryFee())
                .currentNumber(post.getCurrentNumber())
                .limitNumber(post.getLimitNumber())
                .storeName(post.getStore().getName())
                .category(post.getStore().getCategory().name())
                .closeTime(post.getCloseTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")))
                .build();
    }
}
