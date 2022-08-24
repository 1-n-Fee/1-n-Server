package konkuk.nServer.domain.post.dto.requestForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePostProcess {

    private Long postId;
    private String state;

}
