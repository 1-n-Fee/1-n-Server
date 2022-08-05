package konkuk.nServer.domain.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Password {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storemanager_id")
    private Storemanager storemanager;

    public Password(String password, User user) {
        this.password = password;
        this.user = user;
    }

    public Password(String password, Storemanager storemanager) {
        this.password = password;
        this.storemanager = storemanager;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
