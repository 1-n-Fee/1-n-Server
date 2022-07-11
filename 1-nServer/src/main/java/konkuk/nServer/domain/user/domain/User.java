package konkuk.nServer.domain.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "USERS")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private String name;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_detail_id")
    private StudentDetail studentDetail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storemanager_detail_id")
    private StoremanagerDetail storemanagerDetail;
}
