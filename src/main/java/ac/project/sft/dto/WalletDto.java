package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class WalletDto {

    private Long id;
    private Long version;
    private BigDecimal balance;
    private String description;

}
