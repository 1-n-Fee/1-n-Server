package konkuk.nServer.domain.post.dto.requestForm;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
import konkuk.nServer.domain.post.domain.Spot;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RegistryPost {

    @NotNull(message = "spotId는 필수입니다.")
    private Long spotId;

    private String content;

    @NotNull(message = "limitNumber는 필수입니다.")
    private Integer limitNumber;

    @NotNull(message = "storeId는 필수입니다.")
    private Long storeId;

    @NotBlank(message = "closeTime는 필수입니다.")
    private String closeTime; // yyyy.MM.dd.HH.mm

    @Builder
    public RegistryPost(Long spotId, String content, Integer limitNumber, Long storeId, String closeTime) {
        this.spotId = spotId;
        this.content = content;
        this.limitNumber = limitNumber;
        this.storeId = storeId;
        this.closeTime = closeTime;
    }

    public Post toEntity(User user, Store store, Spot spot, LocalDateTime closeTime) {
        return Post.builder()
                .registryTime(LocalDateTime.now())
                .closeTime(closeTime)
                .content(content)
                .process(PostProcess.RECRUITING)
                .currentNumber(0)
                .limitNumber(limitNumber)
                .user(user)
                .store(store)
                .spot(spot)
                .build();
    }
}
