package be.zwoop.ohlc.features.moving_average.util;


import be.zwoop.ohlc.features.moving_average.repository.daily.DailyMaEntity;
import be.zwoop.ohlc.features.ohlc.repository.daily.DailyOhlcEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmaUtil {

    public Double calculateSma(List<Double> closePrices, Double newClose, int period) {
        populateSmaList(closePrices, newClose, period);

        if (closePrices.size() < period) {
            return null;
        }
        return closePrices.stream().mapToDouble(Double::doubleValue).sum() / closePrices.size();
    }

    public void populateSmaList(List<Double> smaList, double newVal, int period) {
        if (smaList.size() >= period) {
            smaList.remove(0);
        }
        smaList.add(newVal);
    }

    public void populateOhlcSmaCross(DailyOhlcEntity ohlc, DailyMaEntity dailyMaEntity) {

        if (ohlc.touchesCandleBody(dailyMaEntity.getSma50())) {
            dailyMaEntity.setCrossOhlcSma50(ohlc.getClose());
        }

        if (ohlc.touchesCandleBody(dailyMaEntity.getSma100())) {
            dailyMaEntity.setCrossOhlcSma100(ohlc.getClose());
        }

        if (ohlc.touchesCandleBody(dailyMaEntity.getSma200())) {
            dailyMaEntity.setCrossOhlcSma200(ohlc.getClose());
        }
    }

}
