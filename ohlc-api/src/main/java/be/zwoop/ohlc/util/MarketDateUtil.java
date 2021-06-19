package be.zwoop.ohlc.util;


import be.zwoop.ohlc.features.ohlc.repository.daily.DailyOhlcEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;

@Component
public class MarketDateUtil {

    public LocalDate getFirstDayByYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public LocalDate getLastDayByYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return cal.getTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private boolean isWeekend(Calendar cal) {
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    public LocalDate getLastFriday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime());

        if (!isWeekend(cal)) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
        }

        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return cal.getTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public int getWeekByLocalDate(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    public Map<String, List<DailyOhlcEntity>> listToOhlcMapPerWeek(List<DailyOhlcEntity> ohlcData) {
        Map<String, List<DailyOhlcEntity>> ohlcPerWeek = new TreeMap<>();

        ohlcData.forEach(ohlc -> {
            int week = getWeekByLocalDate(ohlc.getMarketDate());
            int year = ohlc.getMarketDate().getYear();
            ohlcPerWeek.computeIfAbsent(week + "_" + year, k -> new ArrayList<>()).add(ohlc);
        });

        return ohlcPerWeek;
    }


}
