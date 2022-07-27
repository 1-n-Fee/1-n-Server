package konkuk.nServer.domain.user.dto.requestForm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class RequestSignupForm {

    @NotBlank(message = "accountType은 필수항목입니다.")
    private String accountType;

    @NotBlank(message = "name은 필수항목입니다.")
    private String name;

    @NotBlank(message = "phone은 필수항목입니다.")
    private String phone;

    @NotBlank(message = "role은 필수항목입니다.")
    private String role;

    private Student student;
    private Storemanager storemanager;
    private Account account;


    @Builder
    public RequestSignupForm(String email, String accountType, String nickname, String name, String phone, String role,
                             String major, String sexType, String storeName, String storePhone, String storeAddress,
                             String storeRegistrationNumber, String kakaoId, String naverId, String googleId, String password) {
        this.accountType = accountType;
        this.name = name;
        this.phone = phone;
        this.role = role;

        this.student = new Student(email, major, sexType, nickname);

        this.storemanager = new Storemanager(storeName, storePhone, storeAddress, storeRegistrationNumber);

        this.account = new Account(kakaoId, naverId, googleId, password);
    }

    @Data
    @AllArgsConstructor
    public static class Student {
        private String email;
        private String major;
        private String sexType;
        private String nickname;
    }

    @Data
    @AllArgsConstructor
    public static class Storemanager {
        private String storeName;
        private String storePhone;
        private String storeAddress;
        private String StoreRegistrationNumber;
    }

    @Data
    @AllArgsConstructor
    public static class Account {
        private String kakaoId;
        private String naverId;
        private String googleId;
        private String password;
    }
}
