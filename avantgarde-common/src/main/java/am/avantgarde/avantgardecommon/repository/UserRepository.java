package am.avantgarde.avantgardecommon.repository;

import am.avantgarde.avantgardecommon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByToken(String token);

}
