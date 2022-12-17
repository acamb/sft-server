package ac.project.sft.dto;

import ac.project.sft.model.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SearchTransactionDto {

    LocalDate startDate;
    LocalDate endDate;
    Category categoryDto;
    TransactionType type;
    String name;

    Boolean scheduled;

}
