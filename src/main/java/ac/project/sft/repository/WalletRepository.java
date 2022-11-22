package ac.project.sft.repository;

import ac.project.sft.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface WalletRepository extends CrudRepository<Wallet,Long> {
}
