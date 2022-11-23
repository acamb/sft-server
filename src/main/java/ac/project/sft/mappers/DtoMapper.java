package ac.project.sft.mappers;

import ac.project.sft.dto.*;
import ac.project.sft.model.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    @BeanMapping(ignoreByDefault = false)
    WalletDto walletToDto(Wallet wallet);
    @BeanMapping(ignoreByDefault = false)
    CategoryDto categoryToDto(Category category);

    @BeanMapping(ignoreByDefault = false)
    List<CategoryDto> categoryListToDto(List<Category> category);

    @BeanMapping(ignoreByDefault = false)
    @Mapping(source="category",target="categoryDto")
    ScheduledTransactionDto scheduledTransactionToDto(ScheduledTransaction scheduledTransaction);
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source="category",target="categoryDto")
    @Mapping(source="user",target="userDto")
    TransactionDto transactionToDto(Transaction transaction);
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source="wallet",target="walletDto")
    UserWalletDto userWalletToDto(UserWallet userWallet);

    @BeanMapping(ignoreByDefault = false)
    List<UserWalletDto> userWalletsToDtos(List<UserWallet> userWalletList);

    @BeanMapping(ignoreByDefault = false)
    @Mapping(target="password",ignore = true)
    UserDto userToDto(User user);

    @BeanMapping(ignoreByDefault = false)
    List<UserDto> userListToDto(List<User> user);

    @BeanMapping(ignoreByDefault = false)
    Wallet dtoToWallet(WalletDto wallet);
    @BeanMapping(ignoreByDefault = false)
    Category dtoToCategory(CategoryDto category);
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source="categoryDto",target="category")
    ScheduledTransaction dtoToScheduledTransaction(ScheduledTransactionDto scheduledTransaction);
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source="categoryDto",target="category")
    @Mapping(source="userDto",target="user")
    Transaction dtoToTransaction(TransactionDto transaction);
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source = "walletDto",target="wallet")
    UserWallet dtoToUserWallet(UserWalletDto userWalletDto);
    @BeanMapping(ignoreByDefault = false)
    User dtoToUser(UserDto user);

    @BeanMapping(ignoreByDefault = false)
    List<TransactionDto> transactionListToDto(List<Transaction> all);
    @BeanMapping(ignoreByDefault = false)
    List<ScheduledTransactionDto> scheduledTransactionListToDto(List<ScheduledTransaction> scheduledTransactionList);

    default Integer map(DayOfWeek day){
        return day.getValue();
    }

    default DayOfWeek map(Integer day){
        return Arrays.stream(DayOfWeek.values()).filter(d -> d.getValue() == day).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
