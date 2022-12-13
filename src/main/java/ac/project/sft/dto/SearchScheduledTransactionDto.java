package ac.project.sft.dto;

import ac.project.sft.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchScheduledTransactionDto {

    LocalDate startDate;
    LocalDate endDate;
    Category categoryDto;
    String name;
    String note;

}
