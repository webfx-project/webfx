package webfx.tutorial.service.services.forecast.spi.impl.random;

import webfx.platform.shared.util.async.Future;
import webfx.tutorial.service.services.forecast.spi.ForecastMetrics;
import webfx.tutorial.service.services.forecast.spi.ForecastServiceProvider;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author Bruno Salmon
 */
public class RandomForecastServiceProvider implements ForecastServiceProvider {

    @Override
    public Future<Collection<ForecastMetrics>> getWeekForecast(String place) {
        if (Math.random() < 0.1)
            return Future.failedFuture("Forecast service temporary unavailable, please try later");
        Collection<ForecastMetrics> forecasts = new ArrayList<>();
        ForecastMetrics.SkyState[] possibleSkyStates = ForecastMetrics.SkyState.values();
        for (int i = 0; i < 7; i++) {
            forecasts.add(new ForecastMetrics(
                    Instant.now().plus(i, DAYS),
                    (int) (30 * Math.random()),
                    possibleSkyStates[new Random().nextInt(possibleSkyStates.length)]
            ));
        }
        return Future.succeededFuture(forecasts);
    }
}
