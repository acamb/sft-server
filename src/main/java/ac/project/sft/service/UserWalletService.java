package ac.project.sft.service;

import ac.project.sft.exceptions.NotAuthorizedException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.Wallet;
import ac.project.sft.model.User;
import ac.project.sft.model.UserWallet;
import ac.project.sft.repository.UserWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class UserWalletService {

    public static final String NOT_FOUND_KEY = "association.not.exists";
    @Autowired
    UserService userService;

    @Autowired
    UserWalletRepository repository;

    @Transactional
    public UserWallet create(@Valid Wallet wallet, String username){
        User user = userService.findByUsername(username);
        UserWallet userWallet = new UserWallet();
        userWallet.setWallet(wallet);
        userWallet.setUser(user);
        userWallet.setOwner(true);
        userWallet.setRead(true);
        userWallet.setWrite(true);
        return repository.save(userWallet);
    }

    @Transactional
    public void deassociate(@Valid Wallet wallet, String username){
        User user = userService.findByUsername(username);
        UserWallet userWallet = repository.findByWalletAndUser(wallet,user).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
        repository.delete(userWallet);
    }

    List<UserWallet> getWallets(String username){
        User user = userService.findByUsername(username);
        return repository.findAllByUser(user).stream().filter(a -> a.getRead() || a.getOwner()).toList();
    }

    @Transactional
    public UserWallet modifyGrants(@Valid Wallet wallet, String username, Boolean read, Boolean write, Boolean owner){
        User user = userService.findByUsername(username);
        UserWallet userWallet = repository.findByWalletAndUser(wallet,user).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
        if(userWallet.getWrite() || userWallet.getOwner()) {
            if (read != null) {
                userWallet.setRead(read);
            }
            if (write != null) {
                userWallet.setWrite(write);
            }
            if (owner != null) {
                userWallet.setOwner(owner);
            }
            repository.save(userWallet);
            return userWallet;
        }
        else{
            throw new NotAuthorizedException("user.has.not.grants");
        }
    }

    @Transactional
    public void deleteFor(@Valid Wallet wallet){
        List<UserWallet> userWallets = repository.findAllByWallet(wallet);
        userWallets.forEach(a -> repository.delete(a));
    }

    public UserWallet getWallet(@Valid Wallet wallet, String username){
        User user = userService.findByUsername(username);
        UserWallet userWallet = repository.findByWalletAndUser(wallet,user).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
        if(userWallet.getRead() || userWallet.getOwner()){
            return userWallet;
        }
        else{
            throw new NotAuthorizedException("user.has.not.grants");
        }
    }


    public List<UserWallet> getAssociations(String username) {
        User user = userService.findByUsername(username);
        return repository.findByUserAndOwnerTrue(user).stream().flatMap(
                uw -> repository.findAllByWallet(uw.getWallet())
                        .stream()
                        .filter( w -> w.getUser().equals(user))
        ).toList();
    }
}
