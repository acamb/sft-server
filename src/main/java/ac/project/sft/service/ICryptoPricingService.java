package ac.project.sft.service;

import ac.project.sft.model.CryptoCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ICryptoPricingService {

    public BigDecimal getCurrentPrice(CryptoCurrency currency);

    public BigDecimal getHistoricalPrice(CryptoCurrency currency, LocalDate date);

}
