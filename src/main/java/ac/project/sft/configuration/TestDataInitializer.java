package ac.project.sft.configuration;

import ac.project.sft.dto.ScheduledTransactionDto;
import ac.project.sft.dto.TransactionDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.*;
import ac.project.sft.service.*;
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

    @Autowired
    CategoryService categoryService;

    @Autowired
    DtoMapper mapper;

    @Autowired
    CryptoCurrencyService cryptoCurrencyService;

    @Override
    public void run(String... args) throws Exception {
        if(Arrays.asList(environment.getActiveProfiles()).contains("dev")){
            User user = userService.findByUsername("test");
            Wallet wallet = new Wallet();
            wallet.setName("test");
            wallet.setBalance(BigDecimal.valueOf(100));
            wallet.setDescription("test wallet");
            UserWallet userWallet = walletService.createWallet(wallet,user.getUsername());
            Category category1 = new Category();
            category1.setName("test");
            category1.setCanBeNegative(true);
            category1.setCanBePositive(true);
            category1=categoryService.create(category1);
            Category category2 = new Category();
            category2.setName("test");
            category2.setCanBeNegative(true);
            category2.setCanBePositive(true);
            category2=categoryService.create(category2);
            for(int i=0;i<100;i++){
                TransactionDto transactionDto = new TransactionDto();
                transactionDto.setAmount(BigDecimal.valueOf(i));
                transactionDto.setName("test"+i);
                transactionDto.setDate(ZonedDateTime.now());
                if(i%2 == 0){
                    transactionDto.setCategoryDto(mapper.categoryToDto(category1));
                }
                else{
                    transactionDto.setCategoryDto(mapper.categoryToDto(category2));
                }
                managerService.addTransaction(userWallet.getWallet().getId(),user.getUsername(),transactionDto);
                ScheduledTransaction scheduledTransactionDto = new ScheduledTransaction();
                scheduledTransactionDto.setAmount(BigDecimal.valueOf(i));
                scheduledTransactionDto.setName("sched"+i);
                scheduledTransactionDto.setDate(LocalDate.now());
                scheduledTransactionDto.setRecurrent(false);
                managerService.addScheduled(userWallet.getWallet(), scheduledTransactionDto,user.getUsername());
            }
            //
            CryptoCurrency crypto = new CryptoCurrency();
            crypto.setName("BTC");
            crypto.setTicker("BTC");
            crypto = cryptoCurrencyService.create(crypto);
            Wallet walletC = new Wallet();
            walletC.setName("test c");
            walletC.setBalance(BigDecimal.valueOf(1));
            walletC.setDescription("test wallet crypto");
            walletC.setWalletType(WalletType.CRYPTO);
            walletC.setCurrency(crypto);
            UserWallet userWallet2 = walletService.createWallet(walletC,user.getUsername());

        }
    }
}
