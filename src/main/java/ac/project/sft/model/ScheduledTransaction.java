package ac.project.sft.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Where(clause = "valid = 1")
public class ScheduledTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    private Date date;
    private BigDecimal amount;
    private Boolean recurrent;
    @Enumerated(EnumType.STRING)
    private  RecurrentType type;
    private Date endDate;
    private  Integer dayOfMonth; //end of month?
    private Integer dayOfWeek;
    boolean valid = true;
}
