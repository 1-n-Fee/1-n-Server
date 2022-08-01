package konkuk.nServer.domain.post.repository;

import konkuk.nServer.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
