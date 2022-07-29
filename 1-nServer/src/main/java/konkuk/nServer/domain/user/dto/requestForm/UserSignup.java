package konkuk.nServer.domain.user.dto.requestForm;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserSignup {

    @NotBlank(message = "accountType은 필수항목입니다.")
    private String accountType;

    @NotBlank(message = "name은 필수항목입니다.")
    private String name;

    @NotBlank(message = "phone은 필수항목입니다.")
    private String phone;

    @NotBlank(message = "role은 필수항목입니다.")
    private String role;

    @NotBlank(message = "email은 필수항목입니다.")
    private String email;

    @NotBlank(message = "nickname은 필수항목입니다.")
    private String nickname;

    private String major;
    private String sexType;

    private String kakaoId;
    private String naverId;
    private String googleId;
    private String password;

    @Builder
    public UserSignup(String accountType, String name, String phone, String role, String email, String nickname,
                      String major, String sexType, String kakaoId, String naverId, String googleId, String password) {
        this.accountType = accountType;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.email = email;
        this.nickname = nickname;
        this.major = major;
        this.sexType = sexType;
        this.kakaoId = kakaoId;
        this.naverId = naverId;
        this.googleId = googleId;
        this.password = password;
    }
}
