package konkuk.nServer.domain.user.dto.responseForm;

import lombok.Builder;
import lombok.Data;

@Data
public class UserInfo {

    private String accountType;
    private String name;
    private String phone;
    private String role;
    private String major;
    private String email;
    private String nickname;
    private String sexType;

    @Builder
    public UserInfo(String accountType, String name, String phone, String role, String major,
                    String email, String nickname, String sexType) {
        this.accountType = accountType;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.major = major;
        this.email = email;
        this.nickname = nickname;
        this.sexType = sexType;
    }
}
