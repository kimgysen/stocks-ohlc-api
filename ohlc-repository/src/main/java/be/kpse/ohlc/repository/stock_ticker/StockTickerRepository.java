package be.kpse.ohlc.repository.stock_ticker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTickerRepository extends JpaRepository<StockTickerEntity, Long> {

	boolean existsBySymbol(String symbol);

}
