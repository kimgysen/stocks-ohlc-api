package be.zwoop.ohlc.features.moving_average;

import be.zwoop.ohlc.features.moving_average.postbody.CalcMaPostBody;
import be.zwoop.ohlc.features.moving_average.postbody.FromUntilPostBody;
import be.zwoop.ohlc.features.moving_average.repository.daily.DailyMaEntity;
import be.zwoop.ohlc.features.moving_average.repository.daily.DailyMaRepository;
import be.zwoop.ohlc.features.moving_average.repository.weekly.WeeklyMaEntity;
import be.zwoop.ohlc.features.moving_average.repository.weekly.WeeklyMaRepository;
import be.zwoop.ohlc.features.moving_average.util.SmaUtil;
import be.zwoop.ohlc.features.ohlc.repository.daily.DailyOhlcEntity;
import be.zwoop.ohlc.features.ohlc.repository.daily.DailyOhlcRepository;
import be.zwoop.ohlc.features.ohlc.repository.weekly.WeeklyOhlcEntity;
import be.zwoop.ohlc.features.ohlc.repository.weekly.WeeklyOhlcRepository;
import be.zwoop.ohlc.features.ticker.TickerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/ma")
public class MaController {

    private final Logger logger = LoggerFactory.getLogger(MaController.class);
    private final TickerRepository tickerRepository;
    private final DailyMaRepository dailyMaRepository;
    private final WeeklyMaRepository weeklyMaRepository;
    private final DailyOhlcRepository dailyOhlcRepository;
    private final WeeklyOhlcRepository weeklyOhlcRepository;

    private final SmaUtil smaUtil;

    public MaController(
            TickerRepository tickerRepository,
            DailyMaRepository dailyMaRepository,
            WeeklyMaRepository weeklyMaRepository,
            DailyOhlcRepository dailyOhlcRepository,
            WeeklyOhlcRepository weeklyOhlcRepository,
            SmaUtil smaUtil
    ) {
        this.tickerRepository = tickerRepository;
        this.dailyOhlcRepository = dailyOhlcRepository;
        this.weeklyOhlcRepository = weeklyOhlcRepository;
        this.dailyMaRepository = dailyMaRepository;
        this.weeklyMaRepository = weeklyMaRepository;
        this.smaUtil = smaUtil;
    }

    @PostMapping({"/v1.0/calculate-ma/daily"})
    @ResponseStatus(OK)
    public void calculateDailyMa(@Valid @RequestBody CalcMaPostBody postBody) {

        tickerRepository
                .findAllGreaterThanOrEqualToTickerSymbolOrderByTickerSymbol(postBody.getTickerSymbol())
                .forEach(t -> {
                    logger.info(t.getTickerSymbol());
                    List<DailyOhlcEntity> ohlcData = dailyOhlcRepository.findOhlcEntitiesByTickerSymbolAndMarketDateBetween(
                            t.getTickerSymbol(), postBody.getFrom(), postBody.getUntil());

                    List<Double> sma50List = new ArrayList<>();
                    List<Double> sma100List = new ArrayList<>();
                    List<Double> sma200List = new ArrayList<>();

                    List<DailyMaEntity> maEntities = new ArrayList<>();

                    ohlcData.forEach(ohlc -> {
                        Double sma50 = smaUtil.calculateSma(sma50List, ohlc.getClose(), 50);
                        Double sma100 = smaUtil.calculateSma(sma100List, ohlc.getClose(), 100);
                        Double sma200 = smaUtil.calculateSma(sma200List, ohlc.getClose(), 200);

                        DailyMaEntity maToSave = DailyMaEntity.builder()
                                .marketDate(ohlc.getMarketDate())
                                .tickerSymbol(ohlc.getTickerSymbol())
                                .sma50(sma50)
                                .sma100(sma100)
                                .sma200(sma200)
                                .build();

                            maEntities.add(maToSave);
                    });
                    saveDailyMaEntities(maEntities);
            });
    }

    @Transactional
    void saveDailyMaEntities(List<DailyMaEntity> maEntities) {
        dailyMaRepository.saveAll(maEntities);
        maEntities.clear();
    }


    @PostMapping({"/v1.0/calculate-ma/weekly"})
    @ResponseStatus(OK)
    public void calculateWeeklyMa(@Valid @RequestBody CalcMaPostBody postBody) {

        tickerRepository
                .findAllGreaterThanOrEqualToTickerSymbolOrderByTickerSymbol(postBody.getTickerSymbol())
                .forEach(t -> {
                    logger.info(t.getTickerSymbol());
                    List<WeeklyOhlcEntity> ohlcData = weeklyOhlcRepository.findOhlcEntitiesByTickerSymbolAndMarketDateBetween(
                            t.getTickerSymbol(), postBody.getFrom(), postBody.getUntil());

                    List<Double> sma50List = new ArrayList<>();
                    List<Double> sma100List = new ArrayList<>();
                    List<Double> sma200List = new ArrayList<>();

                    List<WeeklyMaEntity> maEntities = new ArrayList<>();

                    ohlcData.forEach(ohlc -> {
                        Double sma50 = smaUtil.calculateSma(sma50List, ohlc.getClose(), 50);
                        Double sma100 = smaUtil.calculateSma(sma100List, ohlc.getClose(), 100);
                        Double sma200 = smaUtil.calculateSma(sma200List, ohlc.getClose(), 200);

                        WeeklyMaEntity maToSave = WeeklyMaEntity.builder()
                                .marketDate(ohlc.getMarketDate())
                                .tickerSymbol(ohlc.getTickerSymbol())
                                .sma50(sma50)
                                .sma100(sma100)
                                .sma200(sma200)
                                .build();

                        maEntities.add(maToSave);
                    });
                    saveWeeklyMaEntities(maEntities);
                });
    }

    @Transactional
    void saveWeeklyMaEntities(List<WeeklyMaEntity> maEntities) {
        weeklyMaRepository.saveAll(maEntities);
        maEntities.clear();
    }



    @PostMapping({"/v1.0/calculate-ohlc-crosses"})
    @ResponseStatus(OK)
    public void calculateCrosses(@Valid @RequestBody FromUntilPostBody fromUntilPostBody) {
        tickerRepository
                .findAllOrderByTickerSymbol()
                .forEach(t -> {
                    List<DailyOhlcEntity> ohlcData = dailyOhlcRepository.findOhlcEntitiesByTickerSymbolAndMarketDateBetween(
                            t.getTickerSymbol(), fromUntilPostBody.getFrom(), fromUntilPostBody.getUntil());
                    ohlcData.forEach(ohlc -> {

                        Optional<DailyMaEntity> maEntityOpt = dailyMaRepository.findByTickerSymbolAndMarketDate(ohlc.getTickerSymbol(), ohlc.getMarketDate());

                        if (maEntityOpt.isPresent()) {
                            DailyMaEntity dailyMaEntity = maEntityOpt.get();
                            smaUtil.populateOhlcSmaCross(ohlc, dailyMaEntity);

                            if (dailyMaEntity.hasSmaCross()) {
                                dailyMaRepository.save(dailyMaEntity);
                            }

                        }
                    });
                });
    }

}
