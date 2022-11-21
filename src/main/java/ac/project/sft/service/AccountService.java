package ac.project.sft.service;

import ac.project.sft.dto.AccountDto;
import ac.project.sft.dto.UserAccountDto;
import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Account;
import ac.project.sft.model.User;
import ac.project.sft.model.UserAccount;
import ac.project.sft.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    DtoMapper mapper;

    public UserAccount getAccount(Long id,String username){
        Account account =  accountRepository.findById(id).orElseThrow(()->new NotFoundException("account.not.found"));
        return userAccountService.getAccount(account,username);
    }

    public List<UserAccount> getAccounts(String username){
        return userAccountService.getAccounts(username);
    }

    @Transactional
    public UserAccount createAccount(AccountDto dto,String username){
        if(dto.getId() != null){
            throw new BadRequestException("account.exists");
        }
        Account account = mapper.dtoToAccount(dto);
        account = accountRepository.save(account);
        return userAccountService.create(account,username);
    }
    @Transactional
    public Account updateBalance(Account account, BigDecimal balance){
        if(balance.compareTo(BigDecimal.ZERO) < 0){
            throw new BadRequestException("account.balance.negative");
        }
        account.setBalance(balance);
        account = accountRepository.save(account);
        return account;
    }

    @Transactional
    public void deleteAccount(Account account){
        accountRepository.delete(account);
    }

    @Transactional
    public void deleteAccount(Long id){
        Account account = accountRepository.findById(id).orElseThrow(()-> new NotFoundException("account.not.found"));
        accountRepository.delete(account);
        userAccountService.deleteFor(account);
    }
}
