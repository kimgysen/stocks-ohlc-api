package be.kpse.ohlc.features.ohlc.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.time.LocalDate;


@MappedSuperclass
@IdClass(OhlcEntityPk.class)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class OhlcEntity {

    @Id
    @Column(name = "market_date")
    protected LocalDate marketDate;

    @Id
    @Column(name = "ticker_symbol")
    protected String tickerSymbol;

    @Column(name = "open")
    protected Double open;

    @Column(name = "high")
    protected Double high;

    @Column(name = "low")
    protected Double low;

    @Column(name = "close")
    protected Double close;

    @Column(name = "volume")
    protected Long volume;


    public boolean isGreenCandle() {
        return close > open;
    }

    public boolean isRedCandle() {
        return open > close;
    }

    public boolean touchesCandleBody(double marketPrice) {
        return marketPrice >= open && marketPrice <= close;
    }

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
