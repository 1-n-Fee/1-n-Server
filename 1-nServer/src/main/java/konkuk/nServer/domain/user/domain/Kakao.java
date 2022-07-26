package konkuk.nServer.domain.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Kakao {
    @Id
    private Long id;

    @Column(nullable = false)
    private String kakaoId;

    @MapsId("id")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Kakao(String kakaoId, User user) {
        this.kakaoId = kakaoId;
        this.user = user;
    }
}
