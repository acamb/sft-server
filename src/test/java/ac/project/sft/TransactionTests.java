package ac.project.sft;


import ac.project.sft.dto.SearchTransactionDto;
import ac.project.sft.model.*;
import ac.project.sft.repository.UserRepository;
import ac.project.sft.service.CategoryService;
import ac.project.sft.service.TransactionService;
import ac.project.sft.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static ac.project.sft.TestUtils.createTestUser;
import static ac.project.sft.TestUtils.createTestWallet;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class TransactionTests {

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletService walletService;

    @Autowired
    CategoryService categoryService;


    @Test
    public void testSearchDates(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        for(int i=0;i<10;i++){
            Transaction t = createTransaction(user,result.getWallet(),""+i,LocalDate.now().plus(i, ChronoUnit.DAYS));
            transactionService.create(t);
        }
        SearchTransactionDto search = SearchTransactionDto.builder().build();
        search.setStartDate(LocalDate.now().plus(1,ChronoUnit.DAYS));
        assertEquals(9,transactionService.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
        search.setEndDate(LocalDate.now().plus(8,ChronoUnit.DAYS));
        assertEquals(8,transactionService.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
        search.setStartDate(null);
        assertEquals(9,transactionService.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
    }

    @Test
    public void testSearchNames(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        for(int i=0;i<10;i++){
            Transaction t = createTransaction(user,result.getWallet(),""+i,LocalDate.now().plus(i, ChronoUnit.DAYS));
            transactionService.create(t);
        }
        SearchTransactionDto search = SearchTransactionDto.builder().build();
        search.setName("%trx%");
        assertEquals(10,transactionService.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
        search.setName("%9");
        assertEquals(1,transactionService.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
    }

    @Test
    public void testSearchCategory(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        Category c = new Category();
        c.setName("test");
        c = categoryService.create(c);
        for(int i=0;i<10;i++){
            Transaction t = createTransaction(user,result.getWallet(),""+i,LocalDate.now().plus(i, ChronoUnit.DAYS));
            if(i%2==0){
                t.setCategory(c);
            }
            transactionService.create(t);
        }
        SearchTransactionDto search = null;
        assertEquals(10,transactionService.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
        search = SearchTransactionDto.builder()
                .categoryDto(c)
                .build();
        assertEquals(5,transactionService.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
    }


    public Transaction createTransaction(User user, Wallet wallet,String prefix,LocalDate date){
        Transaction result = new Transaction();
        result.setDate(date);
        result.setName("trx"+prefix);
        result.setAmount(BigDecimal.ONE);
        result.setPreviousAmount(BigDecimal.ONE);
        result.setUser(user);
        result.setWallet(wallet);
        return result;
    }

}
