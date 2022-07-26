package konkuk.nServer.domain.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Naver {
    @Id
    private Long id;

    @Column(nullable = false)
    private String naverId;

    @MapsId("id")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Naver(String naverId, User user) {
        this.naverId = naverId;
        this.user = user;
    }
}
