package ac.project.sft.repository;

import ac.project.sft.model.ScheduledTransaction;
import ac.project.sft.model.Wallet;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduledTransactionRepository extends PagingAndSortingRepository<ScheduledTransaction,Long> {

    List<ScheduledTransaction> findAllByWallet(Wallet wallet);

    List<ScheduledTransaction> findAllByNextFireGreaterThanEqual(Date date);
}
