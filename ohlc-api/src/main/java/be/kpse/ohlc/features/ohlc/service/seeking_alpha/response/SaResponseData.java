package be.kpse.ohlc.features.ohlc.service.seeking_alpha.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaResponseData {

    @JsonProperty("attributes")
    SaOhlc attributes;

}
