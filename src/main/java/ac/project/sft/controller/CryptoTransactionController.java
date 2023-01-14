package ac.project.sft.controller;

import ac.project.sft.controller.payloads.request.TransactionRequestPayload;
import ac.project.sft.controller.payloads.response.PaginatedResponse;
import ac.project.sft.dto.SearchTransactionDto;
import ac.project.sft.dto.TransactionDto;
import ac.project.sft.dto.TransactionType;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Category;
import ac.project.sft.model.Transaction;
import ac.project.sft.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/cryptotransaction")
public class CryptoTransactionController {

    @Autowired
    ManagerService managerService;
    @Autowired
    DtoMapper mapper;

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void create(@RequestBody TransactionRequestPayload payload, Authentication authentication){
        managerService.addTransaction(payload.getWalletDto().getId(),authentication.getName(), payload.getTransactionDto());
    }

    @PutMapping("/")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody TransactionRequestPayload payload, Authentication authentication){
        managerService.updateTransaction(authentication.getName(), payload.getTransactionDto());
    }

    @DeleteMapping("/")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody TransactionRequestPayload payload, Authentication authentication){
        managerService.removeTransaction(payload.getWalletDto().getId(),authentication.getName(), payload.getTransactionDto());
    }

    @GetMapping("/{id}")
    public PaginatedResponse<Transaction,TransactionDto> getTransactions(@PathVariable("id") Long walletId,
                                                                         @RequestParam("page") int page,
                                                                         @RequestParam("size") int size,
                                                                         @RequestParam(value = "startDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
                                                                         @RequestParam(value = "endDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
                                                                         @RequestParam(value = "category",required = false) Long category,
                                                                         @RequestParam(value = "type",required = false) TransactionType type,
                                                                         @RequestParam(value = "name",required = false) String name,
                                                                         Authentication authentication){
        SearchTransactionDto search = SearchTransactionDto.builder()
                .type(type)
                .name(name)
                .startDate(startDate)
                .endDate(endDate).build();
        if(category != null) {
            search.setCategoryDto(new Category());
            search.getCategoryDto().setId(category);
        }
        return new PaginatedResponse<>(
                managerService.getAllTransactions(
                walletId,
                authentication.getName(),
                PageRequest.of(page,size, Sort.by("date").descending().and(Sort.by("name"))),
                search
                ),
                l -> mapper.transactionListToDto(l));

    }
}
