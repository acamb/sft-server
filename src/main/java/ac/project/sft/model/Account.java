package ac.project.sft.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Where(clause = "valid = 1")
@SQLDelete(sql="UPDATE account_repository set valid = 0 where id = ?")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    private BigDecimal balance;
    private String description;
    private boolean valid = true;
}
