package ac.project.sft.dto;

import ac.project.sft.model.RecurrentType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
public class ScheduledTransactionDto {
    private Long id;
    private String name;
    private ZonedDateTime date;
    private BigDecimal amount;
    private Boolean recurrent;
    private RecurrentType type;
    private Integer recurrentFrequency;
    private ZonedDateTime endDate;
    private Integer dayOfMonth;
    private Integer dayOfWeek;
    private CategoryDto categoryDto;
    private LocalDate nextFire;
}
