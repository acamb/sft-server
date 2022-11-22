package ac.project.sft.service;

import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.User;
import ac.project.sft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public User findByUsername(String username){
        return repository.findByUsername(username).orElseThrow(() -> new NotFoundException("user.not.exists"));
    }

    public List<User> findAll(){
        return (List<User>) repository.findAll();
    }
}
