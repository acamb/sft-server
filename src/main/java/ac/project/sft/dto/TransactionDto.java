package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class TransactionDto {
    Long id;
    Date date;
    BigDecimal amount;
    BigDecimal previousAmount;
    CategoryDto categoryDto;
    String note;

}
