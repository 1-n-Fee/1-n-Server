package konkuk.nServer.domain.storemanager.repository;

import konkuk.nServer.domain.storemanager.domain.Storemanager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoremanagerRepository extends JpaRepository<Storemanager, Long> {
    Optional<Storemanager> findByEmail(String email);

    boolean existsByEmail(String email);
}
