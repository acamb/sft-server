package ac.project.sft.controller;

import ac.project.sft.dto.AccountDto;
import ac.project.sft.dto.UserAccountDto;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Account;
import ac.project.sft.service.AccountService;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    DtoMapper mapper;
    @Autowired
    AccountService accountService;

    @GetMapping("/:id")
    @ResponseStatus(HttpStatus.OK)
    UserAccountDto getAccount(@PathVariable("id") Long id, Authentication authentication){
        return mapper.userAccountToDto(accountService.getAccount(id,authentication.getName()));
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    List<UserAccountDto> getAccounts(Authentication authentication){
        return mapper.userAccountsToDtos(accountService.getAccounts(authentication.getName()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserAccountDto createAccount(@RequestBody AccountDto account, Authentication authentication){
        return mapper.userAccountToDto(accountService.createAccount(account,authentication.getName()));
    }

    @DeleteMapping("/:id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAccount(@PathVariable("id") Long id){
        accountService.deleteAccount(id);
    }

}
