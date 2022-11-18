package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Where(clause = "valid = 1")
@Getter
@Setter
public class UserDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Version
    Long version;
    String username;
    String password;
    boolean valid = true;
}
