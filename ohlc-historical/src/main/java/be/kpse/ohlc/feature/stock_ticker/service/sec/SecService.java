package be.kpse.ohlc.feature.stock_ticker.service.sec;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public interface SecService {

	void getAllStockTickers(Consumer<InputStream> streamConsumer);
}
