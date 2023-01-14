package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

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
    private boolean scheduled;
    CryptoTransactionDto cryptoTransactionDto;

}
