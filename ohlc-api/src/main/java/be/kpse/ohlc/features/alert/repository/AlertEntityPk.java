package be.kpse.ohlc.features.alert.repository;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AlertEntityPk implements Serializable {

    private String tickerSymbol;
    private double marketPrice;

}
