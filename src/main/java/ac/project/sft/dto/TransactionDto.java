package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class TransactionDto {
    private Long id;
    private Date date;
    private BigDecimal amount;
    private BigDecimal previousAmount;
    private CategoryDto categoryDto;
    private String note;

    private UserDto userDto;

}
