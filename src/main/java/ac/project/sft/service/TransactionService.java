package ac.project.sft.service;

import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.Transaction;
import ac.project.sft.model.Wallet;
import ac.project.sft.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Service
@Validated
public class TransactionService {

    @Autowired
    TransactionRepository repository;

    @Transactional
    public Transaction create(@Valid Transaction transaction){
        if(transaction.getId() != null){
            throw new BadRequestException("transaction.exists");
        }
        checkValidity(transaction);
        return repository.save(transaction);
    }

    @Transactional
    public Transaction update(@Valid Transaction transaction){
        Transaction db = repository.findById(transaction.getId()).orElseThrow(() -> new NotFoundException("transaction.not.exists"));
        db.setName(transaction.getName());
        db.setCategory(transaction.getCategory());
        db.setNote(transaction.getNote());
        checkValidity(transaction);
        return repository.save(db);
    }

    private static void checkValidity(Transaction transaction){
        if(transaction.getCategory() != null){
            if(!transaction.getCategory().isCanBeNegative() && transaction.getAmount().compareTo(BigDecimal.ZERO) < 0){
                throw new BadRequestException("amount.cant.be.negative");
            }
            if(!transaction.getCategory().isCanBePositive() && transaction.getAmount().compareTo(BigDecimal.ZERO) > 0){
                throw new BadRequestException("amount.cant.be.positive");
            }
        }
    }

    @Transactional
    public void delete(@Valid Transaction transaction){
        repository.delete(get(transaction.getId()));
    }

    public List<Transaction> getAll(@Valid Wallet wallet){
        return repository.findAllByWallet(wallet);
    }

    public Transaction get(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("transaction.not.exists"));
    }

}
