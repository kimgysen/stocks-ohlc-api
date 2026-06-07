package be.kpse.ohlc.features.nasdaq_ticker;

import be.kpse.ohlc.repository.stock_ticker.StockTickerEntity;
import be.kpse.ohlc.repository.stock_ticker.StockTickerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class NasdaqTickerService {

    private static final Logger logger = LoggerFactory.getLogger(NasdaqTickerService.class);
    private static final String NASDAQ_LISTED_URL = "https://www.nasdaqtrader.com/dynamic/symdir/nasdaqlisted.txt";

    private final StockTickerRepository stockTickerRepository;

    public NasdaqTickerService(StockTickerRepository stockTickerRepository) {
        this.stockTickerRepository = stockTickerRepository;
    }

    @Transactional
    public int fetchAndSaveTickers() throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(NASDAQ_LISTED_URL))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        List<StockTickerEntity> toSave = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new java.io.StringReader(response.body()))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Skip trailing metadata lines (e.g. "File Creation Time: ...")
                if (!line.contains("|")) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                // Format: Symbol|Security Name|Market Category|Test Issue|Financial Status|Round Lot Size|ETF|NextShares
                if (parts.length < 2) {
                    continue;
                }

                String symbol = parts[0].trim();
                String securityName = parts[1].trim();

                if (symbol.isBlank() || securityName.isBlank()) {
                    continue;
                }

                // Skip test issues (column index 3 = "Y")
                if (parts.length > 3 && "Y".equalsIgnoreCase(parts[3].trim())) {
                    continue;
                }

                if (!stockTickerRepository.existsBySymbol(symbol)) {
                    toSave.add(StockTickerEntity.builder()
                            .symbol(symbol)
                            .title(securityName)
                            .build());
                }
            }
        }

        stockTickerRepository.saveAll(toSave);
        logger.info("Saved {} new tickers from NASDAQ listed file.", toSave.size());
        return toSave.size();
    }
}

