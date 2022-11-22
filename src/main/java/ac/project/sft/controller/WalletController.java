package ac.project.sft.controller;

import ac.project.sft.dto.WalletDto;
import ac.project.sft.dto.UserWalletDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    DtoMapper mapper;
    @Autowired
    WalletService walletService;

    @GetMapping("/:id")
    @ResponseStatus(HttpStatus.OK)
    UserWalletDto getWallet(@PathVariable("id") Long id, Authentication authentication){
        return mapper.userWalletToDto(walletService.getWallet(id,authentication.getName()));
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    List<UserWalletDto> getWallets(Authentication authentication){
        return mapper.userWalletsToDtos(walletService.getWallets(authentication.getName()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserWalletDto createWallet(@RequestBody WalletDto walletDto, Authentication authentication){
        return mapper.userWalletToDto(walletService.createWallet(walletDto,authentication.getName()));
    }

    @DeleteMapping("/:id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteWallet(@PathVariable("id") Long id){
        walletService.deleteWallet(id);
    }

}
