package konkuk.nServer.domain.user.dto.requestForm;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FindEmail {

    @NotBlank(message = "name은 필수항목입니다.")
    private String name;

    @NotBlank(message = "phone은 필수항목입니다.")
    private String phone;
}
