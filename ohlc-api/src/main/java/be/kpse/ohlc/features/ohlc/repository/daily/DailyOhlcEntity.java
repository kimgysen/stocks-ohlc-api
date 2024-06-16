package be.kpse.ohlc.features.ohlc.repository.daily;


import be.kpse.ohlc.features.ohlc.repository.OhlcEntity;
import be.kpse.ohlc.features.ohlc.repository.OhlcEntityPk;
import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Table(name = "ohlc_daily_2022")
@Entity
@IdClass(OhlcEntityPk.class)
@NoArgsConstructor
@SuperBuilder
public class DailyOhlcEntity extends OhlcEntity {


    @Override
    public String toString() {
        return "OhlcEntity{" +
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
