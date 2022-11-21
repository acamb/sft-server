package ac.project.sft.controller.payloads.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginPayload {

    String username;
    String password;
}
