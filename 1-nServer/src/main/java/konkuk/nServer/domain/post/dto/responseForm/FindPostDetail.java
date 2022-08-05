package konkuk.nServer.domain.post.dto.responseForm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FindPostDetail {

    Integer limitNumber;
    Integer currentNumber;
    String closeTime;
    Integer spotId;
    String content;
    String storeName;
    Integer deliveryFee;
    String category;
    List<MenuDetail> menus;

    @Builder
    public FindPostDetail(Integer limitNumber, Integer currentNumber, String closeTime, Integer spotId, String content,
                          String storeName, Integer deliveryFee, String category, List<MenuDetail> menus) {
        this.limitNumber = limitNumber;
        this.currentNumber = currentNumber;
        this.closeTime = closeTime;
        this.spotId = spotId;
        this.content = content;
        this.storeName = storeName;
        this.deliveryFee = deliveryFee;
        this.category = category;
        this.menus = menus;
    }

    @Data
    @AllArgsConstructor
    public static class MenuDetail {
        String name;
        Integer price;
        String image;
    }
}
