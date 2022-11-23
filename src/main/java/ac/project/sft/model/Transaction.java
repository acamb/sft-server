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
@SQLDelete(sql="UPDATE transaction set valid = false where id = ? and version = ?")
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
    @ManyToOne
    private User user;
    @ManyToOne(optional = false)
    private Wallet wallet;
    private boolean valid = true;

}
