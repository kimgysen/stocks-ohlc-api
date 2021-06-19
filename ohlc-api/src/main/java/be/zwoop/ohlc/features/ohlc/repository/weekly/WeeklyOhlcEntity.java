package be.zwoop.ohlc.features.ohlc.repository.weekly;


import be.zwoop.ohlc.features.ohlc.repository.OhlcEntity;
import be.zwoop.ohlc.features.ohlc.repository.OhlcEntityPk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import javax.persistence.*;
import java.time.LocalDate;


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
