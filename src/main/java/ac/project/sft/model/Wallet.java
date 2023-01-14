package ac.project.sft.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Where(clause = "valid = true")
@SQLDelete(sql="UPDATE wallet set valid = false where id = ? and version = ?")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @Min(value = 0)
    private BigDecimal balance;
    @NotBlank
    private String description;
    @NotBlank
    private String name;
    private boolean valid = true;
    @Enumerated(EnumType.STRING)
    private WalletType walletType = WalletType.FIAT;
    @ManyToOne
    private CryptoCurrency currency;
}
