package be.kpse.ohlc.repository.stock_ticker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "STOCK_TICKER")
@Entity
@EqualsAndHashCode
@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockTickerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticker_id")
	private Long tickerId;

	@Column(name = "symbol")
	private String symbol;

	@Column(name = "title")
	private String title;

}
