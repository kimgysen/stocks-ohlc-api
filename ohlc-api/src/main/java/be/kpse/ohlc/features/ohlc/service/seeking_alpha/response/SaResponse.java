package be.kpse.ohlc.features.ohlc.service.seeking_alpha.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaResponse {

    List<SaResponseData> data;

}
