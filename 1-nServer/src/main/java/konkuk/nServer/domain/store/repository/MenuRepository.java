package konkuk.nServer.domain.store.repository;

import konkuk.nServer.domain.store.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
