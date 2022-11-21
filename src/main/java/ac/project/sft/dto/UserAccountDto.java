package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserAccountDto {

    private Long id;
    private Boolean read;
    private Boolean write;
    private Boolean owner;
    private AccountDto accountDto;
}
