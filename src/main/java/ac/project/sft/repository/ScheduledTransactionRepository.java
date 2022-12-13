package ac.project.sft.repository;

import ac.project.sft.model.ScheduledTransaction;
import ac.project.sft.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduledTransactionRepository extends PagingAndSortingRepository<ScheduledTransaction,Long>, JpaSpecificationExecutor {

    Page<ScheduledTransaction> findAllByWallet(Wallet wallet, Pageable pageable);

    List<ScheduledTransaction> findAllByNextFireLessThanEqual(LocalDate date);
}
