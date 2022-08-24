package konkuk.nServer.domain.comment.domain;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    private LocalDateTime createDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User user;

    @Builder
    public Comment(String content, LocalDateTime createDateTime, Post post, User user) {
        this.content = content;
        this.createDateTime = createDateTime;
        this.post = post;
        this.user = user;
    }
}
