package konkuk.nServer.domain.comment.dto.requestForm;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RegistryReply {

    @NotNull
    private Long commentId;

    @NotBlank
    @Length(max = 300)
    private String content;

    @Builder
    public RegistryReply(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
