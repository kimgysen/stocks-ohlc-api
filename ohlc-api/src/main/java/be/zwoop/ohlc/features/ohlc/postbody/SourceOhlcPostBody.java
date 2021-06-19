package be.zwoop.ohlc.features.ohlc.postbody;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Getter
public class SourceOhlcPostBody {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate from;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate until;

}
