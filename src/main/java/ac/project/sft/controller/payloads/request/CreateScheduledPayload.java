package ac.project.sft.controller.payloads.request;

import ac.project.sft.dto.ScheduledTransactionDto;
import ac.project.sft.dto.WalletDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateScheduledPayload {

    private WalletDto wallet;
    private ScheduledTransactionDto scheduledTransaction;

}
