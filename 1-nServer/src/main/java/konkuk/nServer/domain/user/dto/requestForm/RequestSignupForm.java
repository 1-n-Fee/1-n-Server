package konkuk.nServer.domain.user.dto.requestForm;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RequestSignupForm {
    @NotBlank(message = "email은 필수항목입니다.")
    private String email;

    @NotBlank(message = "accountType은 필수항목입니다.")
    private String accountType;

    @NotBlank(message = "nickname은 필수항목입니다.")
    private String nickname;

    @NotBlank(message = "name은 필수항목입니다.")
    private String name;

    @NotBlank(message = "phone은 필수항목입니다.")
    private String phone;

    @NotBlank(message = "role은 필수항목입니다.")
    private String role;

    @NotBlank
    private String major;
    private String sexType;

    private String storeName;
    private String storePhone;
    private String storeAddress;
    private String StoreRegistrationNumber;

    private String kakaoId;
    private String naverId;
    private String googleId;
    private String password;
}
