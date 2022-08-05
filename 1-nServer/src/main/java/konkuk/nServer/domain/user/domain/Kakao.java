package konkuk.nServer.domain.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Kakao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String kakaoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storemanager_id")
    private Storemanager storemanager;


    public Kakao(String kakaoId, User user) {
        this.kakaoId = kakaoId;
        this.user = user;
    }

    public Kakao(String kakaoId, Storemanager storemanager) {
        this.kakaoId = kakaoId;
        this.storemanager = storemanager;
    }
}
