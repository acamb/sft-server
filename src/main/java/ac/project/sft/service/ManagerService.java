package ac.project.sft.service;

import ac.project.sft.dto.TransactionDto;
import ac.project.sft.exceptions.NotAuthorizedException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;

@Service
@Validated
public class ManagerService {

    @Autowired
    TransactionService transactionService;
    @Autowired
    WalletService walletService;
    @Autowired
    DtoMapper mapper;
    @Autowired
    ScheduledTransactionService scheduledTransactionService;


    @Transactional
    public void addTransaction(Long walletId, String username,TransactionDto transactionDto){
        UserWallet userWallet = walletService.getWallet(walletId,username);
        if(userWallet.getWrite() || userWallet.getOwner()){
            addTransaction(userWallet.getWallet(),mapper.dtoToTransaction(transactionDto),userWallet.getUser());
        }
        else{
            throw new NotAuthorizedException("user.has.not.grants");
        }
    }
    @Transactional
    public Wallet addTransaction(@Valid Wallet wallet,@Valid Transaction transaction,@Valid User user){
        transaction.setPreviousAmount(wallet.getBalance());
        transaction.setUser(user);
        transaction = transactionService.create(transaction);
        return walletService.updateBalance(wallet,transaction.getAmount());
    }

    @Transactional
    public void removeTransaction(Long walletId, String username,TransactionDto transactionDto){
        UserWallet userWallet = walletService.getWallet(walletId,username);
        if(userWallet.getWrite() || userWallet.getOwner()){
            Transaction transaction = transactionService.get(transactionDto.getId());
            walletService.updateBalance(userWallet.getWallet(),transaction.getAmount().multiply(BigDecimal.valueOf(-1)));
            transactionService.delete(transaction);
        }
        else{
            throw new NotAuthorizedException("user.has.not.grants");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void processScheduledTransaction(@Valid ScheduledTransaction scheduledTransaction){
        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setAmount(scheduledTransaction.getAmount());
        transaction.setCategory(scheduledTransaction.getCategory());
        transaction.setWallet(scheduledTransaction.getWallet());
        transaction.setPreviousAmount(scheduledTransaction.getWallet().getBalance());
        scheduledTransactionService.schedule(scheduledTransaction);
        Wallet wallet = walletService.getWallet(scheduledTransaction.getWallet().getId());
        addTransaction(wallet,transaction,null);
    }

}
