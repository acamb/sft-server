package ac.project.sft.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Where(clause = "valid = true")
@SQLDelete(sql="UPDATE user_wallet set valid = 0 where id = ?")
public class UserWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    private Boolean read;
    private Boolean write;
    private Boolean owner;
    @ManyToOne
    @NotNull
    private Wallet wallet;
    @ManyToOne
    @NotNull
    private User user;
    private boolean valid = true;
}
