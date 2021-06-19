package be.zwoop.ohlc.features.ohlc.repository.weekly;

import be.zwoop.ohlc.features.ohlc.repository.daily.DailyOhlcEntity;
import be.zwoop.ohlc.features.ohlc.repository.OhlcEntityPk;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyOhlcRepository extends CrudRepository<WeeklyOhlcEntity, OhlcEntityPk> {

    Optional<DailyOhlcEntity> findOhlcEntityByMarketDateAndTickerSymbol(LocalDate marketDate, String tickerSymbol);

    @Query("select distinct o from WeeklyOhlcEntity o where o.tickerSymbol=?1 and o.marketDate between ?2 and ?3")
    List<WeeklyOhlcEntity> findOhlcEntitiesByTickerSymbolAndMarketDateBetween(String tickerSymbol, LocalDate from, LocalDate until);

}
