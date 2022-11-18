package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserAccountDto {

    Long id;
    Boolean read;
    Boolean write;
    Boolean owner;
    AccountDto accountDto;
    UserDto user;
}
