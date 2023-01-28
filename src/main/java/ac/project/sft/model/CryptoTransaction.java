package ac.project.sft.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class CryptoTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    private BigDecimal price;
    private BigDecimal fiatValue;
    private BigDecimal fee;
    private Boolean taxable;
    private CryptoTransactionType transactionType;

}
