package konkuk.nServer.domain.account.repository;

import konkuk.nServer.domain.account.domain.Naver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NaverRepository extends JpaRepository<Naver, Long> {
    Optional<Naver> findByNaverId(String naverId);
}
