package ac.project.sft.mappers;

import ac.project.sft.dto.*;
import ac.project.sft.model.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    AccountDto accountToDto(Account account);
    CategoryDto categoryToDto(Category category);
    ScheduledTransactionDto scheduledTransactionToDto(ScheduledTransaction scheduledTransaction);
    TransactionDto transactionToDto(Transaction transaction);
    UserAccountDto userAccountToDto(UserAccount userAccount);
    UserDto userToDto(User user);

    Account dtoToAccount(AccountDto account);
    Category dtoToCategory(CategoryDto category);
    ScheduledTransaction dtoToScheduledTransaction(ScheduledTransactionDto scheduledTransaction);
    Transaction dtoToTransaction(TransactionDto transaction);
    UserAccount dtoToUserAccount(UserAccountDto userAccount);
    User dotToUser(UserDto user);
}
