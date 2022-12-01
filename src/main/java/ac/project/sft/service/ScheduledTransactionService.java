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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Validated
public class ScheduledTransactionService {

    @Autowired
    ScheduledTransactionRepository repository;

    public static LocalDate getNextFireDate(ScheduledTransaction scheduledTransaction,LocalDate now){
        if(Boolean.FALSE.equals(scheduledTransaction.getRecurrent())){
            return scheduledTransaction.getDate().isAfter(now) ? scheduledTransaction.getDate() : null;
        }
        if(scheduledTransaction.getType().equals(RecurrentType.WEEKLY)){
            int dayDiff = 0;
            if(scheduledTransaction.getDayOfWeek().getValue() < now.getDayOfWeek().getValue()){
                dayDiff = Math.abs(DayOfWeek.SUNDAY.getValue() - now.getDayOfWeek().getValue()) +
                        scheduledTransaction.getDayOfWeek().getValue();
            }
            else{
                dayDiff = scheduledTransaction.getDayOfWeek().getValue() - now.getDayOfWeek().getValue();
            }
            if(dayDiff == 0){
                dayDiff = 7;
            }
            //add 7 days for ech getRecurrentFrequency()-1 (the first week is computed in dayDiff)
            int recurrentDiff = scheduledTransaction.getRecurrentFrequency() > 1 ? 7*(scheduledTransaction.getRecurrentFrequency()-1) : 0;
            LocalDate next =  now.plus((long) dayDiff + recurrentDiff, ChronoUnit.DAYS);
            if(scheduledTransaction.getEndDate() != null){
                return next.isBefore(scheduledTransaction.getEndDate()) ? next : null;
            }
            else{
                return next;
            }
        }
        if(scheduledTransaction.getType().equals(RecurrentType.MONTHLY)){
            LocalDate next =  now.plus(scheduledTransaction.getRecurrentFrequency(),ChronoUnit.MONTHS);
            next = normalizeDate(next.getYear(),next.getMonth().getValue(),scheduledTransaction.getDayOfMonth());
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
        if(scheduledTransaction.getRecurrent() && scheduledTransaction.getRecurrentFrequency() == null){
            scheduledTransaction.setRecurrentFrequency(1);
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
        db.setNextFire(getNextFireDate(db,LocalDate.now()));
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
        scheduledTransaction.setNextFire(getNextFireDate(scheduledTransaction,LocalDate.now()));
        return repository.save(scheduledTransaction);
    }


    private static LocalDate normalizeDate(int year, int month, int day) {
        switch (month) {
            case 2 -> day = Math.min(day, IsoChronology.INSTANCE.isLeapYear(year) ? 29 : 28);
            case 4, 6, 9, 11 -> day = Math.min(day, 30);
            default -> day = Math.min(day,31);
        }
        return LocalDate.of(year, month, day);
    }
}
