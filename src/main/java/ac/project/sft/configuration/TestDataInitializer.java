package ac.project.sft.configuration;

import ac.project.sft.dto.ScheduledTransactionDto;
import ac.project.sft.dto.TransactionDto;
import ac.project.sft.model.ScheduledTransaction;
import ac.project.sft.model.User;
import ac.project.sft.model.UserWallet;
import ac.project.sft.model.Wallet;
import ac.project.sft.service.ManagerService;
import ac.project.sft.service.UserService;
import ac.project.sft.service.WalletService;
import net.bytebuddy.asm.Advice;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class TestDataInitializer implements CommandLineRunner {

    @Autowired
    Environment environment;

    @Autowired
    WalletService walletService;

    @Autowired
    ManagerService managerService;

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if(Arrays.asList(environment.getActiveProfiles()).contains("dev")){
            User user = userService.findByUsername("test");
            Wallet wallet = new Wallet();
            wallet.setName("test");
            wallet.setBalance(BigDecimal.valueOf(100));
            wallet.setDescription("test wallet");
            UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
            for(int i=0;i<100;i++){
                TransactionDto transactionDto = new TransactionDto();
                transactionDto.setAmount(BigDecimal.valueOf(i));
                transactionDto.setName("test"+i);
                transactionDto.setDate(ZonedDateTime.now());
                managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(),transactionDto);
                ScheduledTransaction scheduledTransactionDto = new ScheduledTransaction();
                scheduledTransactionDto.setAmount(BigDecimal.valueOf(i));
                scheduledTransactionDto.setName("sched"+i);
                scheduledTransactionDto.setDate(LocalDate.now());
                scheduledTransactionDto.setRecurrent(false);
                managerService.addScheduled(userWallet.getWallet(), scheduledTransactionDto,user.getUsername());
            }
        }
    }
}
