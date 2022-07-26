package konkuk.nServer.domain.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Google {
    @Id
    private Long id;

    @Column(nullable = false)
    private String googleId;

    @MapsId("id")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Google(String googleId, User user) {
        this.googleId = googleId;
        this.user = user;
    }
}
