package ac.project.sft.repository;

import ac.project.sft.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsernameAndPassword(String username,String password);

    Optional<User> findByUsername(String username);
}
