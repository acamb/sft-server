package ac.project.sft;

import ac.project.sft.model.User;
import ac.project.sft.model.Wallet;
import ac.project.sft.repository.UserRepository;

import java.math.BigDecimal;

public class TestUtils {

    public static User createTestUser(String progressive, UserRepository userRepository){
        if(progressive == null) progressive = "";
        User user = new User();
        user.setUsername("testUser"+progressive);
        user.setPassword("test"+progressive);
        return userRepository.save(user);
    }

    public static Wallet createTestWallet(){
        Wallet newWallet = new Wallet();
        newWallet.setBalance(BigDecimal.valueOf(10));
        newWallet.setDescription("test");
        return newWallet;
    }
}
