package ac.project.sft.dto;

import ac.project.sft.model.RecurrentType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ScheduledTransactionDto {
    private Long id;
    private Date date;
    private BigDecimal amount;
    private Boolean recurrent;
    private RecurrentType type;
    private Date endDate;
    private Integer dayOfMonth; //end of month?
    private Integer dayOfWeek;
    private CategoryDto categoryDto;
}
