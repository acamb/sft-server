package ac.project.sft.controller;

import ac.project.sft.controller.payloads.request.AssociateWalletPayload;
import ac.project.sft.controller.payloads.request.DeleteWalletAssociationPayload;
import ac.project.sft.dto.WalletDto;
import ac.project.sft.dto.UserWalletDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.WalletType;
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserWalletDto getWallet(@PathVariable("id") Long id, Authentication authentication){
        return mapper.userWalletToDto(walletService.getWallet(id,authentication.getName()));
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<UserWalletDto> getWallets(Authentication authentication, @RequestParam(value = "type",required = false)WalletType type){
        return mapper.userWalletsToDtos(walletService.getWallets(authentication.getName(),type));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserWalletDto createWallet(@RequestBody WalletDto walletDto, Authentication authentication){
        return mapper.userWalletToDto(walletService.createWallet(mapper.dtoToWallet(walletDto),authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWallet(@PathVariable("id") Long id){
        walletService.deleteWallet(id);
    }


    @GetMapping("/association")
    public List<UserWalletDto> getAssociations(Authentication authentication){
        return mapper.userWalletsToDtos(
                walletService.getAssociations(authentication.getName())
        );
    }
    @PostMapping("/association")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserWalletDto associate(@RequestBody AssociateWalletPayload payload) {
        return mapper.userWalletToDto(
                walletService.associateWallet(mapper.dtoToWallet(payload.getWalletDto()),
                payload.getUsername(),
                payload.getRead(),
                payload.getWrite(),
                payload.getOwner())
        );
    }

    @PutMapping("/association")
    public UserWalletDto modifyAssociation(@RequestBody AssociateWalletPayload payload) {
        return mapper.userWalletToDto(
                walletService.modifyAssociation(mapper.dtoToWallet(payload.getWalletDto()),
                payload.getUsername(),
                payload.getRead(),
                payload.getWrite(),
                payload.getOwner())
        );
    }

    @DeleteMapping("/association")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssociation(@RequestBody DeleteWalletAssociationPayload payload) {
        walletService.deleteAssociation(mapper.dtoToWallet(payload.getWalletDto()),
                payload.getUsername());
    }
}
