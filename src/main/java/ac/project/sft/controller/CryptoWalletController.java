package ac.project.sft.controller;

import ac.project.sft.controller.payloads.response.CryptoWalletInfoResponse;
import ac.project.sft.service.ICryptoPricingService;
import ac.project.sft.service.ManagerService;
import ac.project.sft.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/cryptowallet")
public class CryptoWalletController {

    @Autowired
    ICryptoPricingService pricingService;

    @Autowired
    WalletService walletService;

    @GetMapping("/info")
    public CryptoWalletInfoResponse getWalletInfos(@RequestParam("ids") String ids, Authentication authentication){
        return new CryptoWalletInfoResponse(
        Arrays.stream(ids.split(","))
                .map(s -> walletService.getWallet(Long.parseLong(s),authentication.getName()))
                .map(w -> new CryptoWalletInfoResponse.CryptoWalletInfo(w.getId(),pricingService.getCurrentPrice(w.getWallet().getCurrency()).multiply(w.getWallet().getBalance())))
                .toList()
        );
    }
}
