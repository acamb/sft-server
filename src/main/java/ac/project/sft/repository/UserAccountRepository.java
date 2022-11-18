package ac.project.sft.repository;

import ac.project.sft.model.User;
import ac.project.sft.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount,Long> {

    List<UserAccount> findByUser(User user);
}
