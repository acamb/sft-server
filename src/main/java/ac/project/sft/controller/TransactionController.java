package ac.project.sft.controller;

import ac.project.sft.controller.payloads.request.TransactionRequestPayload;
import ac.project.sft.controller.payloads.response.PaginatedResponse;
import ac.project.sft.dto.TransactionDto;
import ac.project.sft.dto.WalletDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Transaction;
import ac.project.sft.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

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
        managerService.updateTransaction(payload.getWalletDto().getId(),authentication.getName(), payload.getTransactionDto());
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
                                                                         Authentication authentication){
        return new PaginatedResponse<>(
                managerService.getAllTransactions(
                walletId,
                authentication.getName(),
                        PageRequest.of(page,size)
                        ),
                l -> mapper.transactionListToDto(l));

    }


}
