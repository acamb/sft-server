package ac.project.sft.controller;

import ac.project.sft.controller.payloads.request.TransactionRequestPayload;
import ac.project.sft.dto.TransactionDto;
import ac.project.sft.dto.WalletDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@RequestBody TransactionRequestPayload payload, Authentication authentication){
        managerService.addTransaction(payload.getWalletDto().getId(),authentication.getName(), payload.getTransactionDto());
    }

    @DeleteMapping("/")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody TransactionRequestPayload payload, Authentication authentication){
        managerService.removeTransaction(payload.getWalletDto().getId(),authentication.getName(), payload.getTransactionDto());
    }

    @GetMapping("/:id")
    public List<TransactionDto> getTransactions(@PathVariable Long walletId,Authentication authentication){
        return mapper.transactionListToDto(
                managerService.getAllTransactions(
                        walletId,
                        authentication.getName()
                )
        );
    }


}
