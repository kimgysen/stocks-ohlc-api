package be.kpse.ohlc.repository.ohlc;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyOhlcRepository extends CrudRepository<MonthlyOhlcEntity, OhlcEntityPk> {

	Optional<MonthlyOhlcEntity> findOhlcEntityByMarketDateAndTickerSymbol(LocalDate marketDate, String tickerSymbol);

	@Query("select distinct o from MonthlyOhlcEntity o where o.tickerSymbol=?1 and o.marketDate between ?2 and ?3")
	List<MonthlyOhlcEntity> findOhlcEntitiesByTickerSymbolAndMarketDateBetween(String tickerSymbol, LocalDate from, LocalDate until);

}

