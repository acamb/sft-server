package ac.project.sft.service;

import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.RecurrentType;
import ac.project.sft.model.ScheduledTransaction;
import ac.project.sft.model.Wallet;
import ac.project.sft.repository.ScheduledTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Validated
public class ScheduledTransactionService {

    @Autowired
    ScheduledTransactionRepository repository;

    private LocalDate getNextFireDate(ScheduledTransaction scheduledTransaction){
        if(Boolean.FALSE.equals(scheduledTransaction.getRecurrent())){
            return scheduledTransaction.getDate().isAfter(LocalDate.now()) ? scheduledTransaction.getDate() : null;
        }
        if(scheduledTransaction.getType().equals(RecurrentType.WEEKLY)){
            LocalDate now = LocalDate.now();
            int todayDoW = now.getDayOfWeek().getValue();
            int nextDoW = scheduledTransaction.getDayOfWeek().getValue();
            LocalDate next =  now.plus(Math.abs(todayDoW-nextDoW), ChronoUnit.DAYS);
            if(scheduledTransaction.getEndDate() != null){
                return next.isBefore(scheduledTransaction.getEndDate()) ? next : null;
            }
            else{
                return next;
            }
        }
        if(scheduledTransaction.getType().equals(RecurrentType.MONTHLY)){
            LocalDate now = LocalDate.now();
            LocalDate next =  now.plus(1,ChronoUnit.MONTHS).withDayOfMonth(scheduledTransaction.getDayOfMonth());
            if(scheduledTransaction.getEndDate() != null){
                return next.isBefore(scheduledTransaction.getEndDate()) ? next : null;
            }
            else{
                return next;
            }
        }
        throw new IllegalArgumentException("RecurrentType not supported");
    }

    @Transactional
    public ScheduledTransaction create(@Valid ScheduledTransaction scheduledTransaction){
        if(scheduledTransaction.getId() != null){
            throw new BadRequestException("scheduledTransaction.exists");
        }
        if(scheduledTransaction.getType() == RecurrentType.MONTHLY && scheduledTransaction.getDayOfMonth() == null){
            throw new BadRequestException("day.of.month.is.null");
        }
        if(scheduledTransaction.getType() == RecurrentType.WEEKLY && scheduledTransaction.getDayOfWeek() == null){
            throw new BadRequestException("day.of.week.is.null");
        }
        ScheduledTransaction t =  repository.save(scheduledTransaction);
        return schedule(t);
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
    public ScheduledTransaction schedule(@Valid ScheduledTransaction scheduledTransaction){
        scheduledTransaction.setNextFire(getNextFireDate(scheduledTransaction));
        return repository.save(scheduledTransaction);
    }
}
