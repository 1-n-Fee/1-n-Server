package konkuk.nServer.domain.websocket.repository;

import konkuk.nServer.domain.websocket.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
