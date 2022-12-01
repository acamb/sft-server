package ac.project.sft;

import ac.project.sft.model.ScheduledTransaction;
import ac.project.sft.model.Transaction;
import ac.project.sft.model.User;
import ac.project.sft.model.Wallet;
import ac.project.sft.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class TestUtils {

    public static User createTestUser(String progressive, UserRepository userRepository){
        if(progressive == null) progressive = "";
        User user = new User();
        user.setUsername("testUser"+progressive);
        user.setPassword("test"+progressive);
        return userRepository.save(user);
    }

    public static Wallet createTestWallet(BigDecimal balance){
        Wallet newWallet = new Wallet();
        newWallet.setBalance(balance);
        newWallet.setDescription("test");
        newWallet.setName("test");
        return newWallet;
    }

    public static Transaction createTransaction(BigDecimal amount){
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.now());
        transaction.setAmount(amount);
        transaction.setName("test");
        return transaction;
    }

    public static ScheduledTransaction createScheduledTransaction(BigDecimal amount){
        ScheduledTransaction transaction = new ScheduledTransaction();
        transaction.setDate(LocalDate.now());
        transaction.setAmount(amount);
        transaction.setName("test");
        return transaction;
    }
}
