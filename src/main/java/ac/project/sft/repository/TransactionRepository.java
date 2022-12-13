package ac.project.sft.repository;

import ac.project.sft.model.Transaction;
import ac.project.sft.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction,Long>, JpaSpecificationExecutor {

    Page<Transaction> findAllByWallet(Wallet wallet, Pageable p);

}
