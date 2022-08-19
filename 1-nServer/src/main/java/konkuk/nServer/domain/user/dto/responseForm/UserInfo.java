package konkuk.nServer.domain.user.dto.responseForm;

import com.fasterxml.jackson.annotation.JsonInclude;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class UserInfo {

    private String accountType;
    private String name;
    private String phone;
    private String role;
    private String major;
    private String email;
    private String nickname;
    private String sexType;
    private String storeRegistrationNumber;

    @Builder
    public UserInfo(String accountType, String name, String phone, String role, String major, String email,
                    String nickname, String sexType, String storeRegistrationNumber) {
        this.accountType = accountType;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.major = major;
        this.email = email;
        this.nickname = nickname;
        this.sexType = sexType;
        this.storeRegistrationNumber = storeRegistrationNumber;
    }


    public static UserInfo of(User user) {
        return UserInfo.builder()
                .accountType(user.getAccountType().toString())
                .phone(user.getPhone())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .nickname(user.getNickname())
                .major(user.getMajor())
                .sexType(user.getSexType() != null ? user.getSexType().toString() : null)
                .build();
    }

    public static UserInfo of(Storemanager storemanager) {
        return UserInfo.builder()
                .accountType(storemanager.getAccountType().toString())
                .phone(storemanager.getPhone())
                .name(storemanager.getName())
                .email(storemanager.getEmail())
                .role(storemanager.getRole().toString())
                .storeRegistrationNumber(storemanager.getStoreRegistrationNumber())
                .build();
    }
}
