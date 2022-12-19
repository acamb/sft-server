package ac.project.sft.service;

import ac.project.sft.dto.SearchScheduledTransactionDto;
import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.RecurrentType;
import ac.project.sft.model.ScheduledTransaction;
import ac.project.sft.model.Wallet;
import ac.project.sft.repository.ScheduledTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ac.project.sft.repository.specifications.ScheduledTransactionSpecifications.*;

@Service
@Validated
public class ScheduledTransactionService {

    private static final String RECURRENT_TYPE_NOT_SUPPORTED_KEY = "recurrent.notSupported";
    private static final String NOT_EXISTS_KEY = "scheduledTransaction.exists";
    private static final String DOM_NULL_KEY = "dom.isNull";
    private static final String DOW_NULL_KEY = "dow.isNull";
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
            LocalDate next = now;
            if(now.getDayOfMonth() >= scheduledTransaction.getDayOfMonth()) {
                next = now.plus(scheduledTransaction.getRecurrentFrequency(), ChronoUnit.MONTHS);
            }
            next = normalizeDate(next.getYear(),next.getMonth().getValue(),scheduledTransaction.getDayOfMonth());
            if(scheduledTransaction.getEndDate() != null){
                return next.isBefore(scheduledTransaction.getEndDate()) ? next : null;
            }
            else{
                return next;
            }
        }
        throw new IllegalArgumentException(RECURRENT_TYPE_NOT_SUPPORTED_KEY);
    }

    @Transactional
    public ScheduledTransaction create(@Valid ScheduledTransaction scheduledTransaction){
        if(scheduledTransaction.getId() != null){
            throw new BadRequestException(NOT_EXISTS_KEY);
        }
        if(scheduledTransaction.getType() == RecurrentType.MONTHLY && scheduledTransaction.getDayOfMonth() == null){
            throw new BadRequestException(DOM_NULL_KEY);
        }
        if(scheduledTransaction.getType() == RecurrentType.WEEKLY && scheduledTransaction.getDayOfWeek() == null){
            throw new BadRequestException(DOW_NULL_KEY);
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
        db =  repository.save(db);
        return schedule(db);
    }

    @Transactional
    public void delete(@Valid ScheduledTransaction scheduledTransaction){
        repository.deleteById(scheduledTransaction.getId());
    }

    public Page<ScheduledTransaction> getAll(Wallet wallet, Pageable pageable){
        return getAll(wallet,pageable,null);
    }

    public Page<ScheduledTransaction> getAll(Wallet wallet, Pageable pageable, SearchScheduledTransactionDto search){
        Specification<ScheduledTransaction> spec = Specification.where(wallet(wallet));
        if(search != null){
            spec=spec.and(
                    search.getStartDate() != null ? startDate(search.getStartDate()) : null
            ).and(
                    search.getEndDate() != null ? endDate(search.getEndDate()) : null
            ).and(
                    search.getCategoryDto() != null ? category(search.getCategoryDto()) : null
            ).and(
                    search.getName() != null ? name(search.getName()) : null
            );
        }
        return repository.findAll(spec,pageable);
    }

    public ScheduledTransaction get(Long id){
        return repository.findById(id).orElseThrow(()-> new NotFoundException(NOT_EXISTS_KEY));
    }

    public List<ScheduledTransaction> getScheduledTransactionToBeExecuted(LocalDate startDate){
        return repository.findAllByNextFireLessThanEqual(startDate);
    }

    public List<ScheduledTransaction> getFutureScheduledTransaction(Wallet wallet,LocalDate startDate){
        return repository.findAllByWalletAndNextFireGreaterThanEqual(wallet,startDate);
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
