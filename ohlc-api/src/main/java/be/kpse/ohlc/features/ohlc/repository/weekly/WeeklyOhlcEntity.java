package be.kpse.ohlc.features.ohlc.repository.weekly;


import be.kpse.ohlc.features.ohlc.repository.OhlcEntity;
import be.kpse.ohlc.features.ohlc.repository.OhlcEntityPk;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Table(name = "ohlc_weekly")
@Entity
@IdClass(OhlcEntityPk.class)
@NoArgsConstructor
@SuperBuilder
public class WeeklyOhlcEntity extends OhlcEntity {

    @Override
    public String toString() {
        return "OhlcWeeklyEntity{" +
                "marketDate=" + marketDate +
                ", tickerSymbol='" + tickerSymbol + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}
