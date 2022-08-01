package konkuk.nServer.domain.user.dto.requestForm;

import konkuk.nServer.domain.user.domain.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class StoremanagerSignup {

    @NotBlank(message = "password은 필수항목입니다.")
    private String password;

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


    @Builder
    public StoremanagerSignup(String password, String name, String phone, String email,
                              String storeRegistrationNumber, String role) {
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.storeRegistrationNumber = storeRegistrationNumber;
        this.role = role;
    }
}
