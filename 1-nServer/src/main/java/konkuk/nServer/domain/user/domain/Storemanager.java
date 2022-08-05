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

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @OneToMany(mappedBy = "storemanager", orphanRemoval = true)
    private List<Store> stores = new ArrayList<>();

    @OneToOne(mappedBy = "storemanager", cascade = CascadeType.ALL)
    private Kakao kakao;

    @OneToOne(mappedBy = "storemanager", cascade = CascadeType.ALL)
    private Naver naver;

    @OneToOne(mappedBy = "storemanager", cascade = CascadeType.ALL)
    private Google google;

    @OneToOne(mappedBy = "storemanager", cascade = CascadeType.ALL)
    private Password password;

    @Builder
    public Storemanager(String name, String phone, String email, String storeRegistrationNumber, Role role, AccountType accountType) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.storeRegistrationNumber = storeRegistrationNumber;
        this.role = role;
        this.accountType = accountType;
    }

    public void addStore(Store store) {
        this.stores.add(store);
    }

    public void setKakao(Kakao kakao) {
        this.kakao = kakao;
    }

    public void setNaver(Naver naver) {
        this.naver = naver;
    }

    public void setGoogle(Google google) {
        this.google = google;
    }

    public void setPassword(Password password) {
        this.password = password;
    }
}
