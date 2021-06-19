package be.zwoop.ohlc.features.moving_average.postbody;

import lombok.Getter;

@Getter
public class CalcMaPostBody extends FromUntilPostBody{
    private String tickerSymbol;
}
