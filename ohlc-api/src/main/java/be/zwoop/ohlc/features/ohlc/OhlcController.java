package be.zwoop.ohlc.features.ohlc;


import be.zwoop.ohlc.features.ohlc.postbody.SourceOhlcPostBody;
import be.zwoop.ohlc.features.ohlc.postbody.WeeklyOhlcPostBody;
import be.zwoop.ohlc.features.ohlc.repository.daily.DailyOhlcEntity;
import be.zwoop.ohlc.features.ohlc.repository.daily.DailyOhlcRepository;
import be.zwoop.ohlc.features.ohlc.repository.weekly.WeeklyOhlcEntity;
import be.zwoop.ohlc.features.ohlc.repository.weekly.WeeklyOhlcRepository;
import be.zwoop.ohlc.features.ohlc.service.seeking_alpha.SeekingAlphaService;
import be.zwoop.ohlc.features.ohlc.service.seeking_alpha.exception.SaOhlcException;
import be.zwoop.ohlc.features.ohlc.service.seeking_alpha.response.SaOhlc;
import be.zwoop.ohlc.features.ticker.TickerEntity;
import be.zwoop.ohlc.features.ticker.TickerRepository;
import be.zwoop.ohlc.util.MarketDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;
import static java.util.Comparator.comparing;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping(value = "/ohlc")
public class OhlcController {

    private final Logger logger = LoggerFactory.getLogger(OhlcController.class);
    private final TickerRepository tickerRepository;
    private final DailyOhlcRepository dailyOhlcRepository;
    private final WeeklyOhlcRepository weeklyOhlcRepository;
    private final SeekingAlphaService seekingAlphaService;

    private final MarketDateUtil marketDateUtil;


    public OhlcController(
            TickerRepository tickerRepository,
            DailyOhlcRepository dailyOhlcRepository,
            WeeklyOhlcRepository weeklyOhlcRepository,
            SeekingAlphaService seekingAlphaService,
            MarketDateUtil marketDateUtil
    ) {
        this.tickerRepository = tickerRepository;
        this.dailyOhlcRepository = dailyOhlcRepository;
        this.weeklyOhlcRepository = weeklyOhlcRepository;
        this.seekingAlphaService = seekingAlphaService;
        this.marketDateUtil = marketDateUtil;
    }

    @PostMapping({ "/v1.0" })
    @ResponseStatus(OK)
    @Transactional
    public void sourceOhlc(@Valid @RequestBody SourceOhlcPostBody postBody) {

        Stream<TickerEntity> tickers = tickerRepository.findAllByOrderByTickerSymbol();

        tickers.forEach(t -> {
            try {
                sleep(150);
                Map<String, SaOhlc> saData = seekingAlphaService.getOhlcData(t.getTickerSymbol(), postBody.getFrom(), postBody.getUntil());
                TreeMap<String, SaOhlc> sortedSaData = new TreeMap<>(saData);
                logger.info(t.getTickerSymbol());

                sortedSaData.forEach((key, ohlcData) -> {
                    LocalDate marketDate = LocalDate.parse(key, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    Optional<DailyOhlcEntity> ohlcOpt = dailyOhlcRepository.findOhlcEntityByMarketDateAndTickerSymbol(marketDate, t.getTickerSymbol());

                    if (ohlcOpt.isEmpty()) {
                        Executors.newCachedThreadPool().execute(() -> {
                            DailyOhlcEntity ohlcToSave = DailyOhlcEntity.builder()
                                    .marketDate(marketDate)
                                    .tickerSymbol(t.getTickerSymbol())
                                    .open(ohlcData.getOpen())
                                    .high(ohlcData.getHigh())
                                    .low(ohlcData.getLow())
                                    .close(ohlcData.getClose())
                                    .volume(ohlcData.getVolume())
                                    .build();
                            logger.info(ohlcToSave.toString());
                            dailyOhlcRepository.save(ohlcToSave);
                        });
                    }
                });

            } catch (InterruptedException | SaOhlcException e) {
                e.printStackTrace();
            }
        });

    }

    @PostMapping({"/v1.0/calculate-weekly"})
    @ResponseStatus(OK)
    public void calculateWeeklyOhlc(@Valid @RequestBody WeeklyOhlcPostBody postBody) {
        tickerRepository
                .findAllGreaterThanOrEqualToTickerSymbolOrderByTickerSymbol(postBody.getTickerSymbol())
                .forEach(t -> {
                    LocalDate startDate = marketDateUtil.getFirstDayByYear(postBody.getStartYear());
                    LocalDate endDate = marketDateUtil.getLastFriday();

                    List<DailyOhlcEntity> ohlcData = dailyOhlcRepository.findOhlcEntitiesByTickerSymbolAndMarketDateBetween(
                            t.getTickerSymbol(), startDate, endDate);

                    Map<String, List<DailyOhlcEntity>> ohlcPerWeek = marketDateUtil.listToOhlcMapPerWeek(ohlcData);
                    List<WeeklyOhlcEntity> ohlcWeeklyCandles = new ArrayList<>();

                    for (List<DailyOhlcEntity> ohlcList : ohlcPerWeek.values()) {
                        LocalDate marketDate = ohlcList
                                .stream()
                                .min(comparing(DailyOhlcEntity::getMarketDate))
                                .get()
                                .getMarketDate();

                        double open = ohlcList
                                .stream()
                                .min(comparing(DailyOhlcEntity::getMarketDate))
                                .get()
                                .getOpen();

                        double high = ohlcList
                                .stream()
                                .max(comparing(DailyOhlcEntity::getHigh))
                                .get()
                                .getHigh();

                        double low = ohlcList
                                .stream()
                                .min(comparing(DailyOhlcEntity::getLow))
                                .get()
                                .getLow();

                        double close = ohlcList
                                .stream()
                                .max(comparing(DailyOhlcEntity::getMarketDate))
                                .get()
                                .getClose();

                        long volume = ohlcList
                                .stream()
                                .filter(o -> o.getVolume() != null)
                                .mapToLong(DailyOhlcEntity::getVolume)
                                .sum();

                        WeeklyOhlcEntity ohlcWeeklyCandle = WeeklyOhlcEntity
                                .builder()
                                .marketDate(marketDate)
                                .tickerSymbol(t.getTickerSymbol())
                                .open(open)
                                .high(high)
                                .low(low)
                                .close(close)
                                .volume(volume)
                                .build();

                        ohlcWeeklyCandles.add(ohlcWeeklyCandle);

                    }

                    logger.info("Weekly: " + t.getTickerSymbol());

                    saveWeeklyOhlcCandles(ohlcWeeklyCandles);

                });

    }

    @Transactional
    void saveWeeklyOhlcCandles(List<WeeklyOhlcEntity> ohlcWeeklyCandles) {
        weeklyOhlcRepository.saveAll(ohlcWeeklyCandles);
        ohlcWeeklyCandles.clear();
    }

}
