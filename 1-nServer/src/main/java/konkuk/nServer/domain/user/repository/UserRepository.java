package konkuk.nServer.domain.user.repository;

import konkuk.nServer.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);

    @Query("select i from User i where i.email = :email and i.name = :name and i.phone = :phone")
    Optional<User> findUserForPassword(@Param("email") String email, @Param("name") String name, @Param("phone") String phone);

    Optional<User> findByNameAndPhone(String name, String phone);
}
