package be.kpse.ohlc.features.ohlc.service.seeking_alpha.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaOhlc {
    double open;
    double high;
    double low;
    double close;
    double volume;

    @JsonProperty("as_of_date")
    String asOfDate;
}
