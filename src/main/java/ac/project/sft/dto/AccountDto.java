package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class AccountDto {

    Long id;
    Long version;
    BigDecimal balance;
    String description;

}
