package ac.project.sft.controller.payloads.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPayload {

    String username;
    String password;
}
