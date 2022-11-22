package ac.project.sft.controller.payloads.request;

import ac.project.sft.dto.TransactionDto;
import ac.project.sft.dto.WalletDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionRequestPayload {
    private WalletDto walletDto;
    private TransactionDto transactionDto;
}
