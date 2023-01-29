package ac.project.sft.model;

import ac.project.sft.configuration.DontLog;
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
@SQLDelete(sql="UPDATE user set valid = false where id = ? and version = ?")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @NotBlank
    private String username;
    @NotBlank
    @DontLog
    private String password;
    private boolean valid = true;


}
