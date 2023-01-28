package ac.project.sft;

import ac.project.sft.model.CryptoCurrency;
import ac.project.sft.service.BinancePricingService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;

@SpringBootTest
public class TestBinancePricingService {

    @Autowired
    BinancePricingService service;

    @Test
    public void testCurrentPrice(){
        CryptoCurrency crypto = new CryptoCurrency();
        crypto.setCode("BTC");
        BigDecimal value = service.getCurrentPrice(crypto);
        Assert.notNull(value);
    }

    @Test
    public void testHistoricalPrice(){
        CryptoCurrency crypto = new CryptoCurrency();
        crypto.setCode("BTC");
        BigDecimal value = service.getHistoricalPrice(crypto, LocalDate.of(2022,12,31));
        Assert.notNull(value);
    }

}
