package ac.project.sft.service;

import ac.project.sft.dto.CategoryDto;
import ac.project.sft.dto.SearchTransactionDto;
import ac.project.sft.dto.WalletStatisticDto;
import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Transaction;
import ac.project.sft.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    public static final String STARTDATE_NULL_KEY="statistics.startDate.null";

    @Autowired
    WalletService walletService;

    @Autowired
    TransactionService transactionService;

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
}
