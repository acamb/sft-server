package ac.project.sft.repository;

import ac.project.sft.model.CryptoTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoTransactionRepository extends CrudRepository<CryptoTransaction,Long>{

}
