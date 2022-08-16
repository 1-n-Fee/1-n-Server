package konkuk.nServer.domain.account.domain;

import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Naver {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String naverId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storemanager_id")
    private Storemanager storemanager;

    public Naver(String naverId, User user) {
        this.naverId = naverId;
        this.user = user;
    }

    public Naver(String naverId, Storemanager storemanager) {
        this.naverId = naverId;
        this.storemanager = storemanager;
    }
}
