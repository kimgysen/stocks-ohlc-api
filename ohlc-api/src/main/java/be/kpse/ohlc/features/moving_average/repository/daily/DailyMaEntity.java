package be.kpse.ohlc.features.moving_average.repository.daily;


import be.kpse.ohlc.features.moving_average.repository.MaEntity;
import be.kpse.ohlc.features.ohlc.repository.OhlcEntityPk;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

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
