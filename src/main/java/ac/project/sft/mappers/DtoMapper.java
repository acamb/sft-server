package ac.project.sft.mappers;

import ac.project.sft.dto.*;
import ac.project.sft.model.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    @BeanMapping
    WalletDto walletToDto(Wallet wallet);
    @BeanMapping
    CategoryDto categoryToDto(Category category);

    @BeanMapping
    List<CategoryDto> categoryListToDto(List<Category> category);

    @BeanMapping
    @Mapping(source="category",target="categoryDto")
    ScheduledTransactionDto scheduledTransactionToDto(ScheduledTransaction scheduledTransaction);
    @BeanMapping
    @Mapping(source="category",target="categoryDto")
    @Mapping(source="user",target="userDto")
    TransactionDto transactionToDto(Transaction transaction);
    @BeanMapping
    @Mapping(source="wallet",target="walletDto")
    UserWalletDto userWalletToDto(UserWallet userWallet);

    @BeanMapping
    List<UserWalletDto> userWalletsToDtos(List<UserWallet> userWalletList);

    @BeanMapping
    @Mapping(target="password",ignore = true)
    UserDto userToDto(User user);

    @BeanMapping
    List<UserDto> userListToDto(List<User> user);

    @BeanMapping
    Wallet dtoToWallet(WalletDto wallet);
    @BeanMapping
    Category dtoToCategory(CategoryDto category);
    @BeanMapping
    @Mapping(source="categoryDto",target="category")
    ScheduledTransaction dtoToScheduledTransaction(ScheduledTransactionDto scheduledTransaction);
    @BeanMapping
    @Mapping(source="categoryDto",target="category")
    @Mapping(source="userDto",target="user")
    Transaction dtoToTransaction(TransactionDto transaction);
    @BeanMapping
    @Mapping(source = "walletDto",target="wallet")
    UserWallet dtoToUserWallet(UserWalletDto userWalletDto);
    @BeanMapping
    User dtoToUser(UserDto user);

    @BeanMapping
    List<TransactionDto> transactionListToDto(List<Transaction> all);
    @BeanMapping
    List<ScheduledTransactionDto> scheduledTransactionListToDto(List<ScheduledTransaction> scheduledTransactionList);
}
