package ac.project.sft.controller.payloads.response;

import ac.project.sft.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPayloadResponse {

    UserDto user;
    String token;
}
