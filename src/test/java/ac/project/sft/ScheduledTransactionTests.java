package ac.project.sft;

import ac.project.sft.dto.SearchScheduledTransactionDto;
import ac.project.sft.dto.SearchTransactionDto;
import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.model.*;
import ac.project.sft.repository.UserRepository;
import ac.project.sft.service.CategoryService;
import ac.project.sft.service.ScheduledTransactionService;
import ac.project.sft.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static ac.project.sft.TestUtils.createTestUser;
import static ac.project.sft.TestUtils.createTestWallet;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduledTransactionTests {

    @Autowired
    ScheduledTransactionService service;
    @Autowired
    WalletService walletService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryService categoryService;

    @Test
    void addScheduled(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setWallet(result.getWallet());
        scheduledTransaction.setName("test");
        scheduledTransaction.setAmount(BigDecimal.valueOf(10));
        scheduledTransaction.setDate(LocalDate.now().plusDays(10));
        scheduledTransaction = service.create(scheduledTransaction);

        assertEquals(1,service.getAll(result.getWallet(), PageRequest.of(0,25)).getTotalElements());
        LocalDate expected = LocalDate.now().plusDays(10);
        assertEquals(expected,scheduledTransaction.getNextFire());
    }

    @Test
    void removeScheduled(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setWallet(result.getWallet());
        scheduledTransaction.setName("test");
        scheduledTransaction.setAmount(BigDecimal.valueOf(10));
        scheduledTransaction.setDate(LocalDate.now().plusDays(10));
        scheduledTransaction = service.create(scheduledTransaction);
        service.delete(scheduledTransaction);
        assertEquals(0,service.getAll(result.getWallet(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void addScheduledWeekly(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setWallet(result.getWallet());
        scheduledTransaction.setRecurrent(true);
        scheduledTransaction.setName("test");
        scheduledTransaction.setAmount(BigDecimal.valueOf(10));
        scheduledTransaction.setDate(LocalDate.now());
        scheduledTransaction.setType(RecurrentType.WEEKLY);
        scheduledTransaction.setDayOfWeek(LocalDate.now().getDayOfWeek().plus(1));
        scheduledTransaction = service.create(scheduledTransaction);

        assertEquals(1,service.getAll(result.getWallet(), PageRequest.of(0,25)).getTotalElements());
        LocalDate expected = LocalDate.now().plusDays(1);
        assertEquals(expected,scheduledTransaction.getNextFire());
    }

    @Test
    void addScheduledMonthly(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setWallet(result.getWallet());
        scheduledTransaction.setRecurrent(true);
        scheduledTransaction.setName("test");
        scheduledTransaction.setAmount(BigDecimal.valueOf(10));
        scheduledTransaction.setDate(LocalDate.now());
        scheduledTransaction.setType(RecurrentType.MONTHLY);
        scheduledTransaction.setDayOfMonth(1);
        scheduledTransaction = service.create(scheduledTransaction);

        assertEquals(1,service.getAll(result.getWallet(), PageRequest.of(0,25)).getTotalElements());
        LocalDate expected = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        assertEquals(expected,scheduledTransaction.getNextFire());
    }

    @Test
    void addScheduledWeeklyError(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setWallet(result.getWallet());
        scheduledTransaction.setName("test");
        scheduledTransaction.setAmount(BigDecimal.valueOf(10));
        scheduledTransaction.setDate(LocalDate.now());
        scheduledTransaction.setType(RecurrentType.WEEKLY);
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> service.create(scheduledTransaction),
                "malformed scheduled transaction was created!"
        );
        assertNotNull(ex);
    }

    @Test
    void addScheduledMonthlyError(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setWallet(result.getWallet());
        scheduledTransaction.setName("test");
        scheduledTransaction.setAmount(BigDecimal.valueOf(10));
        scheduledTransaction.setDate(LocalDate.now());
        scheduledTransaction.setType(RecurrentType.MONTHLY);
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> service.create(scheduledTransaction),
                "malformed scheduled transaction was created!"
        );
        assertNotNull(ex);
    }

    @Test
    void nextFireDate(){
        LocalDate testDate = LocalDate.of(2020,10,1);
        ScheduledTransaction sc = new ScheduledTransaction();
        sc.setDate(LocalDate.now());
        sc.setAmount(BigDecimal.valueOf(10));
        sc.setRecurrent(true);
        sc.setType(RecurrentType.MONTHLY);
        //MONTH +1
        sc.setDayOfMonth(1);
        LocalDate result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        LocalDate expected = LocalDate.of(2020,11,1);
        assertEquals(expected,result);
        //MONTH +12
        sc.setDayOfMonth(1);
        sc.setRecurrentFrequency(12);
        result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        expected = LocalDate.of(2021,10,1);
        assertEquals(expected,result);
        sc.setRecurrentFrequency(1);
        //end of year + 1 month
        testDate = LocalDate.of(2020,12,1);
        result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        expected = LocalDate.of(2021,1,1);
        assertEquals(expected,result);
        //month +1 (31 days) on feb
        sc.setDayOfMonth(31);
        testDate = LocalDate.of(2023,2,1);
        result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        expected = LocalDate.of(2023,2,28);
        assertEquals(expected,result);
        //WEEK + 1
        sc.setDayOfMonth(null);
        sc.setType(RecurrentType.WEEKLY);
        sc.setDayOfWeek(DayOfWeek.FRIDAY);
        testDate = LocalDate.of(2022,11,25);
        result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        expected = LocalDate.of(2022,12,2);
        assertEquals(expected,result);
        //WEEK + 7
        sc.setDayOfWeek(DayOfWeek.FRIDAY);
        sc.setRecurrentFrequency(7);
        testDate = LocalDate.of(2022,11,25);
        result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        expected = LocalDate.of(2023,1,13);
        assertEquals(expected,result);
        sc.setRecurrentFrequency(1);
        //end of year + 1 week
        sc.setDayOfWeek(DayOfWeek.WEDNESDAY);
        testDate = LocalDate.of(2022,12,28);
        result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        expected = LocalDate.of(2023,1,4);
        assertEquals(expected,result);
        //WEEK  -- middle of week
        sc.setDayOfWeek(DayOfWeek.TUESDAY);
        sc.setType(RecurrentType.WEEKLY);
        sc.setRecurrentFrequency(1);
        testDate = LocalDate.of(2022,12,1);
        result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        expected = LocalDate.of(2022,12,6);
        assertEquals(expected,result);
        //WEEK  + 2 -- middle of week
        sc.setDayOfWeek(DayOfWeek.TUESDAY);
        sc.setType(RecurrentType.WEEKLY);
        sc.setRecurrentFrequency(2);
        testDate = LocalDate.of(2022,12,1);
        result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        expected = LocalDate.of(2022,12,13);
        assertEquals(expected,result);

        //month +1 (31 days) on feb
        sc.setType(RecurrentType.MONTHLY);
        sc.setDayOfMonth(31);
        sc.setRecurrentFrequency(1);
        testDate = LocalDate.of(2022,2,28);
        result = ScheduledTransactionService.getNextFireDate(sc,testDate);
        expected = LocalDate.of(2022,3,31);
        assertEquals(expected,result);

    }

    @Test
    public void testSearchDates(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        for(int i=0;i<10;i++){
            ScheduledTransaction t = createTransaction(user,result.getWallet(),""+i,LocalDate.now().plus(i, ChronoUnit.DAYS));
            service.create(t);
        }
        SearchScheduledTransactionDto search = new SearchScheduledTransactionDto();
        search.setStartDate(LocalDate.now().plus(1,ChronoUnit.DAYS));
        assertEquals(9,service.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
        search.setEndDate(LocalDate.now().plus(8,ChronoUnit.DAYS));
        assertEquals(8,service.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
        search.setStartDate(null);
        assertEquals(9,service.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
    }

    @Test
    public void testSearchNames(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        for(int i=0;i<10;i++){
            ScheduledTransaction t = createTransaction(user,result.getWallet(),""+i,LocalDate.now().plus(i, ChronoUnit.DAYS));
            service.create(t);
        }
        SearchScheduledTransactionDto search = new SearchScheduledTransactionDto();
        search.setName("%trx%");
        assertEquals(10,service.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
        search.setName("%9");
        assertEquals(1,service.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
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
            ScheduledTransaction t = createTransaction(user,result.getWallet(),""+i,LocalDate.now().plus(i, ChronoUnit.DAYS));
            if(i%2==0){
                t.setCategory(c);
            }
            service.create(t);
        }
        SearchScheduledTransactionDto search = null;
        assertEquals(10,service.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
        search = new SearchScheduledTransactionDto();
        search.setCategoryDto(c);
        assertEquals(5,service.getAll(result.getWallet(), PageRequest.of(0,100),search).getTotalElements());
    }

    public ScheduledTransaction createTransaction(User user, Wallet wallet,String prefix,LocalDate date){
        ScheduledTransaction result = new ScheduledTransaction();
        result.setDate(date);
        result.setName("trx"+prefix);
        result.setName("trx"+prefix);
        result.setAmount(BigDecimal.ONE);
        result.setWallet(wallet);
        return result;
    }
}
