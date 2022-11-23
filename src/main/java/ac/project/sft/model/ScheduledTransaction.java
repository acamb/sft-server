package ac.project.sft.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private LocalDate date;
    private BigDecimal amount;
    @NotNull
    private Boolean recurrent = false;
    @Enumerated(EnumType.STRING)
    private  RecurrentType type;
    private LocalDate endDate;
    private  Integer dayOfMonth;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private boolean valid = true;
    @ManyToOne(optional = false)
    private Wallet wallet;
    @ManyToOne
    private Category category;
    private LocalDate nextFire;
}
