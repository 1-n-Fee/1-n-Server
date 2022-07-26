package konkuk.nServer.domain.user.dto.requestForm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
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



    @Builder
    public RequestSignupForm(String email, String accountType, String nickname, String name, String phone, String role,
                             String major, String sexType, String storeName, String storePhone, String storeAddress,
                             String storeRegistrationNumber, String kakaoId, String naverId, String googleId, String password) {
        this.email = email;
        this.accountType = accountType;
        this.nickname = nickname;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.major = major;
        this.sexType = sexType;
        this.storeName = storeName;
        this.storePhone = storePhone;
        this.storeAddress = storeAddress;
        this.StoreRegistrationNumber = storeRegistrationNumber;
        this.kakaoId = kakaoId;
        this.naverId = naverId;
        this.googleId = googleId;
        this.password = password;
    }
}
