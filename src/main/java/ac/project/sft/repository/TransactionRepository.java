package ac.project.sft.repository;

import ac.project.sft.model.IAvgExpense;
import ac.project.sft.model.Transaction;
import ac.project.sft.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction,Long>, JpaSpecificationExecutor<Transaction> {

    Page<Transaction> findAllByWallet(Wallet wallet, Pageable p);

    @Query(value = "select YEAR(date) year,MONTH(date) date,sum(amount) value from transaction where wallet_id=:walletId and date between :startDate and :endDate group by YEAR(date),MONTH(date) ",nativeQuery = true)
    List<IAvgExpense> getAvgPerDay(@Param("walletId") Long walletId,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

}
