package konkuk.nServer.domain.user.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@NoArgsConstructor
@Getter
public class Student extends User {
    private String major;

    @Enumerated(EnumType.STRING)
    private SexType sexType;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Builder
    public Student(String nickname, String name, String phone, AccountType accountType, Role role,
                   String major, SexType sexType, String email) {
        super(name, phone, accountType, role);
        this.major = major;
        this.sexType = sexType;
        this.email = email;
        this.nickname = nickname;
    }
}
