package ac.project.sft;

import ac.project.sft.dto.WalletStatisticDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.*;
import ac.project.sft.repository.UserRepository;
import ac.project.sft.service.CategoryService;
import ac.project.sft.service.ManagerService;
import ac.project.sft.service.StatisticsService;
import ac.project.sft.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ac.project.sft.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class StatisticsTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ManagerService managerService;
    @Autowired
    WalletService walletService;
    @Autowired
    DtoMapper mapper;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StatisticsService statisticsService;

    @Test
    public void generateStatistics(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(1000));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        Category c = new Category();
        c.setName("test");
        c.setCanBeNegative(true);
        c = categoryService.create(c);
        Category c2 = new Category();
        c2.setName("test");
        c2.setCanBeNegative(true);
        c2 = categoryService.create(c2);
        for(int i=0;i<10;i++){
            Transaction t = createTransaction(BigDecimal.valueOf(-1),c);
            managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
            t = createTransaction(BigDecimal.valueOf(-2),c2);
            managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        }
        //TODO add 2 transactions without category
        WalletStatisticDto statisticDto = statisticsService.getStatistics(userWallet.getWallet().getId(),
                LocalDate.now().minusDays(1),
                null
                );
        assertEquals(BigDecimal.valueOf(-30),statisticDto.getExpenses());
        assertEquals(2,statisticDto.getExpensesByCategory().keySet().size());
        assertEquals(BigDecimal.valueOf(-10),statisticDto.getExpensesByCategory().get(mapper.categoryToDto(c)));
        assertEquals(BigDecimal.valueOf(-20),statisticDto.getExpensesByCategory().get(mapper.categoryToDto(c2)));
    }

    @Test
    public void generateStatisticsWithScheduled(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(1000));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        Category c = new Category();
        c.setName("test");
        c.setCanBeNegative(true);
        c = categoryService.create(c);
        for(int i=0;i<10;i++){
            Transaction t = createTransaction(BigDecimal.valueOf(-1),c);
            if(i%2 == 0){
                t.setScheduled(true);
            }
            managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        }
        WalletStatisticDto statisticDto = statisticsService.getStatistics(userWallet.getWallet().getId(),
                LocalDate.now().minusDays(1),
                null
        );
        assertEquals(BigDecimal.valueOf(-10),statisticDto.getExpenses());

    }
}
