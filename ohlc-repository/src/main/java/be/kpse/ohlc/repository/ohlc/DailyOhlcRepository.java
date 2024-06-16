package be.kpse.ohlc.repository.ohlc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyOhlcRepository extends CrudRepository<DailyOhlcEntity, OhlcEntityPk> {

    Optional<DailyOhlcEntity> findOhlcEntityByMarketDateAndTickerSymbol(LocalDate marketDate, String tickerSymbol);

    @Query("select distinct o from DailyOhlcEntity o where o.tickerSymbol=?1 and o.marketDate between ?2 and ?3")
    List<DailyOhlcEntity> findOhlcEntitiesByTickerSymbolAndMarketDateBetween(String tickerSymbol, LocalDate from, LocalDate until);

}
