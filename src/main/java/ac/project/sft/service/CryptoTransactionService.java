package ac.project.sft.service;

import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.CryptoTransaction;
import ac.project.sft.model.CryptoTransactionType;
import ac.project.sft.repository.CryptoTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class CryptoTransactionService {

    private static final String EXISTS_KEY = "transaction.exists";
    private static final String NOT_FOUND_KEY = "transaction.notFound";

    private static final List<CryptoTransactionType> taxables = List.of(CryptoTransactionType.BUY,CryptoTransactionType.SELL);

    @Autowired
    CryptoTransactionRepository repository;

    @Transactional
    public CryptoTransaction create(@Valid CryptoTransaction transaction){
        if(transaction.getId() != null){
            throw new BadRequestException(EXISTS_KEY);
        }
        transaction.setTaxable(taxables.contains(transaction.getTransactionType()));
        return repository.save(transaction);
    }

    @Transactional
    public CryptoTransaction update(@Valid CryptoTransaction transaction){
        CryptoTransaction db = repository.findById(transaction.getId()).orElseThrow(() -> new NotFoundException(NOT_FOUND_KEY));
        db.setFee(transaction.getFee());
        db.setTaxable(transaction.getTaxable());
        db.setTransactionType(transaction.getTransactionType());
        db.setPrice(transaction.getPrice());
        db.setFiatValue(transaction.getFiatValue());
        return repository.save(db);
    }


    @Transactional
    public void delete(@Valid CryptoTransaction transaction){
        repository.delete(get(transaction.getId()));
    }


    public CryptoTransaction get(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_KEY));
    }
}
