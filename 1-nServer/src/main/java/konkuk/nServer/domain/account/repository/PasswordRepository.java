package konkuk.nServer.domain.account.repository;

import konkuk.nServer.domain.account.domain.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {
}
