package ac.project.sft.service;

import ac.project.sft.dto.*;
import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Transaction;
import ac.project.sft.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toCollection;

@Service
public class StatisticsService {

    public static final String STARTDATE_NULL_KEY="statistics.startDate.null";

    @Autowired
    WalletService walletService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    ScheduledTransactionService scheduledTransactionService;

    @Autowired
    DtoMapper mapper;

    public WalletStatisticDto getStatistics(Long walletId, LocalDate startDate, LocalDate endDate) {
        if(startDate == null){
            throw new BadRequestException(STARTDATE_NULL_KEY);
        }
        Wallet wallet = walletService.getWallet(walletId);
        SearchTransactionDto search = SearchTransactionDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
        List<Transaction> transactions = transactionService.getAll(
                wallet,
                Pageable.unpaged(),
                search
        ).toList();
        final CategoryDto noCategory = new CategoryDto();
        noCategory.setId(-1L);
        noCategory.setName("No Category");
        return WalletStatisticDto.builder()
                .expenses(
                        transactions.stream().map(Transaction::getAmount)
                                .filter(i -> i.compareTo(BigDecimal.ZERO) < 0)
                                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO)
                )
                .incomes(
                        transactions.stream().map(Transaction::getAmount)
                                .filter(i -> i.compareTo(BigDecimal.ZERO) > 0)
                                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO)
                )
                .expensesByCategory(
                        transactions.stream().filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0)
                                .collect( Collectors.toMap(t -> (t.getCategory() != null ? mapper.categoryToDto(t.getCategory()) : noCategory).getName(), Transaction::getAmount,BigDecimal::add))
                )
                .build();
    }

    public WalletPrevisionDto getPrevisions(Long walletId,LocalDate startDate,LocalDate endDate){
        Wallet wallet = walletService.getWallet(walletId);
        Comparator<ScheduledTransactionDto> sortScheduled = Comparator.comparing(ScheduledTransactionDto::getNextFire);
        List<ScheduledTransactionDto> scheduled = mapper.scheduledTransactionListToDto(
                scheduledTransactionService.getFutureScheduledTransaction(wallet,startDate)
        ).stream().sorted(sortScheduled).collect(toCollection(ArrayList::new));

        BigDecimal balance = wallet.getBalance();
        BigDecimal extimatedBalance = balance;
        WalletPrevisionDto prevision = new WalletPrevisionDto();
        prevision.getPrevision().put(startDate.format(DateTimeFormatter.ISO_DATE),balance);
        prevision.getEstimated().put(startDate.format(DateTimeFormatter.ISO_DATE),balance);
        LocalDate lastDate= startDate;
        BigDecimal avgExpensePerDay = transactionService.getAvgExpensePerMonth(wallet).divide(BigDecimal.valueOf(30), RoundingMode.HALF_UP);

        while(!scheduled.isEmpty() && scheduled.get(0).getNextFire().isBefore(endDate)){
            ScheduledTransactionDto next = scheduled.remove(0);
            balance = balance.add(next.getAmount());
            extimatedBalance = extimatedBalance.add(next.getAmount());
            if(!prevision.getPrevision().containsKey(next.getNextFire().format(DateTimeFormatter.ISO_DATE))){
                prevision.getPrevision().put(next.getNextFire().format(DateTimeFormatter.ISO_DATE),BigDecimal.ZERO);
            }
            if(!prevision.getEstimated().containsKey(next.getNextFire().format(DateTimeFormatter.ISO_DATE))){
                prevision.getEstimated().put(next.getNextFire().format(DateTimeFormatter.ISO_DATE),BigDecimal.ZERO);
            }

            prevision.getPrevision().put(next.getNextFire().format(DateTimeFormatter.ISO_DATE),balance);
            long days = DAYS.between(lastDate,next.getNextFire());
            extimatedBalance = extimatedBalance.add(avgExpensePerDay.multiply(BigDecimal.valueOf(days)));
            prevision.getEstimated().put(next.getNextFire().format(DateTimeFormatter.ISO_DATE),extimatedBalance);
            lastDate = next.getNextFire();

            next.setNextFire(ScheduledTransactionService.getNextFireDate(mapper.dtoToScheduledTransaction(next),next.getNextFire()));
            if(next.getNextFire() != null && next.getNextFire().isBefore(endDate)){
                scheduled.add(next);
                scheduled.sort(sortScheduled);
            }
        }

        prevision.setEndBalanceEstimated(extimatedBalance);

        return prevision;
    }
}
