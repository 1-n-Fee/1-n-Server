package konkuk.nServer.domain.comment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    private LocalDateTime createDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public Reply(String content, LocalDateTime createDateTime, Comment comment) {
        this.content = content;
        this.createDateTime = createDateTime;
        this.comment = comment;
    }
}
