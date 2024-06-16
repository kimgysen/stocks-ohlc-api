package be.kpse.ohlc.features.ticker;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.stream.Stream;

import static org.hibernate.annotations.QueryHints.READ_ONLY;
import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;


@Repository
public interface TickerRepository extends org.springframework.data.repository.Repository<TickerEntity, String> {

    @QueryHints(value = {
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = READ_ONLY, value = "true")
    })
    @Query("select distinct t from TickerEntity t order by t.tickerSymbol asc")
    Stream<TickerEntity> findAllByOrderByTickerSymbol();

    @Query("select distinct t from TickerEntity t order by t.tickerSymbol asc")
    List<TickerEntity> findAllOrderByTickerSymbol();

    @Query("select t from TickerEntity t where t.tickerSymbol >= ?1 order by t.tickerSymbol asc")
    List<TickerEntity> findAllGreaterThanOrEqualToTickerSymbolOrderByTickerSymbol(String tickerSymbol);

}

