package be.kpse.ohlc.feature.stock_ticker.api;

import be.kpse.ohlc.feature.stock_ticker.service.stock_ticker.StockTickerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ticker")
@AllArgsConstructor
@Slf4j
public class StockTickerController {

	private final StockTickerService stockTickerService;

	@PostMapping
	public ResponseEntity<?> findAndSaveStockTickers() {
		stockTickerService.getAndSaveAllStockTickers();

		log.info("All SEC tickers were saved.");

		return ResponseEntity.ok().build();
	}

}
