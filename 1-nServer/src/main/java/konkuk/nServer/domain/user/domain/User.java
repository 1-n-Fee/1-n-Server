package konkuk.nServer.domain.user.domain;

import konkuk.nServer.domain.proposal.domain.Proposal;
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
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String nickname;

    private String major;

    @Enumerated(EnumType.STRING)
    private SexType sexType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Kakao kakao;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Naver naver;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Google google;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Password password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposal> proposal = new ArrayList<>();

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

    @Builder
    public User(String name, String phone, AccountType accountType, Role role, String email, String nickname,
                String major, konkuk.nServer.domain.user.domain.SexType sexType) {
        this.name = name;
        this.phone = phone;
        this.accountType = accountType;
        this.role = role;
        this.email = email;
        this.nickname = nickname;
        this.major = major;
        this.sexType = sexType;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeSexType(SexType sexType) {
        this.sexType = sexType;
    }

    public void addProposal(Proposal proposal) {
        this.proposal.add(proposal);
    }
}
