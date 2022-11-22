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
@SQLDelete(sql="UPDATE scheduled_transaction set valid = false where id = ? and version = ?")
public class ScheduledTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @NotNull
    private Date date;
    private BigDecimal amount;
    private Boolean recurrent;
    @Enumerated(EnumType.STRING)
    private  RecurrentType type;
    private Date endDate;
    private  Integer dayOfMonth; //end of month?
    private Integer dayOfWeek;
    private boolean valid = true;
    @ManyToOne
    private Wallet wallet;
    @ManyToOne
    private Category category;
    private Date nextFire;
}
