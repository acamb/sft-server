package ac.project.sft;

import ac.project.sft.exceptions.NotAuthorizedException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.*;
import ac.project.sft.repository.UserRepository;
import ac.project.sft.service.ManagerService;
import ac.project.sft.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static ac.project.sft.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ManagerIntegrationTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ManagerService managerService;
    @Autowired
    WalletService walletService;
    @Autowired
    DtoMapper mapper;

    @Test
    void addTransaction(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(BigDecimal.valueOf(30), wallet.getBalance());
        assertEquals(1, managerService.getAllTransactions(wallet, user.getUsername(), PageRequest.of(0,25)).getTotalElements());
        t = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);
        assertEquals(t.getPreviousAmount(), BigDecimal.valueOf(10));
        assertEquals(t.getUser().getUsername(), user.getUsername());
    }

    @Test
    void addTransactionNoGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> managerService.addTransaction(userWallet.getWallet().getId(),user2.getUsername(), mapper.transactionToDto(t)),
                "transaction added but not authorized"
        );
        assertNotNull(ex);
    }

    @Test
    void addTransactionNoGrants2(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,false);
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        NotAuthorizedException ex = assertThrows(
                NotAuthorizedException.class,
                () -> managerService.addTransaction(userWallet.getWallet().getId(),user2.getUsername(), mapper.transactionToDto(t)),
                "transaction added but not authorized"
        );
        assertNotNull(ex);
    }

    @Test
    void addTransactionWithGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,true,false);
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(BigDecimal.valueOf(30), wallet.getBalance());
        assertEquals(1, managerService.getAllTransactions(wallet, user.getUsername(), PageRequest.of(0,25)).getTotalElements());
        t = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);
        assertEquals(t.getPreviousAmount(), BigDecimal.valueOf(10));
        assertEquals(t.getUser().getUsername(), user.getUsername());
    }

    @Test
    void addTransactionWithGrantsOwner(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,true);
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(BigDecimal.valueOf(30), wallet.getBalance());
        assertEquals(1, managerService.getAllTransactions(wallet, user.getUsername(), PageRequest.of(0,25)).getTotalElements());
        t = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);
        assertEquals(t.getPreviousAmount(), BigDecimal.valueOf(10));
        assertEquals(t.getUser().getUsername(), user.getUsername());
    }

    @Test
    void removeTransaction(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        t = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);
        managerService.removeTransaction(userWallet.getWallet().getId(),user.getUsername(),mapper.transactionToDto(t));

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(BigDecimal.valueOf(10), wallet.getBalance());
        assertEquals(0, managerService.getAllTransactions(wallet, user.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void removeTransactionNoGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        Transaction transaction = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> managerService.removeTransaction(userWallet.getWallet().getId(),user2.getUsername(),mapper.transactionToDto(transaction)),
                "transaction removed but not authorized"
        );
        assertNotNull(ex);
    }

    @Test
    void removeTransactionNoGrants2(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,false);
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        Transaction transaction = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);

        NotAuthorizedException ex = assertThrows(
                NotAuthorizedException.class,
                () -> managerService.removeTransaction(userWallet.getWallet().getId(),user2.getUsername(),mapper.transactionToDto(transaction)),
                "transaction removed but not authorized"
        );
        assertNotNull(ex);
    }

    @Test
    void removeTransactionWithGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,true,false);
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        t = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);
        managerService.removeTransaction(userWallet.getWallet().getId(),user.getUsername(),mapper.transactionToDto(t));

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(BigDecimal.valueOf(10), wallet.getBalance());
        assertEquals(0, managerService.getAllTransactions(wallet, user.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void removeTransactionWithGrantsOwner(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,true);
        Transaction t = createTransaction(BigDecimal.valueOf(20));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        t = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);
        managerService.removeTransaction(userWallet.getWallet().getId(),user.getUsername(),mapper.transactionToDto(t));

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(BigDecimal.valueOf(10), wallet.getBalance());
        assertEquals(0, managerService.getAllTransactions(wallet, user.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }



    @Test
    void addScheduled(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(10));
        assertEquals(1, managerService.getAllScheduled(wallet.getId(), user.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void addScheduledWithGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,true,false);
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        managerService.addScheduled(userWallet.getWallet(),t,user2.getUsername());

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(10));
        assertEquals(1, managerService.getAllScheduled(wallet.getId(), user2.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void addScheduledWithGrantsOwner(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,true);
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        managerService.addScheduled(userWallet.getWallet(),t,user2.getUsername());

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(10));
        assertEquals(1, managerService.getAllScheduled(wallet.getId(), user2.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void addScheduledNoGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,false);

        NotAuthorizedException ex = assertThrows(
                NotAuthorizedException.class,
                () -> managerService.addScheduled(userWallet.getWallet(),t,user2.getUsername()),
                "transaction added but not authorized"
        );
        assertNotNull(ex);

    }

    @Test
    void addScheduledNoGrants2(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));


        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> managerService.addScheduled(userWallet.getWallet(),t,user2.getUsername()),
                "transaction added but not authorized"
        );
        assertNotNull(ex);

    }

    @Test
    void removeScheduled(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        t=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());
        managerService.deleteScheduled(userWallet.getWallet(),t,user.getUsername());

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(10));
        assertEquals(0, managerService.getAllScheduled(wallet.getId(), user.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void removeScheduledWithGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,true,false);
        t=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());
        managerService.deleteScheduled(userWallet.getWallet(),t,user2.getUsername());

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(10));
        assertEquals(0, managerService.getAllScheduled(wallet.getId(), user2.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void removeScheduledWithGrantsOwner(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,true);
        t=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());
        managerService.deleteScheduled(userWallet.getWallet(),t,user2.getUsername());

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(10));
        assertEquals(0, managerService.getAllScheduled(wallet.getId(), user2.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void removeScheduledNoGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,false);
        ScheduledTransaction transaction=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());


        NotAuthorizedException ex = assertThrows(
                NotAuthorizedException.class,
                () -> managerService.deleteScheduled(userWallet.getWallet(),transaction,user2.getUsername()),
                "transaction added but not authorized"
        );
        assertNotNull(ex);

    }

    @Test
    void removeScheduledNoGrants2(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        ScheduledTransaction transaction=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());


        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> managerService.deleteScheduled(userWallet.getWallet(),transaction,user2.getUsername()),
                "transaction added but not authorized"
        );
        assertNotNull(ex);

    }

    @Test
    void updateScheduled(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        t=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());
        t.setAmount(BigDecimal.valueOf(-15));
        t=managerService.updateScheduled(userWallet.getWallet(),t,user.getUsername());

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(10));
        assertEquals(1, managerService.getAllScheduled(wallet.getId(), user.getUsername(), PageRequest.of(0,25)).getTotalElements());
        assertEquals(managerService.getAllScheduled(wallet.getId(), user.getUsername(), PageRequest.of(0,25)).toList().get(0).getAmount(), BigDecimal.valueOf(-15));
    }

    @Test
    void updateScheduledWithGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,true,false);
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        t=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());
        t.setAmount(BigDecimal.valueOf(-15));
        t=managerService.updateScheduled(userWallet.getWallet(),t,user2.getUsername());

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(10));
        assertEquals(1, managerService.getAllScheduled(wallet.getId(), user.getUsername(), PageRequest.of(0,25)).getTotalElements());
        assertEquals(managerService.getAllScheduled(wallet.getId(), user.getUsername(), PageRequest.of(0,25)).toList().get(0).getAmount(), BigDecimal.valueOf(-15));
    }

    @Test
    void updateScheduledWithGrantsOwner(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,true);
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        t=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());
        t.setAmount(BigDecimal.valueOf(-15));
        t=managerService.updateScheduled(userWallet.getWallet(),t,user2.getUsername());

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(10));
        assertEquals(1, managerService.getAllScheduled(wallet.getId(), user.getUsername(), PageRequest.of(0,25)).getTotalElements());
        assertEquals(managerService.getAllScheduled(wallet.getId(), user.getUsername(), PageRequest.of(0,25)).toList().get(0).getAmount(), BigDecimal.valueOf(-15));
    }

    @Test
    void updateScheduledNoGrants(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        ScheduledTransaction transaction=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());
        transaction.setAmount(BigDecimal.valueOf(-15));


        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> managerService.updateScheduled(userWallet.getWallet(),transaction,user2.getUsername()),
                "transaction added but not authorized"
        );
        assertNotNull(ex);
    }

    @Test
    void updateScheduledNoGrants2(){
        User user = createTestUser(null,userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        walletService.associateWallet(userWallet.getWallet(),user2.getUsername(),true,false,false);
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        ScheduledTransaction transaction=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());
        transaction.setAmount(BigDecimal.valueOf(-15));


        NotAuthorizedException ex = assertThrows(
                NotAuthorizedException.class,
                () -> managerService.updateScheduled(userWallet.getWallet(),transaction,user2.getUsername()),
                "transaction added but not authorized"
        );
        assertNotNull(ex);
    }

    @Test
    void processScheduled(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        ScheduledTransaction t = createScheduledTransaction(BigDecimal.valueOf(-5));
        t=managerService.addScheduled(userWallet.getWallet(),t,user.getUsername());
        managerService.processScheduledTransaction(t);

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(wallet.getBalance(), BigDecimal.valueOf(5));
        assertEquals(1, managerService.getAllScheduled(wallet.getId(), user.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }

    @Test
    void addCryptoTransaction(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createCryptoTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());

        Transaction t = createCryptoTransaction(BigDecimal.valueOf(20),CryptoTransactionType.BUY);
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(BigDecimal.valueOf(30), wallet.getBalance());
        assertEquals(1, managerService.getAllTransactions(wallet, user.getUsername(), PageRequest.of(0,25)).getTotalElements());
        t = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);
        assertEquals(t.getPreviousAmount(), BigDecimal.valueOf(10));
        assertEquals(t.getCryptoTransaction().getTaxable(),true);
        assertEquals(t.getCryptoTransaction().getFiatValue(),BigDecimal.valueOf(22.0));
        assertEquals(t.getUser().getUsername(), user.getUsername());
    }

    @Test
    void addCryptoTransactionNoTax(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createCryptoTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());

        Transaction t = createCryptoTransaction(BigDecimal.valueOf(20),CryptoTransactionType.TRANSFER);
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(BigDecimal.valueOf(30), wallet.getBalance());
        assertEquals(1, managerService.getAllTransactions(wallet, user.getUsername(), PageRequest.of(0,25)).getTotalElements());
        t = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);
        assertEquals(t.getPreviousAmount(), BigDecimal.valueOf(10));
        assertEquals(t.getCryptoTransaction().getTaxable(),false);
        assertEquals(t.getCryptoTransaction().getFiatValue(),BigDecimal.valueOf(22.0));
        assertEquals(t.getUser().getUsername(), user.getUsername());
    }

    @Test
    void removeCryptoTransaction(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createCryptoTestWallet(BigDecimal.valueOf(10));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        Transaction t = createCryptoTransaction(BigDecimal.valueOf(20),CryptoTransactionType.BUY);
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        t = managerService.getAllTransactions(wallet,user.getUsername(), PageRequest.of(0,25)).toList().get(0);
        managerService.removeTransaction(userWallet.getWallet().getId(),user.getUsername(),mapper.transactionToDto(t));

        wallet = walletService.getWallet(userWallet.getWallet().getId());
        assertEquals(BigDecimal.valueOf(10), wallet.getBalance());
        assertEquals(0, managerService.getAllTransactions(wallet, user.getUsername(), PageRequest.of(0,25)).getTotalElements());
    }


}
