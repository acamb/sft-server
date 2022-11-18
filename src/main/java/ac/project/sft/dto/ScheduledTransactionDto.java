package ac.project.sft.dto;

import ac.project.sft.model.RecurrentType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ScheduledTransactionDto {
    Long id;
    Date date;
    BigDecimal amount;
    Boolean recurrent;
    RecurrentType type;
    Date endDate;
    Integer dayOfMonth; //end of month?
    Integer dayOfWeek;
}
