package konkuk.nServer.domain.user.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@NoArgsConstructor
@Getter
public class Storemanager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String storeRegistrationNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Storemanager(String name, String phone, String email, String storeRegistrationNumber, String password, Role role) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.storeRegistrationNumber = storeRegistrationNumber;
        this.password = password;
        this.role = role;
    }
}
