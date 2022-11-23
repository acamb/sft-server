package ac.project.sft;

import ac.project.sft.model.User;
import ac.project.sft.model.UserWallet;
import ac.project.sft.model.Wallet;
import ac.project.sft.repository.UserRepository;
import ac.project.sft.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static ac.project.sft.TestUtils.createTestUser;
import static ac.project.sft.TestUtils.createTestWallet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WalletIntegrationTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    WalletService walletService;

    @Test
    void createWallet(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        walletService.createWallet(newWallet,user.getUsername());
        assertEquals(1,walletService.getWallets(user.getUsername()).size());
    }

    @Test
    void deleteWallet(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        assertEquals( 1 , walletService.getWallets(user.getUsername()).size());
        walletService.deleteWallet(result.getWallet());
        assertEquals( 0 , walletService.getWallets(user.getUsername()).size());
    }

    @Test
    void associateWallet(){
        User user = createTestUser("1",userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        walletService.associateWallet(result.getWallet(),user2.getUsername(),true,false,false);
        assertEquals( 1 , walletService.getWallets(user2.getUsername()).size());
    }

    @Test
    void deassociateWallet(){
        User user = createTestUser("1",userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        walletService.associateWallet(result.getWallet(),user2.getUsername(),true,false,false);
        walletService.deleteAssociation(result.getWallet(),user2.getUsername());
        assertEquals( 1 , walletService.getWallets(user.getUsername()).size());
        assertEquals( 0 , walletService.getWallets(user2.getUsername()).size());
    }

    @Test
    void updateBalance(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet(BigDecimal.valueOf(10));
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        Wallet wallet = walletService.updateBalance(result.getWallet(),BigDecimal.valueOf(-5));
        assertEquals( BigDecimal.valueOf(5),wallet.getBalance());
        wallet = walletService.updateBalance(wallet,BigDecimal.valueOf(15));
        assertEquals(BigDecimal.valueOf(20), wallet.getBalance());
    }


}
