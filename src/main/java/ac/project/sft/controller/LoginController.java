package ac.project.sft.controller;

import ac.project.sft.controller.payloads.request.LoginPayload;
import ac.project.sft.controller.payloads.response.LoginPayloadResponse;
import ac.project.sft.dto.UserDto;
import ac.project.sft.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {


    @Autowired
    LoginService loginService;


    @PostMapping
    public LoginPayloadResponse login(@RequestBody LoginPayload payload){
        return loginService.login(payload.getUsername(),payload.getPassword());
    }
}
