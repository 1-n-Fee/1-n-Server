package konkuk.nServer.domain.post.dto.requestForm;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RegistryComment {

    @NotNull
    private Long postId;

    @NotBlank
    @Length(max = 300)
    private String content;

    @Builder
    public RegistryComment(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
