package konkuk.nServer.domain.user.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Student extends User {
    private String major;

    @Enumerated(EnumType.STRING)
    private SexType sexType;

    @Builder
    public Student(String email, String nickname, String name, String phone,
                   AccountType accountType, Role role, String major, SexType sexType) {
        super(email, nickname, name, phone, accountType, role);
        this.major = major;
        this.sexType = sexType;
    }
}
