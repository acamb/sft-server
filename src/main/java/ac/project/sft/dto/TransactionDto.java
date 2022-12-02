package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
public class TransactionDto {
    private Long id;
    private ZonedDateTime date;
    private  String name;
    private BigDecimal amount;
    private BigDecimal previousAmount;
    private CategoryDto categoryDto;
    private String note;

    private UserDto userDto;

}
