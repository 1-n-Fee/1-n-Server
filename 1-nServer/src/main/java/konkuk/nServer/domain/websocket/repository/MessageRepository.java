package konkuk.nServer.domain.websocket.repository;

import konkuk.nServer.domain.websocket.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByPostIdOrderByTimeAsc(Long postId);
}
