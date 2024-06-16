package be.kpse.ohlc.features.alert;


import be.kpse.ohlc.features.alert.repository.AlertEntity;
import be.kpse.ohlc.features.alert.repository.AlertEntityPk;
import be.kpse.ohlc.features.alert.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;


@CrossOrigin
@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private final AlertRepository alertRepository;

    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @GetMapping("/v1.0/")
    @ResponseStatus(OK)
    public List<AlertEntity> getAlerts() {
        return alertRepository.findAllByOrderByTickerSymbol();
    }

    @PostMapping({"/v1.0/"})
    @ResponseStatus(CREATED)
    public AlertEntity postAlert(@Valid @RequestBody AlertEntity alertEntity) {
        Optional<AlertEntity> foundAlertOpt = alertRepository.findById(
                AlertEntityPk.builder()
                        .tickerSymbol(alertEntity.getTickerSymbol())
                        .marketPrice(alertEntity.getMarketPrice())
                .build());

        if (foundAlertOpt.isPresent()) {
            if (foundAlertOpt.get().isActive()) {
                throw new ResponseStatusException(CONFLICT, "The alert already exists");
            } else {
                throw new ResponseStatusException(CONFLICT, "The alert already exists but is inactive");
            }

        } else {
            return alertRepository.save(alertEntity);
        }

    }

    @GetMapping({"/1.0/triggered"})
    @ResponseStatus(OK)
    public List<AlertEntity> getOhlcAlerts(String tickerSymbol, double low, double high) {
        return alertRepository.findAlertEntitiesByTickerSymbolAndMarketPriceBetween(tickerSymbol, low, high);
    }

    @PutMapping({"/v1.0/{tickerSymbol}"})
    @ResponseStatus(NO_CONTENT)
    public AlertEntity updateAlert(
            @PathVariable String tickerSymbol,
            @Valid @RequestBody AlertEntity alertEntity
    ) {
        Optional<AlertEntity> foundAlertOpt = alertRepository.findById(
                AlertEntityPk.builder()
                        .tickerSymbol(tickerSymbol)
                        .marketPrice(alertEntity.getMarketPrice())
                        .build());

        if (foundAlertOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);
        } else {
            AlertEntity foundAlert = foundAlertOpt.get();
            foundAlert.setMarketPrice(alertEntity.getMarketPrice());
            foundAlert.setActive(alertEntity.isActive());
            return alertRepository.save(foundAlert);
        }

    }

    @DeleteMapping({"/v1.0/{tickerSymbol}/{marketPrice}"})
    public void deleteAlert(@PathVariable String tickerSymbol, @PathVariable double marketPrice) {
        Optional<AlertEntity> foundAlertOpt = alertRepository.findById(
                AlertEntityPk.builder()
                        .tickerSymbol(tickerSymbol)
                        .marketPrice(marketPrice)
                        .build());

        if (foundAlertOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);
        } else {
            alertRepository.deleteAlertEntityByTickerSymbolAndMarketPrice(tickerSymbol, marketPrice);
        }
    }

}
