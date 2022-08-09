package konkuk.nServer.domain.post.repository;

import konkuk.nServer.domain.post.domain.Comment;
import konkuk.nServer.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}