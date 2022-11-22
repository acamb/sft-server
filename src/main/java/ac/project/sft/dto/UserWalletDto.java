package ac.project.sft.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserWalletDto {

    private Long id;
    private Boolean read;
    private Boolean write;
    private Boolean owner;
    private WalletDto walletDto;
}
