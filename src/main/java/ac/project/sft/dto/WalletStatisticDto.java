package ac.project.sft.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
public class WalletStatisticDto {
    private BigDecimal expenses;
    private BigDecimal incomes;
    @Builder.Default
    private Map<CategoryDto,BigDecimal> expensesByCategory = new HashMap<>();
}
