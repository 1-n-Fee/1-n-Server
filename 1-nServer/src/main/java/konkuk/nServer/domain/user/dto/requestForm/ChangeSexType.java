package konkuk.nServer.domain.user.dto.requestForm;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeSexType {

    @NotBlank(message = "sexType은 필수항목 입니다.")
    private String sexType;

}
