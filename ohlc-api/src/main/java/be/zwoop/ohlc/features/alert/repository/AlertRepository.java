package be.zwoop.ohlc.features.alert.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlertRepository extends CrudRepository<AlertEntity, AlertEntityPk> {

    List<AlertEntity> findAllByOrderByTickerSymbol();
    List<AlertEntity> findAlertEntitiesByTickerSymbolAndMarketPriceBetween(String tickerSymbol, double low, double high);
    void deleteAlertEntityByTickerSymbolAndMarketPrice(String tickerSymbol, double marketPrice);

}
