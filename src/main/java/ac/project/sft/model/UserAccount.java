package ac.project.sft.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Where(clause = "valid = 1")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Version
    Long version;
    Boolean read;
    Boolean write;
    Boolean owner;
    @ManyToOne
    Account account;
    @ManyToOne
    User user;
    boolean valid = true;
}
