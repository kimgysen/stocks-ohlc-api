package be.zwoop.ohlc.features.ohlc.service.seeking_alpha.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaOhlc {
    double open;
    double high;
    double low;
    double close;
    long volume;
}
