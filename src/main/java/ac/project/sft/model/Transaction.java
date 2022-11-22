package ac.project.sft.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Where(clause = "valid = true")
@SQLDelete(sql="UPDATE transaction set valid = 0 where id = ?")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Version
    private Long version;
    @NotNull
    private Date date;
    @NotNull
    private  BigDecimal amount;
    @NotNull
    private BigDecimal previousAmount;
    @ManyToOne
    private  Category category;
    private  String note;
    private User user;
    @ManyToOne
    @NotNull
    private Wallet wallet;

}
