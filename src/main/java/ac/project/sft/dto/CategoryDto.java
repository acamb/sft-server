package ac.project.sft.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private boolean canBePositive;
    private boolean canBeNegative;

}
