package ac.project.sft.repository;

import ac.project.sft.model.ScheduledTransaction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledTransactionRepository extends PagingAndSortingRepository<ScheduledTransaction,Long> {
}
