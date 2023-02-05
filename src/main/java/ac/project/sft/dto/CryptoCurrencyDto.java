package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoCurrencyDto {
    Long id;
    Long version;
    String name;
    String ticker;
}
