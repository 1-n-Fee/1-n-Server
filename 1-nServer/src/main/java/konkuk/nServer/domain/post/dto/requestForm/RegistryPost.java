package konkuk.nServer.domain.post.dto.requestForm;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class RegistryPost {

    @NotBlank
    private Long spotId;

    private String content;

    @NotBlank
    private Integer limitNumber;

    @NotBlank
    private Long storeId;

    @NotBlank
    private String closeTime; // yyyy.MM.dd.HH.mm

    @Builder
    public RegistryPost(Long spotId, String content, Integer limitNumber, Long storeId, String closeTime) {
        this.spotId = spotId;
        this.content = content;
        this.limitNumber = limitNumber;
        this.storeId = storeId;
        this.closeTime = closeTime;
    }
}
