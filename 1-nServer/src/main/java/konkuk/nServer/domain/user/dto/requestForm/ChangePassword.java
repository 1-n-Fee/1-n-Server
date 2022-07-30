package konkuk.nServer.domain.user.dto.requestForm;

import konkuk.nServer.exception.ExceptionEnum;
import konkuk.nServer.exception.ApiException;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePassword {

    @NotBlank(message = "새로운 비밀번호는 필수항목입니다.")
    private String newPassword;

    public void validate() {
        if (!newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$")) {
            throw new ApiException(ExceptionEnum.INCORRECT_PASSWORD);
        }
    }
}
