package konkuk.nServer.domain.comment.repository;

import konkuk.nServer.domain.comment.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
