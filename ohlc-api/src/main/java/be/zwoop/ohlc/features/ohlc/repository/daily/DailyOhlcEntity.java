package be.zwoop.ohlc.features.ohlc.repository.daily;


import be.zwoop.ohlc.features.ohlc.repository.OhlcEntity;
import be.zwoop.ohlc.features.ohlc.repository.OhlcEntityPk;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;


@Table(name = "ohlc_daily")
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
