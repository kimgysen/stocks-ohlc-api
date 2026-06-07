package be.kpse.ohlc.features.ohlc.service.yahoo;

import be.kpse.ohlc.features.ohlc.service.seeking_alpha.exception.SaOhlcException;
import be.kpse.ohlc.features.ohlc.service.seeking_alpha.response.SaOhlc;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class YahooFinanceService {

    // v8 chart API – no auth required, no bot protection
    private static final String YAHOO_URL =
            "https://query1.finance.yahoo.com/v8/finance/chart/%s?period1=%d&period2=%d&interval=1d&events=history";

    private final RestTemplate restTemplate;

    public YahooFinanceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, SaOhlc> getOhlcData(String tickerSymbol, LocalDate start, LocalDate end)
            throws SaOhlcException {

        long period1 = start.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        // end is exclusive in Yahoo – add one day so the requested date is included
        long period2 = end.plusDays(1).atStartOfDay(ZoneOffset.UTC).toEpochSecond();

        String url = String.format(YAHOO_URL, tickerSymbol, period1, period2);

        try {
            HttpHeaders headers = new HttpHeaders();
            // Yahoo requires a browser-like User-Agent
            headers.set(HttpHeaders.USER_AGENT,
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<YahooChartResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, YahooChartResponse.class);

            if (response.getBody() == null
                    || response.getBody().getChart() == null
                    || response.getBody().getChart().getResult() == null
                    || response.getBody().getChart().getResult().isEmpty()) {
                throw new SaOhlcException("No data returned from Yahoo Finance for: " + tickerSymbol);
            }

            return parseResponse(response.getBody());

        } catch (SaOhlcException e) {
            throw e;
        } catch (Exception e) {
            throw new SaOhlcException("Yahoo Finance request failed for " + tickerSymbol + ": " + e.getMessage());
        }
    }

    private Map<String, SaOhlc> parseResponse(YahooChartResponse response) {
        YahooChartResponse.Result result = response.getChart().getResult().get(0);
        List<Long> timestamps = result.getTimestamps();
        YahooChartResponse.Quote quote = result.getIndicators().getQuote().get(0);

        Map<String, SaOhlc> ohlcMap = new LinkedHashMap<>();

        for (int i = 0; i < timestamps.size(); i++) {
            // Skip entries where any OHLC value is null (can happen for market holidays)
            if (quote.getOpen().get(i) == null || quote.getClose().get(i) == null) {
                continue;
            }

            String date = LocalDate.ofEpochDay(timestamps.get(i) / 86400).toString(); // yyyy-MM-dd

            SaOhlc ohlc = new SaOhlc();
            ohlc.setOpen(quote.getOpen().get(i));
            ohlc.setHigh(quote.getHigh().get(i));
            ohlc.setLow(quote.getLow().get(i));
            ohlc.setClose(quote.getClose().get(i));
            ohlc.setVolume(quote.getVolume() != null && quote.getVolume().get(i) != null
                    ? quote.getVolume().get(i) : 0L);
            ohlc.setAsOfDate(date);

            ohlcMap.put(date, ohlc);
        }

        return ohlcMap;
    }
}

