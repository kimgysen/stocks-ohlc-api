package be.zwoop.ohlc.features.moving_average.repository.daily;

import be.zwoop.ohlc.features.moving_average.repository.MaEntityPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyMaRepository extends JpaRepository<DailyMaEntity, MaEntityPk> {

    Optional<DailyMaEntity> findByTickerSymbolAndMarketDate(String tickerSymbol, LocalDate marketDate);

}
