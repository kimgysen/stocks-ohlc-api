package be.kpse.ohlc.features.ticker;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticker")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TickerEntity {

    @Id
    @Column(name = "ticker_symbol")
    String tickerSymbol;

}
