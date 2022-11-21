package ac.project.sft.mappers;

import ac.project.sft.dto.*;
import ac.project.sft.model.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    @BeanMapping(ignoreByDefault = false)
    AccountDto accountToDto(Account account);
    @BeanMapping(ignoreByDefault = false)
    CategoryDto categoryToDto(Category category);
    @BeanMapping(ignoreByDefault = false)
    ScheduledTransactionDto scheduledTransactionToDto(ScheduledTransaction scheduledTransaction);
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source="category",target="categoryDto")
    TransactionDto transactionToDto(Transaction transaction);
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source="account",target="accountDto")
    UserAccountDto userAccountToDto(UserAccount userAccount);

    @BeanMapping(ignoreByDefault = false)
    List<UserAccountDto> userAccountsToDtos(List<UserAccount> userAccountList);

    @BeanMapping(ignoreByDefault = false)
    @Mapping(target="password",ignore = true)
    UserDto userToDto(User user);


    @BeanMapping(ignoreByDefault = false)
    Account dtoToAccount(AccountDto account);
    @BeanMapping(ignoreByDefault = false)
    Category dtoToCategory(CategoryDto category);
    @BeanMapping(ignoreByDefault = false)
    ScheduledTransaction dtoToScheduledTransaction(ScheduledTransactionDto scheduledTransaction);
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source="categoryDto",target="category")
    Transaction dtoToTransaction(TransactionDto transaction);
    //@Mapping(target = "account",source= "accountDto")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(source = "accountDto",target="account")
    UserAccount dtoToUserAccount(UserAccountDto userAccount);
    @BeanMapping(ignoreByDefault = false)
    User dtoToUser(UserDto user);
}
