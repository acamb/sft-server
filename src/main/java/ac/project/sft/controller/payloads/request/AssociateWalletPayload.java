package ac.project.sft.controller.payloads.request;

import ac.project.sft.dto.WalletDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AssociateWalletPayload {

    WalletDto walletDto;
    String username;
    Boolean read;
    Boolean write;
    Boolean owner;

}
