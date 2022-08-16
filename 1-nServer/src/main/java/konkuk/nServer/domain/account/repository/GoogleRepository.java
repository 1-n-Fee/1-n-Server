package konkuk.nServer.domain.account.repository;

import konkuk.nServer.domain.account.domain.Google;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleRepository extends JpaRepository<Google, Long> {
    Optional<Google> findByGoogleId(String googleId);
}
