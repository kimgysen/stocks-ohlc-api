package be.kpse.ohlc.repository.ohlc;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(name = "ohlc_weekly")
@Entity
@IdClass(OhlcEntityPk.class)
@NoArgsConstructor
@SuperBuilder
public class WeeklyOhlcEntity extends OhlcEntity {

	@Override
	public String toString() {
		return "OhlcWeeklyEntity{" +
				"marketDate=" + marketDate +
				", tickerSymbol='" + tickerSymbol + '\'' +
				", open=" + open +
				", high=" + high +
				", low=" + low +
				", close=" + close +
				", volume=" + volume +
				'}';
	}
}

