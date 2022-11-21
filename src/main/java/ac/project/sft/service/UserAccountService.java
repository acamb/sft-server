package ac.project.sft.service;

import ac.project.sft.exceptions.NotAuthorizedException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.Account;
import ac.project.sft.model.User;
import ac.project.sft.model.UserAccount;
import ac.project.sft.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserAccountService {

    @Autowired
    UserService userService;

    @Autowired
    UserAccountRepository repository;

    @Transactional
    public UserAccount create(Account account, String username){
        User user = userService.findByUsername(username);
        UserAccount userAccount = new UserAccount();
        userAccount.setAccount(account);
        userAccount.setUser(user);
        userAccount.setOwner(true);
        userAccount.setRead(true);
        userAccount.setWrite(true);
        return repository.save(userAccount);
    }

    @Transactional
    public void deassociate(Account account,String username){
        User user = userService.findByUsername(username);
        UserAccount userAccount = repository.findByAccountAndUser(account,user).orElseThrow(()-> new NotFoundException("association.not.exists"));
        repository.delete(userAccount);
    }

    List<UserAccount> getAccounts(String username){
        User user = userService.findByUsername(username);
        return repository.findAllByUser(user).stream().filter(a -> a.getRead() || a.getOwner()).toList();
    }

    @Transactional
    public UserAccount modifyGrants(Account account,String username,Boolean read,Boolean write,Boolean owner){
        User user = userService.findByUsername(username);
        UserAccount userAccount = repository.findByAccountAndUser(account,user).orElseThrow(()-> new NotFoundException("association.not.exists"));
        if(userAccount.getWrite() || userAccount.getOwner()) {
            if (read != null) {
                userAccount.setRead(read);
            }
            if (write != null) {
                userAccount.setWrite(write);
            }
            if (owner != null) {
                userAccount.setOwner(owner);
            }
            repository.save(userAccount);
            return userAccount;
        }
        else{
            throw new NotAuthorizedException("user.has.not.grants");
        }
    }

    @Transactional
    public void deleteFor(Account account){
        List<UserAccount> userAccounts = repository.findAllByAccount(account);
        userAccounts.forEach(a -> repository.delete(a));
    }

    public UserAccount getAccount(Account account,String username){
        User user = userService.findByUsername(username);
        UserAccount userAccount = repository.findByAccountAndUser(account,user).orElseThrow(()-> new NotFoundException("association.not.exists"));
        if(userAccount.getRead() || userAccount.getOwner()){
            return userAccount;
        }
        else{
            throw new NotAuthorizedException("user.has.not.grants");
        }
    }


}
