package konkuk.nServer.domain.user.repository;

import konkuk.nServer.domain.user.domain.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {
}
