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
@SQLDelete(sql="UPDATE wallet set valid = 0 where id = ?")
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
    private boolean valid = true;
}