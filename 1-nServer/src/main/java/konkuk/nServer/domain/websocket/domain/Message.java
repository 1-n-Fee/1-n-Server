package konkuk.nServer.domain.websocket.domain;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime time;

    @Enumerated(value = EnumType.STRING)
    private MessageType type;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Message(String content, LocalDateTime time, MessageType type, Post post, User user) {
        this.content = content;
        this.time = time;
        this.type = type;
        this.post = post;
        this.user = user;
    }
}