package konkuk.nServer.domain.post.repository;

import konkuk.nServer.domain.post.domain.Post;
import konkuk.nServer.domain.post.domain.PostProcess;
import konkuk.nServer.domain.post.domain.Spot;
import konkuk.nServer.domain.store.domain.Store;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findBySpotOrderByCloseTimeAsc(Spot spot);

    List<Post> findByUserId(Long userId);

    List<Post> findByStoreOrderByCloseTimeAsc(Store store);

    List<Post> findByCloseTimeBetween(LocalDateTime start, LocalDateTime end, Sort sort);

    List<Post> findByProcessAndCloseTimeBefore(PostProcess postProcess, LocalDateTime now);

}
