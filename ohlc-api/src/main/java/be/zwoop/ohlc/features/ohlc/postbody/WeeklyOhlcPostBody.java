package be.zwoop.ohlc.features.ohlc.postbody;


import lombok.Getter;

@Getter
public class WeeklyOhlcPostBody {

    private int startYear;
    private String tickerSymbol;

}
