package konkuk.nServer.domain.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Google {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String googleId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storemanager_id")
    private Storemanager storemanager;

    public Google(String googleId, User user) {
        this.googleId = googleId;
        this.user = user;
    }

    public Google(String googleId, Storemanager storemanager) {
        this.googleId = googleId;
        this.storemanager = storemanager;
    }
}
