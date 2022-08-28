package konkuk.nServer.domain.account.repository;

import konkuk.nServer.domain.account.domain.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {
    Optional<EmailCode> findByEmail(String userEmail);
}
