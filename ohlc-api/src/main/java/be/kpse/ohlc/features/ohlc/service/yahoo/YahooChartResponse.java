package be.kpse.ohlc.features.ohlc.service.yahoo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YahooChartResponse {

    @JsonProperty("chart")
    Chart chart;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Chart {
        @JsonProperty("result")
        List<Result> result;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("timestamp")
        List<Long> timestamps;

        @JsonProperty("indicators")
        Indicators indicators;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Indicators {
        @JsonProperty("quote")
        List<Quote> quote;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Quote {
        @JsonProperty("open")
        List<Double> open;
        @JsonProperty("high")
        List<Double> high;
        @JsonProperty("low")
        List<Double> low;
        @JsonProperty("close")
        List<Double> close;
        @JsonProperty("volume")
        List<Long> volume;
    }
}

