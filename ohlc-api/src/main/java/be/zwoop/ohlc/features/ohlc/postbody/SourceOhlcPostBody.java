package be.zwoop.ohlc.features.ohlc.postbody;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
public class SourceOhlcPostBody {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    LocalDate from;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    LocalDate until;

}
