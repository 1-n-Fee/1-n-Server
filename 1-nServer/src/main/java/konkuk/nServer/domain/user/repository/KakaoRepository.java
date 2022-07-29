package konkuk.nServer.domain.user.repository;

import konkuk.nServer.domain.user.domain.Kakao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoRepository extends JpaRepository<Kakao, Long> {

    Optional<Kakao> findByKakaoId(String kakaoId);
}
