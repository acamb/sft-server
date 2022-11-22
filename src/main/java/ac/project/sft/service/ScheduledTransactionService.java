package ac.project.sft.service;

import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.ScheduledTransaction;
import ac.project.sft.model.Wallet;
import ac.project.sft.repository.ScheduledTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Service
@Validated
public class ScheduledTransactionService {

    @Autowired
    ScheduledTransactionRepository repository;

    private Date getNextFireDate(ScheduledTransaction scheduledTransaction){
        //TODO
        return null;
    }

    @Transactional
    public ScheduledTransaction create(@Valid ScheduledTransaction scheduledTransaction){
        if(scheduledTransaction.getId() != null){
            throw new BadRequestException("scheduledTransaction.exists");
        }
        return repository.save(scheduledTransaction);
    }

    @Transactional
    public ScheduledTransaction update(@Valid ScheduledTransaction scheduledTransaction){
        ScheduledTransaction db = repository.findById(scheduledTransaction.getId()).orElseThrow(()-> new NotFoundException("scheduledTransaction.not.exists"));
        db.setAmount(scheduledTransaction.getAmount());
        db.setDate(scheduledTransaction.getDate());
        db.setCategory(scheduledTransaction.getCategory());
        db.setType(scheduledTransaction.getType());
        db.setRecurrent(scheduledTransaction.getRecurrent());
        db.setEndDate(scheduledTransaction.getEndDate());
        db.setDayOfMonth(scheduledTransaction.getDayOfMonth());
        db.setDayOfWeek(scheduledTransaction.getDayOfWeek());
        db.setNextFire(getNextFireDate(db));
        return repository.save(db);
    }

    @Transactional
    public void delete(@Valid ScheduledTransaction scheduledTransaction){
        repository.delete(scheduledTransaction);
    }

    public List<ScheduledTransaction> getAll(Wallet wallet){
        return repository.findAllByWallet(wallet);
    }

    public ScheduledTransaction get(Long id){
        return repository.findById(id).orElseThrow(()-> new NotFoundException("scheduledTransaction.not.exists"));
    }

    public List<ScheduledTransaction> getScheduledTransactionToBeExecuted(Date startDate){
        return repository.findAllByNextFireGreaterThanEqual(startDate);
    }

    @Transactional
    public void schedule(@Valid ScheduledTransaction scheduledTransaction){
        scheduledTransaction.setNextFire(getNextFireDate(scheduledTransaction));
        repository.save(scheduledTransaction);
    }
}
