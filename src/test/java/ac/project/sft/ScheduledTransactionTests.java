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
        //TODO
    }
}
