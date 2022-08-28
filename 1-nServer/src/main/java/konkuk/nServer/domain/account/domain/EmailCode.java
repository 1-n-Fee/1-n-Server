package konkuk.nServer.domain.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class EmailCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createDateTime;

    @Column(nullable = false)
    private LocalDateTime expireDateTime;

    @Builder
    public EmailCode(String email, String code, long expirationTime) {
        this.email = email;
        this.code = code;
        this.createDateTime = LocalDateTime.now();
        this.expireDateTime = createDateTime.plusSeconds(expirationTime);
    }
}
