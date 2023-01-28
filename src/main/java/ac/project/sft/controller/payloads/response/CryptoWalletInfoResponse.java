package ac.project.sft.controller.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CryptoWalletInfoResponse {

    List<CryptoWalletInfo> wallets;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CryptoWalletInfo{
        Long walletId;
        BigDecimal currentValue;
    }
}
