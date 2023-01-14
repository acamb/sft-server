package ac.project.sft;

import ac.project.sft.dto.WalletPrevisionDto;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;

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
        Transaction t = createTransaction(BigDecimal.valueOf(-1),null);
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(),mapper.transactionToDto(t));
        WalletStatisticDto statisticDto = statisticsService.getStatistics(userWallet.getWallet().getId(),
                LocalDate.now().minusDays(1),
                null
                );
        assertEquals(BigDecimal.valueOf(-31),statisticDto.getExpenses());
        assertEquals(3,statisticDto.getExpensesByCategory().keySet().size());
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

    @Test
    public void generatePrevisions(){
        User user = createTestUser(null,userRepository);
        Wallet wallet = createTestWallet(BigDecimal.valueOf(1000));
        UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
        //Transaction 1 on 1/11/2022
        Transaction t = createTransaction(BigDecimal.valueOf(-1));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        //Transaction 2 on 3/12/2022
        t = createTransaction(BigDecimal.valueOf(-3));
        t.setDate(LocalDate.now().minusMonths(1));
        managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(), mapper.transactionToDto(t));
        //avg per month is: -2

        //add 2 scheduled transaction in the future
        ScheduledTransaction s = createScheduledTransaction(BigDecimal.valueOf(-10));
        s.setType(RecurrentType.WEEKLY);
        s.setRecurrent(true);
        s.setRecurrentFrequency(1);
        s.setDayOfWeek(LocalDate.now().getDayOfWeek());
        managerService.addScheduled(userWallet.getWallet(),s,user.getUsername());
        s = createScheduledTransaction(BigDecimal.valueOf(-5));
        s.setType(RecurrentType.WEEKLY);
        s.setRecurrent(true);
        s.setRecurrentFrequency(1);
        s.setDayOfWeek(LocalDate.now().getDayOfWeek());
        managerService.addScheduled(userWallet.getWallet(),s,user.getUsername());

        WalletPrevisionDto previsionDto = statisticsService.getPrevisions(userWallet.getWallet().getId(),LocalDate.now(),LocalDate.now().plusWeeks(3));
        //we have both scheduled executed twice, so balance will be 1000-4-15-15 = 966.
        // the endBalanceEstimated will be 966 - ~2 (avg per month) = 964
        String lastTransaction = previsionDto.getPrevision().keySet().stream().sorted(Comparator.reverseOrder()).findFirst().get();
        BigDecimal lastBalance = previsionDto.getPrevision().get(lastTransaction);
        assertEquals(BigDecimal.valueOf(966),lastBalance);
        assertEquals(BigDecimal.valueOf(964),previsionDto.getEndBalanceEstimated().setScale(0, RoundingMode.DOWN));
    }
}
