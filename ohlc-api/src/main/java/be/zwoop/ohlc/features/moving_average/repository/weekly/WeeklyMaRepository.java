package be.zwoop.ohlc.features.moving_average.repository.weekly;

import be.zwoop.ohlc.features.moving_average.repository.MaEntityPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeeklyMaRepository extends JpaRepository<WeeklyMaEntity, MaEntityPk> {

    Optional<WeeklyMaEntity> findByTickerSymbolAndMarketDate(String tickerSymbol, LocalDate marketDate);

}
