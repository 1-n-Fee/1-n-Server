package konkuk.nServer.domain.post.domain;

import konkuk.nServer.domain.comment.domain.Comment;
import konkuk.nServer.domain.store.domain.Store;
import konkuk.nServer.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Spot spot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostProcess process;

    @Lob
    private String content;

    @Column(nullable = false)
    private LocalDateTime closeTime;

    @Column(nullable = false)
    private LocalDateTime registryTime;

    @Column(nullable = false)
    private Integer currentNumber;

    @Column(nullable = false)
    private Integer limitNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @Builder
    public Post(Spot spot, PostProcess process, String content, LocalDateTime closeTime,
                LocalDateTime registryTime, int currentNumber, int limitNumber, User user, Store store) {
        this.spot = spot;
        this.process = process;
        this.content = content;
        this.closeTime = closeTime;
        this.registryTime = registryTime;
        this.currentNumber = currentNumber;
        this.limitNumber = limitNumber;
        this.user = user;
        this.store = store;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void increaseCurrentNumber() {
        this.currentNumber += 1;
    }

    public void decreaseCurrentNumber() {
        this.currentNumber -= 1;
    }

    public void changeProcess(PostProcess postProcess) {
        this.process = postProcess;
    }
}
