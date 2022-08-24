package konkuk.nServer.domain.post.dto.responseForm;

import konkuk.nServer.domain.comment.domain.Comment;
import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.store.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    List<MenuDetail> menus = new ArrayList<>();

    List<CommentDto> comments = new ArrayList<>();

    @Builder
    public FindPostDetail(Integer limitNumber, Integer currentNumber, String closeTime, Integer spotId, String content,
                          String storeName, Integer deliveryFee, String category, List<MenuDetail> menus, List<CommentDto> comments) {
        this.limitNumber = limitNumber;
        this.currentNumber = currentNumber;
        this.closeTime = closeTime;
        this.spotId = spotId;
        this.content = content;
        this.storeName = storeName;
        this.deliveryFee = deliveryFee;
        this.category = category;
        this.menus = menus;
        this.comments = comments;
    }

    @Data
    @AllArgsConstructor
    public static class MenuDetail {
        String name;
        Integer price;
        String image;

        public static MenuDetail of(Menu menu) {
            return new FindPostDetail.MenuDetail(menu.getName(), menu.getPrice(), menu.getImageUrl());
        }
    }

    @Data
    @AllArgsConstructor
    public static class CommentDto {
        Long userId;
        String nickname;
        String content;
        String createDateTime;

        public static CommentDto of(Comment comment) {
            return new FindPostDetail.CommentDto(comment.getUser().getId(), comment.getUser().getNickname(), comment.getContent(),
                    comment.getCreateDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")));
        }
    }

    public static FindPostDetail of(Post post, List<FindPostDetail.MenuDetail> menus, List<FindPostDetail.CommentDto> comments) {
        return FindPostDetail.builder()
                .currentNumber(post.getCurrentNumber())
                .limitNumber(post.getLimitNumber())
                .spotId(post.getSpot().getId())
                .storeName(post.getStore().getName())
                .closeTime(post.getCloseTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")))
                .deliveryFee(post.getStore().getDeliveryFee())
                .menus(menus)
                .category(post.getStore().getCategory().name())
                .content(post.getContent())
                .comments(comments)
                .build();
    }
}
