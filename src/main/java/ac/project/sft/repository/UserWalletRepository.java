package ac.project.sft.repository;

import ac.project.sft.model.Wallet;
import ac.project.sft.model.User;
import ac.project.sft.model.UserWallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserWalletRepository extends CrudRepository<UserWallet,Long> {

    Optional<UserWallet> findByWalletAndUser(Wallet wallet, User user);

    List<UserWallet> findAllByUser(User user);

    List<UserWallet> findAllByWallet(Wallet wallet);
}
