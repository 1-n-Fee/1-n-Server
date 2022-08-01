package konkuk.nServer.domain.post.domain;

import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Spot spot;

    @Enumerated(EnumType.STRING)
    private PostProcess process;

    @Lob
    private String content;

    private LocalDateTime closeTime;

    private int currentNumber;

    private int limitNumber;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

}
