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
    private Long id;
    @Version
    private Long version;
    private Boolean read;
    private Boolean write;
    private Boolean owner;
    @ManyToOne
    private Account account;
    @ManyToOne
    private User user;
    private boolean valid = true;
}
