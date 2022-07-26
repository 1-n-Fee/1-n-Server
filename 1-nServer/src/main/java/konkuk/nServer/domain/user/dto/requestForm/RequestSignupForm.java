package konkuk.nServer.domain.user.dto.requestForm;

import lombok.Builder;
import lombok.Data;

@Data
public class RequestSignupForm {
    private String email;
    private String accountType;
    private String nickname;
    private String name;
    private String phone;
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
}
