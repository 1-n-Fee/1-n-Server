package konkuk.nServer.domain.user.repository;

import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoremanagerRepository extends JpaRepository<Storemanager, Long> {
    Optional<Storemanager> findByEmail(String email);
}
