package op29sem58.authentication.users;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    public Optional<User> findByNetID(String netId);
}
