package be.kpse.ohlc.features.ohlc.service.seeking_alpha;


import be.kpse.ohlc.features.ohlc.service.seeking_alpha.exception.SaOhlcException;
import be.kpse.ohlc.features.ohlc.service.seeking_alpha.response.SaOhlc;
import be.kpse.ohlc.features.ohlc.service.seeking_alpha.response.SaResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@Service
public class SeekingAlphaService {

    private final RestTemplate restTemplate;

    public SeekingAlphaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, SaOhlc> getOhlcData(String tickerSymbol, LocalDate start, LocalDate end) throws SaOhlcException {
        String SEEKING_ALPHA_URL = "https://seekingalpha.com/api/v3/historical_prices?filter[ticker][slug]=%s&&filter[as_of_date][gte]=%s&filter[as_of_date][lte]=%s&sort=as_of_date";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String strStart = start.format(formatter);
        String strEnd = end.format(formatter);

        String url = String.format(SEEKING_ALPHA_URL, tickerSymbol, strStart, strEnd);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response == null) {
                throw new SaOhlcException("No response from Seeking Alpha for ticker " + tickerSymbol);
            }

//            return parseResponse(response.getBody());
            return null;

        } catch (Throwable e) {
            throw new SaOhlcException(e.getMessage());
        }
    }

    private Map<String, SaOhlc> parseResponse(SaResponse response) {
        return response.getData().get(0).getAttributes();
    }

}
