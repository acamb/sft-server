package ac.project.sft.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Where(clause = "valid = true")
@Getter
@Setter
@Table(name="users")
@SQLDelete(sql="UPDATE user set valid = 0 where id = ?")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private boolean valid = true;


}
