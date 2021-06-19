package be.zwoop.ohlc.features.ohlc.service.seeking_alpha;


import be.zwoop.ohlc.features.ohlc.service.seeking_alpha.exception.SaOhlcException;
import be.zwoop.ohlc.features.ohlc.service.seeking_alpha.response.SaOhlc;
import be.zwoop.ohlc.features.ohlc.service.seeking_alpha.response.SaResponse;
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
        String SEEKING_ALPHA_URL = "https://finance.api.seekingalpha.com/v2/historical-prices?show=daily&adjusted=false&";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String strStart = start.format(formatter);
        String strEnd = end.format(formatter);

        String url = SEEKING_ALPHA_URL + "start=" + strStart + "&end=" + strEnd + "&symbols=SELB";

        try {
            SaResponse response = restTemplate.getForObject(url, SaResponse.class);

            if (response == null) {
                throw new SaOhlcException("No response from Seeking Alpha for ticker " + tickerSymbol);
            }

            return parseResponse(response);

        } catch (Throwable e) {
            throw new SaOhlcException(e.getMessage());
        }
    }

    private Map<String, SaOhlc> parseResponse(SaResponse response) {
        return response.getData().get(0).getAttributes();
    }

}
