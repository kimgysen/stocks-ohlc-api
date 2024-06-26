package be.zwoop.ohlc.features.moving_average.repository.weekly;


import be.zwoop.ohlc.features.moving_average.repository.MaEntity;
import be.zwoop.ohlc.features.ohlc.repository.OhlcEntityPk;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Table(name = "moving_average_weekly")
@Entity
@IdClass(OhlcEntityPk.class)
@NoArgsConstructor
@SuperBuilder
public class WeeklyMaEntity extends MaEntity {

    @Override
    public String toString() {
        return "WeeklyMaEntity{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", marketDate=" + marketDate +
                ", sma50=" + sma50 +
                ", sma100=" + sma100 +
                ", sma200=" + sma200 +
                '}';
    }
}
