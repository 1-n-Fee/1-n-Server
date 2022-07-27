package konkuk.nServer.domain.user.repository;

import konkuk.nServer.domain.user.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByNickname(String nickname);
}
