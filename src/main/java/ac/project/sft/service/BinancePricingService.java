package ac.project.sft.service;

import ac.project.sft.model.CryptoCurrency;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Service
public class BinancePricingService implements ICryptoPricingService{

    @Value("${binance.api.url}")
    public String apiUrl;
    public final RestTemplate restTemplate;

    private static final String CURRENT_PRICE_API_PATH = "/api/v3/avgPrice";
    private static final String HISTORICAL_PRICE_API_PATH = "/api/v3/klines";

    public BinancePricingService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(key = "#a0.ticker")
    public BigDecimal getCurrentPrice(CryptoCurrency currency) {
        //TODO[AC] parametrize fiat
        Map<String,String> uriVariables = Map.of("symbol", currency.getTicker()+"EUR");
        String request = UriComponentsBuilder.fromHttpUrl(apiUrl + CURRENT_PRICE_API_PATH)
                .queryParam("symbol","{symbol}")
                .encode()
                .toUriString();
        //TODO check against BUSD and then BUSD/EUR if the ticker has no match
        return restTemplate.getForObject(request,CurrentPriceResponse.class,uriVariables).getPrice();
    }

    @Override
    @Cacheable(key = "#a0.ticker-#a1.toEpochDay()")
    public BigDecimal getHistoricalPrice(CryptoCurrency currency, LocalDate date) {
        //TODO[AC] parametrize fiat
        Map<String,String> uriVariables = Map.of(
                "symbol",currency.getTicker()+"EUR",
                "interval","1d",
                "startTime",""+date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
                "endTime",""+date.atTime(LocalTime.of(23,59,59)).toInstant(ZoneOffset.UTC).toEpochMilli(),
                "limit","1"
        );
        String request = UriComponentsBuilder.fromHttpUrl(apiUrl + HISTORICAL_PRICE_API_PATH)
                .queryParam("symbol","{symbol}")
                .queryParam("interval","{interval}")
                .queryParam("startTime","{startTime}")
                .queryParam("endTime","{endTime}")
                .queryParam("limit","{limit}")
                .encode()
                .toUriString();
        return new BigDecimal(((List<List<String>>) restTemplate.getForObject(request, List.class, uriVariables)).get(0).get(4));
    }

    @Getter
    @Setter
    public static class CurrentPriceResponse{
        BigDecimal price;
    }

}
