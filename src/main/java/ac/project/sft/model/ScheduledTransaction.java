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
    Long id;
    @Version
    Long version;
    Date date;
    BigDecimal amount;
    Boolean recurrent;
    @Enumerated(EnumType.STRING)
    RecurrentType type;
    Date endDate;
    Integer dayOfMonth; //end of month?
    Integer dayOfWeek;
    boolean valid = true;
}
