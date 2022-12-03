package ac.project.sft.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Where(clause = "valid = true")
@SQLDelete(sql="UPDATE category set valid = false where id = ? and version = ?")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @NotBlank
    private String name;
    private String description;
    private boolean valid = true;
    private boolean canBePositive = true;
    private boolean canBeNegative = true;
}
