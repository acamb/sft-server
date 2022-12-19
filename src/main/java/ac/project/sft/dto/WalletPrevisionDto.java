package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WalletPrevisionDto {

    Map<String, BigDecimal> prevision = new HashMap<>();
    BigDecimal endBalanceEstimated; //weightened on avg non-scheduled expenses
}
