package konkuk.nServer.domain.post.dto.responseForm;

import konkuk.nServer.domain.comment.domain.Comment;
import konkuk.nServer.domain.comment.domain.Reply;
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
        Long menuId;

        public static MenuDetail of(Menu menu) {
            return new FindPostDetail.MenuDetail(menu.getName(), menu.getPrice(), menu.getImageUrl(), menu.getId());
        }
    }

    @Data
    public static class CommentDto {
        Long userId;
        Long commentId;
        String nickname;
        String content;
        String createDateTime;
        List<ReplyDto> replies = new ArrayList<>();

        @Builder
        public CommentDto(Long userId, String nickname, String content, String createDateTime, List<ReplyDto> replies, Long commentId) {
            this.userId = userId;
            this.nickname = nickname;
            this.content = content;
            this.createDateTime = createDateTime;
            this.replies = replies;
            this.commentId = commentId;
        }

        @Data
        @AllArgsConstructor
        public static class ReplyDto {
            String content;
            String createDateTime;

            public static ReplyDto of(Reply reply) {
                return new ReplyDto(reply.getContent(), reply.getCreateDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")));
            }
        }

        public static CommentDto of(Comment comment) {
            return CommentDto.builder()
                    .userId(comment.getUser().getId())
                    .nickname(comment.getUser().getNickname())
                    .content(comment.getContent())
                    .commentId(comment.getId())
                    .createDateTime(comment.getCreateDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")))
                    .replies(comment.getReplies().stream().map(ReplyDto::of).toList())
                    .build();
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
