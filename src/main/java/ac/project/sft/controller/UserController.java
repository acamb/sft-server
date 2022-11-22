package ac.project.sft.controller;

import ac.project.sft.dto.UserDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    DtoMapper mapper;
    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<UserDto> getAll(){
        return mapper.userListToDto(userService.findAll());
    }
}
