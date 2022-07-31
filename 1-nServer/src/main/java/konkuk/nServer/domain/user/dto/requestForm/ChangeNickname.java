package konkuk.nServer.domain.user.dto.requestForm;

import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeNickname {

    @NotBlank(message = "nickname은 필수항목 입니다.")
    @Length(min = 2, max = 8, message = "닉네임의 길이는 2~8 입니다.")
    private String nickname;


    public void validate() {
        if (nickname.contains(" ")) {
            throw new ApiException(ExceptionEnum.INCORRECT_NICKNAME);
        }
    }
}
