package konkuk.nServer.domain.store.repository;

import konkuk.nServer.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
