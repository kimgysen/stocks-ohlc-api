package be.zwoop.ohlc.features.ticker;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
