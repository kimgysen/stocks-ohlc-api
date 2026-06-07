package be.kpse.ohlc.features.nasdaq_ticker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/tickers")
public class NasdaqTickerController {

    private static final Logger logger = LoggerFactory.getLogger(NasdaqTickerController.class);

    private final NasdaqTickerService nasdaqTickerService;

    public NasdaqTickerController(NasdaqTickerService nasdaqTickerService) {
        this.nasdaqTickerService = nasdaqTickerService;
    }

    /**
     * POST /api/tickers/v1.0/sync
     * Fetches the latest NASDAQ listed symbols and saves any new ones to STOCK_TICKER.
     */
    @PostMapping("/v1.0/sync")
    public ResponseEntity<Map<String, Object>> syncNasdaqTickers() {
        try {
            int saved = nasdaqTickerService.fetchAndSaveTickers();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "newTickersSaved", saved
            ));
        } catch (Exception e) {
            logger.error("Failed to sync NASDAQ tickers", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}

