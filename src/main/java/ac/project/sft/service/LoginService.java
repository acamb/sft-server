package ac.project.sft.service;

import ac.project.sft.configuration.JwtTokenHelper;
import ac.project.sft.controller.payloads.response.LoginPayloadResponse;
import ac.project.sft.mappers.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    DtoMapper mapper;
    @Autowired
    JwtTokenHelper jwtTokenHelper;

    public LoginPayloadResponse login(String username, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        LoginPayloadResponse result = new LoginPayloadResponse();
        result.setToken(jwtTokenHelper.generateTokenFromUsername(username));
        result.setUser(mapper.userToDto(userService.findByUsername(username)));
        return result;
    }
}
