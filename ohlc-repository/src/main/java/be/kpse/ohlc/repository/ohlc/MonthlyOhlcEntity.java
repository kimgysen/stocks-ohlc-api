package be.kpse.ohlc.repository.ohlc;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(name = "ohlc_monthly")
@Entity
@IdClass(OhlcEntityPk.class)
@NoArgsConstructor
@SuperBuilder
public class MonthlyOhlcEntity extends OhlcEntity {

	@Override
	public String toString() {
		return "OhlcMonthlyEntity{" +
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

