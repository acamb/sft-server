package ac.project.sft.dto;

import ac.project.sft.model.CryptoTransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CryptoTransactionDto{
        private Long id;
        private Long version;
        private BigDecimal price; //TODO better naming
        private BigDecimal fiatValue;
        private BigDecimal fee;
        private Boolean taxable;
        private CryptoTransactionType transactionType;
}
