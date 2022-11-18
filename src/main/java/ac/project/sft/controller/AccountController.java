package ac.project.sft.controller;

import ac.project.sft.dto.AccountDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Account;
import ac.project.sft.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    DtoMapper mapper;
    @Autowired
    AccountService accountService;

    @GetMapping("/:id")
    @ResponseStatus(HttpStatus.OK)
    AccountDto getAccount(@PathVariable("id") Long id){
        return mapper.accountToDto(accountService.getAccount(id));
    }

}
