package be.zwoop.ohlc.features.alert.repository;


import lombok.*;

import javax.persistence.*;

@Table(name = "alert")
@Entity
@IdClass(AlertEntityPk.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AlertEntity {

    @Id
    @Column(name = "ticker_symbol")
    private String tickerSymbol;

    @Id
    @Column(name = "market_price")
    private double marketPrice;

    @Column(name = "active")
    private boolean isActive = true;

}
