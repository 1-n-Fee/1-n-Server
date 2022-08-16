package konkuk.nServer.domain.account.repository;

import konkuk.nServer.domain.account.domain.Kakao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoRepository extends JpaRepository<Kakao, Long> {

    Optional<Kakao> findByKakaoId(String kakaoId);
}
