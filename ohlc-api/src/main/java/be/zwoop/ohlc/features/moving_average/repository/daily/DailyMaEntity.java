package be.zwoop.ohlc.features.moving_average.repository.daily;


import be.zwoop.ohlc.features.moving_average.repository.MaEntity;
import be.zwoop.ohlc.features.ohlc.repository.OhlcEntityPk;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Table(name = "moving_average_daily")
@Entity
@IdClass(OhlcEntityPk.class)
@NoArgsConstructor
@SuperBuilder
public class DailyMaEntity extends MaEntity {

    @Override
    public String toString() {
        return "DailyMaEntity{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", marketDate=" + marketDate +
                ", sma50=" + sma50 +
                ", sma100=" + sma100 +
                ", sma200=" + sma200 +
                '}';
    }
}
