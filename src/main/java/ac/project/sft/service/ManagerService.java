package ac.project.sft.service;

import ac.project.sft.dto.SearchScheduledTransactionDto;
import ac.project.sft.dto.SearchTransactionDto;
import ac.project.sft.dto.TransactionDto;
import ac.project.sft.exceptions.NotAuthorizedException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Validated
public class ManagerService {

    public static final String NO_GRANTS = "user.has.not.grants";
    @Autowired
    TransactionService transactionService;
    @Autowired
    WalletService walletService;
    @Autowired
    DtoMapper mapper;

    @Autowired
    CategoryService categoryService;
    @Autowired
    ScheduledTransactionService scheduledTransactionService;


    @Transactional
    public void addTransaction(Long walletId, String username,TransactionDto transactionDto){
        UserWallet userWallet = walletService.getWallet(walletId,username);
        if(canWrite(userWallet)){
            addTransaction(userWallet.getWallet(),mapper.dtoToTransaction(transactionDto),userWallet.getUser());
        }
        else{
            throw new NotAuthorizedException(NO_GRANTS);
        }
    }

    private void addTransaction(@Valid Wallet wallet,@Valid Transaction transaction,@Valid User user){
        transaction.setPreviousAmount(wallet.getBalance());
        transaction.setUser(user);
        transaction.setWallet(wallet);
        if(transaction.getCategory() != null) {
            transaction.setCategory(categoryService.get(transaction.getCategory().getId()));
        }
        transaction = transactionService.create(transaction);
        walletService.updateBalance(wallet,transaction.getAmount());
    }

    @Transactional
    public void removeTransaction(Long walletId, String username,TransactionDto transactionDto){
        UserWallet userWallet = walletService.getWallet(walletId,username);
        if(canWrite(userWallet)){
            Transaction transaction = transactionService.get(transactionDto.getId());
            walletService.updateBalance(userWallet.getWallet(),transaction.getAmount().multiply(BigDecimal.valueOf(-1)));
            transactionService.delete(transaction);
        }
        else{
            throw new NotAuthorizedException(NO_GRANTS);
        }
    }

    @Transactional
    public void processScheduledTransaction(@Valid ScheduledTransaction scheduledTransaction){
        Transaction transaction = new Transaction();
        transaction.setAmount(scheduledTransaction.getAmount());
        transaction.setName(scheduledTransaction.getName());
        transaction.setCategory(scheduledTransaction.getCategory());
        transaction.setWallet(scheduledTransaction.getWallet());
        transaction.setPreviousAmount(scheduledTransaction.getWallet().getBalance());
        transaction.setDate(LocalDate.now());
        transaction.setScheduled(true);
        scheduledTransactionService.schedule(scheduledTransaction);
        Wallet wallet = walletService.getWallet(scheduledTransaction.getWallet().getId());
        addTransaction(wallet,transaction,null);
    }
    
    @Transactional
    public ScheduledTransaction addScheduled(@Valid Wallet wallet,@Valid ScheduledTransaction scheduledTransaction,String username){
        UserWallet userWallet = walletService.getWallet(wallet.getId(),username);
        if(canWrite(userWallet)){
            scheduledTransaction.setWallet(wallet);
            if(scheduledTransaction.getCategory() != null) {
                scheduledTransaction.setCategory(categoryService.get(scheduledTransaction.getCategory().getId()));
            }
            return scheduledTransactionService.create(scheduledTransaction);
        }
        else{
            throw new NotAuthorizedException(NO_GRANTS);
        }
    }

    @Transactional
    public ScheduledTransaction updateScheduled(@Valid Wallet wallet,@Valid ScheduledTransaction scheduledTransaction,String username){
        UserWallet userWallet = walletService.getWallet(wallet.getId(),username);
        if(canWrite(userWallet)){
            scheduledTransaction.setWallet(wallet);
            if(scheduledTransaction.getCategory() != null) {
                scheduledTransaction.setCategory(categoryService.get(scheduledTransaction.getCategory().getId()));
            }
            return scheduledTransactionService.update(scheduledTransaction);
        }
        else{
            throw new NotAuthorizedException(NO_GRANTS);
        }
    }

    @Transactional
    public void deleteScheduled(@Valid Wallet wallet,@Valid ScheduledTransaction scheduledTransaction,String username){
        UserWallet userWallet = walletService.getWallet(wallet.getId(),username);
        if(canWrite(userWallet)){
            scheduledTransactionService.delete(scheduledTransaction);
        }
        else{
            throw new NotAuthorizedException(NO_GRANTS);
        }
    }
    public Page<ScheduledTransaction> getAllScheduled(Long walletId, String username, Pageable pageable) {
        return getAllScheduled(walletId,username,pageable,null);
    }

    public Page<ScheduledTransaction> getAllScheduled(Long walletId, String username, Pageable pageable, SearchScheduledTransactionDto search){
        UserWallet userWallet = walletService.getWallet(walletId,username);
        if(canWrite(userWallet)){
            if(search != null && search.getCategoryDto() != null ){
                search.setCategoryDto(categoryService.get(search.getCategoryDto().getId()));
            }
            return scheduledTransactionService.getAll(userWallet.getWallet(),pageable,search);
        }
        else{
            throw new NotAuthorizedException(NO_GRANTS);
        }
    }

    public Page<Transaction> getAllTransactions(@Valid Wallet wallet,String username, Pageable pageable){
        return getAllTransactions(wallet,username,pageable,null);
    }

    public Page<Transaction> getAllTransactions(@Valid Wallet wallet,String username, Pageable pageable, SearchTransactionDto search){
        return getAllTransactions(wallet.getId(),username,pageable,search);
    }

    public Page<Transaction> getAllTransactions(Long walletId, String username, Pageable pageable) {
        return getAllTransactions(walletId,username,pageable,null);
    }

    public Page<Transaction> getAllTransactions(Long walletId, String username, Pageable pageable, SearchTransactionDto search){
        UserWallet userWallet = walletService.getWallet(walletId,username);
        if(canRead(userWallet)){
            if(search != null && search.getCategoryDto() != null ) {
                search.setCategoryDto(categoryService.get(search.getCategoryDto().getId()));
            }
            return transactionService.getAll(userWallet.getWallet(),pageable,search);
        }
        else{
            throw new NotAuthorizedException(NO_GRANTS);
        }
    }
    
    public static boolean canWrite(UserWallet wallet){
        return wallet.getWrite() || wallet.getOwner();
    }

    public static boolean canRead(UserWallet wallet){
        return wallet.getRead() || wallet.getOwner();
    }

    public void updateTransaction(String name, TransactionDto transactionDto) {
        Transaction transaction = transactionService.get(transactionDto.getId());
        transaction.setName(transactionDto.getName());
        if(transactionDto.getCategoryDto() != null){
            transaction.setCategory(categoryService.get(transactionDto.getCategoryDto().getId()));
        }
        transaction.setNote(transactionDto.getNote());
        transaction.setScheduled(transactionDto.isScheduled());
        transactionService.update(transaction);
    }
}
