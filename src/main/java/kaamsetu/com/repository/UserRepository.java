package kaamsetu.com.repository;

import kaamsetu.com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    // Custom finder: email se user find kare
    // Spring method name se query generate karega
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
