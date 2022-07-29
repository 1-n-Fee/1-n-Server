package konkuk.nServer.domain.user.repository;

import konkuk.nServer.domain.user.domain.Google;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleRepository extends JpaRepository<Google, Long> {
    Optional<Google> findByGoogleId(String googleId);
}
