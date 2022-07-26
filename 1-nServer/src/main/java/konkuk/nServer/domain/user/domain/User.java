package konkuk.nServer.domain.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Kakao kakao;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Naver naver;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Google google;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Password password;

    public User(String email, String nickname, String name, String phone, AccountType accountType, Role role) {
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.phone = phone;
        this.accountType = accountType;
        this.role = role;
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
