package konkuk.nServer.domain.user.repository;

import konkuk.nServer.domain.user.domain.Kakao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoRepository extends JpaRepository<Kakao, Long> {
}
