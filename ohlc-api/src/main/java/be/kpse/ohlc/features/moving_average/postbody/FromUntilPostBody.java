package be.kpse.ohlc.features.moving_average.postbody;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class FromUntilPostBody {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate from;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate until;

}
