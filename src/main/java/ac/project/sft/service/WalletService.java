package ac.project.sft.service;

import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Wallet;
import ac.project.sft.model.UserWallet;
import ac.project.sft.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Service
@Validated
public class WalletService {

    public static final String NOT_FOUND_KEY = "wallet.not.found";
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    UserWalletService userWalletService;

    public UserWallet getWallet(Long id, String username){
        Wallet wallet =  walletRepository.findById(id).orElseThrow(()->new NotFoundException(NOT_FOUND_KEY));
        return userWalletService.getWallet(wallet,username);
    }

    public Wallet getWallet(Long id){
        return walletRepository.findById(id).orElseThrow(()->new NotFoundException(NOT_FOUND_KEY));
    }

    public List<UserWallet> getWallets(String username){
        return userWalletService.getWallets(username);
    }

    @Transactional
    public UserWallet createWallet(@Valid Wallet wallet, String username){
        if(wallet.getId() != null){
            throw new BadRequestException("wallet.exists");
        }
        wallet = walletRepository.save(wallet);
        return userWalletService.create(wallet,username);
    }

    @Transactional
    public UserWallet associateWallet(@Valid Wallet wallet,String username,Boolean read,Boolean write,Boolean owner){
        userWalletService.create(wallet,username);
        return userWalletService.modifyGrants(wallet,username,read,write,owner);
    }

    @Transactional
    public Wallet updateBalance(@Valid Wallet wallet, BigDecimal amount){
        wallet.setBalance(wallet.getBalance().subtract(amount));
        if(wallet.getBalance().compareTo(BigDecimal.ZERO) < 0){
            throw new BadRequestException("balance.lesser.zero");
        }
        wallet = walletRepository.save(wallet);
        return wallet;
    }

    @Transactional
    public void deleteWallet(Wallet wallet){
        walletRepository.delete(wallet);
        userWalletService.deleteFor(wallet);
    }

    @Transactional
    public void deleteWallet(Long id){
        Wallet wallet = walletRepository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
        deleteWallet(wallet);
    }

    public UserWallet modifyAssociation(@Valid Wallet wallet, String username, Boolean read, Boolean write, Boolean owner) {
        return userWalletService.modifyGrants(wallet,username,read,write,owner);
    }

    public void deleteAssociation(@Valid Wallet wallet, String username) {
        userWalletService.deassociate(wallet,username);
    }
}
