package ac.project.sft;

import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.model.*;
import ac.project.sft.repository.UserRepository;
import ac.project.sft.service.ScheduledTransactionService;
import ac.project.sft.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

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

    @Test
    void addScheduled(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setWallet(result.getWallet());
        scheduledTransaction.setAmount(BigDecimal.valueOf(10));
        scheduledTransaction.setDate(LocalDate.now().plusDays(10));
        scheduledTransaction = service.create(scheduledTransaction);

        assertEquals(1,service.getAll(result.getWallet()).size());
        LocalDate expected = LocalDate.now().plusDays(10);
        assertEquals(expected,scheduledTransaction.getNextFire());
    }

    @Test
    void addScheduledWeekly(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction();
        scheduledTransaction.setWallet(result.getWallet());
        scheduledTransaction.setRecurrent(true);
        scheduledTransaction.setAmount(BigDecimal.valueOf(10));
        scheduledTransaction.setDate(LocalDate.now());
        scheduledTransaction.setType(RecurrentType.WEEKLY);
        scheduledTransaction.setDayOfWeek(LocalDate.now().getDayOfWeek().plus(1));
        scheduledTransaction = service.create(scheduledTransaction);

        assertEquals(1,service.getAll(result.getWallet()).size());
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
        scheduledTransaction.setAmount(BigDecimal.valueOf(10));
        scheduledTransaction.setDate(LocalDate.now());
        scheduledTransaction.setType(RecurrentType.MONTHLY);
        scheduledTransaction.setDayOfMonth(1);
        scheduledTransaction = service.create(scheduledTransaction);

        assertEquals(1,service.getAll(result.getWallet()).size());
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
        testDate = LocalDate.of(2023,1,1);
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

    }
}
