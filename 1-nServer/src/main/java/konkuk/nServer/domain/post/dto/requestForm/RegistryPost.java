package konkuk.nServer.domain.post.dto.requestForm;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class RegistryPost {

    private String category;

    private Long spotId;

    private String content;

    private int limitNumber;

    private Long storeId;

    private String closeTime; // yyyy.MM.dd.HH.mm

    @Builder
    public RegistryPost(String category, Long spotId, String content, int limitNumber, Long storeId, String closeTime) {
        this.category = category;
        this.spotId = spotId;
        this.content = content;
        this.limitNumber = limitNumber;
        this.storeId = storeId;
        this.closeTime = closeTime;
    }
}
