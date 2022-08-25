package konkuk.nServer.domain.comment.dto.requestForm;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RegistryComment {

    @NotNull(message = "postId는 필수입니다.")
    private Long postId;

    @NotBlank(message = "content는 필수입니다.")
    @Length(max = 300)
    private String content;

    @Builder
    public RegistryComment(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
