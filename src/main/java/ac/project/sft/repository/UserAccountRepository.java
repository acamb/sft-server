package ac.project.sft.repository;

import ac.project.sft.model.Account;
import ac.project.sft.model.User;
import ac.project.sft.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount,Long> {

    List<UserAccount> findByUser(User user);

    Optional<UserAccount> findByAccountAndUser(Account account, User user);

    List<UserAccount> findAllByUser(User user);

    List<UserAccount> findAllByAccount(Account account);
}
