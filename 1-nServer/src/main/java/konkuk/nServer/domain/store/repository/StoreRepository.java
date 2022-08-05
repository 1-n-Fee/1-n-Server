package konkuk.nServer.domain.store.repository;

import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByCategory(Category category);
}