package be.zwoop.ohlc.features.moving_average.repository;


import be.zwoop.ohlc.features.ohlc.repository.OhlcEntityPk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass
@IdClass(OhlcEntityPk.class)
@SuperBuilder
@NoArgsConstructor
@Data
public class MaEntity {

    @Id
    @Column(name = "ticker_symbol")
    protected String tickerSymbol;

    @Id
    @Column(name = "market_date")
    protected LocalDate marketDate;

    @Column(name = "sma_50")
    protected Double sma50;

    @Column(name = "sma_100")
    protected Double sma100;

    @Column(name = "sma_200")
    protected Double sma200;

    @Column(name = "cross_ohlc_sma50")
    protected Double crossOhlcSma50;

    @Column(name = "cross_ohlc_sma100")
    protected Double crossOhlcSma100;

    @Column(name = "cross_ohlc_sma200")
    protected Double crossOhlcSma200;


    public boolean hasSmaCross() {
        return sma50 != null && sma100 != null && sma200 != null;
    }

    @Override
    public String toString() {
        return "MaEntity{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", marketDate=" + marketDate +
                ", sma50=" + sma50 +
                ", sma100=" + sma100 +
                ", sma200=" + sma200 +
                '}';
    }
}
