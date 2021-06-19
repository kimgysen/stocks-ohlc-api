package be.zwoop.ohlc.features.moving_average.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MaEntityPk {

    private LocalDate marketDate;
    private String tickerSymbol;

}
