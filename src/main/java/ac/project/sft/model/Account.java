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
    Long id;
    @Version
    Long version;
    BigDecimal balance;
    String description;
    boolean valid = true;
}
