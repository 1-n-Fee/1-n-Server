package konkuk.nServer.domain.user.dto.requestForm;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class StoremanagerSignup {

    @NotBlank(message = "accountType은 필수항목입니다.")
    private String accountType;

    @NotBlank(message = "name은 필수항목입니다.")
    private String name;

    @NotBlank(message = "phone은 필수항목입니다.")
    private String phone;

    @NotBlank(message = "email은 필수항목입니다.")
    private String email;

    @NotBlank(message = "storeRegistrationNumber은 필수항목입니다.")
    private String storeRegistrationNumber;

    @NotBlank(message = "role은 필수항목입니다.")
    private String role;

    private String code;
    private String password;


    @Builder
    public StoremanagerSignup(String accountType, String name, String phone, String email,
                              String storeRegistrationNumber, String role, String code, String password) {
        this.accountType = accountType;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.storeRegistrationNumber = storeRegistrationNumber;
        this.role = role;
        this.code = code;
        this.password = password;
    }
}
