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

@SpringBootTest
@Transactional
public class WalletIntegrationTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    WalletService walletService;

    @Test
    public void createWallet(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet();
        walletService.createWallet(newWallet,user.getUsername());
        assert 1 == walletService.getWallets(user.getUsername()).size();
    }

    @Test
    public void deleteWallet(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet();
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        assert 1 == walletService.getWallets(user.getUsername()).size();
        walletService.deleteWallet(result.getWallet());
        assert 0 == walletService.getWallets(user.getUsername()).size();
    }

    @Test
    public void associateWallet(){
        User user = createTestUser("1",userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet newWallet = createTestWallet();
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        walletService.associateWallet(result.getWallet(),user2.getUsername(),true,false,false);
        assert 1 == walletService.getWallets(user2.getUsername()).size();
    }

    @Test
    public void deassociateWallet(){
        User user = createTestUser("1",userRepository);
        User user2 = createTestUser("2",userRepository);
        Wallet newWallet = createTestWallet();
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        walletService.associateWallet(result.getWallet(),user2.getUsername(),true,false,false);
        walletService.deleteAssociation(result.getWallet(),user2.getUsername());
        assert 1 == walletService.getWallets(user.getUsername()).size();
        assert 0 == walletService.getWallets(user2.getUsername()).size();
    }

    @Test
    public void updateBalance(){
        User user = createTestUser(null,userRepository);
        Wallet newWallet = createTestWallet();
        UserWallet result = walletService.createWallet(newWallet,user.getUsername());
        Wallet wallet = walletService.updateBalance(result.getWallet(),BigDecimal.valueOf(-5));
        assert wallet.getBalance().equals(BigDecimal.valueOf(5));
        wallet = walletService.updateBalance(wallet,BigDecimal.valueOf(15));
        assert wallet.getBalance().equals(BigDecimal.valueOf(20));
    }


}
