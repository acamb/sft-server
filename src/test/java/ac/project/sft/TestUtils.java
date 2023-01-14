package ac.project.sft;

import ac.project.sft.model.*;
import ac.project.sft.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public static Wallet createCryptoTestWallet(BigDecimal balance){
        Wallet newWallet = new Wallet();
        newWallet.setBalance(balance);
        newWallet.setDescription("test");
        newWallet.setName("test");
        newWallet.setWalletType(WalletType.CRYPTO);
        return newWallet;
    }

    public static Transaction createTransaction(BigDecimal amount){
        return createTransaction(amount,null);
    }

    public static Transaction createCryptoTransaction(BigDecimal amount,CryptoTransactionType type){
        return createCryptoTransaction(amount,type,null);
    }

    public static Transaction createTransaction(BigDecimal amount, Category category){
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.now());
        transaction.setAmount(amount);
        transaction.setName("test");
        transaction.setCategory(category);
        return transaction;
    }

    public static Transaction createCryptoTransaction(BigDecimal amount,CryptoTransactionType type, Category category){
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.now());
        transaction.setAmount(amount);
        transaction.setName("test");
        transaction.setCategory(category);
        CryptoTransaction ct = new CryptoTransaction();
        ct.setTransactionType(type);
        ct.setFee(BigDecimal.valueOf(0.1));
        ct.setBaseValueUsed(BigDecimal.valueOf(1.10));
        transaction.setCryptoTransaction(ct);
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
