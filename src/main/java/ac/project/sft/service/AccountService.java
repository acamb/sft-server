package ac.project.sft.service;

import ac.project.sft.dto.AccountDto;
import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Account;
import ac.project.sft.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    DtoMapper mapper;

    public Account getAccount(Long id){
        return accountRepository.findById(id).orElseThrow(()->new NotFoundException("account.not.found"));
    }
    @Transactional
    public Account createAccount(AccountDto dto){
        if(dto.getId() != null){
            throw new BadRequestException("account.exists");
        }
        Account account = mapper.dtoToAccount(dto);
        account = accountRepository.save(account);
        return account;
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
    }
}
