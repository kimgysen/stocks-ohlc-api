package be.kpse.ohlc.features.ohlc.repository;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OhlcEntityPk implements Serializable {

    private LocalDate marketDate;
    private String tickerSymbol;

}
