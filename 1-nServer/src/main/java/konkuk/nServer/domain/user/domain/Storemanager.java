package konkuk.nServer.domain.user.domain;


import konkuk.nServer.domain.store.domain.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "storemanager", orphanRemoval = true)
    private List<Store> stores = new ArrayList<>();

    @Builder
    public Storemanager(String name, String phone, String email, String storeRegistrationNumber, Role role, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.storeRegistrationNumber = storeRegistrationNumber;
        this.role = role;
        this.password = password;
    }

    public void addStore(Store store) {
        this.stores.add(store);
    }
}
