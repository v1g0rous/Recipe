package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import recipes.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByUsername(String username);

    boolean existsByUsername(String username);
}
