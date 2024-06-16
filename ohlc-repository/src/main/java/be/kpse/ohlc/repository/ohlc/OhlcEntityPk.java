package be.kpse.ohlc.repository.ohlc;

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
