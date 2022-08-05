package konkuk.nServer.domain.post.repository;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findBySpot(Spot spot);
}
