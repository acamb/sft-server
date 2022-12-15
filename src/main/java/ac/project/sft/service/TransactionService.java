package ac.project.sft.service;

import ac.project.sft.dto.SearchTransactionDto;
import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.Transaction;
import ac.project.sft.model.Wallet;
import ac.project.sft.repository.TransactionRepository;
import ac.project.sft.repository.specifications.TransactionSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

import static ac.project.sft.repository.specifications.TransactionSpecifications.*;

@Service
@Validated
public class TransactionService {

    private static final String EXISTS_KEY = "transaction.exists";
    private static final String NOT_FOUND_KEY = "transaction.notFound";
    private static final String AMOUNT_CANT_POSITIVE_KEY = "amount.cantBeNegative";
    private static final String AMOUNT_CANT_NEGATIVE_KEY = "amount.cantBePositive";
    @Autowired
    TransactionRepository repository;

    @Transactional
    public Transaction create(@Valid Transaction transaction){
        if(transaction.getId() != null){
            throw new BadRequestException(EXISTS_KEY);
        }
        checkValidity(transaction);
        return repository.save(transaction);
    }

    @Transactional
    public Transaction update(@Valid Transaction transaction){
        Transaction db = repository.findById(transaction.getId()).orElseThrow(() -> new NotFoundException(NOT_FOUND_KEY));
        db.setName(transaction.getName());
        db.setCategory(transaction.getCategory());
        db.setNote(transaction.getNote());
        checkValidity(transaction);
        return repository.save(db);
    }

    private static void checkValidity(Transaction transaction){
        if(transaction.getCategory() != null){
            if(!transaction.getCategory().isCanBeNegative() && transaction.getAmount().compareTo(BigDecimal.ZERO) < 0){
                throw new BadRequestException(AMOUNT_CANT_POSITIVE_KEY);
            }
            if(!transaction.getCategory().isCanBePositive() && transaction.getAmount().compareTo(BigDecimal.ZERO) > 0){
                throw new BadRequestException(AMOUNT_CANT_NEGATIVE_KEY);
            }
        }
    }

    @Transactional
    public void delete(@Valid Transaction transaction){
        repository.delete(get(transaction.getId()));
    }

    public Page<Transaction> getAll(@Valid Wallet wallet, Pageable pageable, SearchTransactionDto search){
        Specification<Transaction> spec = Specification.where(wallet(wallet));
        if(search != null){
            spec=spec.and(
                    search.getStartDate() != null ? startDate(search.getStartDate()) : null
            ).and(
                    search.getEndDate() != null ? endDate(search.getEndDate()) : null
            ).and(
                    search.getCategoryDto() != null ? category(search.getCategoryDto()) : null
            ).and(
                    search.getType() != null ? type(search.getType()) : null
            ).and(
                    search.getName() != null ? name(search.getName()) : null
            );
        }
        return repository.findAll(spec,pageable);
    }

    public Transaction get(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_KEY));
    }

}
