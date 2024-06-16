package be.kpse.ohlc.feature.stock_ticker.service.stock_ticker;

import be.kpse.ohlc.feature.stock_ticker.service.sec.SecService;
import be.kpse.ohlc.feature.stock_ticker.service.sec.SecTickerDto;
import be.kpse.ohlc.repository.stock_ticker.StockTickerEntity;
import be.kpse.ohlc.repository.stock_ticker.StockTickerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
@Slf4j
public class StockTickerServiceImpl implements StockTickerService {

	private final SecService secService;
	private final StockTickerRepository tickerRepository;

	@Override
	public void getAndSaveAllStockTickers() {
		Consumer<InputStream> getTickersConsumer = inputStream -> {
			try (JsonParser jsonParser = new ObjectMapper().getFactory().createParser(inputStream)) {
				if (jsonParser.nextToken() != JsonToken.START_OBJECT) {
					throw new IllegalStateException("Expected an object");
				}

				while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
					jsonParser.nextToken(); // Move to the value token

					SecTickerDto secTickerDto = new ObjectMapper().readValue(jsonParser, SecTickerDto.class);
					saveStockTickerIfNotExists(secTickerDto);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		};

		secService.getAllStockTickers(getTickersConsumer);

	}

	private void saveStockTickerIfNotExists(SecTickerDto secTickerDto) {
		boolean exists = tickerRepository.existsBySymbol(secTickerDto.getTicker());

		if (!exists) {
			StockTickerEntity tickerEntity = StockTickerEntity.builder()
					.symbol(secTickerDto.getTicker())
					.title(secTickerDto.getTitle())
					.build();
			tickerRepository.saveAndFlush(tickerEntity);

			log.info("Stock ticker '{}' was saved.", secTickerDto.getTicker());
		}

	}

}
