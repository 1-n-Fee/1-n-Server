package konkuk.nServer.domain.user.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Getter
public class Storemanager extends User {

    @Column(nullable = false)
    private String storeName;

    private String storePhone;

    @Column(nullable = false)
    private String storeAddress;

    @Column(nullable = false)
    private String storeRegistrationNumber;

    @Builder
    public Storemanager(String email, String nickname, String name, String phone, AccountType accountType, Role role,
                        String storeName, String storePhone, String storeAddress, String storeRegistrationNumber) {
        super(email, nickname, name, phone, accountType, role);
        this.storeName = storeName;
        this.storePhone = storePhone;
        this.storeAddress = storeAddress;
        this.storeRegistrationNumber = storeRegistrationNumber;
    }
}
